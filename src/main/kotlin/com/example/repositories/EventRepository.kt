package com.example.repositories

import com.example.models.tables.Events
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class EventRepository {

    suspend fun listAll(): List<ResultRow> =
        newSuspendedTransaction(Dispatchers.IO) {
            Events.selectAll()
                .orderBy(Events.id to SortOrder.DESC)
                .toList()
        }

    suspend fun create(ownerId: Long, title: String, description: String): Long =
        newSuspendedTransaction(Dispatchers.IO) {
            Events.insertAndGetId {
                it[Events.ownerId] = ownerId
                it[Events.title] = title
                it[Events.description] = description
            }.value
        }

    suspend fun deleteIfOwner(eventId: Long, ownerId: Long): Boolean =
        newSuspendedTransaction(Dispatchers.IO) {
            Events.deleteWhere {
                Op.build { (Events.id eq eventId) and (Events.ownerId eq ownerId) }
            } > 0
        }
}
