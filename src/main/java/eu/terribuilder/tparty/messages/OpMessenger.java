package eu.terribuilder.tparty.messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OpMessenger {

    private static OpMessenger instance;

    private OpMessenger() {}

    public static OpMessenger getInstance() {
        if (instance == null) {
            instance = new OpMessenger();
        }
        return instance;
    }

    public void messageOPs(String message) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.isOp()) {
                player.sendMessage(message);
            }
        }
    }
}
