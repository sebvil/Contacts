package com.sebastianvm.contacts.routes

import com.sebastianvm.contacts.data.UserRepository
import com.sebastianvm.contacts.routes.util.get
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import kotlin.uuid.Uuid

fun Route.home(userRepository: UserRepository) {
    get<Home.Response, Home> { _ ->
        val userId =
            call.principal<JWTPrincipal>()?.payload?.subject
                ?: return@get Home.Response.Unauthorized
        val username =
            userRepository.getUsername(Uuid.parse(userId)) ?: return@get Home.Response.UserNotFound
        Home.Response.Success(username)
    }
}
