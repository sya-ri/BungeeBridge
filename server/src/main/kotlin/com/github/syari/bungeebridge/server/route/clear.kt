package com.github.syari.bungeebridge.server.route

import com.github.syari.bungeebridge.server.PlayerContainer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.ContentTransformationException
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

data class ClearRequest(
    val name: String
)

suspend fun PipelineContext<Unit, ApplicationCall>.clearAction() {
    val (name) = try {
        call.receive<ClearRequest>()
    } catch (ex: ContentTransformationException) {
        return call.respond(HttpStatusCode.BadRequest)
    }
    PlayerContainer.clear(name)
    call.respond(HttpStatusCode.OK)
}
