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
                buildString {
                    list.forEach { (name, data) ->
                        val players = data.players
                        sender.sendMessage("${ChatColor.AQUA}[$name] ${ChatColor.GOLD}(${players.size}): ${ChatColor.RESET}${players.joinToString()}")
                    }
                    sender.sendMessage("${ChatColor.RESET}Total players online: ${list.values.sumBy { it.players.size }}")
                }
            } else {
                sender.sendMessage("${ChatColor.RED}APIの接続に失敗しました")
            }
        }
    }
}
