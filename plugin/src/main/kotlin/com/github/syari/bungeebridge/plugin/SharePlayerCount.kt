package com.github.syari.bungeebridge.plugin

import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object SharePlayerCount : Listener {
    var playerCount = 0

    val joinPlayers = mutableSetOf<String>()
    val quitPlayers = mutableSetOf<String>()

    @EventHandler
    fun on(event: ProxyPingEvent) {
        event.response.players.online = playerCount
    }

    @EventHandler
    fun on(event: PostLoginEvent) {
        val playerName = event.player.name
        joinPlayers.add(playerName)
        quitPlayers.remove(playerName)
    }

    @EventHandler
    fun on(event: PlayerDisconnectEvent) {
        val playerName = event.player.name
        if (joinPlayers.remove(playerName).not()) {
            quitPlayers.add(event.player.name)
        }
    }
}
