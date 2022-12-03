package moe.oko.alcazar.listener;

import moe.oko.alcazar.handler.DeathMessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private DeathMessageHandler deathMessageHandler;
    public PlayerDeathListener(DeathMessageHandler deathMessageHandler) { this.deathMessageHandler = deathMessageHandler; }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.deathMessage(deathMessageHandler.createDeathMessage(event.getPlayer()));
    }
}