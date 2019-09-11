package bent_bot.commands.warframe;

import com.google.gson.JsonObject;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static bent_bot.commands.warframe.wfNewsCommand.connect;

@CommandInfo(
        name = {"Cycle"},
        description = "Returns a cycle"
)

@Author("Elon (stronous)")
abstract class wfCycleCommand extends Command
{
    MessageEmbed getCycleEmbed(String cycleType, String title)
    {
        try {
            JsonObject cycle = connect().getAsJsonObject(cycleType + "Cycle").getAsJsonObject();
            String state = cycle.get("state").getAsString();
            String expiry = cycle.get("expiry").getAsString();
            OffsetDateTime odt = OffsetDateTime.parse(expiry);
            long timeLeftSec = OffsetDateTime.now().until(odt, ChronoUnit.SECONDS);
            String[] emoji = new String[]{" ☀", " \uD83C\uDF11"};
            String thumbnail = null;
            switch (cycleType) {
                case "earth":
                    thumbnail = "https://vignette.wikia.nocookie.net/warframe/images/1/1e/Earth.png";
                    break;
                case "cetus":
                    thumbnail = "https://vignette.wikia.nocookie.net/warframe/images/3/32/OstronSyndicateFlag.png";
                    break;
                case "vallis":
                    thumbnail = "https://vignette.wikia.nocookie.net/warframe/images/7/70/SolarisUnitedSyndicateFlagRC.png";
                    emoji = new String[]{" ❄", " \uD83D\uDD25"};
                    break;
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(0x04506F)
                    .setTitle(title)
                    .setThumbnail(thumbnail)
                    .setFooter("via https://hub.warframestat.us/", "https://hub.warframestat.us/img/icons/favicon-32x32.png");

            embed.addField("\n\nTime Left", getTimeLeft(timeLeftSec), true);
            embed.addField("\n\nCurrent State", StringUtils.capitalize(state) + (state.equals("day") || state.equals("cold") ? emoji[0] : emoji[1]), true);

            return embed.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTimeLeft(long in)
    {
        long hours = in / 3600;
        long mins = (in - hours * 3600) / 60;
        long secs = (in - hours * 3600) - mins * 60;

        return (hours > 0 ? hours + "h " : "") + (mins > 0 ? mins + "m " : "") + (secs > 0 ? secs + "s " : "");
    }
}
