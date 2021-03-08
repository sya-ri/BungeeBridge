package com.github.syari.bungeebridge.plugin

import com.github.syari.bungeebridge.plugin.BungeeBridge.Companion.plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.util.UUID

object Config {
    private const val DefaultUpdateTime = 1000L

    lateinit var url: String
    lateinit var serverName: String
    var updateTime = DefaultUpdateTime

    fun load() {
        val file = plugin.dataFolder.apply(File::mkdirs).resolve("config.yml").apply(File::createNewFile)
        val provider = ConfigurationProvider.getProvider(YamlConfiguration::class.java)
        val configuration: Configuration = provider.load(file)
        url = configuration.getString("url").ifEmpty {
            "http://localhost:8080".apply {
                configuration.set("url", this)
            }
        }
        serverName = configuration.getString("serverName").ifEmpty {
            UUID.randomUUID().toString().apply {
                configuration.set("serverName", this)
            }
        }
        updateTime = configuration.getLong("updateTime")
        if (updateTime < 1) {
            updateTime = DefaultUpdateTime.apply {
                configuration.set("updateTime", this)
            }
        }
        provider.save(configuration, file)
    }
}
