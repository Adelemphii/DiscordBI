package me.adelemphii.discordbirework.server.events;

import me.adelemphii.discordbirework.DiscordBIRework;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

public class ServerListChangeEvent implements Listener {

    public DiscordBIRework plugin;
    public ServerListChangeEvent(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void serverListEvent(ServerListPingEvent event) {
        if(plugin.getConfig().getString("motd") != null) {
            event.setMotd(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(plugin.getConfig().getString("motd"))));
        }
    }
}
