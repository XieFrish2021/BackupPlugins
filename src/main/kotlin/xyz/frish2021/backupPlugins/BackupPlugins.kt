package xyz.frish2021.backupPlugins

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.plugin.java.JavaPlugin
import xyz.frish2021.backupPlugins.backup.BackupUtils
import xyz.frish2021.backupPlugins.command.BackupCommand
import xyz.frish2021.backupPlugins.language.Language
import java.io.File

class BackupPlugins : JavaPlugin() {
    private val logger : ConsoleCommandSender  = Bukkit.getConsoleSender()

    override fun onEnable() {
        BackupUtils.initBackupDir()

        // Register Plugins Command
        this.registerCommand()

        //Register Plugins Events
        this.registerEvents()

        logger.sendMessage("${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${Language.get("starting")}")
    }

    override fun onDisable() {
        logger.sendMessage("${ChatColor.WHITE}[${ChatColor.YELLOW}BackupPlugins${ChatColor.WHITE}] - ${Language.get("shutdown")}")
    }

    override fun onLoad() {
        this.loadConfig()

        logger.sendMessage("${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${Language.get("loading")}")
    }

    private fun loadConfig() {
        this.saveDefaultConfig()
        this.createCacheDir()
        this.createBackupDir();

        this.saveResource("language/zh_CN.json", false)
    }

    private fun createBackupDir() {
        val backupDir = File(this.dataFolder, "backup")
        if (!backupDir.exists()) {
            backupDir.mkdirs()
        }
    }

    private fun createCacheDir() {
        val cacheDir = File(this.dataFolder, ".cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
    }

    private fun registerCommand() {
        this.getCommand("backup").executor = BackupCommand()
        this.getCommand("backup").tabCompleter = BackupCommand()
    }

    private fun registerEvents() {}
}