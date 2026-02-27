package com.sebastianvm.contacts.routes.util

import com.sebastianvm.contacts.routes.ContactsRoute
import com.sebastianvm.contacts.routes.NetworkResponse
import com.sebastianvm.contacts.routes.PostBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.CannotTransformContentToTypeException
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext

/**
 * Extension function for handling POST requests with type-safe route and body parameters.
 *
 * Automatically handles request body parsing, error responses for invalid bodies, and response
 * serialization based on the result status.
 *
 * @param Response The type of the server response.
 * @param R The type of the route resource.
 * @param Body The type of the request body.
 * @param body The handler function that processes the route and body parameters.
 */
inline fun <
    reified Response : NetworkResponse,
    reified R : ContactsRoute<Response>,
    reified Body : PostBody<R>,
> Route.post(noinline body: suspend RoutingContext.(R, Body) -> Response) =
    post<R> { route ->
        val requestBody =
            try {
                call.receive<Body>()
            } catch (_: BadRequestException) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid body"))
                return@post
            } catch (_: CannotTransformContentToTypeException) {
                call.respond(
                    HttpStatusCode.UnsupportedMediaType,
                    ErrorResponse("Body must be JSON."),
                )
                return@post
            }
        val result = body(route, requestBody)
        call.respond(status = result.code, message = result)
    }

/**
 * Extension function for handling GET requests with type-safe route and body parameters.
 *
 * Automatically handles response serialization based on the result status.
 *
 * @param Response The type of the server response.
 * @param R The type of the route resource.
 * @param body The handler function that processes the route parameters.
 */
inline fun <reified Response : NetworkResponse, reified R : ContactsRoute<Response>> Route.get(
    noinline body: suspend RoutingContext.(R) -> Response
) =
    this.get<R> { route ->
        val result = body(route)
        call.respond(status = result.code, message = result)
    }
