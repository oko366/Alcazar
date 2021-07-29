package moe.oko.alcazar;

import moe.oko.alcazar.commands.InventoryCommand;
import moe.oko.alcazar.events.MessageOfTheDayListener;
import moe.oko.alcazar.events.PlayerDeathListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Alcazar extends JavaPlugin {
    String plugin = "[Alcazar] ";
    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new MessageOfTheDayListener(), this);

        getServer().getConsoleSender().sendMessage(plugin + "Events loaded.");

        // Register commands
        this.getCommand("inv").setExecutor(new InventoryCommand());
        getServer().getConsoleSender().sendMessage(plugin + "Commands loaded.");

        getServer().getConsoleSender().sendMessage(plugin + "Alcazar loaded.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(plugin + "Shutting down.");
    }

}

