package com.github.syari.bungeebridge.server.properties

object Option : PropertiesFile("option.properties") {
    private val PortAsString by keyValue("Port")
    private val CrashTimeString by keyValue("CrashTime")

    val Port = PortAsString.toIntOrNull() ?: 8080
    val CrashTime = CrashTimeString.toLongOrNull() ?: 30000L
}
