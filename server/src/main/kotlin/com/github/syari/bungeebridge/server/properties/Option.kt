package com.github.syari.bungeebridge.server.properties

object Option : PropertiesFile("option.properties") {
    private val PortAsString by keyValue("Port")

    val Port = PortAsString.toIntOrNull() ?: 8080
}
