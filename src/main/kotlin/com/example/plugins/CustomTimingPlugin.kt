package com.example.plugins

import io.ktor.server.application.*
import io.ktor.util.*
import kotlin.system.*

private val StartTimeKey = AttributeKey<Long>("start-time")

val TimingHeaderPlugin = createApplicationPlugin("TimingHeaderPlugin") {
    onCall { call ->
        call.attributes.put(StartTimeKey, System.nanoTime())
    }
    onCallRespond { call, _ ->
        val start = call.attributes[StartTimeKey]
        val tookMs = (System.nanoTime() - start) / 1_000_000
        call.response.headers.append("X-Response-Time-Ms", tookMs.toString())
    }
}

fun Application.configureCustomTiming() {
    install(TimingHeaderPlugin)
}
