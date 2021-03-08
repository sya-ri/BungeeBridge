package com.github.syari.bungeebridge.plugin

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
        proxy.pluginManager.registerListener(this, SharePlayerCount)
    }

    override fun onDisable() {
        APIClient.runClearBlocking()
    }
}
