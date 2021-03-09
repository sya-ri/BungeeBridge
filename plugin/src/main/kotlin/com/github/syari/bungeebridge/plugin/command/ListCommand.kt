package com.github.syari.bungeebridge.plugin.command

import com.github.syari.bungeebridge.plugin.APIClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

object ListCommand : Command(List.name, List.permission) {
    @Suppress("DEPRECATION")
    override fun execute(sender: CommandSender, args: Array<out String>) {
        GlobalScope.launch(Dispatchers.IO) {
            val list = APIClient.list()
            if (list != null) {
                val players = mutableMapOf<String, Set<String>>().apply {
                    list.forEach { (name, serverData) ->
                        put(name, serverData.players.keys)
                    }
                }
                sender.sendPlayerList(players, ChatColor.AQUA)
            } else {
                sender.sendMessage("${ChatColor.RED}APIの接続に失敗しました")
            }
        }
    }
}
