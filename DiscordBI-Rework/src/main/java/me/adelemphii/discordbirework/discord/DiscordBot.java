package me.adelemphii.discordbirework.discord;

import me.adelemphii.discordbirework.DiscordBIRework;
import me.adelemphii.discordbirework.discord.commands.*;
import me.adelemphii.discordbirework.discord.listeners.VouchListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    private DiscordBot instance;
    private JDA jda;
    private final DiscordBIRework plugin;

    public DiscordBot(DiscordBIRework plugin) {
        this.plugin = plugin;

        try {
            this.jda = buildJDA();
            jda.awaitReady();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }

        plugin.getLogger().info("Registering Listeners...");
        registerListeners(jda);
        registerSlashCommands(jda);
        jda.updateCommands().queue();

        plugin.getLogger().info("You can invite the bot using: "
                + jda.getInviteUrl(Permission.MESSAGE_MANAGE, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
                Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ADD_REACTION, Permission.MANAGE_ROLES,
                Permission.MESSAGE_HISTORY, Permission.USE_SLASH_COMMANDS, Permission.MANAGE_WEBHOOKS));
    }

    public JDA getJDA() {
        if(jda == null) {
            try {
                jda = buildJDA();
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }
        return jda;
    }

    public DiscordBot getInstance() {
        if(instance == null) {
            this.instance = this;
            return instance;
        }

        return instance;
    }

    private JDA buildJDA() throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(plugin.getToken());

        builder.setActivity(Activity.watching("A Pioneer's Playground!"));
        builder.setStatus(OnlineStatus.ONLINE);

        return builder.build();
    }

    private void registerListeners(JDA jda) {
        jda.addEventListener(new CreditsCommand(plugin));
        jda.addEventListener(new HelpCommand(plugin));
        jda.addEventListener(new OnlineCommand(plugin));
        jda.addEventListener(new StatsCommand(plugin));
        jda.addEventListener(new StatusCommand(plugin));

        jda.addEventListener(new VouchListener());
    }

    private void registerSlashCommands(JDA jda) {
        jda.upsertCommand("help", "Displays the help menu!").queue();
        jda.upsertCommand("online", "Displays what players logged in to the server!").queue();
        /*jda.upsertCommand("stats", "Displays the stats of a player who plays on the server!")
                .addOption(OptionType.STRING, "player", "The player to get the stats of!", true)
                .addOption(OptionType.STRING, "statistic", "The statistic to get!", true)
                .queue();*/
        jda.upsertCommand("status", "Displays the status of the server!").queue();
        jda.upsertCommand("credits", "Displays the credits for the bot!").queue();
    }

}
