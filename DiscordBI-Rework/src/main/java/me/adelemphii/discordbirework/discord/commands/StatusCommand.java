package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
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
        if(event.getAuthor().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getMessage().getContentRaw().equalsIgnoreCase("p-status")) {
                event.getChannel().sendMessageEmbeds(fetchStatusEmbed()).queue();
            }
            return;
        }
        if(event.getMessage().getContentRaw().equalsIgnoreCase("p-status")) {
            event.getChannel().sendMessageEmbeds(fetchStatusEmbed()).queue();
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getUser().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild() == null) return;
            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getName().equalsIgnoreCase("status")) {
                event.replyEmbeds(fetchStatusEmbed()).queue();
            }
        }
        if(event.getName().equalsIgnoreCase("status")) {
            event.replyEmbeds(fetchStatusEmbed()).queue();
        }
    }

    private MessageEmbed fetchStatusEmbed() {
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

        return status.build();
    }
}
