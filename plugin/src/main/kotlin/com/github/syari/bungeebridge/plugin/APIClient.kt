package com.github.syari.bungeebridge.plugin

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.github.syari.bungeebridge.plugin.BungeeBridge.Companion.plugin
import com.github.syari.bungeebridge.shared.ClearRequest
import com.github.syari.bungeebridge.shared.ListResponse
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
import net.md_5.bungee.api.ProxyServer
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
        val players = SharePlayerCount.players
        val response = update(players)
        if (response != null && response.all) {
            update(
                ProxyServer.getInstance().players.associate {
                    it.name to it.server.info.name
                }
            )
        }
    }

    private suspend fun update(players: Map<String, String>): UpdateResponse? {
        return call<UpdateResponse>(Path.Update) {
            method = HttpMethod.Post
            contentType(ContentType.Application.Json)
            body = UpdateRequest(Config.serverName, players)
        }?.apply {
            players.forEach { SharePlayerCount.players.remove(it.key) }
            SharePlayerCount.playerCount = allCount
        }
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

    suspend fun list(): ListResponse? {
        return call(Path.List) {
            method = HttpMethod.Get
        }
    }

    private suspend inline fun <reified T> call(path: String, action: HttpRequestBuilder.() -> Unit): T? {
        val url = Config.url + path
        return try {
            client.request<T>(url, action)
        } catch (ex: ConnectException) {
            plugin.logger.warning("Fail Connection ($url)")
            null
        } catch (ex: MismatchedInputException) {
            plugin.logger.warning("Mismatched Input ($url)")
            null
        }
    }
}
