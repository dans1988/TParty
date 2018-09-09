package eu.terribuilder.tparty.party;

import com.google.common.collect.Sets;
import eu.terribuilder.tparty.messages.OpMessenger;
import eu.terribuilder.tparty.team.TeamsSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PartySystem {

    private static PartySystem partySystem;
    private Map<String, Set<String>> invites;
    private boolean running;
    private Integer limit;

    private PartySystem() {
        invites = new HashMap<>();
        running = false;
        limit = 1;
    }

    public static PartySystem getInstance() {
        if (partySystem == null) {
            partySystem = new PartySystem();
        }
        return partySystem;
    }

    public void invite(String inviter, String invitee) {
        OfflinePlayer invitedPlayer = Bukkit.getOfflinePlayer(invitee);
        TeamsSystem.getInstance().createTeamIfNeeded(inviter);
        Set<String> invitees = invites.getOrDefault(inviter, Sets.newHashSet());
        invitees.add(invitedPlayer.getName());
        invites.put(inviter, invitees);
        showInvited(inviter, invitee);
        if (!invitedPlayer.isOnline()) {
            invitedPlayer.setWhitelisted(true);
            showWhitelisted(inviter, invitee);
        }
    }

    public void accept(String inviter, String invitee) {
        if (isInvited(inviter, invitee)) {
            checkLimitAndAddToTeam(inviter, invitee);
        } else {
            showNotInvitedError(inviter, invitee);
        }
    }

    public void leave(String player) {
        TeamsSystem.getInstance().removeFromTeam(player);
        invites.remove(player);
    }

    private void checkLimitAndAddToTeam(String inviter, String invitee) {
        Team team = TeamsSystem.getInstance().getTeamByMembership(inviter);
        if (team != null && team.getSize() < limit) {
            invites.remove(invitee);
            showAccepted(inviter, invitee);
            TeamsSystem.getInstance().addToTeam(team.getName(), invitee);
        } else if (team.getSize() >= limit){
            showPartyFull(inviter, invitee);
        } else if (team == null){
            showPartyDoesNotExistError(inviter, invitee);
        }
    }

    private boolean isInvited(String inviter, String invitee) {
        boolean partyExists = invites.containsKey(inviter);
        boolean isInvited = false;
        if (partyExists) {
            isInvited = invites.get(inviter).contains(invitee);
        }
        return partyExists && isInvited;
    }

    private void showWhitelisted(String inviter, String invitee) {
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.RED
                + "Player '" + invitee + "' has been whitelisted.");
        OpMessenger.getInstance().messageOPs(ChatColor.GRAY
                + "[Player '" + invitee + "' has been whitelisted.]");
    }


    private void showNotInvitedError(String inviter, String invitee) {
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.RED
                + "You were not invited to a party by " + inviter + " or the party no longer exists.");
    }

    private void showPartyDoesNotExistError(String inviter, String invitee) {
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.RED
                + inviter + "'s party no longer exists.");
    }

    private void showPartyFull(String inviter, String invitee) {
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.RED
                + inviter + "'s party is full and you can't join it.");
    }

    private void showInvited(String inviter, String invitee) {
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.GREEN + "You invited " + invitee + " to your party.");

        Player inviteePlayer = Bukkit.getPlayer(invitee);
        if (inviteePlayer != null) {
            Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN
                    + "You were invited to a party by " + ChatColor.GOLD + inviter + ".");
            Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN + "To accept use the following command: ");
            Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN + "/party accept " + inviter);
        }
    }

    private void showAccepted(String inviter, String invitee) {
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.GREEN
                + "Your invite was accepted by " + invitee + ".");

        Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN
                + "You joined " + inviter + "'s team.");
    }

    public void listInviters(String invitee) {
        String inviters = "";
        for (String inviter : invites.keySet()) {
            Set<String> invitedPlayers = invites.get(inviter);
            for (String invitedPlayer: invitedPlayers) {
                if (invitedPlayer.compareToIgnoreCase(invitee) == 0) {
                    inviters += inviter + " ";
                }
            }
        }

        if (!inviters.isEmpty()) {
            Player player = Bukkit.getPlayer(invitee);
            player.sendMessage(ChatColor.GREEN + "You were invited to a party by the following players: "
                    + inviters);
            player.sendMessage(ChatColor.GREEN + "Do " + ChatColor.YELLOW + "/party accept [name]"
                    + ChatColor.GREEN+ " to join a team");
        }
    }

    public void start(Integer maxPartySize) {
        this.running = true;
        this.limit = maxPartySize;
        broadcastPartyOpen();
    }

    public void stop() {
        this.running = false;
        broadcastPartyClose();
    }

    public boolean isRunning() {
        return running;
    }

    private void broadcastPartyOpen() {
        String message = ChatColor.GREEN
                + "Party system is now open. You can create parties up to " + limit + " players. "
                + "Do" + ChatColor.YELLOW + " /party " + ChatColor.GREEN + "to see how to create teams.";
        Bukkit.broadcastMessage(message);
    }

    private void broadcastPartyClose() {
        String message = ChatColor.GREEN
                + "Party system is now closed.";
        Bukkit.broadcastMessage(message);
    }

}
