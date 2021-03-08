package com.github.syari.bungeebridge.plugin

import com.github.syari.bungeebridge.plugin.BungeeBridge.Companion.plugin
import com.github.syari.bungeebridge.shared.ClearRequest
import com.github.syari.bungeebridge.shared.UpdateRequest
import com.github.syari.bungeebridge.shared.UpdateResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.net.ConnectException
import java.util.concurrent.TimeUnit

object APIClient {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
    }

    fun startAutoUpdate() {
        val task = {
            runBlocking {
                update()
            }
        }
        plugin.proxy.scheduler.schedule(plugin, task, 0, Config.updateTime, TimeUnit.MILLISECONDS)
    }

    private suspend fun update() {
        val url = "${Config.url}/update"
        val join = SharePlayerCount.joinPlayers
        val quit = SharePlayerCount.quitPlayers
        val response = try {
            client.post<UpdateResponse>(url) {
                contentType(ContentType.Application.Json)
                body = UpdateRequest(Config.serverName, join, quit)
            }
        } catch (ex: ConnectException) {
            plugin.logger.warning("Fail Connection ($url)")
            return
        }
        SharePlayerCount.joinPlayers.removeAll(join)
        SharePlayerCount.quitPlayers.removeAll(quit)
        SharePlayerCount.playerCount = response.allCount
    }

    fun runClearBlocking() {
        runBlocking {
            clear()
        }
    }

    private suspend fun clear() {
        val url = "${Config.url}/clear"
        try {
            client.post<String>(url) {
                contentType(ContentType.Application.Json)
                body = ClearRequest(Config.serverName)
            }
        } catch (ex: ConnectException) {
            plugin.logger.warning("Fail Connection ($url)")
            return
        }
    }
}
