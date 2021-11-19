package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineCommand extends ListenerAdapter {

    private DiscordBIRework plugin;
    public OnlineCommand(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getChannelType().isGuild()) return;

        if(event.getAuthor().isBot()) return;
        if(event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

        List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
        if(!discordBotChannels.contains(event.getChannel().getIdLong())) return;

        if(event.getMessage().getContentRaw().equalsIgnoreCase("p-online")) {
            // Collect the names of all online players
            String onlinePlayers = Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.joining(", "));

            // Check if there are any online players
            if (onlinePlayers.isEmpty()) {
                EmbedBuilder builder = new EmbedBuilder()
                        .addField("Online Players", "There are no players online", false)
                        .setColor(Color.PINK);
                event.getChannel().sendMessageEmbeds(builder.build()).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder()
                    .addField("Online Players - " + Bukkit.getOnlinePlayers().size(), onlinePlayers, false)
                    .setColor(Color.PINK);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }

    }
}
