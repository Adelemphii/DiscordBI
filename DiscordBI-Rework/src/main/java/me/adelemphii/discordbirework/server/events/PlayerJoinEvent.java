package me.adelemphii.discordbirework.server.events;

import me.adelemphii.discordbirework.DiscordBIRework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoinEvent implements Listener {

    public DiscordBIRework plugin;
    public PlayerJoinEvent(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerPreJoin(AsyncPlayerPreLoginEvent event) {

    }

}
