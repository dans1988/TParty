package eu.terribuilder.tparty.team.color;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class TeamColors {

    private static final ChatColor[] ORDERED_TEAM_COLORS = {
            ChatColor.DARK_RED,
            ChatColor.AQUA,
            ChatColor.LIGHT_PURPLE,
            ChatColor.GREEN,
            ChatColor.YELLOW,
            ChatColor.GRAY,
            ChatColor.GOLD,
            ChatColor.DARK_PURPLE,
            ChatColor.DARK_GREEN,
            ChatColor.BLUE,
            ChatColor.RED,
            ChatColor.DARK_GRAY,
            ChatColor.BLACK,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_BLUE
    };

    private static final ChatColor[] ORDERED_MODIFIERS = {
            ChatColor.ITALIC,
            ChatColor.BOLD,
            ChatColor.STRIKETHROUGH,
            ChatColor.UNDERLINE
    };

    public static List<String> getAllColorPrefixes() {
        List<String> prefixes = Lists.newArrayList();
        for (ChatColor chatColor: ORDERED_TEAM_COLORS) {
            prefixes.add(chatColor.toString());
        }
        for (ChatColor modifier: ORDERED_MODIFIERS) {
            for (ChatColor chatColor: ORDERED_TEAM_COLORS) {
                prefixes.add(chatColor + modifier.toString());
            }
        }
        return prefixes;
    }

}
