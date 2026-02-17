package com.example.routing

import com.example.models.dto.*
import com.example.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val auth by inject<AuthService>()

    route("/auth") {
        post("/register") {
            val req = call.receive<RegisterRequest>()
            val userId = auth.register(req.username, req.password)
            call.respond(HttpStatusCode.Created, mapOf("userId" to userId))
        }

        post("/login") {
            val req = call.receive<LoginRequest>()
            val token = auth.login(req.username, req.password, call.application.environment.config)
            call.respond(TokenResponse(token))
        }
    }
}
