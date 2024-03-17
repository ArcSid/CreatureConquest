//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.arcsid.creatureconquest;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SummonBoss implements Listener {
    String bossType = "Boss";
    String minionType = "Minion";
    public int requiredToKill = 5;
    int x = 0;
    int y = 55;
    int z = 0;
    World world = Bukkit.getWorld("Buildingworld1");
    Location location;
    MythicMob boss;
    MythicMob minion;
    public int killed;
    boolean bossAlive;
    Entity entity;
    Map<String, Double> m;

    public SummonBoss() {
        this.location = new Location(this.world, (double)this.x, (double)this.y, (double)this.z);
        this.boss = (MythicMob)MythicBukkit.inst().getMobManager().getMythicMob(this.bossType).orElse((MythicMob) null);
        this.minion = (MythicMob)MythicBukkit.inst().getMobManager().getMythicMob(this.minionType).orElse((MythicMob) null);
        this.killed = 0;
        this.bossAlive = false;
        this.m = new HashMap();
    }

    @EventHandler
    public void onMobKill(MythicMobDeathEvent mobDeath) throws InterruptedException {
        if (mobDeath.getMob().getType() == this.minion && !this.bossAlive) {
            ++this.killed;
            if (this.requiredToKill == this.killed) {
                try {
                    this.entity = (new BukkitAPIHelper()).spawnMythicMob(this.bossType, this.location, 1);
                } catch (InvalidMobTypeException var3) {
                    throw new RuntimeException(var3);
                }

                Bukkit.broadcastMessage("BOSS SPAWNED: " + this.entity.getName() + " in " + this.entity.getLocation().getWorld().getName());
                this.bossAlive = true;
            }
        }

        if (mobDeath.getMob().getType() == this.boss) {
            this.printMap(mobDeath.getKiller().getName());
            this.m.clear();
            this.bossAlive = false;
            this.killed = 0;
        }

    }

    @EventHandler
    public void onBossDamaged(EntityDamageByEntityEvent damageEvent) {
        if (damageEvent.getEntity() == this.entity) {
            Bukkit.broadcastMessage(damageEvent.getDamager().getName() + " dealt " + damageEvent.getDamage() + " damage!");
            if (this.m.containsKey(damageEvent.getDamager().getName())) {
                this.m.merge(damageEvent.getDamager().getName(), damageEvent.getDamage(), Double::sum);
            } else {
                this.m.put(damageEvent.getDamager().getName(), damageEvent.getDamage());
            }
        }

    }

    void printMap(String lastDamager) {
        Bukkit.broadcastMessage("*********************************************");
        Bukkit.broadcastMessage("           Boss has been defeated!           ");
        Bukkit.broadcastMessage("                                             ");
        Bukkit.broadcastMessage("               Top damagers:                 ");
        Iterator var2 = this.m.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, Double> me = (Map.Entry)var2.next();
            Bukkit.broadcastMessage((String)me.getKey() + " : " + me.getValue());
        }

        Bukkit.broadcastMessage(lastDamager + " dealt the last blow");
        Bukkit.broadcastMessage("                                             ");
        Bukkit.broadcastMessage("*********************************************");
    }
}
