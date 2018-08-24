package eu.terribuilder.tparty.commands;

import eu.terribuilder.tparty.team.TeamsSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamCommands implements CommandExecutor {

    private static final String ADD_PLAYER_COMMAND = "add";
    private static final int MIN_ADD_PLAYER_COMMAND_LEN = 4;
    private static final String LEAVE_PLAYER_COMMAND = "leave";
    private static final int MIN_LEAVE_PLAYER_COMMAND_LEN = 3;
    private static final String DELETE_COMMAND = "delete";
    private static final int MIN_DELETE_TEAM_LEN = 3;
    private static final int TEAM_NAME_INDEX = 1;
    private static final int PLAYER_NAME_INDEX = 2;
    private static final int SUBCOMMAND_INDEX = 0;

    public static final String MAIN_COMMAND = "team";


    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        processTeamCommands(commandSender, args);
        return true;
    }

    private void processTeamCommands(CommandSender commandSender, String[] args) {
        String commandSenderName = commandSender.getName();
        if (args.length == 0) {
            showTeamCommands(commandSenderName);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(ADD_PLAYER_COMMAND) == 0) {
            processAddPlayerCommand(args);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(LEAVE_PLAYER_COMMAND) == 0) {
            processRemovePlayerCommand(args);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(DELETE_COMMAND) == 0) {
            processDeleteTeamCommand(args);
        }
    }

    private void processAddPlayerCommand(String[] args) {
        if (args.length >= MIN_ADD_PLAYER_COMMAND_LEN) {
            String teamName = args[TEAM_NAME_INDEX];
            String playerName = args[PLAYER_NAME_INDEX];
            TeamsSystem.getInstance().addToTeam(teamName, playerName);
        }
    }

    private void processRemovePlayerCommand(String[] args) {
        if (args.length >= MIN_LEAVE_PLAYER_COMMAND_LEN) {
            String playerName = args[PLAYER_NAME_INDEX];
            TeamsSystem.getInstance().removeFromTeam(playerName);
        }
    }

    private void processDeleteTeamCommand(String[] args) {
        if (args.length >= MIN_DELETE_TEAM_LEN) {
            String teamName = args[TEAM_NAME_INDEX];
            TeamsSystem.getInstance().deleteTeam(teamName);
        }
    }

    private void showTeamCommands(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage(ChatColor.YELLOW + "/team add [team] [player] - add player to a team");
        player.sendMessage(ChatColor.YELLOW + "/team leave [name] - remove player from his team");
        player.sendMessage(ChatColor.YELLOW + "/team delete [team] - remove a team ");
    }

}
