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

public class HelpCommand extends ListenerAdapter {

    private final DiscordBIRework plugin;
    public HelpCommand(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getMessage().getContentRaw().equalsIgnoreCase("p-help")) {
                event.getChannel().sendMessageEmbeds(makeHelpEmbed()).queue();
            }
            return;
        }
        if(event.getMessage().getContentRaw().equalsIgnoreCase("p-help")) {
            event.getChannel().sendMessageEmbeds(makeHelpEmbed()).queue();
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

            if(event.getName().equalsIgnoreCase("help")) {
                event.replyEmbeds(makeHelpEmbed()).queue();
            }
        }
        if(event.getName().equalsIgnoreCase("help")) {
            event.replyEmbeds(makeHelpEmbed()).queue();
        }
    }

    private MessageEmbed makeHelpEmbed() {
        EmbedBuilder helpEmbed = new EmbedBuilder()
                .setTitle("AdelemBot Command List")

                .addField("p-status", "Displays the status of the SMP server!", false)
                .addField("p-online", "Displays the amount of players online!", false)
                .addField("p-help", "Displays this information box!", false)
                .addField("p-stats", "Displays the chosen statistic information!", false)
                .addField("p-credits", "Displays the credits for the bot!", false)

                .setFooter("This bot was made by Adelemphii#6213")

                .setColor(Color.PINK);

        return helpEmbed.build();
    }
}
