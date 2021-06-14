package me.adelemphii.discordbi.command.discord;

import me.adelemphii.discordbi.DiscordBI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class WhitelistCommands implements MessageCreateListener {

    public DiscordBI plugin;
    public WhitelistCommands(DiscordBI plugin) {
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

            if (event.getMessageContent().equalsIgnoreCase("wifeyWhitelist")) {
                // Collect the names of all online players
                String whitelistedPlayers = Bukkit.getWhitelistedPlayers()
                        .stream()
                        .map(OfflinePlayer::getName)
                        .collect(Collectors.joining(", "));

                // Check if there are any online players
                if (whitelistedPlayers.isEmpty()) {
                    EmbedBuilder online = new EmbedBuilder()
                            .addField("Whitelisted Players", "There are no players whitelisted")
                            .setColor(Color.PINK);
                    event.getChannel().sendMessage(online);
                    return;
                }

                EmbedBuilder online = new EmbedBuilder()
                        .addField("Whitelisted Players - " + Bukkit.getWhitelistedPlayers().size(), whitelistedPlayers)
                        .setColor(Color.PINK);
                event.getChannel().sendMessage(online);

            }
        }
    }

    public UUID fetchUUID(String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        String uuid = (String)((JSONObject)new JSONParser().parse(new InputStreamReader(url.openStream()))).get("id");
        String realUUID = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
        return UUID.fromString(realUUID);
    }
}
