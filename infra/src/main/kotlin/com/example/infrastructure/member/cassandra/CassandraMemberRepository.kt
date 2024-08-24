package com.example.infrastructure.member.cassandra

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class CassandraMemberRepository(
    private val cassandraTemplate: ReactiveCassandraTemplate
) : MemberRepository {

    override fun save(member: Member): Mono<Member> {
        return cassandraTemplate.insert(member)
    }

    override fun findById(id: MemberId): Mono<Member> {
        return cassandraTemplate.selectOneById(id.id, Member::class.java)
    }

    override fun deleteById(id: MemberId): Mono<Void> {
        return cassandraTemplate.deleteById(id.id, Member::class.java).then()
    }

    override fun findAll(): Flux<Member> {
        return cassandraTemplate.select("SELECT * FROM members", Member::class.java)
    }
}