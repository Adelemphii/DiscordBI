package me.adelemphii.discordbi.events.plugin;

import me.adelemphii.discordbi.DiscordBI;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoinEvent implements Listener {

    public DiscordBI plugin;
    public PlayerJoinEvent(DiscordBI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerPreJoin(AsyncPlayerPreLoginEvent event) {
        if(plugin.getConfig().getBoolean("playtime")) {
            return;
        }

        if(!plugin.getConfig().getList("player-bypass").contains(event.getUniqueId().toString())) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "Playtime is not enabled! The playable times are: \n" +
                    "EST: 2pm - 6am | BST: 7pm - 11am");
        }
    }

}
