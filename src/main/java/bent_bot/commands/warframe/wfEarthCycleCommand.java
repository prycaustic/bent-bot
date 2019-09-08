package bent_bot.commands.warframe;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = {"Warframe Earth Cycle"},
        description = "Returns the current Earth Cycle"
)

@Author("Elon (stronous)")
public class wfEarthCycleCommand extends wfCycleCommand
{
    public wfEarthCycleCommand()
    {
        this.name = "wfearth";
        this.help = "returns the current Earth cycle";
        this.guildOnly = false;
        this.aliases = new String[]{"wfec", "ec"};
        this.category = new Category("Warframe");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        event.reply(getCycleEmbed("earth", "Earth Cycle"));
    }
}
