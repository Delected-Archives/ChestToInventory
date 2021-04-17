package me.delected.chesttoinventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.Arrays;

public class SetItemDisplayName implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("cti.name")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
            return true;
        }

        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "Invalid arguments given! Please run /<command> <name of the item>");
            return true;
        }

        ItemStack hand = p.getInventory().getItemInMainHand();

        if (!hand.hasItemMeta() || hand.getType() == Material.AIR) {
            p.sendMessage(ChatColor.RED + "You cannot re-name an item without metadata!");
            return true;
        }

        ItemMeta handMeta = hand.getItemMeta();

        StringBuilder name = new StringBuilder();

        Arrays.stream(args).forEach(arg -> name.append(ChatColor.translateAlternateColorCodes('&', arg)).append(" "));

        handMeta.setDisplayName(name.toString());
        hand.setItemMeta(handMeta);

        p.sendMessage(ChatColor.GREEN + "You have successfully renamed this item!");

        return true;
    }
}
