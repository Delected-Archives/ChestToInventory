package me.delected.chesttoinventory;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class ChestToInventory extends JavaPlugin {
    private static JavaPlugin INSTANCE = null;

    public static JavaPlugin getInst() { return INSTANCE; }

    public static Map<Player, String> ctiPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        if (!getConfig().getBoolean("enabled", true)) {
            getLogger().warning("CTI is configured to be disabled in config. If you don't want this, change the value from 'false' to 'true'");
            this.setEnabled(false);
            return;
        }

        INSTANCE = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new RightClickBlockEvent(), this);
        getCommand("cti").setExecutor(new CTICommand());
        getCommand("setdisplayname").setExecutor(new SetItemDisplayName());
        getCommand("addlore").setExecutor(new AddItemLore());
        getCommand("removelore").setExecutor(new RemoveItemLore());
    }
}
