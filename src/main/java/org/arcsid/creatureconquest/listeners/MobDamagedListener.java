package org.arcsid.creatureconquest.listeners;

import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobInteractEvent;
import org.arcsid.creatureconquest.BossMobManager;
import org.arcsid.creatureconquest.CreatureConquest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDamagedListener implements Listener {

    BossMobManager bossMobManager;

    public MobDamagedListener(BossMobManager bossMobManager) {
        this.bossMobManager = bossMobManager;
        Bukkit.getPluginManager().registerEvents(this, CreatureConquest.getProvidingPlugin(CreatureConquest.class));
    }

    @EventHandler
    public void onBossDamaged(EntityDamageByEntityEvent entityDamageByEntityEvent){
        bossMobManager.onBossDamaged(entityDamageByEntityEvent);
    }
}
