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

    Location location;
    MythicMob boss;
    MythicMob minion;
    public int killed;
    boolean bossAlive;
    Entity entity;
    Map<String, Double> m;
    private Context ctx;

    public SummonBoss(Context context) {
        ctx = context;

        World world = Bukkit.getWorld(ctx.worldName);
        location = new Location(world, ctx.x, ctx.y, ctx.z);

        boss = MythicBukkit.inst().getMobManager().getMythicMob(ctx.bossType).orElse(null);
        minion = MythicBukkit.inst().getMobManager().getMythicMob(ctx.minionType).orElse(null);

        killed = 0;
        bossAlive = false;
        m = new HashMap<>();

    }

    @EventHandler
    public void onMobKill(MythicMobDeathEvent mobDeath) throws InterruptedException {
        if (mobDeath.getMob().getType() == minion && !bossAlive) {
            ++killed;
            if (ctx.requiredToKill == killed) {
                try {
                    entity = (new BukkitAPIHelper()).spawnMythicMob(ctx.bossType, location, 1);
                } catch (InvalidMobTypeException var3) {
                    throw new RuntimeException(var3);
                }

                Bukkit.broadcastMessage("&c&lBOSS SPAWNED: " + entity.getName() + " in " + entity.getLocation().getWorld().getName());
                bossAlive = true;
            }
        }

        if (mobDeath.getMob().getType() == boss) {
            printMap(mobDeath.getKiller().getName());
            m.clear();
            bossAlive = false;
            killed = 0;
        }

    }

    @EventHandler
    public void onBossDamaged(EntityDamageByEntityEvent damageEvent) {
        if (damageEvent.getEntity() == entity) {
            Bukkit.broadcastMessage(damageEvent.getDamager().getName() + " dealt " + damageEvent.getDamage() + " damage!");
            if (m.containsKey(damageEvent.getDamager().getName())) {
                m.merge(damageEvent.getDamager().getName(), damageEvent.getDamage(), Double::sum);
            } else {
                m.put(damageEvent.getDamager().getName(), damageEvent.getDamage());
            }
        }

    }

    void printMap(String lastDamager) {
        Bukkit.broadcastMessage("*********************************************");
        Bukkit.broadcastMessage("           Boss has been defeated!           ");
        Bukkit.broadcastMessage("                                             ");
        Bukkit.broadcastMessage("               Top damagers:                 ");
        Iterator var2 = m.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, Double> me = (Map.Entry)var2.next();
            Bukkit.broadcastMessage((String)me.getKey() + " : " + me.getValue());
        }

        Bukkit.broadcastMessage(lastDamager + " dealt the last blow");
        Bukkit.broadcastMessage("                                             ");
        Bukkit.broadcastMessage("*********************************************");
    }

    public static class Context {
        String worldName;
        String bossType = "NoBossName";
        String minionType = "NoMinionName";
        public int requiredToKill = -1;
        int x = 0;
        int y = 0;
        int z = 0;

        public Context setBossType(String bossType) {
            this.bossType = bossType;
            return this;
        }

        public Context setMinionType(String minionType) {
            this.minionType = minionType;
            return this;
        }

        public Context setRequiredToKill(int requiredToKill) {
            this.requiredToKill = requiredToKill;
            return this;
        }

        public Context setX(int x) {
            this.x = x;
            return this;
        }

        public Context setY(int y) {
            this.y = y;
            return this;
        }

        public Context setZ(int z) {
            this.z = z;
            return this;
        }

        public Context setWorldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        @Override
        public String toString() {
            return "Context{" +
                    "worldName='" + worldName + '\'' +
                    ", bossType='" + bossType + '\'' +
                    ", minionType='" + minionType + '\'' +
                    ", requiredToKill=" + requiredToKill +
                    ", x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
}
