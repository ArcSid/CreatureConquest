//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.arcsid.creatureconquest;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class CreatureConquest extends JavaPlugin implements @NotNull Listener {
    public static ConfigReader AConfig;

    public CreatureConquest() {
    }

    public void onEnable() {
        Logger log = Bukkit.getLogger();
        log.info("test");
        AConfig = new ConfigReader(this, "settings/", "bosses.yml");
        AConfig.saveDefaultConfig();
        FileConfiguration aconfig = AConfig.getConfig();
        ConfigurationSection confSection = aconfig.getConfigurationSection("/");
        List<String> str = confSection.getStringList("/");

        for (String s : str) {
            log.info("AAAA " + s);
        }

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            log.info("MythicMobs found!");
        } else {
            this.getLogger().warning("Could not find MythicMobs! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        SummonBoss bossSummoner = new SummonBoss();
        this.getServer().getPluginManager().registerEvents(bossSummoner, this);
    }

    public @NotNull ComponentLogger getComponentLogger() {
        return super.getComponentLogger();
    }

    public void onDisable() {
    }
}
