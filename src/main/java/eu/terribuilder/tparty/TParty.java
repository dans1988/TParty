package eu.terribuilder.tparty;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class TParty extends JavaPlugin {

    public void onEnable() {
        getLogger().log(Level.INFO, "{0}onEnable", ChatColor.RED);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
