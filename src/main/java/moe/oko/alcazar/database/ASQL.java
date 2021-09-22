package moe.oko.alcazar.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static moe.oko.alcazar.Alcazar.instance;

public class ASQL {
    static Connection connection = null;
    public static void initConnection() {
        if (connection != null) {
            System.err.println("[DATABASE] WARN | The moe.oko.alcazar.database.ASQL.initConnection has already ran. A database connection was found.");
            return;
        }
        try {
            connection = DriverManager.getConnection(instance.getConfig().getString("sql"));
            System.out.println("[DATABASE] SUCCESS | Connection established.");
        } catch (SQLException e) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.initConnection failed to execute!");
        }
    }

    /**
     * Counts the number of inventories created by a player.
     *
     * @param UUID UUID of a player
     * @return The number of saved inventories for the player.
     */
    public static int countPlayerInventories(String UUID){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM inventory_table WHERE uuid=?");
            preparedStatement.setString(1, UUID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            preparedStatement.close();
            resultSet.close();
            return count;

        } catch (SQLException exception) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.countPlayerInventories function failed to execute successfully.");
            System.err.println(exception);
        }
        return 0;
    }

    public static Boolean addNewInventory(String UUID, String name, String serializedInv, String serializedArmor) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("REPLACE INTO inventory_table(uuid, inventory_name, si, sa) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, UUID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, serializedInv);
            preparedStatement.setString(4, serializedArmor);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException exception) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.addNewInventory function failed to execute successfully.");
            System.err.println(exception);
        }
        return false;
    }

    public static String[] getInv(String invName){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM inventory_table WHERE inventory_name=?");
            preparedStatement.setString(1, invName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String inventory = resultSet.getString("si");
                String armor = resultSet.getString("sa");
                preparedStatement.close();
                resultSet.close();
                return new String[] { inventory, armor };
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.getInv function failed to execute successfully.");
            System.err.println(e);
        }

        return null;
    }

    /**
     * Removes an inventory from the database.
     *
     * @param name The name of the inventory to be deleted.
     */
    public static Boolean removeInventory(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM inventory_table WHERE inventory_name=?");
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException exception) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.removeInventory function failed to execute successfully.");
            System.err.println(exception);
        }
        return false;
    }

    /**
     * Generates a List containing all saved inventories.
     *
     * @return a list with all entries from column inventory_name.
     */
    public static List<String> getInvNames(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT inventory_name FROM inventory_table");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                List<String> names = new ArrayList<>();
                names.add(resultSet.getString("inventory_name"));
                while(resultSet.next()){
                    names.add(resultSet.getString("inventory_name"));
                }
                preparedStatement.close();
                resultSet.close();
                return names;
            }
        } catch (SQLException e) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.getInvNames function failed to execute successfully.");
            System.err.println(e);
        }
        return null;
    }

}
