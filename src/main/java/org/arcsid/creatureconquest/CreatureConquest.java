//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.arcsid.creatureconquest;

import java.util.logging.Logger;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.arcsid.creatureconquest.listeners.MythicMobKillListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class CreatureConquest extends JavaPlugin{
    private final static Logger log = Bukkit.getLogger();


    public void onEnable() {
        assertDependencyPlugins();

        log.info("test");

        ConfigReader bossConfigs=new ConfigReader(this, "settings/", "bosses.yml");
        bossConfigs.saveDefaultConfig();

        log.info("ENABLED");

        BossMobManager bossMobManager = new BossMobManager(bossConfigs.getConfig());
        MythicMobKillListener mythicMobKillListener = new MythicMobKillListener(bossMobManager);
    }

    private void assertDependencyPlugins(){
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            log.info("MythicMobs found!");
        } else {
            this.getLogger().warning("Could not find MythicMobs! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
            System.out.println("WILL THIS WORK????");
        }
    }

    public @NotNull ComponentLogger getComponentLogger() {
        return super.getComponentLogger();
    }

    public void onDisable() {
    }
}
