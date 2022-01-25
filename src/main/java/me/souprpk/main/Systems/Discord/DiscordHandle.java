package me.souprpk.main.Systems.Discord;

import me.souprpk.main.Banker;
import me.souprpk.main.Tools.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class DiscordHandle {

    public JDA jda;
    private Utilities utils;
    private TextChannel textChannel;

    public DiscordHandle() {
        utils = new Utilities(Banker.getMain());
        String botToken = utils.getConfigString("discord.bot-token");
        try{
            jda = JDABuilder.createDefault(botToken)
                    .build()
                    .awaitReady();
        }catch (InterruptedException |LoginException e){
            e.printStackTrace();
        }

        if(jda == null) return;

        String channelId = utils.getConfigString("discord.channel-id");
        textChannel = jda.getTextChannelById(channelId);

    }

    public void sendMessage(Player player, String content, boolean contentInAuthorLine, Color color){
        try{
            if(jda == null) return;
            if(textChannel == null) return;

            EmbedBuilder builder = new EmbedBuilder()
                    .setAuthor(
                            contentInAuthorLine ? content : player.getDisplayName(),
                            null,
                            "https://crafatar.com/avatars/" + player.getUniqueId().toString() + "?overlay=1"
                    );

            if(!contentInAuthorLine)
                builder.setDescription(content);
            textChannel.sendMessageEmbeds(builder.build()).queue();
        }catch(Exception ignored){

        }
    }

    public JDA getJda(){
        return  jda;
    }
}
