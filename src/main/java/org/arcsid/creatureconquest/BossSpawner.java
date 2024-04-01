package org.arcsid.creatureconquest;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.logging.Logger;

public class BossSpawner {
    private final static Logger log = Bukkit.getLogger();
    int minionsKilled = 0;
    boolean alive = false;
    Location location;
    BossMobManager.BossDescriptor bossDescription;

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
    public void spawnBoss(){
        try{
            Entity entity = (new BukkitAPIHelper()).spawnMythicMob(bossDescription.bossType, location, 1);
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

    public void resetOnBossDeath(){
        minionsKilled = 0;
        alive = false;
        log.info("BOSS KILLED");
    }

}
