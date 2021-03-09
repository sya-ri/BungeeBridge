package com.github.syari.bungeebridge.server

import com.github.syari.bungeebridge.server.properties.Option
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.timerTask

object PlayerContainer {
    private val servers = ConcurrentHashMap<String, ConcurrentHashMap<String, String>>()
    private val crashWatchdogTasks = ConcurrentHashMap<String, TimerTask>()

    fun update(name: String, players: Map<String, String>) {
        servers.getOrPut(name, ::ConcurrentHashMap).run {
            players.forEach { (playerName, serverName) ->
                if (serverName.isEmpty()) {
                    remove(playerName)
                } else {
                    put(playerName, serverName)
                }
            }
        }
        crashWatchdogTasks[name]?.cancel()
        crashWatchdogTasks[name] = timerTask {
            clear(name)
            crashWatchdogTasks.remove(name)
            BungeeBridge.logger.info("$name is Not Updated. Delete All Players.")
        }.apply {
            Timer().schedule(this, Option.CrashTime)
        }
    }

    fun clear(name: String) {
        servers.remove(name)
    }

    val allPlayer
        get() = servers.toMap()

    val allPlayerCount
        get() = servers.values.sumBy { it.size }
}
