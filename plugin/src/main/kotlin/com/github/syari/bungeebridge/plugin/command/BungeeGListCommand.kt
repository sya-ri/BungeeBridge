package com.github.syari.bungeebridge.plugin.command

import net.md_5.bungee.Util
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

object BungeeGListCommand : Command(BungeeGList.name, BungeeGList.permission, *BungeeGList.aliases) {
    @Suppress("DEPRECATION")
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val proxyServer = ProxyServer.getInstance()
        proxyServer.servers.values.forEach { server ->
            if (server.canAccess(sender)) {
                val players = server.players.map(ProxiedPlayer::getDisplayName).sorted()
                sender.sendMessage(proxyServer.getTranslation("command_list", server.name, server.players.size, Util.format(players, "${ChatColor.RESET}, ")))
            }
        }

        sender.sendMessage(proxyServer.getTranslation("total_players", ProxyServer.getInstance().onlineCount))
    }
}
