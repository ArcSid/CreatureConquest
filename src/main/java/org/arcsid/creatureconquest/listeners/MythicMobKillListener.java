package org.arcsid.creatureconquest.listeners;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.arcsid.creatureconquest.BossMobManager;
import org.arcsid.creatureconquest.CreatureConquest;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class MythicMobKillListener implements Listener {
    BossMobManager bossMobManager;
    public MythicMobKillListener(BossMobManager bossMobManager) {
        this.bossMobManager = bossMobManager;
        Bukkit.getPluginManager().registerEvents(this, CreatureConquest.getProvidingPlugin(CreatureConquest.class));
    }
    @EventHandler
    public void onMobKill(MythicMobDeathEvent mobDeath){
        bossMobManager.onMobKilled(mobDeath.getMobType());
    }
}
