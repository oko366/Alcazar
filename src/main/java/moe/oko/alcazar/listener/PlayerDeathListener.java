package moe.oko.alcazar.listener;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import moe.oko.alcazar.Alcazar;
import moe.oko.alcazar.handler.DeathMessageHandler;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {
    private final Alcazar plugin;
    private final DeathMessageHandler deathMessageHandler;
    private final HolographicDisplaysAPI holographicDisplaysAPI;
    public PlayerDeathListener(Alcazar plugin, DeathMessageHandler deathMessageHandler, HolographicDisplaysAPI holographicDisplaysAPI) {
        this.plugin = plugin;
        this.deathMessageHandler = deathMessageHandler;
        this.holographicDisplaysAPI = holographicDisplaysAPI;
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();

        event.deathMessage(deathMessageHandler.createDeathMessage(player));
        if (holographicDisplaysAPI != null) {
            final var hologram = holographicDisplaysAPI.createHologram(player.getEyeLocation());
            hologram.getLines().appendText(player.getName() + " ☠ " + LegacyComponentSerializer.legacySection().serialize(
                    deathMessageHandler.getPotionInfo(player, PotionType.INSTANT_HEAL, PotionType.SPEED, PotionType.REGEN, PotionType.STRENGTH)));
            hologram.getLines().appendText("goodbye, cruel world!"); // TODO: provide randomized flavor message from a configurable list.

            new BukkitRunnable() { @Override public void run() { hologram.delete();}}.runTaskLater(plugin, 3600L);
        }
    }
}