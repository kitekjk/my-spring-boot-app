package com.example.infrastructure.member.dynamodb

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

@Configuration
class DynamoDbConfig {

    @Bean
    fun dynamoDbAsyncClient(): DynamoDbAsyncClient {
        return DynamoDbAsyncClient.builder()
            .region(Region.US_EAST_1)  // 사용할 AWS 리전을 지정합니다.
            .credentialsProvider(DefaultCredentialsProvider.create())  // AWS 자격 증명 제공자를 설정합니다.
            .build()
    }

    @Bean
    fun dynamoDbEnhancedAsyncClient(dynamoDbAsyncClient: DynamoDbAsyncClient): DynamoDbEnhancedAsyncClient {
        return DynamoDbEnhancedAsyncClient.builder()
            .dynamoDbClient(dynamoDbAsyncClient)
            .build()
    }
}