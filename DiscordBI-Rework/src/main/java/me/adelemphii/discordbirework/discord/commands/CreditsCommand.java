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

public class CreditsCommand extends ListenerAdapter {

    DiscordBIRework plugin;
    public CreditsCommand(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getMessage().getContentRaw().equalsIgnoreCase("p-credits")) {
                event.getChannel().sendMessageEmbeds(makeCreditsEmbed()).queue();
            }
            return;
        }
        if(event.getMessage().getContentRaw().equalsIgnoreCase("p-credits")) {
            event.getChannel().sendMessageEmbeds(makeCreditsEmbed()).queue();
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

            if(event.getName().equalsIgnoreCase("credits")) {
                event.replyEmbeds(makeCreditsEmbed()).queue();
            }
            return;
        }
        if(event.getName().equalsIgnoreCase("credits")) {
            event.replyEmbeds(makeCreditsEmbed()).queue();
        }
    }

    private MessageEmbed makeCreditsEmbed() {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setTitle("__**Bot Credits & Socials**__");

        builder.addField("*Created By*", "The bot was created by <@113826553246253061>", true);
        builder.addField("*Discord*", "[The Birb Cage](https://discord.gg/sX6FUau)", true);

        builder.addField("*Portfolio & Comissions Info*", "Find Adelemphii's Portfolio @ https://adelemphii.me",
                false);
        builder.addField("*Twitch*", "Follow my Twitch @ https://twitch.tv/Adelemphii", false);
        builder.addField("*Twitter*", "Follow my Twitter @ https://twitter.com/Adelemphii", false);
        builder.addField("*Requests & Suggetions*", "Please send all requests & suggestions to " +
                "<@113826553246253061> on Discord!", false);
        builder.setColor(Color.PINK);

        return builder.build();
    }
}
