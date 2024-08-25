package com.example.infrastructure.member.dynamodb

import com.example.domain.model.member.MemberId
import org.springframework.stereotype.Component
import software.amazon.awssdk.enhanced.dynamodb.Key

@Component
class DynamoDbRequestBuilder {
    fun buildKey(memberId: MemberId): Key {
        return Key.builder()
            .partitionValue(memberId.toString())
            .build()
    }
}