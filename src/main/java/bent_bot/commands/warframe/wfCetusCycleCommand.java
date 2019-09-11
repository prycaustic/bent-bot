package bent_bot.commands.warframe;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = {"Cetus Cycle"},
        description = "Returns the current Cetus Cycle"
)

@Author("Elon (stronous)")
public class wfCetusCycleCommand extends wfCycleCommand
{
    public wfCetusCycleCommand()
    {
        this.name = "wfcetus";
        this.help = "returns the current cetus cycle";
        this.guildOnly = false;
        this.aliases = new String[]{"wfcc", "cc"};
        this.category = new Category("Warframe");
    }

    public void execute(CommandEvent event)
    {
        event.reply(getCycleEmbed("cetus", "Cetus - Plains of Eidolon Cycle"));
    }
}
