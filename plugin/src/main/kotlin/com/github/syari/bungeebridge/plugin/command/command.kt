package com.github.syari.bungeebridge.plugin.command

object List {
    const val name = "blist"
    const val permission = "bridge.admin"
}

object GList {
    const val name = "glist"
    const val permission = "bungeecord.command.list"
    val aliases = arrayOf("gblist")
}

object BungeeGList {
    const val name = "bungee:glist"
    const val permission = "bungeecord.command.list"
    val aliases = arrayOf("glist:bungee")
}
