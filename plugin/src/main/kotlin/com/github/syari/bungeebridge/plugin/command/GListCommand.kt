package com.github.syari.bungeebridge.plugin.command

import com.github.syari.bungeebridge.plugin.APIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

object GListCommand : Command(GList.name, GList.permission, *GList.aliases) {
    @Suppress("DEPRECATION")
    override fun execute(sender: CommandSender, args: Array<out String>) {
        GlobalScope.launch(Dispatchers.IO) {
            val list = APIClient.list()
            if (list != null) {
                val players = mutableMapOf<String, MutableSet<String>>().apply {
                    list.values.forEach {
                        it.players.forEach { (playerName, serverName) ->
                            getOrPut(serverName, ::mutableSetOf).add(playerName)
                        }
                    }
                }
                sender.sendPlayerList(players, ChatColor.LIGHT_PURPLE)
            } else {
                sender.sendMessage("${ChatColor.RED}APIの接続に失敗しました")
            }
        }
    }
}
