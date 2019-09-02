package bent_bot.commands;

import bent_bot.bent_bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.util.Arrays;

@CommandInfo(
        name = {"Avatar"},
        description = "Returns the avatar of the specified user"
)

@Author("Elon (stronous)")
public class AvatarCommand extends Command
{

    public AvatarCommand()
    {
        this.name = "avatar";
        this.help = "returns the avatar of the specified user";
        this.guildOnly = true;
        this.category = new Category("Fetch");
        this.aliases = new String[]{"pfp"};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        //find a target
        Member targetMember = bent_bot.getTargetMember(event);

        if (!event.getAuthor().isBot())
        {
            if (targetMember != null)
            {
                EmbedBuilder pfp = new EmbedBuilder()
                        .setColor(targetMember.getColor())
                        .setTitle(targetMember.getUser().getName() + "#" + targetMember.getUser().getDiscriminator())
                        .setImage(targetMember.getUser().getEffectiveAvatarUrl() + "?size=2048");

                event.reply(pfp.build());
            }
            else
            {
                sendHelp(event);
            }
        }
    }

    private void sendHelp(CommandEvent event)
    {
        EmbedBuilder help = new EmbedBuilder()
                .setColor(event.getMember().getColor())
                .setTitle("Avatar Help");

        help.addField("aliases", Arrays.toString(aliases), false);

        help.addField("search", "``<name>``  --  Search for an avatar or leave empty for your own.", false);

        event.reply(help.build());
    }
}
