package eu.terribuilder.tparty.team;

import eu.terribuilder.tparty.team.color.TeamColors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TeamsSystem {

    private static final String TEAM_NAME_PREFIX = "UHC";
    private static TeamsSystem teamsSystem;

    private int teamIndex;

    private TeamsSystem() {
        teamIndex = Bukkit.getScoreboardManager().getMainScoreboard().getTeams().size();
    }

    public static TeamsSystem getInstance() {
        if (teamsSystem == null) {
            teamsSystem = new TeamsSystem();
        }
        return teamsSystem;
    }

    public void createEmptyTeam() {
        getScoreboard().registerNewTeam(getTeamName());
    }

    public void createTeamIfNeeded(String playerName) {
        if (!hasATeam(playerName)){
            createTeam(Arrays.asList(playerName));
        }
    }

    public void createTeam(List<String> playerNames) {
        for (String playerName : playerNames) {
            Player player = Bukkit.getPlayer(playerName);
            Team team = getScoreboard().registerNewTeam(getTeamName());
            team.addPlayer(player);
        }
    }

    public Team getTeamByMembership(String teamMemberName) {
        Player teamMember = Bukkit.getPlayer(teamMemberName);
        return getScoreboard().getPlayerTeam(teamMember);
    }

    public void colorizeTeams() {
        Iterator<Team> teamsIterator = getScoreboard().getTeams().iterator();
        List<String> prefixes = TeamColors.getAllColorPrefixes();
        Iterator<String> prefixIterator = prefixes.iterator();
        while (teamsIterator.hasNext()) {
            Team team = teamsIterator.next();
            if (team.getName().startsWith(TEAM_NAME_PREFIX)) {
                team.setPrefix(prefixIterator.next());
                team.setSuffix(ChatColor.WHITE.toString());
                sendColorAssignedMessage(team);
            }
        }
    }

    public void addToTeam(String teamName, String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        Team team = getScoreboard().getTeam(teamName);
        if (team != null) {
            team.addPlayer(player);
            sendAddedToTeamMessage(player.getPlayer(), team);
        }
    }

    public void removeFromTeam(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        Team team = getScoreboard().getPlayerTeam(player);
        if (team != null) {
            team.removePlayer(player);
            sendRemovedFromTeam(player, team);
            unregisterIfEmpty(team);
        }
    }

    public void deleteTeams() {
        for (Team team: getScoreboard().getTeams()) {
            String teamName = team.getName();
            if (teamName.startsWith(TEAM_NAME_PREFIX)) {
                team.unregister();

            }
        }
        sendTeamsDeleted();
    }

    public void deleteTeam(String teamName) {
        Team team = getScoreboard().getTeam(teamName);
        if (team != null) {
            team.unregister();
            sendTeamDeleted(team);
        }
    }

    private void unregisterIfEmpty(Team team) {
        if (team.getPlayers().isEmpty()) {
            team.unregister();
        }
    }

    private boolean hasATeam(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player) != null;
    }

    private String getTeamName() {
        return TEAM_NAME_PREFIX + teamIndex++;
    }

    private Scoreboard getScoreboard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }

    private void sendAddedToTeamMessage(Player player, Team team) {
        if (player.isOnline()) {
            player.sendMessage(ChatColor.GREEN + "You were added to team " + team.getPrefix() + team.getName() + ".");
        }
    }

    private void sendRemovedFromTeam(Player player, Team team) {
        if (player.isOnline()) {
            player.sendMessage(ChatColor.GOLD + "You were removed from team " + team.getPrefix() + team.getName() + ".");
        }
    }

    private void sendColorAssignedMessage(Team team) {
        messageTeam(team, ChatColor.GREEN + "Your team has been assigned a "
                + team.getPrefix() + "team color" + ChatColor.WHITE.toString() + ".");
    }

    private void sendTeamsDeleted() {
        Bukkit.broadcastMessage(ChatColor.GOLD + "All teams have been deleted.");
    }

    private void sendTeamDeleted(Team team) {
        messageTeam(team, ChatColor.GOLD + "Your team was removed.");
    }

    private void messageTeam(Team team, String message) {
        for (OfflinePlayer offlinePlayer: team.getPlayers()) {
            Player player = offlinePlayer.getPlayer();
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }
}
