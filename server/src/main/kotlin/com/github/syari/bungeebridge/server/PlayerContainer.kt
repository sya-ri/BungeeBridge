package com.github.syari.bungeebridge.server

import java.util.concurrent.ConcurrentHashMap

object PlayerContainer {
    private val servers = ConcurrentHashMap<String, MutableSet<String>>()

    fun update(name: String, join: Set<String>, quit: Set<String>) {
        servers.getOrPut(name, ::mutableSetOf).run {
            removeAll(quit)
            addAll(join)
        }
    }

    fun clear(name: String) {
        servers.remove(name)
    }

    val allPlayerCount
        get() = servers.values.sumBy { it.size }
}
