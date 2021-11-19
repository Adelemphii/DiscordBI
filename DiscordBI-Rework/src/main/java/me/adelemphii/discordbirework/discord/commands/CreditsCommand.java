package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
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
        if(!event.getChannelType().isGuild()) return;

        if(event.getAuthor().isBot()) return;
        if(event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

        List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
        if(!discordBotChannels.contains(event.getChannel().getIdLong())) return;

        if(event.getMessage().getContentRaw().contains("p-credits")) {
            EmbedBuilder builder = new EmbedBuilder();

            builder.setTitle("__**Bot Credits & Socials**__");

            builder.addField("*Created By*", "The bot was created by <@113826553246253061>", true);
            builder.addField("*Discord*", "[The Birb Cage](https://discord.gg/sX6FUau)", true);

            builder.addField("*Portfolio & Comissions Info*", "Find Adelemphii's Portfolio @ https://adelemphii.me",
                    false);
            builder.addField("*Twitch*", "Follow my Twitch @ https://twitch.tv/Adelemphii", false);
            builder.addField("*Twitter*", "Follow my Twitter @ https://twitter.com/Adelemphii", false);
            builder.addField("*Requests & Suggetions*", "Please send all requests & suggetions to " +
                            "<@113826553246253061> on Discord!", false);
            builder.setColor(Color.PINK);

            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }
}
