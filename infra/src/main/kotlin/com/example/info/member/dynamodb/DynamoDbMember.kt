package com.example.info.member.dynamodb

import com.example.domain.model.member.Member
import com.example.domain.model.member.MemberId
import com.example.domain.model.member.MemberStatus
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class DynamoDbMember(
    @get:DynamoDbPartitionKey
    val id: String,
    val name: String,
    val email: String,
    val status: String
) {
    companion object {
        fun fromDomain(member: Member): DynamoDbMember {
            return DynamoDbMember(
                id = member.id.toString(),
                name = member.name,
                email = member.email,
                status = member.getStatus().name
            )
        }

        fun toDomain(dynamoDbMember: DynamoDbMember): Member {
            val member = Member(
                id = MemberId.fromString(dynamoDbMember.id),
                name = dynamoDbMember.name,
                email = dynamoDbMember.email
            )
            if (dynamoDbMember.status == MemberStatus.INACTIVE.name) {
                member.deactivate()
            }
            return member
        }
    }
}