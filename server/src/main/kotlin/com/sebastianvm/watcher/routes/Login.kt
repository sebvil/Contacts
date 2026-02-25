package com.sebastianvm.watcher.routes

import com.sebastianvm.watcher.data.UserRepository
import com.sebastianvm.watcher.routes.util.post
import io.ktor.server.routing.Route

/**
 * Configures the login route for user authentication.
 *
 * @param userRepository The repository to handle user authentication.
 */
fun Route.login(userRepository: UserRepository) {
    post<Login.Response, Login, Login.Body> { _, body ->
        userRepository.login(body.username, body.password)
    }
}
