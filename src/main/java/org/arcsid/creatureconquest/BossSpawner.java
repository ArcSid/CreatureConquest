package org.arcsid.creatureconquest;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.arcsid.creatureconquest.listeners.MobDamagedListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Async;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class BossSpawner {
    private final static Logger log = Bukkit.getLogger();

    int minionsKilled = 0;
    boolean alive = false;

    Integer bossId;

    Entity spawnedBoss;
    Location location;

    BossMobManager.BossDescriptor bossDescription;
    Map<String, Double> damagerList = new HashMap<>();

    RewardManager rewardManager = new RewardManager(bossDescription, null);

    public BossSpawner(BossMobManager.BossDescriptor boss){
        bossDescription = boss;
        World world = Bukkit.getWorld(bossDescription.worldName);
        location = new Location(world, bossDescription.x, bossDescription.y, bossDescription.z);
    }
    public void addKill(){
        if(alive){
           return;
        }

        minionsKilled++;

        if(minionsKilled == bossDescription.requiredToKill){
            spawnBoss();
        }
    }

    public void onBossDamaged(MythicMobDeathEvent mobDeath){
        //rewardManager.addDamage(mobDeath);
    }

    public void spawnBoss(){
        try{
            spawnedBoss = (new BukkitAPIHelper()).spawnMythicMob(bossDescription.bossType, location, 1);
            rewardManager = new RewardManager(bossDescription, spawnedBoss);
            spawnedBoss.getEntityId();

            //TODO: bad design
            rewardManager.spawnedBoss = this.spawnedBoss;

            spawnedBoss.getEntityId();

            log.info("BOSS SPAWNED");
            alive = true;
        }catch (InvalidMobTypeException e){
            log.severe("BOSS TYPE NOT FOUND");
            minionsKilled = 0;
        }
    }

    public String bossType(){
        return bossDescription.bossType;
    }

}
