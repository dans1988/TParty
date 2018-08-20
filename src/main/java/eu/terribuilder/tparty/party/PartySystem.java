package eu.terribuilder.tparty.party;

import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PartySystem {

    private static PartySystem partySystem;
    private Map<String, Set<String>> invites;

    private PartySystem() {
        invites = new HashMap<String, Set<String>>();
    }

    public static PartySystem getInstance() {
        if (partySystem == null) {
            partySystem = new PartySystem();
        }
        return partySystem;
    }

    public void invite(String inviter, String invitee) {
        Player invitedPlayer = Bukkit.getPlayer(invitee);
        if (invitedPlayer != null) {
            TeamsSystem.getInstance().createTeamIfNeeded(inviter);
            Set<String> invitees = invites.getOrDefault(inviter, Sets.newHashSet());
            invitees.add(invitedPlayer.getName());
        } else {
            showNotOnlineError(inviter);
        }
    }

    public void accept(String inviter, String invitee) {
        if (isInvited(inviter, invitee)) {
            TeamsSystem.getInstance().addToTeamByMembership(inviter, invitee);
            invites.remove(invitee);
        } else {
            showNotInvitedError(inviter);
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
        return partyExists && isInvited;
    }

    private void showNotOnlineError(String playerName) {
        Bukkit.getPlayer(playerName).sendMessage(ChatColor.RED
                + "Player '" + playerName + "' is not online and can't be invited.");
    }

    private void showNotInvitedError(String inviter) {
        Bukkit.getPlayer(inviter).sendMessage(ChatColor.RED
                + "You were not invited to a party by '" + inviter + "' or the party no longer exists.");
    }

}
