package com.example.repositories

import com.example.models.tables.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepository {

    suspend fun createUser(username: String, passwordHash: String): Long =
        newSuspendedTransaction(Dispatchers.IO) {
            Users.insertAndGetId {
                it[Users.username] = username
                it[Users.passwordHash] = passwordHash
            }.value
        }

    suspend fun findByUsername(username: String): ResultRow? =
        newSuspendedTransaction(Dispatchers.IO) {
            Users
                .select { Users.username eq username }
                .singleOrNull()
        }

    suspend fun findById(id: Long): ResultRow? =
        newSuspendedTransaction(Dispatchers.IO) {
            Users
                .select { Users.id eq id }
                .singleOrNull()
        }
}
