package xyz.frish2021.backupPlugins.backup.thread

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable
import xyz.frish2021.backupPlugins.backup.BackupUtils
import xyz.frish2021.backupPlugins.language.Language
import kotlin.jvm.Throws

class BackingFileThread(private val sender: CommandSender) : BukkitRunnable() {
    override fun run() {
        try {
            BackupUtils.saveBackup(sender)
        } catch (e : Exception) {
            sender.sendMessage("${ChatColor.WHITE}[${ChatColor.RED}BackupPlugins${ChatColor.WHITE}] - ${Language.get("backed_error")}")
        }

        this.cancel()
    }
}