package moe.oko.alcazar.command;

import moe.oko.alcazar.handler.InventoryHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class InventoryCommand implements TabExecutor {
    InventoryHandler inv;

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
                        ? (ChatColor.of("#986de0") + "Saved " + ChatColor.of("#ffffff") + "%s" + ChatColor.of("#986de0") + "!").formatted(args[1])
                        : (ChatColor.of("#986de0") + "Could not save "  + ChatColor.of("#ffffff") +  "%s".formatted(args[1]));
                sender.sendMessage(message);
            }
            case "load" -> {
                if (args.length != 2)
                    return false;
                var message = inv.load((Player) sender, args[1])
                        ? (ChatColor.of("#986de0") + "Loaded " + ChatColor.of("#ffffff") + "%s" + ChatColor.of("#986de0") + "!").formatted(args[1])
                        : (ChatColor.of("#ffffff") + "%s" + ChatColor.of("#986de0") + " not found.").formatted(args[1]);
                sender.sendMessage(message);
            }
            case "remove" -> {
                if (args.length != 2)
                    return false;
                var message = inv.remove((Player) sender, args[1])
                        ? (ChatColor.of("#986de0") + "Removed " + ChatColor.of("#ffffff") + "%s" + ChatColor.of("#986de0") + "!").formatted(args[1])
                        : (ChatColor.of("#986de0") + "Could not remove "  + ChatColor.of("#ffffff") +  "%s").formatted(args[1]);
                sender.sendMessage(message);
            }
            case "list" -> {
                var inventories = inv.list();
                if (inventories != null)
                    sender.sendMessage(ChatColor.of("#986de0") + "There are " + ChatColor.of("#ffffff") + inventories.size() + ChatColor.of("#986de0") + " saved inventories: " + ChatColor.of("#ffffff") + String.join((ChatColor.of("#986de0") + ", " + ChatColor.of("#ffffff")), inventories));
                else
                    sender.sendMessage(ChatColor.of("#986de0") + "There are no saved inventories.");
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
