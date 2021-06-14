package me.adelemphii.discordbi.command.discord;

import me.adelemphii.discordbi.DiscordBI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class StatusCommands implements MessageCreateListener {

    UUID uuid;

    DiscordBI plugin;
    public StatusCommands(DiscordBI plugin) {
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

            if (event.getMessageAuthor().isBotUser()) {
                return;
            }

            Message msg = event.getMessage();

            plugin.getLogger().log(Level.INFO, "Message (" + event.getMessageAuthor() + ", content: " + msg.getContent() + ")");

            if (msg.getContent().equalsIgnoreCase("wifeyStatus")) {
                String onlinePlayers = Integer.toString(plugin.getServer().getOnlinePlayers().size());
                String maxPlayers = Integer.toString(plugin.getServer().getMaxPlayers());

                String whitelists = Integer.toString(plugin.getServer().getWhitelistedPlayers().size() - 2);
                String serverIP = null;

                if(plugin.getConfig().getString("server-domain") == null) {
                    serverIP = plugin.getServer().getIp() + plugin.getServer().getPort();
                } else {
                    serverIP = plugin.getConfig().getString("server-domain");
                }

                EmbedBuilder status = new EmbedBuilder()
                        .setTitle("Server Status")

                        .addField("MoTD", plugin.getServer().getMotd())
                        .addField("Server IP", serverIP)
                        .addField("Server Version", plugin.getServer().getVersion())
                        .addField("Capacity", onlinePlayers + "/" + maxPlayers)
                        .addField("Whitelist Status", whitelists + "/20 - FULL")

                        .addInlineField("Playable Time EST", "2pm EST - 6am EST")
                        .addInlineField("Playable Time BST", "7pm BST - 11am BST")

                        .setFooter("Try using 'wifeyRules' to access the rules page!")
                        .setColor(Color.GREEN);

                event.getChannel().sendMessage(status);
                return;
            }

            if (msg.getContent().equalsIgnoreCase("wifeyrules")) {

                EmbedBuilder ruleEmbed = new EmbedBuilder()
                        .setTitle("SMP Rules")
                        .addField("Rule 1", "No Griefing/Blowing up bases")
                        .addField("Rule 2", "No spawn camping")
                        .addField("Rule 3", "Spawn Island is a safe zone")

                        .addInlineField("Playable Time EST", "3pm EST - 7am EST")
                        .addInlineField("Playable Time BST", "8pm BST - 12pm BST")

                        .setFooter("Try using 'wifeyStatus' to check the status of the server!")
                        .setColor(Color.PINK);

                event.getChannel().sendMessage(ruleEmbed);
                return;
            }

            if (msg.getContent().equalsIgnoreCase("wifeyhelp")) {

                EmbedBuilder helpEmbed = new EmbedBuilder()
                        .setTitle("AdelemBot Command List")

                        .addField("wifeyStatus", "Displays the status of the SMP server!")
                        .addField("wifeyRules", "Displays the rules of the SMP server!")
                        .addField("wifeyOnline", "Displays the amount of players online!")
                        .addField("wifeyHelp", "Displays this information box!")
                        .addField("wifeyStats", "Displays the chosen statistic information!")
                        .addField("wifeyCredits", "Displays the credits for the bot!")

                        .setFooter("This bot was made by Adelemphii#6213")

                        .setColor(Color.PINK);

                event.getChannel().sendMessage(helpEmbed);
                return;
            }

            if (event.getMessageContent().equalsIgnoreCase("wifeyOnline")) {
                // Collect the names of all online players
                String onlinePlayers = Bukkit.getOnlinePlayers()
                        .stream()
                        .map(Player::getName)
                        .collect(Collectors.joining(", "));

                // Check if there are any online players
                if (onlinePlayers.isEmpty()) {
                    EmbedBuilder online = new EmbedBuilder()
                            .addField("Online Players", "There are no players online")
                            .setColor(Color.PINK);
                    event.getChannel().sendMessage(online);
                    return;
                }

                EmbedBuilder online = new EmbedBuilder()
                        .addField("Online Players - " + Bukkit.getOnlinePlayers().size(), onlinePlayers)
                        .setColor(Color.PINK);
                event.getChannel().sendMessage(online);

            }

            if (msg.getContent().toLowerCase().contains("wifeystats")) {

                String[] strings = msg.getContent().split(" ");

                ArrayList<String> args = new ArrayList<>(Arrays.asList(strings));

                if (args.size() < 2) {
                    msg.getChannel().sendMessage("That is not a valid usage of the command!");

                    EmbedBuilder statsHelp = new EmbedBuilder()
                            .setTitle("Stats Command Args")
                            .addField("Argument 1", "<username>")
                            .addField("Argument 2 - CASE SENSITIVE", "<statistic to check>")
                            .addField("Javadocs", "Check the linked url on statistics you can check!" +
                                    " Make sure to copy it entirely!")
                            .addField("Example", "wifeystats Adelemphii MOB_KILLS")
                            .setUrl("https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Statistic.html")
                            .setColor(Color.PINK);
                    msg.getChannel().sendMessage(statsHelp);
                    return;
                }

                if (args.size() == 2) {
                    try {

                        uuid = fetchUUID(args.get(1));

                        if (Bukkit.getPlayer(uuid) != null) {
                            Player player = Bukkit.getPlayer(uuid);

                            assert player != null;
                            long playTimeInHours = (((player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60) / 60);

                            EmbedBuilder generalStats = new EmbedBuilder()
                                    .setTitle(player.getDisplayName() + "'s stats")

                                    .addField("Playtime in Hours",
                                            String.valueOf(playTimeInHours))
                                    .addField("Deaths",
                                            String.valueOf(player.getStatistic(Statistic.DEATHS)))
                                    .addField("Player Kills",
                                            String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)))
                                    .addField("Distance Walked",
                                            (player.getStatistic(Statistic.WALK_ONE_CM) / 100) + " block(s) walked")
                                    .addField("Distance Flown",
                                            (player.getStatistic(Statistic.AVIATE_ONE_CM) / 100) + " block(s) flown")
                                    .addField("Distance Swam",
                                            (player.getStatistic(Statistic.SWIM_ONE_CM) / 100) + " block(s) swam")

                                    .setColor(Color.PINK);

                            msg.getChannel().sendMessage(generalStats);
                        } else {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                            long playTimeInHours = (((player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60) / 60);

                            EmbedBuilder generalStats = new EmbedBuilder()
                                    .setTitle(player.getName() + "'s stats")

                                    .addField("Playtime in Hours",
                                            String.valueOf(playTimeInHours))
                                    .addField("Deaths",
                                            String.valueOf(player.getStatistic(Statistic.DEATHS)))
                                    .addField("Player Kills",
                                            String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)))
                                    .addField("Distance Walked",
                                            (player.getStatistic(Statistic.WALK_ONE_CM) / 100) + " block(s) walked")
                                    .addField("Distance Flown",
                                            (player.getStatistic(Statistic.AVIATE_ONE_CM) / 100) + " block(s) flown")
                                    .addField("Distance Swam",
                                            (player.getStatistic(Statistic.SWIM_ONE_CM) / 100) + " block(s) swam")

                                    .setColor(Color.PINK);

                            msg.getChannel().sendMessage(generalStats);
                        }
                        return;

                    } catch (IOException | ParseException e) {
                        msg.getChannel().sendMessage("Invalid user! (Check your spelling!)");
                        e.printStackTrace();
                        return;
                    }
                }

                args.remove(0);

                args.set(1, args.get(1).toUpperCase());

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    try {
                        uuid = fetchUUID(args.get(0));

                        if (Bukkit.getPlayer(uuid) != null) {
                            Player player = Bukkit.getPlayer(uuid);

                            assert player != null;
                            EmbedBuilder stats = new EmbedBuilder()
                                    .setTitle(player.getDisplayName() + "'s " + args.get(1) + " stat")
                                    .addField(args.get(1), String.valueOf(player.getStatistic(Statistic.valueOf(args.get(1)))))
                                    .setColor(Color.PINK);
                            msg.getChannel().sendMessage(stats);
                        } else {
                            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                            EmbedBuilder stats = new EmbedBuilder()
                                    .setTitle(player.getName() + "'s " + args.get(1) + " stat")
                                    .addField(args.get(1), String.valueOf(player.getStatistic(Statistic.valueOf(args.get(1)))))
                                    .setColor(Color.PINK);
                            msg.getChannel().sendMessage(stats);
                        }

                    } catch (IOException | ParseException | IllegalArgumentException e) {
                        msg.getChannel().sendMessage("Something went wrong with your input, either the name was incorrect (check for username changes)" +
                                ", you input an incorrect statistic name, or something is wrong with the plugin (Report to Adelemphii#6213 in this case)");
                        e.printStackTrace();
                    }
                });

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
