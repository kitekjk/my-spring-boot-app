package com.example.application.member

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MemberQueryService(
    private val memberRepository: MemberRepository
) {

    fun getMember(id: MemberId): Mono<Member> {
        return memberRepository.findById(id)
    }

    fun getAllMembers(): Flux<Member> {
        return memberRepository.findAll()
    }
}