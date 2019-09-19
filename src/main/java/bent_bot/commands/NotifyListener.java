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

    /**
     * Everytime the bot joins a new guild, set all the default variables for that guild
     *
     */
    public void onGuildJoin(GuildJoinEvent event)
    {
        try (InputStream in = new FileInputStream("config/notify.properties")) {
            //load the input stream into the config object
            config.load(in);

            //set defaults
            config.setProperty(event.getGuild().toString(), event.getGuild().getDefaultChannel().getId());
            config.setProperty(event.getGuild().toString() + ".WELCOME", "USER_NAME has joined the server.");
            config.setProperty(event.getGuild().toString() + ".GOODBYE", "USER_NAME has left the server.");

            config.store(new FileOutputStream("config/notify.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Everytime a new member joins a guild, send the notification
     *
     */
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        try (FileInputStream in = new FileInputStream("config/notify.properties"))
        {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        id = config.getProperty(event.getGuild().toString());
        String welcomeMessage = config.getProperty(event.getGuild().toString() + ".WELCOME");
        if (welcomeMessage == null)
                welcomeMessage = "USER_NAME joined the server.";

        if (id != null)
            event.getGuild().getTextChannelById(id).sendMessage(welcomeMessage.replaceAll("USER_NAME", event.getMember().getAsMention())).queue();
    }

    /**
     * Everytime a member leaves a guild, send the notification
     *
     */
    public void onGuildMemberLeave(GuildMemberLeaveEvent event)
    {
        try (FileInputStream in = new FileInputStream("config/notify.properties"))
        {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        id = config.getProperty(event.getGuild().toString());
        String goodbyeMessage = config.getProperty(event.getGuild().toString() + ".GOODBYE");
        if (goodbyeMessage == null)
            goodbyeMessage = "USER_NAME left the server.";

        if (id != null)
            event.getGuild().getTextChannelById(id).sendMessage(goodbyeMessage.replaceAll("USER_NAME", "<@"+event.getMember().getId()+">")).queue();
    }
}
