package me.adelemphii.discordbi.events.plugin;

import me.adelemphii.discordbi.DiscordBI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Objects;

public class ServerListChangeEvent implements Listener {

    public DiscordBI plugin;
    public ServerListChangeEvent(DiscordBI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void serverListEvent(ServerListPingEvent event) {
        if(plugin.getConfig().getBoolean("playtime")) {
            if(plugin.getConfig().getString("motd-enabled") != null) {
                event.setMotd(Objects.requireNonNull(plugin.getConfig().getString("motd-enabled"), "motd-enabled must not be null!"));
            } else {
                event.setMotd("WifeySMP - Playable");
            }
        } else {
            if(plugin.getConfig().getString("motd-disabled") != null) {
                event.setMotd(Objects.requireNonNull(plugin.getConfig().getString("motd-disabled"), "motd-enabled must not be null!"));
            } else {
                event.setMotd("WifeySMP - Offline");
            }
        }
    }
}
