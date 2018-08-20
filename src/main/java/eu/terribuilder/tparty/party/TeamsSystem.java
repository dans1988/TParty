package eu.terribuilder.tparty.party;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import java.util.Arrays;
import java.util.List;

public class TeamsSystem {

    private static final String teamNamePrefix = "UHC";

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

    public void addToTeamByMembership(String teamMemberName, String newMemberName) {
        Player teamMember = Bukkit.getPlayer(teamMemberName);
        Player newMember = Bukkit.getPlayer(newMemberName);
        Team team = getScoreboard().getPlayerTeam(teamMember);
        team.addPlayer(newMember);
    }

    public void removeFromTeam(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        Team team = getScoreboard().getPlayerTeam(player);
        if (team != null) {
            team.removePlayer(player);
            uregisterIfEmpty(team);
        }
    }

    private void uregisterIfEmpty(Team team) {
        if (team.getPlayers().isEmpty()) {
            team.unregister();
        }
    }

    private boolean hasATeam(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        return Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(player) != null;
    }

    private String getTeamName() {
        return teamNamePrefix + teamIndex++;
    }

    private Scoreboard getScoreboard() {
        return Bukkit.getScoreboardManager().getMainScoreboard();
    }
}
