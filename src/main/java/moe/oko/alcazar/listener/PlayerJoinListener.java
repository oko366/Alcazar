package moe.oko.alcazar.listener;

import moe.oko.alcazar.Alcazar;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    private String welcomeMessage;
    public PlayerJoinListener(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (!player.hasPlayedBefore())
            player.sendMessage(MiniMessage.miniMessage().deserialize(welcomeMessage));
    }
}
