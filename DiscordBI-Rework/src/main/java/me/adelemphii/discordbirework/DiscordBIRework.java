package me.adelemphii.discordbirework;

import lombok.Getter;
import me.adelemphii.discordbirework.discord.DiscordBot;
import me.adelemphii.discordbirework.server.events.PlayerJoinEvent;
import me.adelemphii.discordbirework.server.events.ServerListChangeEvent;
import net.dv8tion.jda.api.JDA;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordBIRework extends JavaPlugin {

    private @Getter DiscordBot discordBot;
    private @Getter JDA api;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        discordBot = new DiscordBot(this);
        api = discordBot.getJDA();

        registerCE();
    }

    @Override
    public void onDisable() {
        api.shutdownNow();
    }

    private void registerCE() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoinEvent(this), this);
        pm.registerEvents(new ServerListChangeEvent(this), this);
    }

    public String getToken() {
        if(getConfig().getString("token") != null) {
            return getConfig().getString("token");
        } else {
            return null;
        }
    }
}
