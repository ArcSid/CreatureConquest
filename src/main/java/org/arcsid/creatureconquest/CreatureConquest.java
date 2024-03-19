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

        log.info("ENABLED FFF");
        SummonBoss.Context bossContext = new SummonBoss.Context();
        for (String bossName : aconfig.getKeys(false)) {
            log.info(bossName);
            ConfigurationSection bossConf = aconfig.getConfigurationSection(bossName);
//                log.info("bossName " + confSec.getString("bossType"));
//                log.info("minionName " + confSec.getString("minionType"));
//                log.info("minionsRequiredToKill " + confSec.getInt("minionsRequiredToKill"));
//                log.info("topNtoReward " + confSec.getInt("rewards.topNtoReward"));
//                List<String> lsStr = confSec.getStringList("rewards.topRewards");
//                for(String s: lsStr){
//                    log.info(s);
//                }
//
//            log.info("rewardForAllParticipants " + bossConf.getString("rewards.rewardForAllParticipants"));
//            log.info("spawnWorldName " + bossConf.getString("spawnWorld.spawnWorldName"));
            if(bossConf == null){
                continue;
            }
            bossContext
                    .setBossType(bossConf.getString("bossType"))
                    .setMinionType(bossConf.getString("minionType"))
                    .setX(bossConf.getInt("bossSpawn.X"))
                    .setY(bossConf.getInt("bossSpawn.Y"))
                    .setZ(bossConf.getInt("bossSpawn.Z"))
                    .setWorldName(bossConf.getString("bossSpawn.worldName"))
                    .setRequiredToKill(bossConf.getInt("minionsRequiredToKill"));
            log.info(bossContext.toString());
        }


        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            log.info("MythicMobs found!");
        } else {
            this.getLogger().warning("Could not find MythicMobs! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        SummonBoss bossSummoner = new SummonBoss(bossContext);
        this.getServer().getPluginManager().registerEvents(bossSummoner, this);
    }

    public @NotNull ComponentLogger getComponentLogger() {
        return super.getComponentLogger();
    }

    public void onDisable() {
    }
}
