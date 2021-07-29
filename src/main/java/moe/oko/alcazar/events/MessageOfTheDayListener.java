package moe.oko.alcazar.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MessageOfTheDayListener implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        // send MOTD to connecting players
        // TODO: Have MOTD defined in config
        player.sendMessage("[*] Message of the Day");
        player.sendMessage("[*] " + "TODO: ADD CUSTOMIZABLE MOTD STRING" + ChatColor.RED + "\u2764\u2764\u2764");

    }
}
