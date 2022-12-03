package moe.oko.alcazar.handler;

import moe.oko.alcazar.Alcazar;
import moe.oko.alcazar.AlcazarDB;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class WarpHandler {
    private final Alcazar plugin;
    private final AlcazarDB db;
    private List<String> warpCache;

    public WarpHandler (Alcazar plugin, AlcazarDB db) {
        this.plugin = plugin;
        this.db = db;
        updateCache();
    }

    /**
     * Updates a cached list of inventories.
     */
    private void updateCache() { warpCache = db.getAllWarpNames(); }

    /**
     * Upserts an inventory to the database.
     * TODO: create a configurable limit of inventories per player.
     * @param player the player saving the inventory.
     * @param warpName the identifier for the warp.
     * @return if successful
     */
    public boolean save(Player player, String warpName){
        var UUID = player.getUniqueId().toString();
        var base64Location = locationToBase64(player.getLocation());

        if (db.addNewWarp(UUID, warpName, base64Location)) {
            plugin.info("%s has saved warp %s".formatted(player, warpName));
            updateCache();
            return true;
        }
        return false;
    }

    /**
     * Sends a player to a warp.
     * @param player the receiving player.
     * @param warpName the identifier for the warp.
     * @return if successful.
     */
    public boolean load(Player player, String warpName) {
    	var base64Location = db.getWarp(warpName);

        try {
        	player.teleport(locationFromBase64(base64Location));
            plugin.info("%s was sent to warp %s".formatted(player, warpName));
            updateCache();
            return true;
        } catch (Exception e) { return false; }
    }

    /**
     * Removes an inventory from the database.
     * @param player the player requesting the removal. Nullable.
     * @param warpName the identifier for the inventory.
     * @return if successful.
     */
    public boolean remove(@Nullable Player player, String warpName) {
        if (db.removeWarp(warpName)) {
            if (player != null)
                plugin.info("%s has removed warp %s".formatted(player, warpName));
            var msg = player != null
                    ? "%s has removed warp %s".formatted(player, warpName)
                    : "warp %s was removed".formatted(warpName);
            plugin.info(msg);
            updateCache();
            return true;
        }
        return false;
    }
    
    public List<String> list() {
        return warpCache;
    }

    public String locationToBase64(Location location) throws IllegalStateException {
        try {
            var outputStream = new ByteArrayOutputStream();
            var dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save the location
            dataOutput.writeObject(location);

            // Serialize the data
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save location.", e);
        }
    }

    public String toBase64(Location location) throws IllegalStateException {
        try {
            var outputStream = new ByteArrayOutputStream();
            var dataOutput = new BukkitObjectOutputStream(outputStream);

            // Save the location
            dataOutput.writeObject(location);

            // Serialize the data
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save location.", e);
        }
    }

    public Location locationFromBase64(String data) throws IOException {
        try {
            var inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            var dataInput = new BukkitObjectInputStream(inputStream);

            // Read the serialized inventory
            var toReturn = (Location) dataInput.readObject();

            dataInput.close();
            return toReturn;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
