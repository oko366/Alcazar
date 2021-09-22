package moe.oko.alcazar.handlers;

import moe.oko.alcazar.database.ASQL;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static moe.oko.alcazar.Alcazar.instance;

public class InventoryHandler {
    public static void save(Player player, String invName){
        String UUID = player.getUniqueId().toString();
        PlayerInventory playerInv = player.getInventory();

        // Check if player has too many inventories
        if (instance.getConfig().getBoolean("inventories.limit.enabled")) {
            if(ASQL.countPlayerInventories(UUID) >= instance.getConfig().getInt("inventories.limit.maximum")) {
                player.sendMessage("Could not save " + invName + " as inventory");
                return;
            }
        }

        // If not serialize the players inventory
        String[] serializedPlayerInventory = playerInventoryToBase64(playerInv);
        String serializedInv = serializedPlayerInventory[0];
        String serializedArmor = serializedPlayerInventory[1];

        if(ASQL.addNewInventory(UUID, invName, serializedInv, serializedArmor)){
            player.sendMessage("Saved " + invName + "!");
        }
    }

    public static void load(Player player, String invName) throws IOException {
        String[] serializedPlayerInventory = ASQL.getInv(invName);

        if(serializedPlayerInventory == null){
            player.sendMessage(ChatColor.RED + "Unknown inventory '" + invName + "'");
            return;
        }

        String serializedInv = serializedPlayerInventory[0];
        String serializedArmor = serializedPlayerInventory[1];
        ItemStack[] inventory;
        ItemStack[] armor;
        inventory = itemStackArrayFromBase64(serializedInv);
        armor = itemStackArrayFromBase64(serializedArmor);

        player.getInventory().setContents(inventory);
        player.getInventory().setArmorContents(armor);
        player.updateInventory();
        player.sendMessage("Loaded " + invName + "!");

    }

    public static void remove(Player player, String invName) {
        if (ASQL.removeInventory(invName)) {
            player.sendMessage("Removed " + invName + "!");
        } else {
            // This probably won't ever trigger since it will always respond with an OK.
            player.sendMessage("Could not remove " + invName);
        }
    }

    public static void list(Player player) {
        List<String> inventories = ASQL.getInvNames();
        if (inventories == null) { inventories = Collections.emptyList(); }
        player.sendMessage("There are " + inventories.size() + " saved inventories: " + String.join(", ", inventories));
    }

    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        // Get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
