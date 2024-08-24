package com.example.application.member

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

class MemberQueryServiceTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var memberQueryService: MemberQueryService

    @BeforeEach
    fun setUp() {
        memberRepository = mock()
        memberQueryService = MemberQueryService(memberRepository)
    }

    @Test
    fun `should get a member by id successfully`() {
        val memberId = MemberId(UUID.randomUUID())
        val member = Member(id = memberId, name = "John Doe", email = "john.doe@example.com")
        whenever(memberRepository.findById(memberId)).thenReturn(Mono.just(member))

        val result = memberQueryService.getMember(memberId)

        StepVerifier.create(result)
            .expectNextMatches { it.id == memberId && it.name == "John Doe" }
            .verifyComplete()
    }

    @Test
    fun `should get all members successfully`() {
        val members = listOf(
            Member(name = "John Doe", email = "john.doe@example.com"),
            Member(name = "Jane Doe", email = "jane.doe@example.com")
        )
        whenever(memberRepository.findAll()).thenReturn(Flux.fromIterable(members))

        val result = memberQueryService.getAllMembers()

        StepVerifier.create(result)
            .expectNextCount(2)
            .verifyComplete()
    }
}