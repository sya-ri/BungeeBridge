package com.github.syari.bungeebridge.plugin

import com.github.syari.bungeebridge.plugin.command.BungeeGListCommand
import com.github.syari.bungeebridge.plugin.command.GListCommand
import com.github.syari.bungeebridge.plugin.command.ListCommand
import net.md_5.bungee.api.plugin.Plugin

@Suppress("unused")
class BungeeBridge : Plugin() {
    companion object {
        internal lateinit var plugin: Plugin
    }

    init {
        plugin = this
    }

    override fun onEnable() {
        Config.load()
        proxy.scheduler.runAsync(this) {
            APIClient.runClearBlocking()
            APIClient.startAutoUpdate()
        }
        proxy.pluginManager.run {
            registerListener(plugin, SharePlayerCount)
            registerCommand(plugin, ListCommand)
            getPlugin("cmd_list")?.let(::unregisterCommands)
            registerCommand(plugin, GListCommand)
            registerCommand(plugin, BungeeGListCommand)
        }
    }

    override fun onDisable() {
        APIClient.runClearBlocking()
    }
}
