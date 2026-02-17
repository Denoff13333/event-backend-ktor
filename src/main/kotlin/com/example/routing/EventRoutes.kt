package com.example.routing

import com.example.models.dto.EventCreateRequest
import com.example.services.EventService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private fun JWTPrincipal.userId(): Long =
    this.payload.getClaim("userId").asLong()

fun Route.eventRoutes() {
    val events by inject<EventService>()

    route("/events") {
        get {
            call.respond(events.listAll())
        }

        authenticate("auth-jwt") {
            post {
                val principal = call.principal<JWTPrincipal>()!!
                val ownerId = principal.userId()
                val req = call.receive<EventCreateRequest>()
                val id = events.create(ownerId, req.title, req.description)
                call.respond(HttpStatusCode.Created, mapOf("eventId" to id))
            }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()!!
                val ownerId = principal.userId()
                val id = call.parameters["id"]?.toLongOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid id"))

                val ok = events.delete(id, ownerId)
                if (ok) call.respond(HttpStatusCode.NoContent)
                else call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Not owner or not found"))
            }
        }
    }
}
