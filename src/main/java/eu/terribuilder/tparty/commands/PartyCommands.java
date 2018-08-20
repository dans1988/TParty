package eu.terribuilder.tparty.commands;

import eu.terribuilder.tparty.party.PartySystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommands implements CommandExecutor {

    private static final String MAIN_COMMAND = "party";
    private static final String INVITE_COMMAND = "invite";
    private static final int INVITE_COMMAND_LENGTH = 2;
    private static final String ACCEPT_COMMAND = "accept";
    private static final int ACCEPT_COMMAND_LENGTH = 2;
    private static final String LEAVE_COMMAND = "leave";
    private static final int SUBCOMMAND_INDEX = 1;
    private static final int NAME_INDEX = 1;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (command.getName().compareToIgnoreCase(MAIN_COMMAND) == 0 && args.length > 0) {
            processPartyCommands(commandSender, command, label, args);
        }
        return true;
    }

    private void processPartyCommands(CommandSender commandSender, Command command, String label, String[] args) {
        String commandSenderName = commandSender.getName();
        if (args.length == 0) {
            showPartyCommands(commandSenderName);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(INVITE_COMMAND) == 0) {
            processInviteCommand(commandSenderName, args);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(ACCEPT_COMMAND) == 0) {
            processAcceptCommand(commandSenderName, args);
        } else if (args[SUBCOMMAND_INDEX].compareToIgnoreCase(LEAVE_COMMAND) == 0) {
            processLeaveCommand(commandSenderName);
        }
    }

    private void processInviteCommand(String commandSenderName, String[] args) {
        if (args.length < INVITE_COMMAND_LENGTH) {
            showInviteNoNameError(commandSenderName);
        } else {
            String invitee = args[NAME_INDEX];
            PartySystem.getInstance().invite(commandSenderName, invitee);
        }
    }

    private void processAcceptCommand(String commandSenderName, String[] args) {
        if (args.length < ACCEPT_COMMAND_LENGTH) {
            showAcceptNoNameError(commandSenderName);
        } else {
            String inviter = args[NAME_INDEX];
            PartySystem.getInstance().accept(inviter, commandSenderName);
        }
    }

    private void processLeaveCommand(String commandSenderName) {
        PartySystem.getInstance().leave(commandSenderName);
    }

    private void showPartyCommands(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage(ChatColor.YELLOW + "/party invite [name] - invite a player to your party");
        player.sendMessage(ChatColor.YELLOW + "/party accept [name] - accept player's invitation");
        player.sendMessage(ChatColor.YELLOW + "/party leave - leave your current party");
    }

    private void showInviteNoNameError(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage(ChatColor.RED + "You must specify player's name - /party invite [name]");
    }

    private void showAcceptNoNameError(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage(ChatColor.RED + "You must specify player's name - /party accept [name]");
    }
}

