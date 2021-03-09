package com.github.syari.bungeebridge.server.route

import com.github.syari.bungeebridge.server.PlayerContainer
import com.github.syari.bungeebridge.shared.ListResponse
import com.github.syari.bungeebridge.shared.ServerData
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.listAction() {
    val allPlayers: ListResponse = PlayerContainer.allPlayer.mapValues { ServerData(it.value) }
    call.respond(allPlayers)
}
