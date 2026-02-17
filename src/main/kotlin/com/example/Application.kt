package com.example

import com.example.plugins.*
import com.example.routing.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureLogging()
    configureCustomTiming()
    configureStatusPages()
    configureDatabase()
    configureKoin()
    configureSecurity()
    configureRouting()
}
