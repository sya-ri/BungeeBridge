package com.github.syari.bungeebridge.plugin

import net.md_5.bungee.api.plugin.Plugin

@Suppress("unused")
class BungeeBridge : Plugin() {
    override fun onEnable() {
        logger.info("This is Plugin")
    }
}
