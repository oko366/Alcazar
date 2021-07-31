package moe.oko.alcazar.commands;

import moe.oko.alcazar.handlers.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to run this command!");
            return false;
        }
        if (args.length == 0) {
            // send help message
            sender.sendMessage("args needed lol");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "save" -> InventoryHandler.save((Player) sender, args[1]);

            case "load" -> InventoryHandler.load((Player) sender, args[1]);

            default -> sender.sendMessage("0");
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
