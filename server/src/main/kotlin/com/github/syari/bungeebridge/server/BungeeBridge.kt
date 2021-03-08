package com.github.syari.bungeebridge.server

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BungeeBridge {
    val logger: Logger = LoggerFactory.getLogger("BungeeBridgeServer")

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("This is Server")
    }
}
