package xyz.frish2021.backupPlugins.time

import org.bukkit.plugin.java.JavaPlugin
import xyz.frish2021.backupPlugins.BackupPlugins
import java.text.SimpleDateFormat
import java.util.Date

class TimeUtils {
    companion object {
        private val plugin = JavaPlugin.getProvidingPlugin(BackupPlugins::class.java)
        private val config = plugin.config
        private val simpleDateFormat = SimpleDateFormat(config.getString("time-format"))
        private val date = Date()
        private val time = simpleDateFormat.format(date)

        fun getTime() : String {
            return time
        }
    }
}