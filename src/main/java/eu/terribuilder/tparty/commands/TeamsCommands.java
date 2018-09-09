package eu.terribuilder.tparty.commands;

import com.google.common.collect.Lists;
import eu.terribuilder.tparty.party.PartySystem;
import eu.terribuilder.tparty.team.TeamsSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TeamsCommands implements CommandExecutor {

    private static final String CREATE_COMMAND = "create";
    private static final String ERASE_COMMAND = "erase";
    private static final String COLORIZE_COMMAND = "colorize";
    private static final String PARTY_COMMAND = "party";
    private static final String PARTY_OPEN_COMMAND = "open";
    private static final String PARTY_CLOSE_COMMAND = "close";
    private static final int NAMES_START_INDEX = 1;
    private static final int PARTY_SIZE_INDEX = 2;
    private static final int SUBCOMMAND_INDEX = 0;
    private static final int PARTY_SUBCOMMAND_INDEX = 1;
    private static final int PARTY_OPEN_COMMAND_LEN = 3;
    private static final int PARTY_CLOSE_COMMAND_LEN = 2;

    public static final String MAIN_COMMAND = "teams";

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        processTeamsCommands(commandSender, args);
        return true;
    }

    private void processTeamsCommands(CommandSender commandSender, String[] args) {
        String commandSenderName = commandSender.getName();
        if (args.length == 0) {
            showTeamsCommands(commandSenderName);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(CREATE_COMMAND) == 0) {
            processCreateCommand(args);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(ERASE_COMMAND) == 0) {
            processEraseCommand();
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(COLORIZE_COMMAND) == 0) {
            processColorizeCommand();
        } else if (isPartyOpenCommand(args)) {
            processPartyOpenCommand(args);
        } else if (isPartyCloseCommand(args)) {
            processPartyCloseCommand(args);
        }
    }


    private void processPartyOpenCommand(String[] args) {
        Integer maxPartySize = Integer.parseInt(args[PARTY_SIZE_INDEX]);
        PartySystem.getInstance().start(maxPartySize);
    }

    private void processPartyCloseCommand(String[] args) {
        PartySystem.getInstance().stop();
    }

    private boolean isPartyOpenCommand(String[] args) {
        return args.length >= PARTY_OPEN_COMMAND_LEN
                && args[SUBCOMMAND_INDEX].compareToIgnoreCase(PARTY_COMMAND) == 0
                && args[PARTY_SUBCOMMAND_INDEX].compareToIgnoreCase(PARTY_OPEN_COMMAND) == 0;
    }

    private boolean isPartyCloseCommand(String[] args) {
        return args.length >= PARTY_CLOSE_COMMAND_LEN
                && args[SUBCOMMAND_INDEX].compareToIgnoreCase(PARTY_COMMAND) == 0
                && args[PARTY_SUBCOMMAND_INDEX].compareToIgnoreCase(PARTY_CLOSE_COMMAND) == 0;
    }

    private void processCreateCommand(String[] args) {
        if (args.length == 1) {
            TeamsSystem.getInstance().createEmptyTeam();
        } else {
            List<String> playerNames = createPlayerNamesFromArgs(args);
            TeamsSystem.getInstance().createTeam(playerNames);
        }
    }

    private void processColorizeCommand() {
        TeamsSystem.getInstance().colorizeTeams();
    }

    private void processEraseCommand() {
        TeamsSystem.getInstance().deleteTeams();
    }

    private List<String> createPlayerNamesFromArgs(String[] args) {
        List<String> playerNames = Lists.newArrayList();
        for (int i = NAMES_START_INDEX; i < args.length; i++) {
            playerNames.add(args[i]);
        }
        return playerNames;
    }

    private void showTeamsCommands(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage(ChatColor.YELLOW + "/teams create - create a new team");
        player.sendMessage(ChatColor.YELLOW
                + "/teams create [name1] [name2] [name3]... - create a new team with players");
        player.sendMessage(ChatColor.YELLOW + "/teams erase - erase all teams");
        player.sendMessage(ChatColor.YELLOW + "/teams colorize - colorize teams");
        player.sendMessage(ChatColor.YELLOW + "/teams party open [team size]" +
                " - allow party creation of team size up to [team size]");
        player.sendMessage(ChatColor.YELLOW + "/teams party close - disallow party creation");
    }


}
