package me.souprpk.main.Systems.Discord;

import me.souprpk.main.Banker;
import me.souprpk.main.Tools.Utilities;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class DiscordHandle {

    public JDA jda;
    private Utilities utils;

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

    }

    public JDA getJda(){
        return  jda;
    }
}
