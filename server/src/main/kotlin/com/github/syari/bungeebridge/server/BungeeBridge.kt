package com.github.syari.bungeebridge.server

import com.github.syari.bungeebridge.server.properties.Option
import com.github.syari.bungeebridge.server.route.clearAction
import com.github.syari.bungeebridge.server.route.listAction
import com.github.syari.bungeebridge.server.route.updateAction
import com.github.syari.bungeebridge.shared.Path
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BungeeBridge {
    val logger: Logger = LoggerFactory.getLogger("BungeeBridgeServer")

    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, port = Option.Port) {
            install(ContentNegotiation) {
                jackson()
            }
            routing {
                post(Path.Update) { updateAction() }
                post(Path.Clear) { clearAction() }
                get(Path.List) { listAction() }
            }
        }.start(wait = true)
    }
}
