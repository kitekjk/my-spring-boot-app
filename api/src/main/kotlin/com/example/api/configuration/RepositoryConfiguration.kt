package com.example.api.configuration

import com.example.domain.model.member.MemberRepository
import com.example.info.member.cassandra.CassandraMemberRepository
import com.example.info.member.dual.DualMemberRepositoryProxy
import com.example.info.member.dynamodb.DynamoDbMemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfiguration {
    @Bean
    fun memberRepository(
        cassandraRepository: CassandraMemberRepository,
        dynamoDbRepository: DynamoDbMemberRepository
    ): MemberRepository {
        return DualMemberRepositoryProxy(cassandraRepository, dynamoDbRepository)
    }
}