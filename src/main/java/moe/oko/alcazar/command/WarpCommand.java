package moe.oko.alcazar.command;

import moe.oko.alcazar.handler.WarpHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class WarpCommand implements TabExecutor {
    final WarpHandler handler;

    public WarpCommand (WarpHandler warpHandler) { handler = warpHandler; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command!");
            return true;
        }

        if (args.length != 1)
            return false;
        var message = handler.load((Player) sender, args[0])
                ? "Warping to %s!".formatted(args[0])
                : "%s not found.".formatted(args[0]);
        sender.sendMessage(message);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return switch (args.length) {
            case 1 -> handler.list();
            default -> null;
        };
    }
}
