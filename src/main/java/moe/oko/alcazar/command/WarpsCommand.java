package moe.oko.alcazar.command;

import moe.oko.alcazar.handler.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpsCommand implements TabExecutor {
    WarpHandler handler;

    public WarpsCommand (WarpHandler warpHandler) { handler = warpHandler; }

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
                var message = handler.save((Player) sender, args[1])
                        ? (ChatColor.of("#986de0") + "Saved " + ChatColor.of("#ffffff") + "%s" + ChatColor.of("#986de0") + "!").formatted(args[1])
                        : (ChatColor.of("#986de0") + "Could not save "  + ChatColor.of("#ffffff") +  "%s".formatted(args[1]));
                sender.sendMessage(message);
            }
            case "remove" -> {
                if (args.length != 2)
                    return false;
                var message = handler.remove((Player) sender, args[1])
                        ? (ChatColor.of("#986de0") + "Removed " + ChatColor.of("#ffffff") + "%s" + ChatColor.of("#986de0") + "!").formatted(args[1])
                        : (ChatColor.of("#986de0") + "Could not remove "  + ChatColor.of("#ffffff") +  "%s").formatted(args[1]);
                sender.sendMessage(message);
            }
            case "list" -> {
                var warps = handler.list();
                var tags = handler.tagList();
                if (warps != null) {
                    sender.sendMessage(ChatColor.of("#986de0") + "There are " + warps.size() + " saved warps:");
                    for (int i = 0; i < warps.size(); i++) {
                        if (tags.get(i).toString().equals("user")) {
                            sender.sendMessage("   " + ChatColor.of("#ffffff") + warps.get(i) + " (§b" + tags.get(i) + ChatColor.of("#ffffff") + ")");
                        } else {
                            sender.sendMessage("   " + ChatColor.of("#ffffff") + warps.get(i) + " (§d" + tags.get(i) + ChatColor.of("#ffffff") + ")");
                        }
                    }
                }
                else {
                    sender.sendMessage(ChatColor.of("#986de0") + "There are no saved warps.");
                }
            }
            default -> { return false; }
        }
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return switch (args.length) {
            case 1 -> List.of("save", "remove", "list");
            case 2 -> handler.list();
            default -> null;
        };
    }
}
