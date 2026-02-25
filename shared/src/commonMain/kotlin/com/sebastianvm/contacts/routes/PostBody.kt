package com.sebastianvm.contacts.routes

/**
 * Marker interface for POST request bodies.
 *
 * @param R The route type this body is associated with.
 */
interface PostBody<R : ContactsRoute<*>>
