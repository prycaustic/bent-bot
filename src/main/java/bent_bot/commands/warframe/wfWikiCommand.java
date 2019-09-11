package bent_bot.commands.warframe;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class wfWikiCommand extends Command
{
    public wfWikiCommand()
    {
        this.name = "wfwiki";
        this.help = "returns the wiki page for the search term";
        this.guildOnly = false;
        this.aliases = new String[]{"wfw", "ws"};
        this.category = new Category("Warframe");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try {
            String query = event.getArgs().contains(" ") ? event.getArgs().substring(0, event.getArgs().indexOf(" ")) : event.getArgs();
            String psearch = "https://warframe.fandom.com/api.php?format=xml&action=query&generator=allpages&gaplimit=50&gapfrom=" + query + "&prop=info&inprop=url";

            Document wiki = Jsoup.connect(psearch).get();

            Elements pages = wiki.getElementsByTag("page");
            String title = "";
            String fullurl = "";
            for (int i = 0; i < pages.size(); i++)
            {
                if (pages.get(i).attr("title").toLowerCase().contains(event.getArgs().toLowerCase()))
                {
                    title = pages.get(i).attr("title");
                    fullurl = pages.get(i).attr("fullurl");
                    i = pages.size();
                }
            }
            try {
                Document wikiPage = Jsoup.connect(fullurl).get();
                //String image = wikiPage.getElementsByClass("image image-thumbnail").get(0).attr("href");

                EmbedBuilder wikiEmbed = new EmbedBuilder()
                        .setColor(0x04506F)
                        .setTitle(title, fullurl)
                        //.setImage(image)
                        .setFooter("via https://warframe.fandom.com/api.php", "https://hub.warframestat.us/img/icons/favicon-32x32.png");

                for (int i = 0; i < wikiPage.getElementsByTag("p").size() && i < 3; i++)
                    wikiEmbed.appendDescription("\n" + wikiPage.getElementsByTag("p").get(i).wholeText());

                try {
                    wikiEmbed.addField("Sources", wikiPage.getElementsByAttributeValue("data-source", "autoDrops").get(0).text().replace(") ", ")\n").replaceFirst("Sources", ""), false);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                event.reply(wikiEmbed.build());
            } catch (Exception e) {
                event.replyError("No page found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
