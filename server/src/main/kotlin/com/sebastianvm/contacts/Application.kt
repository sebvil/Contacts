package com.sebastianvm.contacts

import com.sebastianvm.contacts.routes.contactRoutes
import io.ktor.resources.Resource
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(factory = Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Resources)
    install(ContentNegotiation) {
        json()
    }
    routing {
        contactRoutes()
    }
}
