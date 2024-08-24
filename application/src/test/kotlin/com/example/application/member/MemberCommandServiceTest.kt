package com.example.application.member

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import com.example.domain.model.member.MemberStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

class MemberCommandServiceTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var memberCommandService: MemberCommandService

    @BeforeEach
    fun setUp() {
        memberRepository = mock()
        memberCommandService = MemberCommandService(memberRepository)
    }

    @Test
    fun `should create a member successfully`() {
        val member = Member(name = "John Doe", email = "john.doe@example.com")
        whenever(memberRepository.save(any())).thenReturn(Mono.just(member))

        val result = memberCommandService.createMember("John Doe", "john.doe@example.com")

        StepVerifier.create(result)
            .expectNextMatches { it.name == "John Doe" && it.email == "john.doe@example.com" }
            .verifyComplete()
    }

    @Test
    fun `should activate a member successfully`() {
        val memberId = MemberId(UUID.randomUUID())
        val member = Member(id = memberId, name = "Jane Doe", email = "jane.doe@example.com")
        whenever(memberRepository.findById(memberId)).thenReturn(Mono.just(member))
        whenever(memberRepository.save(any())).thenReturn(Mono.just(member.apply { activate() }))

        val result = memberCommandService.activateMember(memberId)

        StepVerifier.create(result)
            .expectNextMatches { it.getStatus() == MemberStatus.ACTIVE }
            .verifyComplete()
    }
}