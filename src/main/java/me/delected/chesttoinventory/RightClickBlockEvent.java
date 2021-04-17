package me.delected.chesttoinventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RightClickBlockEvent implements Listener {
    @EventHandler
    public void onPlayerRightClickChest(PlayerInteractEvent e) {
        if (!(e.getClickedBlock().getType() == Material.CHEST)) return;
        Player p = e.getPlayer();
        if (!ChestToInventory.ctiPlayers.containsKey(p)) return;

        String name = ChatColor.translateAlternateColorCodes('&', ChestToInventory.ctiPlayers.get(p));

        ChestToInventory.ctiPlayers.remove(p);

        File f = new File(ChestToInventory.getInst().getDataFolder() + name + ".txt");

        if (f.exists()) {
            p.sendMessage(ChatColor.RED + "This inventory already exists! Please choose another name.");
            return;
        }

        boolean wasCreated = false;

        try {
            wasCreated = f.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (!wasCreated) {
                p.sendMessage(ChatColor.RED + "For some reason, the file was not created. Please try again!");
            }
        }

        Chest chest =  (Chest) e.getClickedBlock();

        ItemStack[] chestContents = chest.getInventory().getContents();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));

            writer.append(String.format("Inventory inv = Bukkit.createInventory(null, %o, %s);\n", chest.getInventory().getSize(), "\"" + name + "\""));

            for (int i = 0; i < chestContents.length; i++) {
                // create itemstack
                writer.append("ItemStack item").append(String.valueOf(i)).append(" = new ItemStack(%mat%);\n".replace("%mat%", String.valueOf(chestContents[i].getType())));

                // create itemmeta
                if (!chestContents[i].hasItemMeta()) continue;
                String metaName = "itemMeta" + i;
                writer.append("ItemMeta " + metaName + " = item.getItemMeta();\n");

                // write displayname
                if (chestContents[i].getItemMeta().hasDisplayName()) {
                    writer.append(metaName).append(".setDisplayName(\"").append(chestContents[i].getItemMeta().getDisplayName()).append("\");\n");
                }

                // write lore
                if (chestContents[i].getItemMeta().hasLore()) {
                    List<String> lore = chestContents[i].getItemMeta().getLore();
                    writer.append(metaName).append(".setLore(Arrays.asList(");

                    for (int j = 0; j < lore.size(); j++) {
                        if (j != lore.size() - 1) {
                            writer.append("\"").append(lore.get(j)).append("\", ");
                            continue;
                        }
                        writer.append("\"").append(lore.get(j)).append("\"");
                    }
                    writer.append(");\n");
                }

                // write itemmeta
                writer.append("item").append(String.valueOf(i)).append(".setItemMeta(").append(metaName).append(");\n");

                // add to inv
                writer.append("inv.setItem(").append(String.valueOf(i)).append(", item").append(String.valueOf(i)).append(");\n");
            }

            writer.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
