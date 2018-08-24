package eu.terribuilder.tparty.team.color;

import org.bukkit.ChatColor;

public class TeamColorPrefix {

    private final ChatColor chatColor;
    private final ChatColor modifier;
    private final String prefix;

    public TeamColorPrefix(ChatColor chatColor, ChatColor modifier, String prefix) {
        this.chatColor = chatColor;
        this.modifier = modifier;
        this.prefix = prefix;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public ChatColor getModifier() {
        return modifier;
    }

    public String getPrefix() {
        return prefix;
    }
}
