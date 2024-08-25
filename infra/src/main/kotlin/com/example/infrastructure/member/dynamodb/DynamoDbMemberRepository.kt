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
class DynamoDbMemberRepository(
    private val dynamoDbClient: DynamoDbEnhancedAsyncClient,
    private val requestBuilder: DynamoDbRequestBuilder
) :MemberRepository {

    private val memberTable: DynamoDbAsyncTable<DynamoDbMember> =
        dynamoDbClient.table("Members", TableSchema.fromBean(DynamoDbMember::class.java))

    override fun save(member: Member): Mono<Member> {
        val dynamoDbMember = DynamoDbMember.fromDomain(member)
        return Mono.fromCompletionStage(memberTable.putItem(dynamoDbMember))
            .thenReturn(DynamoDbMember.toDomain(dynamoDbMember))
    }

    override fun findById(id: MemberId): Mono<Member> {
        val key = requestBuilder.buildKey(id)
        val future = memberTable.getItem(key)

        // Add logging to inspect the future
        return Mono.fromFuture { future }
            .doOnNext { dynamoDbMember -> println("Fetched member: $dynamoDbMember") }
            .flatMap { dynamoDbMember ->
                if (dynamoDbMember != null) {
                    Mono.just(DynamoDbMember.toDomain(dynamoDbMember))
                } else {
                    Mono.empty()
                }
            }
            .doOnError { error -> println("Error occurred: $error") }
            .switchIfEmpty(Mono.error(NoSuchElementException("Member not found with id: $id")))
    }

    override fun deleteById(id: MemberId): Mono<Void> {
        val key = requestBuilder.buildKey(id)
        return Mono.fromCompletionStage(memberTable.deleteItem(key))
            .then()
    }

    override fun findAll(): Flux<Member> {
        return Flux.from(memberTable.scan())
            .flatMap { scanResult -> Flux.fromIterable(scanResult.items()) }
            .map { DynamoDbMember.toDomain(it) }
    }
}