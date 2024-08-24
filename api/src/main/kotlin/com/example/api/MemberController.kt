package com.example.api

import com.example.application.member.MemberCommandService
import com.example.application.member.MemberQueryService
import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberCommandService: MemberCommandService,
    private val memberQueryService: MemberQueryService
) {

    @PostMapping
    fun createMember(@RequestBody request: CreateMemberRequest): Mono<Member> {
        return memberCommandService.createMember(request.name, request.email)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: UUID): Mono<Member> {
        return memberQueryService.getMember(MemberId(id))
    }

    @PutMapping("/{id}")
    fun updateMember(
        @PathVariable id: UUID,
        @RequestBody request: UpdateMemberRequest
    ): Mono<Member> {
        return memberCommandService.updateMember(MemberId(id), request.name, request.email)
    }

    @DeleteMapping("/{id}")
    fun deleteMember(@PathVariable id: UUID): Mono<Void> {
        return memberCommandService.deleteMember(MemberId(id))
    }

    @PostMapping("/{id}/activate")
    fun activateMember(@PathVariable id: UUID): Mono<Member> {
        return memberCommandService.activateMember(MemberId(id))
    }

    @PostMapping("/{id}/deactivate")
    fun deactivateMember(@PathVariable id: UUID): Mono<Member> {
        return memberCommandService.deactivateMember(MemberId(id))
    }

    @GetMapping
    fun getAllMembers(): Flux<Member> {
        return memberQueryService.getAllMembers()
    }
}

data class CreateMemberRequest(val name: String, val email: String)
data class UpdateMemberRequest(val name: String, val email: String)