package eu.terribuilder.tparty.commands;

import eu.terribuilder.tparty.party.PartySystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCommands implements CommandExecutor {

    private static final String INVITE_COMMAND = "invite";
    private static final int INVITE_COMMAND_LENGTH = 2;
    private static final String ACCEPT_COMMAND = "accept";
    private static final int ACCEPT_COMMAND_LENGTH = 2;
    private static final String LEAVE_COMMAND = "leave";
    private static final int SUBCOMMAND_INDEX = 0;
    private static final int NAME_INDEX = 1;

    public static final String MAIN_COMMAND = "party";

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        PartySystem partySystem = PartySystem.getInstance();
        if (partySystem.isRunning()) {
            processPartyCommands(commandSender, args);
        } else {
            showPartySystemNotRunning(commandSender.getName());
        }
        return true;
    }

    private void processPartyCommands(CommandSender commandSender, String[] args) {
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
        player.sendMessage(ChatColor.YELLOW + "/party invite [name] - invite a player to your party," +
                " this will also whitelist him/her");
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

    private void showPartySystemNotRunning(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        player.sendMessage(ChatColor.RED + "You can't create parties at this moment");
    }


}

