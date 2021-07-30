package moe.oko.alcazar;

import moe.oko.alcazar.commands.InventoryCommand;
import moe.oko.alcazar.events.MessageOfTheDayListener;
import moe.oko.alcazar.events.PlayerDeathListener;
import moe.oko.alcazar.handlers.PrefixHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Alcazar extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new MessageOfTheDayListener(), this);

        getServer().getConsoleSender().sendMessage(PrefixHandler.Plugin + "Events loaded.");

        // Register commands
        this.getCommand("inv").setExecutor(new InventoryCommand());
        getServer().getConsoleSender().sendMessage(PrefixHandler.Plugin + "Commands loaded.");

        getServer().getConsoleSender().sendMessage(PrefixHandler.Plugin + "Alcazar loaded.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(PrefixHandler.Plugin + "Shutting down.");
    }

}
