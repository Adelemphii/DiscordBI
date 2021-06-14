package me.adelemphii.discordbi;

import me.adelemphii.discordbi.command.discord.CreditsCommand;
import me.adelemphii.discordbi.command.discord.PlaytimeCommand;
import me.adelemphii.discordbi.command.discord.StatusCommands;
import me.adelemphii.discordbi.command.discord.WhitelistCommands;
import me.adelemphii.discordbi.events.plugin.PlayerJoinEvent;
import me.adelemphii.discordbi.events.plugin.ServerListChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DiscordBI extends JavaPlugin {

    private DiscordApi api;

    public boolean alreadyEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if(getConfig().getBoolean("playtime")) {
            alreadyEnabled = true;
        } else {
            alreadyEnabled = false;
        }

        registerCE();
        timeRunnable();

        // Connect to Discord
        new DiscordApiBuilder()
                .setToken(getToken()) // Set the token of the bot here
                .setWaitForServersOnStartup(false)
                .login() // Log the bot in
                .thenAccept(this::onConnectToDiscord) // Call #onConnectToDiscord(...) after a successful login
                .exceptionally(error -> {
                    // Log a warning when the login to Discord failed (wrong token?)
                    getLogger().warning("Failed to connect to Discord! Disabling plugin! (Make sure to check your token)");
                    getPluginLoader().disablePlugin(this);
                    return null;
                });
    }

    @Override
    public void onDisable() {
        if (api != null) {
            // Make sure to disconnect the bot when the plugin gets disabled
            api.disconnect();
            api = null;
        }

        getServer().getScheduler().cancelTasks(this);
    }

    private void registerCE() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoinEvent(this), this);
        pm.registerEvents(new ServerListChangeEvent(this), this);
    }

    private void onConnectToDiscord(DiscordApi api) {
        this.api = api;
        api.addListener(new StatusCommands(this));
        api.addListener(new PlaytimeCommand(this));
        api.addListener(new WhitelistCommands(this));
        api.addListener(new CreditsCommand(this));

        // Log a message that the connection was successful and log the url that is needed to invite the bot
        getLogger().info("Connected to Discord as " + api.getYourself().getDiscriminatedName());
        getLogger().info("Open the following url to invite the bot: " + api.createBotInvite());
        Bukkit.broadcastMessage(ChatColor.GREEN + api.getYourself().getDiscriminatedName() + " has connected to Discord!");

    }

    public String getToken() {
        if(getConfig().getString("token") != null) {
            return getConfig().getString("token");
        } else {
            return null;
        }
    }

    public void timeRunnable() {

        PlaytimeCommand pt = new PlaytimeCommand(this);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {

            LocalTime localTime = LocalTime.now();

            if(isBetween(localTime, LocalTime.of(9, 59), LocalTime.of(18, 0))) {
                if(alreadyEnabled) {
                    return;
                }
                pt.swapPlaytime();
                alreadyEnabled = true;
            } else {
                if(!alreadyEnabled) {
                    return;
                }
                pt.swapPlaytime();
                alreadyEnabled = false;
            }

        }, 0L, 200L);
    }

    public boolean isBetween(LocalTime time, LocalTime end, LocalTime start) {

        return time.isBefore(end) || time.isAfter(start);

    }
}
