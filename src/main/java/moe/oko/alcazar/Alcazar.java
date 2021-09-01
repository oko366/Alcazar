package moe.oko.alcazar;

import moe.oko.alcazar.commands.InventoryCommand;
import moe.oko.alcazar.commands.WhoisCommand;
import moe.oko.alcazar.database.ASQL;
import moe.oko.alcazar.events.PlayerDeathListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Alcazar extends JavaPlugin {

    private static Alcazar instance;
    public static Alcazar getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        // Manage config
        this.saveDefaultConfig();

        // Connect to database
        ASQL.initConnection();

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

        getServer().getConsoleSender().sendMessage("Events loaded.");

        // Register commands
        this.getCommand("inv").setExecutor(new InventoryCommand());
        this.getCommand("whois").setExecutor(new WhoisCommand());
        getServer().getConsoleSender().sendMessage("Commands loaded.");

        getServer().getConsoleSender().sendMessage("Alcazar loaded.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("Shutting down.");
    }

}
