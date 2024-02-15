package xyz.frish2021.backupPlugins.language

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.io.FileUtils
import org.bukkit.configuration.Configuration
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.FileUtil
import xyz.frish2021.backupPlugins.BackupPlugins
import java.io.File

class Language {
    companion object {
        private val plugin : JavaPlugin = JavaPlugin.getProvidingPlugin(BackupPlugins::class.java)
        private val config : Configuration = plugin.config
        private val languageFile : File = File(plugin.dataFolder, "language/${config.getString("language")}.json")
        private val string : String = FileUtils.readFileToString(languageFile, "utf-8")
        private val json : JsonObject = Gson().fromJson(string, JsonObject::class.java).asJsonObject

        @JvmStatic
        fun get(key : String) : String {
            return json.get(key).asString
        }
    }
}