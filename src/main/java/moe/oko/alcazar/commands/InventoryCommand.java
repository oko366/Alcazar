package moe.oko.alcazar.commands;

import moe.oko.alcazar.handlers.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class InventoryCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "save" -> InventoryHandler.save((Player) sender, args[1]);

            case "load" -> {
                try {
                    InventoryHandler.load((Player) sender, args[1]);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            default -> {return false;}
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        // TODO: make onTabComplete not complete insanity
        if (args.length == 1) {
            return List.of("load", "save");
        }
        return List.of("");
    }
}
