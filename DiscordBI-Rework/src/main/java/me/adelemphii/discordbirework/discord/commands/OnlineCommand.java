package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineCommand extends ListenerAdapter {

    private final DiscordBIRework plugin;
    public OnlineCommand(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getMessage().getContentRaw().equalsIgnoreCase("p-online")) {
                event.getChannel().sendMessageEmbeds(makeOnlineEmbed()).queue();
            }
            return;
        }
        if(event.getMessage().getContentRaw().equalsIgnoreCase("p-online")) {
            event.getChannel().sendMessageEmbeds(makeOnlineEmbed()).queue();
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

            if(event.getName().equalsIgnoreCase("online")) {
                event.replyEmbeds(makeOnlineEmbed()).queue();
            }
        }
        if(event.getName().equalsIgnoreCase("online")) {
            event.replyEmbeds(makeOnlineEmbed()).queue();
        }
    }

    private MessageEmbed makeOnlineEmbed() {
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
            return builder.build();
        }

        EmbedBuilder builder = new EmbedBuilder()
                .addField("Online Players - " + Bukkit.getOnlinePlayers().size(), onlinePlayers, false)
                .setColor(Color.PINK);
        return builder.build();
    }
}
