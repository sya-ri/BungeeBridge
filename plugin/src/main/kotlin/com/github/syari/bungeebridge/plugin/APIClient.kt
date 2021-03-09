package com.github.syari.bungeebridge.plugin

import com.github.syari.bungeebridge.plugin.BungeeBridge.Companion.plugin
import com.github.syari.bungeebridge.shared.ClearRequest
import com.github.syari.bungeebridge.shared.Path
import com.github.syari.bungeebridge.shared.UpdateRequest
import com.github.syari.bungeebridge.shared.UpdateResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
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
        val join = SharePlayerCount.joinPlayers
        val quit = SharePlayerCount.quitPlayers
        val response = call<UpdateResponse>(Path.Update) {
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            body = UpdateRequest(Config.serverName, join, quit)
        } ?: return
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
        call<String>(Path.Clear) {
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            body = ClearRequest(Config.serverName)
        }
    }

    private suspend inline fun <reified T> call(path: String, action: HttpRequestBuilder.() -> Unit): T? {
        val url = Config.url + path
        return try {
            client.request<T>(url, action)
        } catch (ex: ConnectException) {
            plugin.logger.warning("Fail Connection ($url)")
            null
        }
    }
}
