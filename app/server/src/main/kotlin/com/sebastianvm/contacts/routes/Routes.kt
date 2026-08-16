package com.sebastianvm.contacts.routes

import dev.zacsweers.metro.Inject
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

@Inject
class Routes(private val contactRoutes: ContactRoutes) {

    context(application: Application)
    operator fun invoke() {
        with(application) {
            routing {
                contactRoutes()
            }
        }
    }
}
