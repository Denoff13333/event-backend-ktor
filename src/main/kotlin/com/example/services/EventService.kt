package com.example.services

import com.example.models.dto.EventResponse
import com.example.models.tables.Events
import com.example.repositories.EventRepository
import com.example.repositories.UserRepository

class EventService(
    private val events: EventRepository,
    private val users: UserRepository
) {
    suspend fun listAll(): List<EventResponse> =
        events.listAll().map {
            EventResponse(
                id = it[Events.id].value,
                title = it[Events.title],
                description = it[Events.description],
                ownerId = it[Events.ownerId].value
            )
        }

    suspend fun create(ownerId: Long, title: String, description: String): Long {
        require(title.isNotBlank() && title.length <= 140) { "Title must be 1..140 chars" }
        require(description.isNotBlank()) { "Description must be non-empty" }
        require(users.findById(ownerId) != null) { "User not found" }
        return events.create(ownerId, title.trim(), description.trim())
    }

    suspend fun delete(eventId: Long, ownerId: Long): Boolean =
        events.deleteIfOwner(eventId, ownerId)
}
