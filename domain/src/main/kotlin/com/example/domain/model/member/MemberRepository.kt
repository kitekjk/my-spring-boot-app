package com.example.domain.model.member

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface MemberRepository {
    fun save(member: Member): Mono<Member>
    fun findById(id: MemberId): Mono<Member>
    fun deleteById(id: MemberId): Mono<Void>
    fun findAll(): Flux<Member>
}