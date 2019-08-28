package bent_bot.commands;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.util.Properties;

public class NotifyListener extends ListenerAdapter
{
    private Properties config = new Properties();
    private String id;
    private String welcomeMessage;
    private String goodbyeMessage;

    public void onGuildJoin(GuildJoinEvent event)
    {
        try (InputStream in = new FileInputStream("config/notify.properties")) {
            //load the input stream into the config object
            config.load(in);

            //set default join messages
            config.setProperty(event.getGuild().toString() + ".WELCOME", "USER_NAME has joined the server.");
            config.setProperty(event.getGuild().toString() + ".GOODBYE", "USER_NAME has left the server.");

            config.store(new FileOutputStream("config/notify.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        try (FileInputStream in = new FileInputStream("config/notify.properties"))
        {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        id = config.getProperty(event.getGuild().toString());
        welcomeMessage = config.getProperty(event.getGuild().toString() + ".WELCOME");

        event.getGuild().getTextChannelById(id).sendMessage(welcomeMessage.replaceAll("USER_NAME", event.getMember().getAsMention())).queue();
    }

    public void onGuildMemberLeave(GuildMemberLeaveEvent event)
    {
        try (FileInputStream in = new FileInputStream("config/notify.properties"))
        {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        id = config.getProperty(event.getGuild().toString());
        goodbyeMessage = config.getProperty(event.getGuild().toString() + ".GOODBYE");

        event.getGuild().getTextChannelById(id).sendMessage(goodbyeMessage.replaceAll("USER_NAME", "<@"+event.getMember().getId()+">")).queue();
    }
}
