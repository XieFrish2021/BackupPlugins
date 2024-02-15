package xyz.frish2021.backupPlugins.command

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin
import xyz.frish2021.backupPlugins.BackupPlugins
import xyz.frish2021.backupPlugins.backup.thread.BackingFileThread
import xyz.frish2021.backupPlugins.backup.thread.BackingFileThreadToWorld
import xyz.frish2021.backupPlugins.language.Language

class BackupCommand : CommandExecutor, TabExecutor {
    val plugin: JavaPlugin = JavaPlugin.getProvidingPlugin(BackupPlugins::class.java)

    override fun onCommand(
        sender: CommandSender?,
        command: Command?,
        string: String?,
        args: Array<out String>?
    ): Boolean {
        if (sender?.isOp!!) {
            if (args!!.isEmpty()) {
                for (i in 1..5) {
                    sender.sendMessage(
                        "${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${
                            Language.get(
                                "help_${i}"
                            )
                        }"
                    )
                }
            } else if (args.size == 1) {
                val arg = args[0]
                when (arg) {
                    "reload" -> {
                        sender.sendMessage(
                            "${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${
                                Language.get(
                                    "reloading"
                                )
                            }"
                        )

                        Thread.sleep(2000)

                        plugin.server.pluginManager.disablePlugin(plugin)
                        plugin.server.pluginManager.enablePlugin(plugin)
                        plugin.reloadConfig()

                        Thread.sleep(2000)

                        sender.sendMessage(
                            "${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${
                                Language.get(
                                    "reloaded"
                                )
                            }"
                        )
                    }

                    "save" -> {
                        commandBacking(sender)
                        Thread.sleep(1000)
                        BackingFileThread(sender).runTaskAsynchronously(plugin)
                        Thread.sleep(1000)
                        commandBacked(sender)
                    }

                    else -> {
                        commandError(sender)
                    }
                }
            } else if (args.size == 2) {
                val arg = args[0]
                val arg2 = args[1]

                when (arg) {
                    "saveWorld" -> {
                        commandBacking(sender)

                        Thread.sleep(1000)
                        BackingFileThreadToWorld(sender, arg2).runTaskAsynchronously(plugin)
                        Thread.sleep(1000)
                    }

                    else -> {
                        commandError(sender)
                    }
                }
            } else {
                commandError(sender)
            }
        } else {
            sender.sendMessage("${ChatColor.WHITE}[${ChatColor.RED}BackupPlugins${ChatColor.WHITE}] - ${Language.get("permissions_error")}")
        }
        return false
    }

    override fun onTabComplete(
        p0: CommandSender?,
        p1: Command?,
        p2: String?,
        p3: Array<out String>?
    ): MutableList<String>? {
        if (p3!!.size == 1) {
            val list : MutableList<String> = ArrayList()
            list.add("reload")
            list.add("save")

            return list
        } else {
            return null
        }
    }

    private fun commandError(sender: CommandSender) {
        sender.sendMessage("${ChatColor.WHITE}[${ChatColor.RED}BackupPlugins${ChatColor.WHITE}] - ${Language.get("command_error")}")
    }

    private fun commandBacking(sender: CommandSender) {
        sender.sendMessage("${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${Language.get("backing")}")
    }

    private fun commandBacked(sender: CommandSender) {
        sender.sendMessage("${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${Language.get("backed")}")
    }
}