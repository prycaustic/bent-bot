package bent_bot.commands;

import bent_bot.bent_bot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.sql.ClientInfoStatus;

import static net.dv8tion.jda.api.utils.TimeUtil.getDateTimeString;

@CommandInfo(
        name = {"Info"},
        description = "Returns info about the specified user"
)

@Author("Elon (stronous)")
public class InfoCommand extends Command {

    public InfoCommand()
    {
        this.name = "info";
        this.help = "returns info about the specified user";
        this.guildOnly = false;
        this.aliases = new String[]{};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        //find a target
        Member targetMember = bent_bot.getTargetMember(event);

        if (targetMember != null)
        {
            EmbedBuilder infoEmbed = new EmbedBuilder()
                    .setColor(targetMember.getColor())
                    .setThumbnail(targetMember.getUser().getEffectiveAvatarUrl());

            infoEmbed.setTitle(targetMember.getUser().getName() + "#" + targetMember.getUser().getDiscriminator() + (targetMember.getNickname() != null ? " (" + targetMember.getEffectiveName() + ")" : " "));
            infoEmbed.addField("ID", targetMember.getId(), true);
            infoEmbed.addField("Shared Servers", String.valueOf(targetMember.getUser().getMutualGuilds().size()), true);
            infoEmbed.addField("Status", String.valueOf(targetMember.getOnlineStatus()).replace("_", " "), true);
            infoEmbed.addField("Name Color", "#" + Integer.toHexString(targetMember.getColorRaw()).toUpperCase(), true);
            infoEmbed.addField("Account Created", getDateTimeString(targetMember.getTimeCreated()), false);
            infoEmbed.addField("Join Date", getDateTimeString(targetMember.getTimeJoined()), false);

            if (targetMember.getTimeBoosted() != null)
                infoEmbed.addField("Boosting Since", getDateTimeString(targetMember.getTimeBoosted()), false);

            if (!targetMember.getRoles().isEmpty())
            {
                StringBuilder rolesBuilder = new StringBuilder();

                for (int i = 0; i < targetMember.getRoles().size(); i++)
                {
                    if (i > 0)
                    {
                        rolesBuilder.append(", ");
                    }
                    rolesBuilder.append(targetMember.getRoles().get(i).getName());
                }

                String roles = rolesBuilder.toString();

                infoEmbed.addField("Roles " + "(" + targetMember.getRoles().size() + ")", roles, false);
            }

            event.reply(infoEmbed.build());
        }
    }
}
