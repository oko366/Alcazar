package moe.oko.alcazar.listener;

import moe.oko.alcazar.handler.PlayerDeathHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public static void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        PlayerDeathHandler.handlePlayerDeath(event.getEntity());
    }
}