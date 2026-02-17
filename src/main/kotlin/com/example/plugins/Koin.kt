package com.example.plugins

import com.example.repositories.EventRepository
import com.example.repositories.UserRepository
import com.example.services.AuthService
import com.example.services.EventService
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            module {
                single { UserRepository() }
                single { EventRepository() }
                single { AuthService(get()) }
                single { EventService(get(), get()) }
            }
        )
    }
}
