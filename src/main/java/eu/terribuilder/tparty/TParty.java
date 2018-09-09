package eu.terribuilder.tparty;

import eu.terribuilder.tparty.commands.PartyCommands;
import eu.terribuilder.tparty.commands.TeamCommands;
import eu.terribuilder.tparty.commands.TeamsCommands;
import eu.terribuilder.tparty.listeners.PlayerJoinListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class TParty extends JavaPlugin {

    public void onEnable() {
        getLogger().log(Level.INFO, "{0}onEnable", ChatColor.RED);

        getCommand(TeamsCommands.MAIN_COMMAND).setExecutor(new TeamsCommands());
        getCommand(TeamCommands.MAIN_COMMAND).setExecutor(new TeamCommands());
        getCommand(PartyCommands.MAIN_COMMAND).setExecutor(new PartyCommands());

        PlayerJoinListener playerJoinListener = new PlayerJoinListener();
        getServer().getPluginManager().registerEvents(playerJoinListener, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
