package bent_bot.commands.warframe;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = {"Warframe Vallis Cycle"},
        description = "Returns the current Vallis Cycle"
)

@Author("Elon (stronous)")
public class wfVallisCycleCommand extends wfCycleCommand
{
    public wfVallisCycleCommand()
    {
        this.name = "wfvallis";
        this.help = "returns the current Vallis cycle";
        this.guildOnly = false;
        this.aliases = new String[]{"wfvc", "vc"};
        this.category = new Category("Warframe");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        event.reply(getCycleEmbed("vallis", "Fortuna - Orb Vallis Cycle"));
    }
}
