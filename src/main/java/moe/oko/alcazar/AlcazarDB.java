package moe.oko.alcazar;

import com.google.common.base.Strings;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import moe.oko.alcazar.model.DatabaseCredentials;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlcazarDB {
    private final Alcazar plugin;
    private HikariDataSource dataSource;
    private Connection connection = null;

    public AlcazarDB(Alcazar plugin, DatabaseCredentials credentials) {
        this.plugin = plugin;
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://%s:%s/%s".formatted(credentials.host(), credentials.port(), credentials.db()));

        config.setConnectionTimeout(credentials.connTimeout());
        config.setIdleTimeout(credentials.idleTimeout());
        config.setMaxLifetime(credentials.maxLifetime());
        config.setMaximumPoolSize(credentials.poolSize());
        config.setUsername(credentials.username());

        if (!Strings.isNullOrEmpty(credentials.password()))
            config.setPassword(credentials.password());
        this.dataSource = new HikariDataSource(config);

        try {
            connection = dataSource.getConnection();
            plugin.info("Connected to database.");
        } catch (SQLException e) {
            plugin.severe("Unable to connect to database.");
            e.printStackTrace();
            this.dataSource = null;
        }
        initializeTables();
    }

    private void initializeTables() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `inventories`" +
                    "(uuid VARCHAR(128), name VARCHAR(64) UNIQUE, si TEXT, sa TEXT);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `warps`" +
                    "(uuid VARCHAR(128), name VARCHAR(64), location TEXT);").execute();
        } catch (SQLException e) {
            plugin.severe("Failed to initialize SQL tables (permissions issue?)");
            e.printStackTrace();
        }
    }

    /**
     * Counts the number of inventories created by a player.
     *
     * @param UUID UUID of a player
     * @return The number of saved inventories for the player.
     */
    public short countPlayerInventories(String UUID){
        try {
            var ps = connection.prepareStatement("SELECT COUNT(*) FROM inventories WHERE uuid=?");
            ps.setString(1, UUID);
            var rs = ps.executeQuery();
            rs.next();
            short count = rs.getShort(1);
            ps.close();
            rs.close();
            return count;
        } catch (SQLException e) {
            plugin.severe("Unable to get inventory count.");
            return 0;
        }
    }

    public boolean addNewInventory(String UUID, String name, String serializedInv, String serializedArmor) {
        try {
            var ps = connection.prepareStatement("REPLACE INTO inventories(uuid, name, si, sa) VALUES (?, ?, ?, ?)");
            ps.setString(1, UUID);
            ps.setString(2, name);
            ps.setString(3, serializedInv);
            ps.setString(4, serializedArmor);
            ps.execute();
            ps.close();
            return true;
        } catch (SQLException e) {
            plugin.severe("Unable to add inventory %s to database.".formatted(name));
            e.printStackTrace();
            return false;
        }
    }


    public boolean addNewWarp(String UUID, String name, String base64Location) {
        try {
            var ps = connection.prepareStatement("REPLACE INTO warps(uuid, name, location) VALUES (?, ?, ?)");
            ps.setString(1, UUID);
            ps.setString(2, name);
            ps.setString(3, base64Location);
            ps.execute();
            ps.close();
            return true;
        } catch (SQLException e) {
            plugin.severe("Unable to add warp %s to database.".formatted(name));
            e.printStackTrace();
            return false;
        }
    }

    public String[] getInv(String name){
        try {
            var ps = connection.prepareStatement("SELECT * FROM inventories WHERE name=?");
            ps.setString(1, name);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var inventory = rs.getString("si");
                var armor = rs.getString("sa");
                ps.close();
                rs.close();
                return new String[] { inventory, armor };
            }
        } catch (SQLException e) {
            plugin.severe("Unable to get inventory %s from database.".formatted(name));
        }
        return null;
    }

    public String getWarp(String name){
        try {
            var ps = connection.prepareStatement("SELECT * FROM warps WHERE name=?");
            ps.setString(1, name);
            var rs = ps.executeQuery();
            if (rs.next()) {
                var location = rs.getString("location");
                ps.close();
                rs.close();
                return location;
            }
        } catch (SQLException e) {
            plugin.severe("Unable to get warp %s from database.".formatted(name));
        }
        return null;
    }

    /**
     * Removes an inventory from the database.
     *
     * @param name The name of the inventory to be deleted.
     */
    public boolean removeInventory(String name) {
        try {
            var ps = connection.prepareStatement("DELETE FROM inventories WHERE name=?");
            ps.setString(1, name);
            ps.execute();
            ps.close();
            return true;
        } catch (SQLException exception) {
            plugin.severe("Unable to remove inventory %s.".formatted(name));
            return false;
        }
    }
    

    public boolean removeWarp(String name) {
        try {
            var ps = connection.prepareStatement("DELETE FROM warps WHERE name=?");
            ps.setString(1, name);
            ps.execute();
            ps.close();
            return true;
        } catch (SQLException exception) {
            plugin.severe("Unable to remove warp %s.".formatted(name));
            return false;
        }
    }

    /**
     * Generates a List containing all saved inventories.
     *
     * @return a list with all entries from column name.
     */
    public List<String> getAllInvNames(){
        try {
            var ps = connection.prepareStatement("SELECT name FROM inventories");
            var rs = ps.executeQuery();
            if (rs.next()) {
                List<String> names = new ArrayList<>();
                names.add(rs.getString("name"));
                while(rs.next()){
                    names.add(rs.getString("name"));
                }
                ps.close();
                rs.close();
                return names;
            }
        } catch (SQLException e) {
            plugin.severe("Unable to get the inventory list.");
        }
        return null;
    }

    public List<String> getAllWarpNames(){
        try {
            var ps = connection.prepareStatement("SELECT name FROM warps");
            var rs = ps.executeQuery();
            if (rs.next()) {
                List<String> names = new ArrayList<>();
                names.add(rs.getString("name"));
                while(rs.next()){
                    names.add(rs.getString("name"));
                }
                ps.close();
                rs.close();
                return names;
            }
        } catch (SQLException e) {
            plugin.severe("Unable to get the warp list.");
        }
        return null;
    }

}
