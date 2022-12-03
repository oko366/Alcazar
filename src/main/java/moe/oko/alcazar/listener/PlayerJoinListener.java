package moe.oko.alcazar.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {
    private final String welcomeMessage;
    public PlayerJoinListener(String welcomeMessage) { this.welcomeMessage = welcomeMessage; }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (!player.hasPlayedBefore())
            player.sendMessage(MiniMessage.miniMessage().deserialize(welcomeMessage));
    }
}
