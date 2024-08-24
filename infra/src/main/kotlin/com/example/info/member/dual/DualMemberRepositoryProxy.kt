package com.example.info.member.dual

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class DualMemberRepositoryProxy(
    private val cassandraRepository: MemberRepository,
    private val dynamoDbRepository: MemberRepository
) : MemberRepository {

    override fun save(member: Member): Mono<Member> {
        return Mono.zip(
            cassandraRepository.save(member),
            dynamoDbRepository.save(member)
        ).thenReturn(member)
    }

    override fun findById(id: MemberId): Mono<Member> {
        return cassandraRepository.findById(id)
            .flatMap { member ->
                dynamoDbRepository.findById(id)
                    .doOnNext { dynamoMember ->
                        if (member != dynamoMember) {
                            println("Data inconsistency detected for member ID $id between Cassandra and DynamoDB")
                        }
                    }
                    .thenReturn(member)
            }
    }

    override fun deleteById(id: MemberId): Mono<Void> {
        return Mono.zip(
            cassandraRepository.deleteById(id),
            dynamoDbRepository.deleteById(id)
        ).then()
    }

    override fun findAll(): Flux<Member> {
        return cassandraRepository.findAll()
            .flatMap { member ->
                dynamoDbRepository.findById(member.id)
                    .doOnNext { dynamoMember ->
                        if (member != dynamoMember) {
                            println("Data inconsistency detected for member ID ${member.id} between Cassandra and DynamoDB")
                        }
                    }
                    .thenReturn(member)
            }
    }
}