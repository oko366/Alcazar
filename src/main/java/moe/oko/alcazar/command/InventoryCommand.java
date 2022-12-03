package moe.oko.alcazar.command;

import moe.oko.alcazar.handler.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class InventoryCommand implements TabExecutor {
    final InventoryHandler inv;

    public InventoryCommand (InventoryHandler inventoryHandler) { inv = inventoryHandler; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }
        if (args.length == 0)
            return false;

        switch (args[0].toLowerCase()) {
            case "save" -> {
                // crashes without args[1]
                if (args.length != 2)
                    return false;
                var message = inv.save((Player) sender, args[1])
                        ? "Saved %s!".formatted(args[1])
                        : "Unable to save %s".formatted(args[1]);
                sender.sendMessage(message);
            }
            case "load" -> {
                if (args.length != 2)
                    return false;
                var message = inv.load((Player) sender, args[1])
                        ? "Loaded %s!".formatted(args[1])
                        : "%s not found.".formatted(args[1]);
                sender.sendMessage(message);
            }
            case "remove" -> {
                if (args.length != 2)
                    return false;
                var message = inv.remove((Player) sender, args[1])
                        ? "Removed %s!".formatted(args[1])
                        : "Could not remove %s".formatted(args[1]);
                sender.sendMessage(message);
            }
            case "list" -> {
                var inventories = inv.list();
                if (inventories != null)
                    sender.sendMessage("There are " + inventories.size() + " saved inventories: " + String.join(", ", inventories));
                else
                    sender.sendMessage("There are no saved inventories");
            }
            default -> { return false; }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("load", "save", "remove", "list");
            case 2 -> inv.list();
            default -> null;
        };
    }
}
