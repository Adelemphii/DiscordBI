package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StatsCommand extends ListenerAdapter {

    private final DiscordBIRework plugin;
    public StatsCommand(DiscordBIRework plugin) {
        this.plugin = plugin;
    }

    private UUID uuid;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getChannelType().isGuild()) return;

        if(event.getAuthor().isBot()) return;
        if(event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

        List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
        if(!discordBotChannels.contains(event.getChannel().getIdLong())) return;

        if(event.getMessage().getContentRaw().equalsIgnoreCase("p-stats")) {
            String[] strings = event.getMessage().getContentRaw().split(" ");

            ArrayList<String> args = new ArrayList<>(Arrays.asList(strings));

            if (args.size() < 2) {
                event.getChannel().sendMessage("That is not a valid usage of the command!").queue();

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("Stats Command Args", "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Statistic.html")
                        .addField("Argument 1", "<username>", false)
                        .addField("Argument 2 - CASE SENSITIVE", "<statistic to check>", false)
                        .addField("Javadocs", "Check the linked url on statistics you can check!" +
                                " Make sure to copy it entirely!", false)
                        .addField("Example", "p-stats Adelemphii MOB_KILLS", false)
                        .setColor(Color.PINK);
                event.getChannel().sendMessageEmbeds(builder.build()).queue();
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

                                .addField("Playtime in Hours", String.valueOf(playTimeInHours), false)
                                .addField("Deaths",
                                        String.valueOf(player.getStatistic(Statistic.DEATHS)), false)
                                .addField("Player Kills",
                                        String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)), false)
                                .addField("Distance Walked",
                                        (player.getStatistic(Statistic.WALK_ONE_CM) / 100) + " block(s) walked", false)
                                .addField("Distance Flown",
                                        (player.getStatistic(Statistic.AVIATE_ONE_CM) / 100) + " block(s) flown", false)
                                .addField("Distance Swam",
                                        (player.getStatistic(Statistic.SWIM_ONE_CM) / 100) + " block(s) swam", false)

                                .setColor(Color.PINK);
                        event.getChannel().sendMessageEmbeds(generalStats.build()).queue();
                    } else {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                        long playTimeInHours = (((player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60) / 60);

                        EmbedBuilder generalStats = new EmbedBuilder()
                                .setTitle(player.getName() + "'s stats")

                                .addField("Playtime in Hours", String.valueOf(playTimeInHours), false)
                                .addField("Deaths", String.valueOf(player.getStatistic(Statistic.DEATHS)), false)
                                .addField("Player Kills", String.valueOf(player.getStatistic(Statistic.PLAYER_KILLS)), false)
                                .addField("Distance Walked",
                                        (player.getStatistic(Statistic.WALK_ONE_CM) / 100) + " block(s) walked", false)
                                .addField("Distance Flown",
                                        (player.getStatistic(Statistic.AVIATE_ONE_CM) / 100) + " block(s) flown", false)
                                .addField("Distance Swam",
                                        (player.getStatistic(Statistic.SWIM_ONE_CM) / 100) + " block(s) swam", false)

                                .setColor(Color.PINK);
                        event.getChannel().sendMessageEmbeds(generalStats.build()).queue();
                    }
                    return;

                } catch (IOException | ParseException e) {
                    event.getChannel().sendMessage("Invalid user! (Check your spelling!)").queue();
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
                                .addField(args.get(1), String.valueOf(player.getStatistic(Statistic.valueOf(args.get(1)))), false)
                                .setColor(Color.PINK);
                        event.getChannel().sendMessageEmbeds(stats.build()).queue();
                    } else {
                        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                        EmbedBuilder stats = new EmbedBuilder()
                                .setTitle(player.getName() + "'s " + args.get(1) + " stat")
                                .addField(args.get(1), String.valueOf(player.getStatistic(Statistic.valueOf(args.get(1)))), false)
                                .setColor(Color.PINK);
                        event.getChannel().sendMessageEmbeds(stats.build()).queue();
                    }

                } catch (IOException | ParseException | IllegalArgumentException e) {
                    event.getChannel().sendMessage("Something went wrong with your input, either the name was incorrect (check for username changes)" +
                            ", you input an incorrect statistic name, or something is wrong with the plugin (Report to Adelemphii#6213 in this case)").queue();
                    e.printStackTrace();
                }
            });

        }
    }

    private UUID fetchUUID(String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        String uuid = (String)((JSONObject)new JSONParser().parse(new InputStreamReader(url.openStream()))).get("id");
        String realUUID = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
        return UUID.fromString(realUUID);
    }
}
