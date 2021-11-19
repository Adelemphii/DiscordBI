package me.adelemphii.discordbirework.discord.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class VouchListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.isFromType(ChannelType.PRIVATE)) return;
        if(event.getChannel().getIdLong() != 910720380270948392L) return;

        if(event.getUser() == null) return;
        if(event.getUser().isBot()) return;

        Message message = event.retrieveMessage().complete();

        if(message.getAuthor().isBot()) return;

        if(event.getReactionEmote().getName().equals("\uD83D\uDE03")) {
            event.getChannel().sendMessage("<@" + message.getAuthor().getId() + "> has been vouched for by <@" +
                    event.getUser().getId() + ">!").queue();

            event.getGuild().addRoleToMember(message.getAuthor().getId(), Objects.requireNonNull(event.getGuild().getRoleById(910720119339102289L))).queue();
            event.getGuild().removeRoleFromMember(message.getAuthor().getId(), Objects.requireNonNull(event.getGuild().getRoleById(910720815539044402L))).queue();
            message.delete().queue();
        }
    }
}
