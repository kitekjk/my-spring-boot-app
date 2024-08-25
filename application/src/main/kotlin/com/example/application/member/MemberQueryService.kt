package com.example.application.member

import com.example.application.member.dto.MemberDto
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class MemberQueryService(
    private val memberRepository: MemberRepository
) {

    fun getMember(id: MemberId): Mono<MemberDto> {
        return memberRepository.findById(id).map { MemberDto.fromDomain(it) }
    }

    fun getAllMembers(): Flux<MemberDto> {
        return memberRepository.findAll().map { MemberDto.fromDomain(it) }
    }
}