package com.example.infrastructure.member.dynamodb

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class DynamoDbMemberRepository(private val dynamoDbEnhancedAsyncClient: DynamoDbEnhancedAsyncClient) :
    MemberRepository {

    private val memberTable: DynamoDbAsyncTable<DynamoDbMember> =
        dynamoDbEnhancedAsyncClient.table("Members", TableSchema.fromBean(DynamoDbMember::class.java))

    override fun save(member: Member): Mono<Member> {
        val dynamoDbMember = DynamoDbMember.fromDomain(member)
        return Mono.fromCompletionStage(memberTable.putItem(dynamoDbMember))
            .thenReturn(DynamoDbMember.toDomain(dynamoDbMember))
    }

    override fun findById(id: MemberId): Mono<Member> {
        return Mono.fromCompletionStage(memberTable.getItem { r -> r.key { k -> k.partitionValue(id.toString()) } })
            .map { DynamoDbMember.toDomain(it) }
    }

    override fun deleteById(id: MemberId): Mono<Void> {
        return Mono.fromCompletionStage(memberTable.deleteItem { r -> r.key { k -> k.partitionValue(id.toString()) } })
            .then()
    }

    override fun findAll(): Flux<Member> {
        return Flux.from(memberTable.scan())
            .flatMap { scanResult -> Flux.fromIterable(scanResult.items()) }
            .map { DynamoDbMember.toDomain(it) }
    }
}