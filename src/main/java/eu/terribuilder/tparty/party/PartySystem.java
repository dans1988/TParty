package eu.terribuilder.tparty.party;

import com.google.common.collect.Sets;
import eu.terribuilder.tparty.team.TeamsSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
        OfflinePlayer invitedPlayer = Bukkit.getPlayer(invitee);
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
            TeamsSystem.getInstance().addToTeamByMembership(inviter, invitee);
            invites.remove(invitee);
            showAccepted(inviter, invitee);
        } else {
            showNotInvitedError(inviter, invitee);
        }
    }

    public void leave(String player) {
        TeamsSystem.getInstance().removeFromTeam(player);
        invites.remove(player);
    }

    private boolean isInvited(String inviter, String invitee) {
        boolean partyExists = invites.containsKey(inviter);
        boolean isInvited = false;
        if (partyExists) {
            isInvited = invites.get(inviter).contains(invitee);
        }
        Bukkit.broadcastMessage(partyExists + "");
        Bukkit.broadcastMessage(isInvited + "");
        return partyExists && isInvited;
    }

    private void showWhitelisted(String inviter, String invitee) {
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.RED
                + "Player '" + invitee + "' has been whitelisted.");
    }


    private void showNotInvitedError(String inviter, String invitee) {
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.RED
                + "You were not invited to a party by '" + inviter + "' or the party no longer exists.");
    }

    private void showInvited(String inviter, String invitee) {
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN
                + "You were invited to a party by " + ChatColor.GOLD +  inviter + ".");
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN +  "To accept use the following command: ");
        Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN + "/party accept " + inviter);
    }

    private void showAccepted(String inviter, String invitee) {
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.GREEN
                + "Your invite was accepted by " + invitee + ".");

        Bukkit.getPlayer(invitee).sendMessage(ChatColor.GREEN
                + "You joined " + invitee + "'s team.");
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
            player.sendMessage(ChatColor.GREEN + "You were invited to a party by the following players: " + inviters);
            player.sendMessage(ChatColor.GREEN + "Do /party accept [name] to join a team");
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public boolean isRunning() {
        return running;
    }
}
