package com.github.syari.bungeebridge.plugin

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import java.util.concurrent.ConcurrentHashMap

object SharePlayerCount : Listener {
    var playerCount = 0

    val players = ConcurrentHashMap<String, String>()

    @EventHandler
    fun on(event: ProxyPingEvent) {
        event.response.players.online = playerCount
    }

    @EventHandler
    fun on(event: ServerConnectedEvent) {
        val playerName = event.player.name
        players[playerName] = event.server.info.name
    }

    @EventHandler
    fun on(event: PlayerDisconnectEvent) {
        val playerName = event.player.name
        players[playerName] = ""
    }
}
