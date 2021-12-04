package me.adelemphii.discordbirework.discord.commands;

import me.adelemphii.discordbirework.DiscordBIRework;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
        if(event.getAuthor().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getMessage().getContentRaw().startsWith("p-stats")) {

                String[] strings = event.getMessage().getContentRaw().split(" ");

                ArrayList<String> args = new ArrayList<>(Arrays.asList(strings));

                if (args.size() < 2) {
                    event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                    return;
                }

                if (args.size() == 2) {
                    MessageEmbed generalStats = fetchGeneralStats(args.get(1));

                    if(generalStats == null) {
                        event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(generalStats).queue();
                    return;
                }

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    MessageEmbed specificStat = fetchSpecificStat(args.get(1), args.get(2).toUpperCase());

                    if(specificStat == null) {
                        event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                        return;
                    }
                    event.getChannel().sendMessageEmbeds(specificStat).queue();
                });
            }
            return;
        }
        if(event.getMessage().getContentRaw().startsWith("p-stats")) {

            String[] strings = event.getMessage().getContentRaw().split(" ");

            ArrayList<String> args = new ArrayList<>(Arrays.asList(strings));

            if (args.size() < 2) {
                event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                return;
            }

            if (args.size() == 2) {
                MessageEmbed generalStats = fetchGeneralStats(args.get(1));

                if(generalStats == null) {
                    event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(generalStats).queue();
                return;
            }

            plugin.getServer().getScheduler().runTask(plugin, () -> {
                MessageEmbed specificStat = fetchSpecificStat(args.get(1), args.get(2).toUpperCase());

                if(specificStat == null) {
                    event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                    return;
                }
                event.getChannel().sendMessageEmbeds(specificStat).queue();
            });
        }
    }

    /*
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if(event.getUser().isBot()) return;
        if(event.getChannelType() != ChannelType.PRIVATE) {

            if (event.getGuild() == null) return;
            if (event.getGuild().getIdLong() != plugin.getConfig().getLong("discord-server")) return;

            List<Long> discordBotChannels = plugin.getConfig().getLongList("bot-channels");
            if (!discordBotChannels.contains(event.getChannel().getIdLong())) return;

            if(event.getName().equalsIgnoreCase("stats")) {
                String player = event.getOption("player").getAsString();
                String statistic = event.getOption("statistic").getAsString();

                event.deferReply().queue();

                if(player.isEmpty()) {
                    event.replyEmbeds(incorrectUsage()).queue();
                    return;
                }

                if(statistic.isEmpty()) {
                    MessageEmbed messageEmbed = fetchGeneralStats(player);

                    if(messageEmbed == null) {
                        event.replyEmbeds(incorrectUsage()).queue();
                    } else {
                        event.replyEmbeds(messageEmbed).queue();
                    }
                    return;
                }

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    MessageEmbed specificStat = fetchSpecificStat(player, statistic);

                    if(specificStat == null) {
                        event.getChannel().sendMessage("You either input an incorrect user, or you input an incorrect statistic!").queue();
                        event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                    }
                    assert specificStat != null;
                    event.replyEmbeds(specificStat).queue();
                });
            }
        } else {
            if (event.getName().equalsIgnoreCase("stats")) {
                String player = event.getOption("player").getAsString();
                String statistic = event.getOption("statistic").getAsString();

                event.deferReply().queue();

                if (player.isEmpty()) {
                    event.replyEmbeds(incorrectUsage()).queue();
                    return;
                }

                if (statistic.isEmpty()) {
                    MessageEmbed messageEmbed = fetchGeneralStats(player);

                    if (messageEmbed == null) {
                        event.replyEmbeds(incorrectUsage()).queue();
                    } else {
                        event.replyEmbeds(messageEmbed).queue();
                    }
                    return;
                }

                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    MessageEmbed specificStat = fetchSpecificStat(player, statistic);

                    if (specificStat == null) {
                        event.getChannel().sendMessage("You either input an incorrect user, or you input an incorrect statistic!").queue();
                        event.getChannel().sendMessageEmbeds(incorrectUsage()).queue();
                    }
                    assert specificStat != null;
                    event.replyEmbeds(specificStat).queue();
                });
            }
        }
    }
     */
    private UUID fetchUUID(String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        String uuid = (String)((JSONObject)new JSONParser().parse(new InputStreamReader(url.openStream()))).get("id");
        String realUUID = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
        return UUID.fromString(realUUID);
    }

    private MessageEmbed incorrectUsage() {
        EmbedBuilder builder = new EmbedBuilder()
                .setDescription("Incorrect usage of the command! Either the Statistic is wrong, or the user is wrong!")

                .setTitle("Stats Command Args", "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Statistic.html")
                .addField("Argument 1", "<username>", false)
                .addField("Argument 2 - CASE SENSITIVE", "<statistic to check>", false)
                .addField("Javadocs", "Check the linked url on statistics you can check!" +
                        " Make sure to copy it entirely!", false)
                .addField("Example", "p-stats Adelemphii MOB_KILLS", false)
                .setColor(Color.PINK);

        return builder.build();
    }

    private MessageEmbed fetchSpecificStat(String name, String stat) {
        try {
            uuid = fetchUUID(name);

            if (Bukkit.getPlayer(uuid) != null) {
                Player player = Bukkit.getPlayer(uuid);

                assert player != null;
                EmbedBuilder stats = new EmbedBuilder()
                        .setTitle(player.getDisplayName() + "'s " + stat + " stat")
                        .addField(stat, String.valueOf(player.getStatistic(Statistic.valueOf(stat))), false)
                        .setColor(Color.PINK);
                return stats.build();
            } else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

                if(player.getName() == null) {
                    return null;
                }

                EmbedBuilder stats = new EmbedBuilder()
                        .setTitle(player.getName() + "'s " + stat + " stat")
                        .addField(stat, String.valueOf(player.getStatistic(Statistic.valueOf(stat))), false)
                        .setColor(Color.PINK);
                return stats.build();
            }
        } catch (IOException | ParseException | IllegalArgumentException e) {
            // if it is null, they inputted the wrong name/uuid or the wrong statistic name
            e.printStackTrace();
            return null;
        }
    }

    private MessageEmbed fetchGeneralStats(String name) {
        try {

            this.uuid = fetchUUID(name);

            if (Bukkit.getPlayer(this.uuid) != null) {
                Player player = Bukkit.getPlayer(this.uuid);

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
                return generalStats.build();
            } else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(this.uuid);

                if(player.getName() == null) {
                    return null;
                }

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
                return generalStats.build();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
