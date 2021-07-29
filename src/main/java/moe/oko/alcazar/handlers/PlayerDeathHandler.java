package moe.oko.alcazar.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
     * [*] Alice [0⚗] was slain by Bob [16⚗] using Diamond Sword.
     */
    public static void handlePlayerDeath(Player player) {
        String system = "[*] ";
        char potion = '\u2697';
        int deathPotions = countPotions(player.getInventory());
        String coloredPotion = "" + ChatColor.RED + potion + ChatColor.RESET;

        // This is displayed when the player dies without an assailant.
        String msg = system + player.getDisplayName() + " [" + deathPotions + coloredPotion + "] died.";
        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            String item;
            ItemStack weapon = killer.getInventory().getItemInMainHand();
            // TODO: have item display hoverable metadata with JSON, simplify if statements to ternary
            // If null, default to "their hands".
            // Use custom item name when applicable.
            item = (weapon.getType() == Material.AIR) ? "their hands" : weapon.getItemMeta().getDisplayName();
            if (!item.equals("")) {
                item = " using " + item;
            }
            int killerPotions = countPotions(killer.getInventory());
            // TODO: possibly add dynamic death types based on weapon
            msg = system + player.getDisplayName() + " [" + deathPotions + coloredPotion + "] was killed by " + killer.getDisplayName() + " [" + killerPotions + coloredPotion + "]" + item + ".";
        }
        Bukkit.broadcastMessage(msg);
    }

    // Counts the sum of instant healing potions within a player's inventory.
    private static int countPotions(Inventory inv) {
        int count = 0;
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
