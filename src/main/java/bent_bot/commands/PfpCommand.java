package bent_bot.commands;

import bent_bot.bent_bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

@CommandInfo(
        name = {"Pfp"},
        description = "Returns the avatar of the specified user"
)

@Author("Elon (stronous)")
public class PfpCommand extends Command
{

    public PfpCommand()
    {
        this.name = "pfp";
        this.help = "returns info the avatar of the specified user";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        //find a target
        Member targetMember = bent_bot.getTargetMember(event);

        if (targetMember != null)
        {
            EmbedBuilder pfp = new EmbedBuilder()
                    .setColor(targetMember.getColor())
                    .setTitle(targetMember.getUser().getName() +"#"+ targetMember.getUser().getDiscriminator())
                    .setImage(targetMember.getUser().getEffectiveAvatarUrl() + "?size=2048");

            event.reply(pfp.build());
        }
    }
}
