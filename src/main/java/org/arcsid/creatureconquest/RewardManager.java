package org.arcsid.creatureconquest;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class RewardManager {
    BossMobManager.BossDescriptor bossDescription;
    Map<String, Double> damagerList = new HashMap<>();
    boolean alive = false;
    Entity spawnedBoss;

    public RewardManager(BossMobManager.BossDescriptor bossDescription, Entity boss){
        this.bossDescription = bossDescription;
        spawnedBoss = boss;
        alive = true;
    }

    public void onBossDamaged(Entity damagedEntity, String damager, Double damage){
        if(damagedEntity != null && alive){
            if(damagedEntity == spawnedBoss){
                addDamage(damager, damage);
            }
        }
    }

    public void addDamage(String playerName, Double damage){
        if (damagerList.containsKey(playerName)) {
            damagerList.merge(playerName, damage, Double::sum);
        } else {
            damagerList.put(playerName, damage);
        }
    }

    public void clearDamagerList(){
        damagerList.clear();
    }

    public void rollRewards(){
        sortDamagerList();
        alive = false;

    }

    public void rewardAll(){

    }

    public void sortDamagerList(){
        damagerList = damagerList.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
