package com.github.syari.bungeebridge.server.route

import com.github.syari.bungeebridge.server.BungeeBridge
import com.github.syari.bungeebridge.server.PlayerContainer
import com.github.syari.bungeebridge.shared.UpdateRequest
import com.github.syari.bungeebridge.shared.UpdateResponse
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.ContentTransformationException
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.updateAction() {
    val (name, players) = try {
        call.receive<UpdateRequest>()
    } catch (ex: ContentTransformationException) {
        return call.respond(HttpStatusCode.BadRequest)
    }
    val playerInfo = buildString {
        players.forEach { (playerName, serverName) ->
            append(" $playerName-$serverName")
        }
    }
    BungeeBridge.logger.info("$name:$playerInfo")
    val firstRequest = PlayerContainer.request(name)
    PlayerContainer.update(name, players)
    call.respond(UpdateResponse(PlayerContainer.allPlayerCount, firstRequest))
}
