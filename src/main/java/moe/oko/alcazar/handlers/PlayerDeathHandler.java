package moe.oko.alcazar.handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;


public class PlayerDeathHandler {

    /**
     * Generates a dynamic death message.
     * Example:
     * Alice [0⚗] was killed by Bob [16⚗] using Diamond Sword.
     */
    public static void handlePlayerDeath(Player player) {
        // TODO: REDO ALL OF THIS. IT IS VERY LATE
        final char potion = '\u2697';
        final char info = '\u2139';
        final short deathPotions = countPotions(player.getInventory());

        final TextComponent deadMessage = Component.text()
                .append(player.displayName())
                .hoverEvent(
                        Component.text(info + " ")
                                .append(player.displayName())
                )
                .clickEvent(
                        // TODO: make this trigger WhoIs command instead.
                        ClickEvent.openUrl("https://namemc.com/profile/" + player.getUniqueId())
                )
                .build();

        final TextComponent deathPotionMessage = Component.text()
                .content(" [" + deathPotions)
                .append(
                        Component.text(potion)
                                // Instant Healing
                                .color(TextColor.color(248,36,35))
                )
                .hoverEvent(
                        Component.text(deathPotions + " Potion(s) remaining")
                )
                .append(Component.text("]"))
                .build();

        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            short killerPotions = countPotions(killer.getInventory());
            ItemStack weapon = killer.getInventory().getItemInMainHand();

            final TextComponent killerMessage = Component.text()
                    .append(killer.displayName())
                    .hoverEvent(
                            Component.text(info + " ")
                                    .append(killer.displayName())
                    )
                    .clickEvent(
                            // TODO: make this trigger whois command instead.
                            ClickEvent.openUrl("https://namemc.com/profile/" + killer.getUniqueId())
                    )
                    .build();

            final TextComponent killerPotionMessage = Component.text()
                    .content(" [" + killerPotions)
                    .append(
                            Component.text(potion)
                                    // Instant Healing
                                    .color(TextColor.color(248,36,35))
                    )
                    .hoverEvent(
                            Component.text(killerPotions + " Potion(s) remaining")
                    )
                    .append(Component.text("]"))
                    .build();

            TextComponent itemMessage = Component.text()
                    .content(" using ")
                    .append(weapon.displayName())
                    .hoverEvent(weapon.asHoverEvent())
                    .build();

            // If null, default to "their hands".
            // Use custom item name when applicable.
            if (weapon.getType() == Material.AIR) {
                itemMessage = Component.text().content(" using their hands").build();
            }
            if (weapon.getItemMeta().getDisplayName().equals("")) {
                itemMessage = Component.text().build();
            }
            Bukkit.broadcast(deadMessage.append(deathPotionMessage).append(Component.text(" was killed by ")).append(killerMessage).append(killerPotionMessage).append(itemMessage));
        }
        else {
            Bukkit.broadcast(deadMessage.append(deathPotionMessage).append(Component.text(" died.")));
        }
    }

    // Counts the sum of instant healing potions within a player's inventory.
    private static short countPotions(Inventory inv) {
        short count = 0;
        for(ItemStack item : inv) {
            if(item != null && item.getItemMeta() != null && item.hasItemMeta()) {
                if (item.getItemMeta() instanceof final PotionMeta meta) {
                    final PotionData data = meta.getBasePotionData();
                    if(data.getType() == PotionType.INSTANT_HEAL) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
