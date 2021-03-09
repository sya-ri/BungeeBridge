package com.github.syari.bungeebridge.plugin.command

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender

@Suppress("DEPRECATION")
fun CommandSender.sendPlayerList(players: Map<String, Collection<String>>, serverNameColor: ChatColor) {
    players.forEach { (name, list) ->
        sendMessage("$serverNameColor[$name] ${ChatColor.YELLOW}(${list.size}): ${ChatColor.RESET}${list.sorted().joinToString()}")
    }
    sendMessage("Total players online: ${players.values.sumBy { it.size }}")
}
