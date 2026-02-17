package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.tables.Users
import com.example.repositories.UserRepository
import io.ktor.server.config.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

class AuthService(private val users: UserRepository) {

    suspend fun register(username: String, password: String): Long {
        require(username.length in 3..64) { "Username must be 3..64 chars" }
        require(password.length >= 6) { "Password must be >= 6 chars" }

        val existing = users.findByUsername(username)
        require(existing == null) { "Username already taken" }

        val hash = BCrypt.hashpw(password, BCrypt.gensalt())
        return users.createUser(username, hash)
    }

    suspend fun login(username: String, password: String, cfg: ApplicationConfig): String {
        val row = users.findByUsername(username) ?: throw IllegalArgumentException("Invalid credentials")
        val userId = row[Users.id].value
        val hash = row[Users.passwordHash]
        require(BCrypt.checkpw(password, hash)) { "Invalid credentials" }

        val issuer = cfg.property("jwt.issuer").getString()
        val audience = cfg.property("jwt.audience").getString()
        val secret = cfg.property("jwt.secret").getString()
        val expiresMs = cfg.property("jwt.expiresMs").getString().toLong()

        return JWT.create()
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + expiresMs))
            .sign(Algorithm.HMAC256(secret))
    }
}
