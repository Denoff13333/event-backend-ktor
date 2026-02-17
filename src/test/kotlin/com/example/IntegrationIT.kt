package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.*

class IntegrationIT {

    private fun testConfig(): ApplicationConfig =
        ApplicationConfig("application-test.yaml")

    @Test
    fun `register then login returns token`() = testApplication {
        environment {
            config = testConfig()
        }
        application { module() }

        val reg = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"u1","password":"secret123"}""")
        }
        assertEquals(HttpStatusCode.Created, reg.status)

        val login = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"username":"u1","password":"secret123"}""")
        }
        assertEquals(HttpStatusCode.OK, login.status)
        val body = login.bodyAsText()
        assertTrue(body.contains("token"), "Expected token in response: $body")
    }

    @Test
    fun `create event requires token`() = testApplication {
        environment { config = testConfig() }
        application { module() }

        val create = client.post("/events") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"t","description":"d"}""")
        }
        assertEquals(HttpStatusCode.Unauthorized, create.status)
    }
}
