package moe.oko.alcazar.events;

import moe.oko.alcazar.Alcazar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        // TODO: Move this functionality to future chat plugin
        // TODO: Use MiniMessage for motd syntax
        Player player = event.getPlayer();
        String motd = Alcazar.getInstance().getConfig().getString("motd");
        player.sendMessage("Message of the Day:");
        player.sendMessage(motd);
    }
}
