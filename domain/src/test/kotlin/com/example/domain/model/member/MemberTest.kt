package com.example.domain.model.member

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MemberTest {

    @Test
    fun `should activate member successfully`() {
        // Given
        val member = Member(name = "John Doe", email = "john.doe@example.com")

        // When
        member.activate()

        // Then
        assertEquals(MemberStatus.ACTIVE, member.getStatus())
    }

    @Test
    fun `should deactivate member successfully`() {
        // Given
        val member = Member(name = "John Doe", email = "john.doe@example.com")

        // When
        member.deactivate()

        // Then
        assertEquals(MemberStatus.INACTIVE, member.getStatus())
    }

    @Test
    fun `should not change status if already active`() {
        // Given
        val member = Member(name = "John Doe", email = "john.doe@example.com")

        // When
        member.activate()

        // Then
        assertEquals(MemberStatus.ACTIVE, member.getStatus())
    }
}