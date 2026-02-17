package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity() {
    val cfg = environment.config
    val issuer = cfg.property("jwt.issuer").getString()
    val audience = cfg.property("jwt.audience").getString()
    val realm = cfg.property("jwt.realm").getString()
    val secret = cfg.property("jwt.secret").getString()

    authentication {
        jwt("auth-jwt") {
            this.realm = realm
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asLong()
                if (userId != null) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
