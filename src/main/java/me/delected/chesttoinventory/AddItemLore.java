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

public class AddItemLore implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command!");
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("cti.addlore")) {
            p.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
            return true;
        }

        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "Invalid arguments given! Please run /<command> <lore for this line>");
            return true;
        }

        ItemStack hand = p.getInventory().getItemInMainHand();

        if (hand.getType() == Material.AIR) {
            p.sendMessage(ChatColor.RED + "You cannot add lore to your fist!");
            return true;
        }

        ItemMeta handMeta = hand.getItemMeta();

        StringBuilder lore = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            lore.append(ChatColor.translateAlternateColorCodes('&', args[i]));
            if (i == args.length - 1) continue;
            lore.append(" ");
        }
        if (handMeta.getLore() != null && !handMeta.getLore().isEmpty()) {
            List<String> existingLore = handMeta.getLore();
            existingLore.add(lore.toString());
            handMeta.setLore(existingLore);
            hand.setItemMeta(handMeta);
            p.sendMessage(ChatColor.GREEN + "You have successfully added lore to this item!");
            return true;
        }

        handMeta.setLore(Collections.singletonList(lore.toString()));
        hand.setItemMeta(handMeta);

        p.sendMessage(ChatColor.GREEN + "You have successfully created lore for this item!");

        return true;
    }
}
