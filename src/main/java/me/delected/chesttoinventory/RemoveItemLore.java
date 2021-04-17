package me.delected.chesttoinventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RemoveItemLore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("cti.removelore")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Invalid arguments given! Please run /<command> <line to remove>");
            return true;
        }

        ItemStack hand = p.getInventory().getItemInMainHand();

        if (!hand.hasItemMeta() || hand.getType() == Material.AIR) {
            p.sendMessage(ChatColor.RED + "You cannot add lore to an item without metadata!");
            return true;
        }

        ItemMeta handMeta = hand.getItemMeta();

        if (handMeta.getLore() == null && handMeta.getLore().isEmpty()) {
            p.sendMessage(ChatColor.RED + "You cannot remove lore from an item without lore!");
            return true;
        }

        List<String> existingLore = handMeta.getLore();

        existingLore.remove(args[0]);

        handMeta.setLore(existingLore);
        hand.setItemMeta(handMeta);

        p.sendMessage(ChatColor.GREEN + "You have successfully remove line " + args[0] + " from this item's lore!");

        return true;
    }
}
