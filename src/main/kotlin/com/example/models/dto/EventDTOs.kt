package com.example.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class EventCreateRequest(val title: String, val description: String)

@Serializable
data class EventResponse(val id: Long, val title: String, val description: String, val ownerId: Long)
