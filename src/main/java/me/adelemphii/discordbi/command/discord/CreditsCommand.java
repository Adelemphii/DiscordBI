package me.adelemphii.discordbi.command.discord;

import me.adelemphii.discordbi.DiscordBI;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.util.ArrayList;

public class CreditsCommand implements MessageCreateListener {

    DiscordBI plugin;
    public CreditsCommand(DiscordBI plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        if (event.getServer().isPresent()) {
            Server server = event.getServer().get();
            Channel channel = event.getChannel();

            ArrayList<Long> servers = (ArrayList<Long>) plugin.getConfig().getLongList("discord-servers");
            ArrayList<Long> channels = (ArrayList<Long>) plugin.getConfig().getLongList("bot-channels");

            if (!servers.contains(server.getId())) {
                return;
            }
            if (!channels.contains(channel.getId())) {
                return;
            }

            Message msg = event.getMessage();

            if (msg.getContent().toLowerCase().contains("wifeycredit")) {
                EmbedBuilder creditsEmbed = new EmbedBuilder()
                        .setTitle("Bot Credits & Socials")

                        .addField("Created By", "The bot was created by Adelemphii")
                        .addField("Discord Tag", "Adelemphii#6213")
                        .addField("Portfolio & Commissions Info", "Find Adelemphii's Portfolio " +
                                "@ https://www.adelemphii.me")
                        .addField("Twitch", "Follow my twitch @ https://twitch.tv/Adelemphii")
                        .addField("Twitter", "Follow my Twitter @ https://twitter.com/Adelemphii")
                        .addField("Requests & Suggestions", "Send all requests and suggestions" +
                                " to Adelemphii#6213 on Discord!")

                        .setColor(Color.PINK);

                event.getChannel().sendMessage(creditsEmbed);
            }
        }
    }
}
