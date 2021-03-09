package com.github.syari.bungeebridge.plugin.command

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

object BungeeGListCommand : Command(BungeeGList.name, BungeeGList.permission, *BungeeGList.aliases) {
    @Suppress("DEPRECATION")
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val players = ProxyServer.getInstance().servers.values.associate {
            it.name to it.players.map(ProxiedPlayer::getDisplayName)
        }
        sender.sendPlayerList(players, ChatColor.GREEN)
    }
}
