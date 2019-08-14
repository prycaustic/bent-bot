package bent_bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

import java.time.temporal.ChronoUnit;

@CommandInfo(
        name = {"Ping"},
        description = "Checks the bot's latency"
)

@Author ("Elon (stronous)")
public class PingCommand extends Command
{
    public PingCommand()
    {
        this.name = "ping";
        this.help = "checks the bot's latency";
        this.guildOnly = false;
        this.aliases = new String[]{};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        event.reply("pong! ...", m ->
        {
            //calculate the ping
            long ping = event.getMessage().getTimeCreated().until(m.getTimeCreated(), ChronoUnit.MILLIS);
            m.editMessage("pong! ``" + ping + "ms``").queue();
        });
    }
}
