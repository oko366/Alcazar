package moe.oko.alcazar.handler;

import moe.oko.alcazar.model.PotionColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;


public class DeathMessageHandler {

    /**
     * Generates a dynamic death message.
     * Example:
     * Alice [0⚗] was killed by Bob [16⚗] using Diamond Sword.
     * @param victim The killed player.
     * @return a Component containing a complete death message.
     */
    public Component createDeathMessage(Player victim) {
        final var killer = victim.getKiller();
        final var deathMessage = Component.text().content(victim.getName() + ' ')
                .append(getPotionInfo(victim, PotionType.INSTANT_HEAL));
        if (killer == null) {
            deathMessage.append(Component.text(" died."));
        } else {
            final var weapon = killer.getInventory().getItemInMainHand();
            deathMessage.append(Component.text(" was killed by " + killer.getName() + ' '))
                    .append(getPotionInfo(killer, PotionType.INSTANT_HEAL));
            deathMessage.append(weapon.getType() == Material.AIR
                    ? Component.text(" using their hands.")
                    : Component.text(" using ")
                    .append(weapon.displayName()));
        }
        return deathMessage.build();
    }

    /**
     * Generates a list of the requested potion counts on a player.
     * @param player
     * @param potionTypes 1 or more PotionTypes
     * @return a stylized Component.
     * @apiNote make sure the requested PotionType has a corresponding PotionColor.
     */
    private Component getPotionInfo(Player player, @Nonnull PotionType... potionTypes) {
        final var potionComponent = Component.text().content("[");
        byte i = 1;
        for (PotionType potionType : potionTypes) {
            potionComponent.append(Component.text(countPotions(player.getInventory(), potionType)))
                    .append(Component.text( "\u2697")
                            .color(TextColor.color(PotionColor.getColor(potionType))));
                    if (i++ != potionTypes.length)
                        potionComponent.append(Component.text(", "));
        }
        potionComponent.append(Component.text("]"));
        return potionComponent.build();
    }

    /**
     * Counts the sum of a requested PotionType within an inventory.
     * @param inv the inventory to count.
     * @param potionType the PotionType object to count.
     * @return the sum of potions.
     * @apiNote does not count certain command-generated potion items.
     */
    private short countPotions(Inventory inv, PotionType potionType) {
        short count = 0;
        for(ItemStack item : inv) {
            if(item != null && item.getItemMeta() != null && item.hasItemMeta()) {
                if (item.getItemMeta() instanceof final PotionMeta meta) {
                    final var data = meta.getBasePotionData();
                    if(data.getType() == potionType) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
