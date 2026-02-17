package com.example.models.tables

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Events : LongIdTable("events") {
    val title = varchar("title", 140)
    val description = text("description")
    val ownerId = reference("owner_id", Users, onDelete = ReferenceOption.CASCADE)
}
