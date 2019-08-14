package bent_bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = {"Pfp"},
        description = "Returns the avatar of the specified user"
)

@Author("Elon (stronous)")
public class PfpCommand extends Command {

    public PfpCommand()
    {
        this.name = "info";
        this.help = "returns info about the specified user";
        this.guildOnly = false;
        this.aliases = new String[]{};
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
