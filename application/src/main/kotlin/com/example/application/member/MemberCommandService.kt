package com.example.application.member

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import reactor.core.publisher.Mono

class MemberCommandService(
    private val memberRepository: MemberRepository
) {

    fun createMember(name: String, email: String): Mono<Member> {
        val member = Member(name = name, email = email)
        return memberRepository.save(member)
    }

    fun updateMember(id: MemberId, name: String, email: String): Mono<Member> {
        return memberRepository.findById(id)
            .flatMap { existingMember ->
                val updatedMember = existingMember.copy(name = name, email = email)
                memberRepository.save(updatedMember)
            }
    }

    fun deleteMember(id: MemberId): Mono<Void> {
        return memberRepository.deleteById(id)
    }

    fun activateMember(id: MemberId): Mono<Member> {
        return memberRepository.findById(id)
            .flatMap { member ->
                member.activate()
                memberRepository.save(member)
            }
    }

    fun deactivateMember(id: MemberId): Mono<Member> {
        return memberRepository.findById(id)
            .flatMap { member ->
                member.deactivate()
                memberRepository.save(member)
            }
    }
}