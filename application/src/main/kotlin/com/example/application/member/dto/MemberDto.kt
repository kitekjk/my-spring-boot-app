package com.example.application.member.dto

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberStatus
import java.util.*

data class MemberDto(
    val id: String,
    val name: String,
    val email: String,
    val status: String
) {
    companion object {
        fun fromDomain(member: Member): MemberDto {
            return MemberDto(
                id = member.id.id.toString(),  // UUID 문자열로 변환
                name = member.name,
                email = member.email,
                status = member.getStatus().name
            )
        }
    }

    fun toDomain(): Member {
        return Member(
            id = MemberId(UUID.fromString(id)),  // DTO에서 도메인 모델로 변환
            name = name,
            email = email,
            status = MemberStatus.valueOf(status)
        )
    }
}