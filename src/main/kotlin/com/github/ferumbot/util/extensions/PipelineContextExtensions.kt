package com.github.ferumbot.util.extensions

import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend inline fun <reified T> PipelineContext<Unit, ApplicationCall>.receive(): T {
    return call.receive()
}

suspend inline fun <reified T: Any> PipelineContext<Unit, ApplicationCall>.send(response: T) {
    return call.respond(response)
}


fun PipelineContext<Unit, ApplicationCall>.pathParameter(name: String): String? {
    return call.parameters[name]
}
