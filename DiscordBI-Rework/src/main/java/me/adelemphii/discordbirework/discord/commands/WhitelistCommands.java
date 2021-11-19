package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WhitelistCommands extends ListenerAdapter {

    public DiscordBIRework plugin;
    public WhitelistCommands(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getChannelType().isGuild()) return;

        if(event.getAuthor().isBot()) return;
        if(event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

        List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
        if(!discordBotChannels.contains(event.getChannel().getIdLong())) return;

        if(event.getMessage().getContentRaw().contains("p-whitelist")) {
            // Collect the names of all online players
            String whitelistedPlayers = Bukkit.getWhitelistedPlayers()
                    .stream()
                    .map(OfflinePlayer::getName)
                    .collect(Collectors.joining(", "));

            // Check if there are any online players
            if (whitelistedPlayers.isEmpty()) {
                EmbedBuilder online = new EmbedBuilder()
                        .addField("Whitelisted Players", "There are no players whitelisted", false)
                        .setColor(Color.PINK);
                event.getChannel().sendMessageEmbeds(online.build()).queue();
                return;
            }

            EmbedBuilder online = new EmbedBuilder()
                    .addField("Whitelisted Players - " + Bukkit.getWhitelistedPlayers().size(), whitelistedPlayers, false)
                    .setColor(Color.PINK);
            event.getChannel().sendMessageEmbeds(online.build()).queue();

        }

    }

    public UUID fetchUUID(String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        String uuid = (String)((JSONObject)new JSONParser().parse(new InputStreamReader(url.openStream()))).get("id");
        String realUUID = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
        return UUID.fromString(realUUID);
    }
}
