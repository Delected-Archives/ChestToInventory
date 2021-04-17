package me.delected.chesttoinventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RightClickBlockEvent implements Listener {
    @EventHandler
    public void onPlayerRightClickChest(PlayerInteractEvent e) {

        // this is null for some reason
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock().getType() != Material.CHEST || !(e.getClickedBlock().getState() instanceof Chest)) return;
        Player p = e.getPlayer();
        if (!ChestToInventory.ctiPlayers.containsKey(p)) return;

        String name = ChatColor.translateAlternateColorCodes('&', ChestToInventory.ctiPlayers.get(p));

        ChestToInventory.ctiPlayers.remove(p);

        File f = new File(ChestToInventory.getInst().getDataFolder() + "/" + name + ".txt");

        if (f.exists()) {
            p.sendMessage(ChatColor.RED + "This inventory already exists! Please choose another name. Removing you from CTI mode...");
            p.closeInventory();
            ChestToInventory.ctiPlayers.remove(p);
            return;
        }

        boolean wasCreated = false;
        /* Create file */
        try {
            wasCreated = f.createNewFile();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            if (!wasCreated) {
                p.sendMessage(ChatColor.RED + "For some reason, the file was not created. Please try again! Removing you from CTI mode...");
                p.closeInventory();
                ChestToInventory.ctiPlayers.remove(p);
            }
        }

        Chest chest = (Chest) e.getClickedBlock().getState();

        ItemStack[] chestContents = chest.getInventory().getContents();

        /* Write to file */
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            writer.append("Inventory inv = Bukkit.createInventory(null, %size%, \"%name%\");\n"
                    .replace("%size%", String.valueOf(chest.getInventory().getSize()))
                    .replace("%name%", ChatColor.translateAlternateColorCodes('&', name)));


            for (int i = 0; i < chestContents.length; i++) {
                if (chestContents[i] == null) continue;

                // create itemstack
                writer.append("ItemStack item").append(String.valueOf(i)).append(" = new ItemStack(Material.%mat%);\n".replace("%mat%", String.valueOf(chestContents[i].getType())));

                // create itemmeta
                if (!chestContents[i].hasItemMeta()) continue;
                String metaName = "itemMeta" + i;
                writer.append("ItemMeta ").append(metaName).append(" = item").append(String.valueOf(i)).append(".getItemMeta();\n");

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
            p.closeInventory();
            p.sendMessage(ChatColor.GREEN + "You have successfully saved this chest into a file! Now exiting CTI mode...");

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
