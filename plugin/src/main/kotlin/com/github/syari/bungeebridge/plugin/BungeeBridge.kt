package com.github.syari.bungeebridge.plugin

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
        APIClient.startAutoUpdate()
        proxy.pluginManager.run {
            registerListener(plugin, SharePlayerCount)
            registerCommand(plugin, ListCommand)
        }
    }

    override fun onDisable() {
        APIClient.runClearBlocking()
    }
}
