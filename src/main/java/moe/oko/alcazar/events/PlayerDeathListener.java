package moe.oko.alcazar.events;

import moe.oko.alcazar.handlers.PlayerDeathHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    // Listen for death
    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        PlayerDeathHandler.handlePlayerDeath(event.getEntity());
    }

}