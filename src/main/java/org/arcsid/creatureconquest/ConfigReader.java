//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.arcsid.creatureconquest;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigReader {
    private File file;
    private File filepath;
    private final JavaPlugin plugin;
    private final String name;
    private final String path;
    private FileConfiguration config;

    public void saveDefaultConfig() {
        if (!this.filepath.exists()) {
            boolean success = this.filepath.mkdirs();
            if (!success) {
                Bukkit.getLogger().severe("Error creating the config. Please try again.");
            }
        }

        if (!this.file.exists()) {
            this.plugin.saveResource(this.path + this.name, false);
        }

    }

    public FileConfiguration getConfig() {
        if (!this.filepath.exists()) {
            this.saveDefaultConfig();
        }

        if (this.config == null) {
            this.reloadConfig();
        }

        return this.config;
    }

    public void reloadConfig() {
        if (this.filepath == null) {
            this.filepath = new File(this.plugin.getDataFolder(), this.path);
        }

        if (this.file == null) {
            this.file = new File(this.filepath, this.name);
        }

        this.config = YamlConfiguration.loadConfiguration(this.file);
        InputStream stream = this.plugin.getResource(this.name);
        if (stream != null) {
            YamlConfiguration YmlFile = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            this.config.setDefaults(YmlFile);
        }

    }

    public void saveConfig() {
        try {
            this.config.save(this.file);
        } catch (Throwable var2) {
            Bukkit.getLogger().severe("Error saving the config. Please try again.");
        }

    }

    public ConfigReader(JavaPlugin plugin, String pathname, String filename) {
        this.plugin = plugin;
        this.path = pathname;
        this.name = filename;
        this.filepath = new File(plugin.getDataFolder(), this.path);
        this.file = new File(this.filepath, this.name);
    }

    public static void save(ConfigReader config) {
        config.saveConfig();
        config.reloadConfig();
    }
}
