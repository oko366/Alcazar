package moe.oko.alcazar.handlers;

import moe.oko.alcazar.database.ASQL;
import org.bukkit.Bukkit;
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

public class InventoryHandler {
    public static boolean save(Player player, String invName){
        String UUID = player.getUniqueId().toString();
        PlayerInventory playerInv = player.getInventory();

        // Check if player has too many inventories
        if(ASQL.countPlayerInventories(UUID) >= 5) return false;

        // If not serialize the players inventory
        String[] serializedPlayerInventory = playerInventoryToBase64(playerInv);
        String serializedInv = serializedPlayerInventory[0];
        String serializedArmor = serializedPlayerInventory[1];

        if(ASQL.addNewInventory(UUID, invName, serializedInv, serializedArmor)){
            player.sendMessage("Saved " + invName + "!");
            return true;
        } else {
            // Failed to execute maybe put an error message here.
            return false;
        }

    }

    public static boolean load(Player player, String invName) throws IOException {
        String UUID = player.getUniqueId().toString();
        String[] serializedPlayerInventory = ASQL.getInv(UUID, invName);

        if(serializedPlayerInventory == null){
            return false;
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

        return true;
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
            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
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

    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());


            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
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
