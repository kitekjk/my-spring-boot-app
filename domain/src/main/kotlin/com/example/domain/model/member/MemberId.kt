package com.example.domain.model.member

import java.util.*

data class MemberId(val id: UUID) {
    override fun toString(): String = id.toString()

    companion object {
        fun generate(): MemberId = MemberId(UUID.randomUUID())
        fun fromString(id: String): MemberId = MemberId(UUID.fromString(id))
    }
}