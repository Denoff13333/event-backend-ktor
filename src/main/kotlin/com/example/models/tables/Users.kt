package com.example.models.tables

import org.jetbrains.exposed.dao.id.LongIdTable

object Users : LongIdTable("users") {
    val username = varchar("username", 64).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
}
