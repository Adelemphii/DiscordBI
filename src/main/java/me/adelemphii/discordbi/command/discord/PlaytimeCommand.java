package me.adelemphii.discordbi.command.discord;

import me.adelemphii.discordbi.DiscordBI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.logging.Level;

public class PlaytimeCommand implements MessageCreateListener {

    public DiscordBI plugin;
    public PlaytimeCommand(DiscordBI plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if(!event.getMessageAuthor().isBotOwner()) return;

        Message msg = event.getMessage();

        if(msg.getContent().equalsIgnoreCase("wifeyReload")) {
            plugin.reloadConfig();
            msg.getChannel().sendMessage("Config Reloaded!");
        }

        if(msg.getContent().equalsIgnoreCase("wifeyStart")) {
            if(plugin.getConfig().getBoolean("playtime")) {
                msg.getChannel().sendMessage("Playtime is already enabled! Run wifeyStop to stop playtime!");
            } else {
                plugin.getConfig().set("playtime", true);
                plugin.saveConfig();
                plugin.reloadConfig();

                Bukkit.broadcastMessage(ChatColor.GREEN + "Playtime has been enabled!");
                msg.getChannel().sendMessage("Playtime set to *true*");
            }
        }

        if(msg.getContent().equalsIgnoreCase("wifeyStop")) {
            if(!plugin.getConfig().getBoolean("playtime")) {
                msg.getChannel().sendMessage("Playtime is already disabled! Run wifeyStart to start playtime!");
            } else {
                plugin.getConfig().set("playtime", false);
                plugin.saveConfig();
                plugin.reloadConfig();

                Bukkit.broadcastMessage(ChatColor.RED + "Playtime has been disabled!");
                msg.getChannel().sendMessage("Playtime set to *false*");

                if(!plugin.getServer().getOnlinePlayers().isEmpty()) {
                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                        if(!plugin.getConfig().getList("player-bypass").contains(player.getUniqueId().toString())) {
                            plugin.getServer().getScheduler().runTask(plugin, () -> {
                                player.kickPlayer("Playtime has been disabled!");
                            });
                        }
                    }
                }
            }
        }
    }

    public void swapPlaytime() {
        if(!plugin.getConfig().getBoolean("playtime")) {

            plugin.getConfig().set("playtime", true);
            plugin.saveConfig();
            plugin.reloadConfig();

            Bukkit.broadcastMessage(ChatColor.GREEN + "Playtime has been enabled!");
            plugin.getLogger().log(Level.INFO, "Playtime set to TRUE");
        } else {
            plugin.getConfig().set("playtime", false);
            plugin.saveConfig();
            plugin.reloadConfig();

            Bukkit.broadcastMessage(ChatColor.RED + "Playtime has been disabled!");
            plugin.getLogger().log(Level.INFO, "Playtime set to FALSE");

            if(!plugin.getServer().getOnlinePlayers().isEmpty()) {
                for(Player player : plugin.getServer().getOnlinePlayers()) {
                    if(!plugin.getConfig().getList("player-bypass").contains(player.getUniqueId().toString())) {
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            player.kickPlayer("Playtime has been disabled!");
                        });
                    }
                }
            }
        }
    }
}
