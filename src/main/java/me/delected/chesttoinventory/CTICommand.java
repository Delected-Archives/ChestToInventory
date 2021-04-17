package me.delected.chesttoinventory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CTICommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("cti.toggle")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
            return true;
        }

        if (args.length == 0) {
            if (ChestToInventory.ctiPlayers.containsKey(p)) {
                p.sendMessage(ChatColor.GREEN + "You have successfully exited CTI mode!");
                ChestToInventory.ctiPlayers.remove(p);
                return true;
            }
            p.sendMessage(ChatColor.RED + "Invalid arguments given to enter CTI mode. Please provide 1 (/cti <name of inventory to create>)");
        } else if (args.length == 1) {
            if (!ChestToInventory.ctiPlayers.containsKey(p)) {
                p.sendMessage(ChatColor.GREEN + "You have successfully entered CTI mode!");
                ChestToInventory.ctiPlayers.put(p, args[0]);
                return true;
            }
            p.sendMessage(ChatColor.RED + "Invalid arguments given to exit CTI mode. Please provide none (/cti)");
        }

        p.sendMessage(ChatColor.RED + "Invalid arguments! Provide 1 to enter CTI mode, and none to exit!");
        return true;
    }
}
