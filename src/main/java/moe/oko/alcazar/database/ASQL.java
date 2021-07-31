package moe.oko.alcazar.database;

import java.sql.*;

public class ASQL {
    static Connection connection = null;
    // If you need help dm me
    private static String connectionString = "";
    public static void initConnection() {
        if (connection != null) {
            System.err.println("[DATABASE] WARN | The moe.oko.alcazar.database.ASQL.initConnection has already ran. A database connection was found.");
            return;
        }
        try {
            connection = DriverManager.getConnection(connectionString);
            System.out.println("[DATABASE] SUCCESS | Connection established.");
        } catch (SQLException e) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.initConnection failed to execute!");
        }
    }

    public static int countPlayerInventories(String UUID){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM inventory_table WHERE uuid=?");
            preparedStatement.setString(1, UUID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("total");
                preparedStatement.close();
                resultSet.close();
                return count;
            }
            preparedStatement.close();
            resultSet.close();
            return 0;

        } catch (SQLException exception) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.countPlayerInventories function failed to execute successfully.");
        }
        return 0;
    }

    public static Boolean addNewInventory(String UUID, String name, String serializedInv, String serializedArmor) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO inventory_table(uuid, inventory_name, si, sa) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, UUID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, serializedInv);
            preparedStatement.setString(4, serializedArmor);
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException exception) {
            System.err.println("[DATABASE] ERROR | The moe.oko.alcazar.database.ASQL.addNewInventory function failed to execute successfully.");
        }
        return false;
    }

    public static String[] getInv(String UUID, String invName){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM positions WHERE uuid=? AND inventory_name=?");
            preparedStatement.setString(1, UUID);
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
        }

        return null;
    }

}
