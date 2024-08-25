package com.example.infrastructure.member.dynamodb

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import reactor.test.StepVerifier
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import java.util.*
import java.util.concurrent.CompletableFuture

class DynamoDbMemberRepositoryTest {

    private lateinit var dynamoDbClient: DynamoDbEnhancedAsyncClient
    private lateinit var memberTable: DynamoDbAsyncTable<DynamoDbMember>
    private lateinit var dynamoDbMemberRepository: DynamoDbMemberRepository
    private lateinit var requestBuilder: DynamoDbRequestBuilder

    @BeforeEach
    fun setUp() {
        dynamoDbClient = mock()
        memberTable = mock()
        requestBuilder = mock()
        whenever(dynamoDbClient.table("Members", TableSchema.fromBean(DynamoDbMember::class.java)))
            .thenReturn(memberTable)

        dynamoDbMemberRepository = DynamoDbMemberRepository(dynamoDbClient, requestBuilder)
    }

    @Test
    fun `should save a member successfully`() {
        val member = Member(name = "John Doe", email = "john.doe@example.com")
        val dynamoDbMember = DynamoDbMember.fromDomain(member)
        whenever(memberTable.putItem(dynamoDbMember)).thenReturn(CompletableFuture.completedFuture(any()))

        val result = dynamoDbMemberRepository.save(member)

        StepVerifier.create(result)
            .expectNextMatches { it.name == "John Doe" }
            .verifyComplete()
    }

    @Test
    fun `should find a member by id successfully`() {
        val memberId = MemberId(UUID.randomUUID())
        val dynamoDbMember = DynamoDbMember(
            id = memberId.toString(),
            name = "John Doe",
            email = "john.doe@example.com",
            status = "ACTIVE"
        )

        val key = mock<Key>()
        whenever(requestBuilder.buildKey(memberId)).thenReturn(key)
        whenever(memberTable.getItem(key)).thenReturn(CompletableFuture.completedFuture(dynamoDbMember))

        val result = dynamoDbMemberRepository.findById(memberId)

        StepVerifier.create(result)
            .expectNextMatches { it.id == memberId && it.name == "John Doe" }
            .verifyComplete()
    }

    @Test
    fun `should delete a member by id successfully`() {
        val memberId = MemberId(UUID.randomUUID())

        val key = mock<Key>()
        whenever(requestBuilder.buildKey(memberId)).thenReturn(key)
        whenever(memberTable.deleteItem(key)).thenReturn(CompletableFuture.completedFuture(null))

        val result = dynamoDbMemberRepository.deleteById(memberId)

        StepVerifier.create(result)
            .verifyComplete()
    }
}