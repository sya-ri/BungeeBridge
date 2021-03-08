package com.github.syari.bungeebridge.server.route

import com.github.syari.bungeebridge.server.BungeeBridge
import com.github.syari.bungeebridge.server.PlayerContainer
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.ContentTransformationException
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

data class UpdateRequest(
    val name: String,
    val join: Set<String> = emptySet(),
    val quit: Set<String> = emptySet()
)

data class UpdateResponse(
    val allCount: Int
)

suspend fun PipelineContext<Unit, ApplicationCall>.updateAction() {
    val (name, join, quit) = try {
        call.receive<UpdateRequest>()
    } catch (ex: ContentTransformationException) {
        return call.respond(HttpStatusCode.BadRequest)
    }
    BungeeBridge.logger.info("$name: +(${join.joinToString()}) -(${quit.joinToString()})")
    PlayerContainer.update(name, join, quit)
    call.respond(UpdateResponse(PlayerContainer.allPlayerCount))
}
