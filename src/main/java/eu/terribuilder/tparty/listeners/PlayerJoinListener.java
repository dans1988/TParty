package eu.terribuilder.tparty.listeners;

import eu.terribuilder.tparty.party.PartySystem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        PartySystem partySystem = PartySystem.getInstance();
        if (partySystem.isRunning()) {
            partySystem.listInviters(event.getPlayer().getName());
        }
    }
}
