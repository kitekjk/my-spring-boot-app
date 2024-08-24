package com.example.infrastructure.member.cassandra

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

class CassandraMemberRepositoryTest {

    private lateinit var cassandraTemplate: ReactiveCassandraTemplate
    private lateinit var cassandraMemberRepository: CassandraMemberRepository

    @BeforeEach
    fun setUp() {
        cassandraTemplate = mock()
        cassandraMemberRepository = CassandraMemberRepository(cassandraTemplate)
    }

    @Test
    fun `should save a member successfully`() {
        val member = Member(name = "John Doe", email = "john.doe@example.com")
        whenever(cassandraTemplate.insert(member)).thenReturn(Mono.just(member))

        val result = cassandraMemberRepository.save(member)

        StepVerifier.create(result)
            .expectNextMatches { it.name == "John Doe" }
            .verifyComplete()
    }

    @Test
    fun `should find a member by id successfully`() {
        val memberId = MemberId(UUID.randomUUID())
        val member = Member(id = memberId, name = "John Doe", email = "john.doe@example.com")
        whenever(cassandraTemplate.selectOneById(memberId.id, Member::class.java)).thenReturn(Mono.just(member))

        val result = cassandraMemberRepository.findById(memberId)

        StepVerifier.create(result)
            .expectNextMatches { it.id == memberId }
            .verifyComplete()
    }
}