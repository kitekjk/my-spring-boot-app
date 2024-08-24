package com.example.api.configuration

import com.example.application.member.MemberCommandService
import com.example.application.member.MemberQueryService
import com.example.domain.model.member.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServiceConfiguration {
    @Bean
    fun memberCommandService(memberRepository: MemberRepository): MemberCommandService {
        return MemberCommandService(memberRepository)
    }

    @Bean
    fun memberQueryService(memberRepository: MemberRepository): MemberQueryService {
        return MemberQueryService(memberRepository)
    }
}