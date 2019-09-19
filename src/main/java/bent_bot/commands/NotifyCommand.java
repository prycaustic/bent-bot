package bent_bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.io.*;
import java.util.Properties;

@CommandInfo(
        name = {"Notify"},
        description = "Sends a message whenever someone joins or leaves the server"
)

@Author("Elon (stronous)")
public class NotifyCommand extends Command
{
    public NotifyCommand()
    {
        this.name = "notify";
        this.help = "server leave / join notifications";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.category = new Category("Miscellaneous");
    }

    public void execute(CommandEvent event)
    {
        if (event.getMember().hasPermission(userPermissions)) {
            if (event.getArgs().toLowerCase().startsWith("set")) {
                //create a properties object
                Properties config = new Properties();

                //new input stream
                try (InputStream in = new FileInputStream("config/notify.properties")) {
                    //load the input stream into the config object
                    config.load(in);

                    //check for mentioned channels
                    if (!event.getMessage().getMentionedChannels().isEmpty()) {
                        //add the welcome channel
                        config.setProperty(event.getGuild().toString(), event.getMessage().getMentionedChannels().get(0).getId());
                        event.reactSuccess();
                    } else
                        event.replyWarning("No welcome channel was mentioned.");

                    config.store(new FileOutputStream("config/notify.properties"), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //WELCOME MESSAGE
            else if (event.getArgs().toLowerCase().startsWith("welcome")) {
                //create a properties object
                Properties config = new Properties();

                //new input stream
                try (InputStream in = new FileInputStream("config/notify.properties")) {
                    //load the input stream into the config object
                    config.load(in);

                    //check for mentioned channels
                    if (!event.getArgs().isEmpty()) {
                        //set the goodbye message
                        config.setProperty(event.getGuild() + ".WELCOME", event.getArgs().replaceFirst("welcome ", ""));
                        event.reactSuccess();
                    } else
                        event.replyWarning("No welcome message was entered.");

                    config.store(new FileOutputStream("config/notify.properties"), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //GOODBYE MESSAGE
            else if (event.getArgs().toLowerCase().startsWith("goodbye")) {
                //create a properties object
                Properties config = new Properties();

                //new input stream
                try (InputStream in = new FileInputStream("config/notify.properties")) {
                    //load the input stream into the config object
                    config.load(in);

                    //check for mentioned channels
                    if (!event.getArgs().isEmpty()) {
                        //set the goodbye message
                        config.setProperty(event.getGuild() + ".GOODBYE", event.getArgs().replaceFirst("goodbye ", ""));
                        event.reactSuccess();
                    } else
                        event.replyWarning("No goodbye message was entered.");

                    config.store(new FileOutputStream("config/notify.properties"), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                sendHelp(event);
            }
        }
        else
        {
            if (event.getArgs().toLowerCase().startsWith("help"))
                sendHelp(event);
            else
                event.replyError("You are not allowed to use this command.");
        }

    }

    private void sendHelp(CommandEvent event)
    {
        try {
            FileInputStream in = new FileInputStream("config/notify.properties");
            Properties prop = new Properties();
            prop.load(in);
            String channelId = prop.getProperty(event.getGuild().toString());
            EmbedBuilder help = new EmbedBuilder()
                    .setColor(event.getMember().getColor())
                    .setTitle("Notification Help");

            help.appendDescription("If you would like to disable the notifications, simply set the channel to ``null``." +
                    "\nThe current notification channel is set to " + (channelId != null ? "#"+event.getGuild().getGuildChannelById(channelId).getName() : "null") + ".");
            help.addField("set", "``set <channel>``  --  This sets the notification channel.", false);
            help.addField("welcome", "``welcome <message>``  --  This sets the welcome message and will be sent " +
                    "everytime someone new joins the server (use USER_NAME to mention the new member).", false);
            help.addField("goodbye", "``goodbye <message>``  --  This sets the goodbye message and will be sent " +
                    "everytime someone leaves the server (use USER_NAME as before).", false);

            event.reply(help.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
