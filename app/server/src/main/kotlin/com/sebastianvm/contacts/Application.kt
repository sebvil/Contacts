package com.sebastianvm.contacts

import com.sebastianvm.contacts.config.Config
import com.sebastianvm.contacts.di.AppGraph
import com.sebastianvm.contacts.routes.Routes
import dev.zacsweers.metro.createGraphFactory
import io.ktor.http.HttpMethod
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.DefaultJson
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.getAs
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecodingException

fun main(args: Array<String>) = EngineMain.main(args)

suspend fun Application.appModule() {
    val config = environment.config.getAs<Config>()
    val graph = createGraphFactory<AppGraph.Factory>().create(config.ktor.database)
    module(graph.routes())
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.module(routes: Routes) {
    install(Resources)
    install(ContentNegotiation) {
        json(
            json =
                Json(from = DefaultJson) {
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
        )
    }
    install(CORS) {
        allowMethod(HttpMethod.Get)
        // The web app is served from its own webpack dev server port (e.g. localhost:8081),
        // which counts as a different origin than this API (localhost:8080) as far as the
        // browser is concerned.
        anyHost()
    }

    install(StatusPages) {
        exception<BadRequestException> { call, e ->
            val cause =
                (e.cause as? JsonConvertException)?.cause
                    ?: run {
                        this@module.log.trace("Bad request of type: {}", e::class)
                        call.respondText("Bad request")
                    }
            when (cause) {
                is MissingFieldException -> {
                    call.respondText(
                        "Missing required fields: ${cause.missingFields.joinToString()}"
                    )
                }

                is JsonDecodingException -> {
                    call.respondText(cause.message)
                }

                else -> {
                    this@module.log.trace("Bad request of type: {}.", cause)
                    call.respondText("Bad request")
                }
            }
        }
    }
    routes()
}
