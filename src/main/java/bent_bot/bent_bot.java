package bent_bot;

import bent_bot.commands.InfoCommand;
import bent_bot.commands.PingCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;

import javax.security.auth.login.LoginException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class bent_bot
{
    public static void main(String[] args) throws Exception
    {
        //finds config.txt
        List<String> list = Files.readAllLines(Paths.get("config.ini"));

        //config variables
        String token = getConfig("token=", list);
        String activity = getConfig("activity=", list);
        String activityMessage = getConfig("activityMessage=", list);

        //define an event waiter
        EventWaiter waiter = new EventWaiter();

        //define a command client builder
        CommandClientBuilder client = new CommandClientBuilder();

        //set the bot's prefix
        client.setPrefix(getConfig("prefix=", list))
                .setAlternativePrefix("");

        //set the bot's owner's ID
        client.setOwnerId(getConfig("ownerID=", list));

        // sets emojis used throughout the bot on successes, warnings, and failures
        client.setEmojis("✅", 	"⚠", 	"❌");

        //add commands
        client.addCommands(new PingCommand(), new InfoCommand());

        //create the help consumer
        Consumer<CommandEvent> helpConsumer = (event) -> {
            EmbedBuilder helpEmbed = new EmbedBuilder();
            helpEmbed.setTitle("**Commands**");
            helpEmbed.setColor(event.isFromType(ChannelType.TEXT) ? event.getMember().getColor() : null);
            Command.Category category = null;
            for (Command command : client.build().getCommands())
            {
                if (!command.isHidden() && (!command.isOwnerCommand() || event.isOwner()))
                {
                    if (!Objects.equals(category, command.getCategory()))
                    {
                        category = command.getCategory();
                        helpEmbed.addField("category", category!=null ? "No Category" : category.getName(), false);
                    }
                    helpEmbed.appendDescription("\n**"+client.build().getPrefix() + command.getName()+"**" + " — " + (command.getArguments()==null ? "" : "`"+ command.getArguments()+"`") + command.getHelp());
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

        //set the help consumer
        client.setHelpConsumer(helpConsumer);

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

            jda.addEventListeners(waiter, client.build());
            jda.build();
        }
        catch (LoginException e)
        {
            //If anything goes wrong in terms of authentication, this is the exception that will represent it
            e.printStackTrace();
        }
    }

    public static String getConfig(String setting, List<String> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).startsWith(setting))
            {
                //remove 'token=' from the string
                list.set(i, list.get(i).replace(setting,""));

                //set token as the extracted token
                return list.get(i);
            }
        }
        return "";
    }

    public static void shutdown(JDA jda)
    {
        jda.shutdown();
        System.out.println("Shut down success...");
        System.exit(0);
    }
}
