package org.arcsid.creatureconquest;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;
import java.util.logging.Logger;

public class BossMobManager {
    private final static Logger log = Bukkit.getLogger();
    Map<String, BossSpawner> minionToBossSpawner = new HashMap<>();
    Map<String, BossSpawner> bossToBossSpawner = new HashMap<>();
    Map<Integer, BossSpawner> spawnedBosses = new HashMap<>();
    List<BossDescriptor> bosses = new ArrayList<>();

    public BossMobManager(FileConfiguration bossesConfig) {
        createBossSpawners(bossesConfig);
        createMinionToBossTypeMap();
        printMap();
    }

    public void onBossDamaged(EntityDamageByEntityEvent entityDamageByEntityEvent){
        System.out.println("NAME " + entityDamageByEntityEvent.getEntity().getEntityId());
    }

    public void onMobKilled(MythicMob killedMob) {
        BossSpawner boss  = minionToBossSpawner.get(killedMob.getInternalName());
        log.info(killedMob.getInternalName());

        if(boss != null){

            log.info(boss.minionsKilled + " " + killedMob.getInternalName());

            boss.addKill();

            return;
        }

        boss = bossToBossSpawner.get(killedMob.getInternalName());

        if(boss != null){
            //boss.resetOnBossDeath();
        }
    }

    private void printMap(){
        for (Map.Entry<String, BossSpawner> stringBossSpawnerEntry : minionToBossSpawner.entrySet()) {
            log.info((stringBossSpawnerEntry.getKey() + " : " + stringBossSpawnerEntry.getValue().bossDescription.toString()));
        }
    }
    private void createMinionToBossTypeMap(){
        for (BossDescriptor boss : bosses) {
            BossSpawner bossSpawner = new BossSpawner(boss);
            minionToBossSpawner.put(boss.minionType, bossSpawner);
        }
        minionToBossSpawner.forEach((key, value) -> {
            bossToBossSpawner.put(value.bossType(), value);
        });
    }
    private void createBossSpawners(FileConfiguration bossesConfig){
        for (String sectionName : bossesConfig.getKeys(false)) {
            log.info(sectionName);
            ConfigurationSection bossConf = bossesConfig.getConfigurationSection(sectionName);
            if(bossConf == null){
                continue;
            }
            BossDescriptor bossDescriptor = new BossDescriptor();
            bossDescriptor
                    .setBossType(bossConf.getString("bossType"))
                    .setMinionType(bossConf.getString("minionType"))
                    .setX(bossConf.getInt("bossSpawn.X"))
                    .setY(bossConf.getInt("bossSpawn.Y"))
                    .setZ(bossConf.getInt("bossSpawn.Z"))
                    .setWorldName(bossConf.getString("bossSpawn.worldName"))
                    .setRequiredToKill(bossConf.getInt("minionsRequiredToKill"))
                    .setTopRewardList(bossConf.getStringList("rewards.topRewards"))
                    .setRewardForAll(bossConf.getString("rewards.rewardForAllParticipants"));
            log.info(bossDescriptor.toString());
            bosses.add(bossDescriptor);
        }
    }

    public static class BossDescriptor {
        String worldName;
        String bossType = "NoBossName";
        String minionType = "NoMinionName";
        List<String> topRewards = new ArrayList<>();
        String rewardForAll = "Nothing";

        public int requiredToKill = -1;
        int x = 0;
        int y = 0;
        int z = 0;
        int killed = 0;
        public BossDescriptor setTopRewardList(List<String> list){
            this.topRewards = list;
            return this;
        }

        public BossDescriptor setRewardForAll(String reward){
            this.rewardForAll= reward;
            return this;
        }

        public BossDescriptor setBossType(String bossType) {
            this.bossType = bossType;
            return this;
        }

        public BossDescriptor setMinionType(String minionType) {
            this.minionType = minionType;
            return this;
        }

        public BossDescriptor setRequiredToKill(int requiredToKill) {
            this.requiredToKill = requiredToKill;
            return this;
        }

        public BossDescriptor setX(int x) {
            this.x = x;
            return this;
        }

        public BossDescriptor setY(int y) {
            this.y = y;
            return this;
        }

        public BossDescriptor setZ(int z) {
            this.z = z;
            return this;
        }

        public BossDescriptor setWorldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        @Override
        public String toString() {
            return "\nContext{" +
                    "\nworldName='" + worldName + '\'' +
                    "\n, bossType='" + bossType + '\'' +
                    "\n, minionType='" + minionType + '\'' +
                    "\n, requiredToKill=" + requiredToKill +
                    "\n, x=" + x +
                    "\n, y=" + y +
                    "\n, z=" + z +
                    '}';
        }
    }
}
