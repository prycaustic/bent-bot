package bent_bot;

import bent_bot.commands.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.ShutdownCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class bent_bot
{
    private static String token;
    private static String ownerID;
    private static String prefix;
    private static String activity;
    private static String activityMessage;

    public static void main(String[] args) throws Exception
    {
        try (InputStream in = new FileInputStream("config/config.properties"))
        {
            Properties config = new Properties();

            //load the properties file
            config.load(in);

            //get the config properties
            token = config.getProperty("token");
            ownerID = config.getProperty("ownerID");
            prefix = config.getProperty("prefix");
            activity = config.getProperty("activity");
            activityMessage = config.getProperty("activityMessage");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //define an event waiter
        EventWaiter waiter = new EventWaiter();

        //define a command client builder
        CommandClientBuilder client = new CommandClientBuilder();

        //set the bot's prefix
        client.setPrefix(prefix).setAlternativePrefix("");

        //set the bot's owner's ID
        client.setOwnerId(ownerID);

        // sets emojis used throughout the bot on successes, warnings, and failures
        client.setEmojis("✅", 	"⚠", 	"❌");

        //add commands
        client.addCommands(
                new PingCommand(),
                new InfoCommand(),
                new PfpCommand(),
                new ArchillectCommand(),
                new NotifyCommand(),
                new ShutdownCommand(),
                new GarfieldCommand()
        );

        //set the help consumer
        client.setHelpConsumer(getHelpConsumer(client));

        try
        {
            //builds the bot
            JDABuilder jda = new JDABuilder(token);

            //check for the activity and set accordingly
            if (activity.equalsIgnoreCase("playing"))
                client.setActivity(Activity.playing(activityMessage));
            else if (activity.equalsIgnoreCase("listening"))
                client.setActivity(Activity.listening(activityMessage));
            else if (activity.equalsIgnoreCase("watching"))
                client.setActivity(Activity.watching(activityMessage));
            else
                client.setActivity(Activity.playing("Please set a valid activity."));

            jda.addEventListeners(waiter, client.build(), new SpamListener(), new NotifyListener());
            jda.build();
        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
    }

    /**
     * Search for a guild member
     *
     * @param event     the CommandEvent in which the member is specified
     * @return          a Member object that matches the search, this can be null
     */
    private static Member searchGuildForMember(CommandEvent event)
    {
        //sort the list in alphabetical order
        List<Member> guildMembers = new ArrayList<>(event.getGuild().getMembers());

        guildMembers.sort((m1, m2) -> m1.getEffectiveName().compareToIgnoreCase(m2.getEffectiveName()));

        //trim the string down to just the first argument
        String firstArg = event.getArgs().substring(event.getArgs().indexOf(" ") + 1);

        //go through every member in the guild
        for (Member i : guildMembers)
        {
            //go through nicknames first
            if (i.getEffectiveName().startsWith(firstArg))
                return i;
            else if (i.getEffectiveName().toLowerCase().startsWith(firstArg.toLowerCase()))
                return i;
            else if (i.getEffectiveName().contains(firstArg))
                return i;
            else if (i.getEffectiveName().toLowerCase().contains(firstArg.toLowerCase()))
                return i;
            //then check everyone's username
            else if (i.getUser().getName().startsWith(firstArg))
                return i;
            else if (i.getUser().getName().toLowerCase().startsWith(firstArg.toLowerCase()))
                return i;
            else if (i.getUser().getName().contains(firstArg))
                return i;
            else if (i.getUser().getName().toLowerCase().contains(firstArg.toLowerCase()))
                return i;
        }

        //nobody was found
        return null;
    }

    /**
     * find the "targetMember" to use with a command that needs to find a specific member
     *
     * @param event     the CommandEvent that needs a targetMember
     * @return          a Member object to be used in the command
     */
    public static Member getTargetMember(CommandEvent event)
    {
        //if they mentioned somebody then use them
        if (!event.getMessage().getMentionedMembers().isEmpty())
        {
            return event.getEvent().getMessage().getMentionedMembers().get(0);
        }
        //if nobody is mentioned then see if they put in a search query
        else if (!event.getArgs().isEmpty())
        {
            if (bent_bot.searchGuildForMember(event) != null)
                return bent_bot.searchGuildForMember(event);
            else
                event.replyWarning("Nobody with that name could be found.");
        }
        //if there's no search just do the person who sent the commmand
        else
            return event.getMember();
        return null;
    }

    /**
     * this method is just for organization
     *
     * @param client        the CommandClientBuilder object which is used in initializing the bot
     * @return              a Consumer<CommandEvent> object to be used as the helpConsumer
     */
    private static Consumer<CommandEvent> getHelpConsumer(CommandClientBuilder client)
    {
        return (event) -> {
            EmbedBuilder helpEmbed = new EmbedBuilder();
            helpEmbed.setTitle("**Commands**");
            helpEmbed.setColor(event.isFromType(ChannelType.TEXT) ? event.getMember().getColor() : null);

            for (Command command : client.build().getCommands())
            {
                if (!command.isHidden() && (!command.isOwnerCommand() || event.isOwner()))
                {
                    helpEmbed.appendDescription("\n**"+client.build().getPrefix() + command.getName()+"**" + " — " + (command.getArguments()==null ? "" : "`"+ command.getArguments()+"` ") + command.getHelp());
                }
            }
            User owner = event.getJDA().getUserById(client.build().getOwnerId());
            if (owner != null)
            {
                if(client.build().getServerInvite()==null)
                    helpEmbed.setFooter("For additional help, contact " + owner.getName() + "#" + owner.getDiscriminator(), owner.getAvatarUrl());
                else
                    helpEmbed.setFooter("For additional help, contact " + owner.getName() + owner.getDiscriminator() + " or join " + client.build().getServerInvite(), owner.getAvatarUrl());
            }
            event.replyInDm(helpEmbed.build(), unused ->
            {
                if(event.isFromType(ChannelType.TEXT))
                    event.reactSuccess();
            }, t -> event.replyWarning("Help cannot be sent because you are blocking Direct Messages."));
        };
    }
}
