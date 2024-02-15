package xyz.frish2021.backupPlugins.backup

import com.google.gson.GsonBuilder
import org.apache.commons.io.FileUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import xyz.frish2021.backupPlugins.BackupPlugins
import xyz.frish2021.backupPlugins.language.Language
import xyz.frish2021.backupPlugins.time.TimeUtils
import xyz.frish2021.backupPlugins.zip.ZipUtils
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.jvm.Throws

class BackupUtils {
    companion object {
        private val plugin = JavaPlugin.getProvidingPlugin(BackupPlugins::class.java)
        private val serverRootDir = File(System.getProperty("user.dir"))
        private val backupRootDir = File(plugin.dataFolder, "backup")
        private val worlds = Bukkit.getWorlds()

        fun initBackupDir() {
            for (world: World in worlds) {
                val worldBackupDir = File(backupRootDir, world.name)
                if (!worldBackupDir.exists()) {
                    worldBackupDir.mkdirs()
                }
            }
        }

        @Throws(IOException::class)
        fun saveBackup(sender: CommandSender) {
            for (world: World in worlds) {
                val map: MutableMap<String, String> = HashMap()
                val list: MutableList<String> = ArrayList()
                for (file: File in File(backupRootDir, world.name).listFiles()!!) {
                    if (file.name.endsWith(".zip")) {
                        list.add(file.name)
                    }
                }
                val backupId: String = list.size.plus(1).toString()
                map["backupPlayer"] = sender.name
                map["backupTime"] = TimeUtils.getTime()
                val json: String = GsonBuilder().setPrettyPrinting().create().toJson(map)
                val backupName =
                    plugin.config.getString("BackupFile-name").replace("\${time}", TimeUtils.getTime())
                        .replace("\${id}", backupId)
                val file = File(backupRootDir, "${world.name}/${backupName}.json")
                if (!file.exists()) {
                    file.createNewFile()
                    Files.writeString(file.toPath(), json)
                } else {
                    Files.writeString(file.toPath(), json)
                }
                ZipUtils.zipFiles(
                    File(serverRootDir, world.name).absolutePath,
                    File(backupRootDir, "${world.name}/${backupName}.zip").absolutePath
                )
            }
        }

        @Throws(IOException::class)
        fun saveBackupToWorld(sender: CommandSender, worldName: String) {
            if (worlds.contains(Bukkit.getWorld(worldName))) {
                for (world: World in worlds) {
                    if (world.name == worldName) {
                        val map: MutableMap<String, String> = HashMap()
                        val list: MutableList<String> = ArrayList()
                        for (file: File in File(backupRootDir, world.name).listFiles()!!) {
                            if (file.name.endsWith(".zip")) {
                                list.add(file.name)
                            }
                        }
                        val backupId: String = list.size.plus(1).toString()
                        map["backupPlayer"] = sender.name
                        map["backupTime"] = TimeUtils.getTime()
                        val json: String = GsonBuilder().setPrettyPrinting().create().toJson(map)
                        val backupName =
                            plugin.config.getString("BackupFile-name").replace("\${time}", TimeUtils.getTime())
                                .replace("\${id}", backupId)
                        val file = File(backupRootDir, "${world.name}/${backupName}.json")
                        if (!file.exists()) {
                            file.createNewFile()
                            Files.writeString(file.toPath(), json)
                        } else {
                            Files.writeString(file.toPath(), json)
                        }
                        ZipUtils.zipFiles(
                            File(serverRootDir, world.name).absolutePath,
                            File(backupRootDir, "${world.name}/${backupName}.zip").absolutePath
                        )
                    }
                }

                sender.sendMessage(
                    "${ChatColor.WHITE}[${ChatColor.GREEN}BackupPlugins${ChatColor.WHITE}] - ${
                        Language.get(
                            "backed"
                        )
                    }"
                )
            } else {
                sender.sendMessage(
                    "${ChatColor.WHITE}[${ChatColor.RED}BackupPlugins${ChatColor.WHITE}] - ${
                        Language.get(
                            "world-404"
                        )
                    }"
                )
            }
        }

        fun loadBackup(fileName: String, player: Player) {
            player.kickPlayer("${ChatColor.RED}${Language.get("backup-loading")}")

            for (world : World in worlds) {
                Bukkit.unloadWorld(world.name, true)

                FileUtils.deleteDirectory(File(serverRootDir, world.name))
                File(serverRootDir, world.name).mkdirs()
                ZipUtils.unZipFiles(File(backupRootDir, "${world.name}${fileName}.zip").absolutePath, File(serverRootDir, world.name).absolutePath)

                WorldCreator(world.name).createWorld()
            }

            Thread.sleep(2000)
        }

        fun loadBackup(worldName: String, fileName: String, player: Player) {
            player.kickPlayer("${ChatColor.RED}${Language.get("backup-loading")}")
        }
    }
}
