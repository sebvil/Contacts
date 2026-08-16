package com.sebastianvm.contacts

import com.sebastianvm.contacts.config.Config
import com.sebastianvm.contacts.di.AppGraph
import com.sebastianvm.contacts.routes.Routes
import dev.zacsweers.metro.createGraphFactory
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.config.getAs
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.resources.Resources

fun main(args: Array<String>) = EngineMain.main(args)

suspend fun Application.appModule() {
    val config = environment.config.getAs<Config>()
    val graph = createGraphFactory<AppGraph.Factory>().create(config.ktor.database)
    module(graph.routes())
}

fun Application.module(routes: Routes) {
    install(Resources)
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowMethod(HttpMethod.Get)
        // The web app is served from its own webpack dev server port (e.g. localhost:8081),
        // which counts as a different origin than this API (localhost:8080) as far as the
        // browser is concerned.
        anyHost()
    }
    routes()
}
