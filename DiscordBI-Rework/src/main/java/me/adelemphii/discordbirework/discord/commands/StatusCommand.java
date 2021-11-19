package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.List;

public class StatusCommand extends ListenerAdapter {

    private final DiscordBIRework plugin;
    public StatusCommand(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getChannelType().isGuild()) return;

        if(event.getAuthor().isBot()) return;
        if(event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

        List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
        if(!discordBotChannels.contains(event.getChannel().getIdLong())) return;

        if(event.getMessage().getContentRaw().contains("p-status")) {
            String onlinePlayers = Integer.toString(plugin.getServer().getOnlinePlayers().size());
            String maxPlayers = Integer.toString(plugin.getServer().getMaxPlayers());

            String serverIP;

            if(plugin.getConfig().getString("server-domain") == null) {
                serverIP = plugin.getServer().getIp() + plugin.getServer().getPort();
            } else {
                serverIP = plugin.getConfig().getString("server-domain");
            }

            EmbedBuilder status = new EmbedBuilder()
                    .setTitle("Server Status")

                    .addField("MoTD", plugin.getServer().getMotd(), false)
                    .addField("Server IP", serverIP, false)
                    .addField("Server Version", plugin.getServer().getVersion(), false)
                    .addField("Capacity", onlinePlayers + "/" + maxPlayers, false)

                    .setFooter("Try using 'p-help' to access the help page!")
                    .setColor(Color.GREEN);

            event.getChannel().sendMessageEmbeds(status.build()).queue();
        }
    }
}
