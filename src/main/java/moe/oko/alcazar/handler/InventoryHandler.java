package moe.oko.alcazar.handler;

import moe.oko.alcazar.Alcazar;
import moe.oko.alcazar.AlcazarDB;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class InventoryHandler {
    private final Alcazar plugin;
    private final AlcazarDB db;
    private List<String> invCache;

    public InventoryHandler (Alcazar plugin, AlcazarDB db) {
        this.plugin = plugin;
        this.db = db;
        updateCache();
    }

    /**
     * Updates a cached list of inventories.
     */
    private void updateCache() { invCache = db.getAllInvNames(); }

    /**
     * Upserts an inventory to the database.
     * TODO: create a configurable limit of inventories per player.
     * @param player the player saving the inventory.
     * @param invName the identifier for the inventory.
     * @return if successful
     */
    public boolean save(Player player, String invName){
        var UUID = player.getUniqueId().toString();
        var serializedPlayerInventory = playerInventoryToBase64(player.getInventory());
        var serializedInv = serializedPlayerInventory[0];
        var serializedArmor = serializedPlayerInventory[1];

        if (db.addNewInventory(UUID, invName, serializedInv, serializedArmor)) {
            plugin.info("%s has saved inventory %s".formatted(player.getName(), invName));
            updateCache();
            return true;
        }
        return false;
    }

    /**
     * Provides a player with an inventory.
     * @param player the receiving player.
     * @param invName the identifier for the inventory.
     * @return if successful.
     */
    public boolean load(Player player, String invName) {
        var serializedPlayerInventory = db.getInv(invName);

        if (serializedPlayerInventory == null)
            return false;

        var serializedInv = serializedPlayerInventory[0];
        var serializedArmor = serializedPlayerInventory[1];

        try {
            var inventory = itemStackArrayFromBase64(serializedInv);
            var armor = itemStackArrayFromBase64(serializedArmor);
            player.getInventory().setContents(inventory);
            player.getInventory().setArmorContents(armor);
            player.updateInventory();
            plugin.info("%s was given inventory %s".formatted(player.getName(), invName));
            updateCache();
            return true;
        } catch (IOException e) { return false; }
    }

    /**
     * Removes an inventory from the database.
     * @param player the player requesting the removal. Nullable.
     * @param invName the identifier for the inventory.
     * @return if successful.
     */
    public boolean remove(@Nullable Player player, String invName) {
        if (db.removeInventory(invName)) {
            if (player != null)
                plugin.info("%s has removed inventory %s".formatted(player.getName(), invName));
            var msg = player != null
                    ? "%s has removed inventory %s".formatted(player.getName(), invName)
                    : "inventory %s was removed".formatted(invName);
            plugin.info(msg);
            updateCache();
            return true;
        }
        return false;
    }

    public List<String> list() {
        return invCache;
    }

    public String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        // Get the main content part, this doesn't return the armor
        var content = toBase64(playerInventory);
        var armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    public String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            var outputStream = new ByteArrayOutputStream();
            var dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items)
                dataOutput.writeObject(item);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            var outputStream = new ByteArrayOutputStream();
            var dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++)
                dataOutput.writeObject(inventory.getItem(i));

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            var inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            var dataInput = new BukkitObjectInputStream(inputStream);
            var items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) dataInput.readObject();

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
