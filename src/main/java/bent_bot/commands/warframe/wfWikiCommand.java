package bent_bot.commands.warframe;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class wfWikiCommand extends Command
{
    private EventWaiter waiter;

    public wfWikiCommand(EventWaiter waiter)
    {
        this.name = "wfwiki";
        this.help = "returns the wiki page for the search term";
        this.guildOnly = false;
        this.aliases = new String[]{"wfw", "ws"};
        this.category = new Category("Warframe");
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try {
            String query = event.getArgs().contains(" ") ? event.getArgs().substring(0, event.getArgs().indexOf(" ")) : event.getArgs();
            String psearch = "https://warframe.fandom.com/api.php?format=xml&action=query&generator=allpages&gaplimit=75&gapfrom=" + query + "&prop=info&inprop=url";

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

                EmbedBuilder wikiEmbed = new EmbedBuilder()
                        .setColor(0x04506F)
                        .setTitle(title, fullurl)
                        .setFooter("via https://warframe.fandom.com/api.php", "https://hub.warframestat.us/img/icons/favicon-32x32.png");

                for (int i = 0; i < wikiPage.getElementsByTag("p").size() && i < 3; i++)
                    wikiEmbed.appendDescription("\n" + wikiPage.getElementsByTag("p").get(i).wholeText());

                //check for a thumbnail
                if (!wikiPage.getElementsByClass("image image-thumbnail").isEmpty())
                    wikiEmbed.setThumbnail(wikiPage.getElementsByClass("image image-thumbnail").get(0).attr("href"));
                else if (!wikiPage.getElementsByClass("image image-thumbnail ").isEmpty())
                    wikiEmbed.setThumbnail(wikiPage.getElementsByClass("image image-thumbnail ").get(0).attr("href"));

                //check for source
                if (!wikiPage.getElementsByAttributeValue("data-source", "autoDrops").isEmpty())
                    wikiEmbed.addField("Sources", wikiPage.getElementsByAttributeValue("data-source", "autoDrops").get(0).text().replace(") ", ")\n").replaceFirst("Sources", ""), false);

                //check for a foundry table
                if (!wikiPage.getElementsByClass("foundrytable").isEmpty())
                {
                    Element table = wikiPage.getElementsByClass("foundrytable").get(0);
                    Elements td = table.getElementsByTag("td");
                    String credits = td.get(0).wholeText();
                    List<String> items = new ArrayList<>();
                    for (int i = 1; i < td.size(); i++)
                    {
                        Element tdi = td.get(i);
                        if (td.get(i) != null)
                        {
                            String name = tdi.getElementsByTag("a").attr("href");
                            items.add(name + (!name.equals("") ? ": " : "" )+ tdi.wholeText());
                        }
                    }
                    StringBuilder sitems = new StringBuilder();
                    for (int i = 0; i < 4; i++)
                        sitems.append(items.get(i).replace("/wiki/", "").replace("_", " ").replace(": Time", "Time"));
                    wikiEmbed.addField("Manufacturing Requirements", "Credits: " + credits + sitems, false);
                }

                //check for drop locations
                if (!wikiPage.getElementsByAttributeValue("title", "PC").isEmpty())
                {
                    Elements drops = wikiPage.getElementsByAttributeValue("title", "PC").get(0).getElementsByTag("tr");

                    wikiEmbed.addField("Drop Locations", "", false);
                    for (Element i : drops)
                    {
                        String fieldName = i.getElementsByTag("td").get(0).text();
                        StringBuilder fieldText = new StringBuilder();
                        if (!i.getElementsByTag("span").isEmpty())
                        {
                            for (Element j : i.getElementsByTag("span"))
                            {
                                fieldText.append("\n").append(j.text());
                            }
                        }
                        else
                        {
                            fieldText.append(i.text());
                        }
                        wikiEmbed.addField(fieldName, fieldText.toString(), true);
                    }
                }

                //check for emod table
                if (!wikiPage.getElementsByClass("emodtable").isEmpty()) {
                    Elements components = wikiPage.getElementsByClass("emodtable").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
                    if (wikiPage.getElementsByAttributeValue("data-tracking", "categories-top-1").text().equals("Relic"))
                    {
                        Elements td = components.get(1).getElementsByTag("td");
                        if (td.size() > 3)
                        {
                            StringBuilder common = new StringBuilder();
                            StringBuilder uncommon = new StringBuilder();
                            StringBuilder rare = new StringBuilder();
                            for (int i = 1; i < 9; i++)
                            {
                                td = components.get(i).getElementsByTag("td");
                                if (!td.get(0).text().equals("")) {
                                    if (i < 5)
                                        common.append(td.get(0).text()).append("\n");
                                    else if (i < 8)
                                        uncommon.append(td.get(0).text()).append("\n");
                                    else
                                        rare.append(td.get(0).text()).append("\n");
                                }
                            }
                            wikiEmbed.addField("Common", common.toString(), false);
                            wikiEmbed.addField("Uncommon", uncommon.toString(), false);
                            wikiEmbed.addField("Rare", rare.toString(), false);
                        }
                    }
                }

                //check for drop chances
                if (!wikiPage.getElementsByAttributeValue("data-expandtext", "Drop Locations").isEmpty())
                {
                    Elements drops = wikiPage.getElementsByAttributeValue("data-expandtext", "Drop Locations").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
                    StringBuilder desc = new StringBuilder();
                    for (int i = 1; i < drops.size(); i++)
                    {
                        Elements td = drops.get(i).getElementsByTag("td");
                        desc.append("**").append(td.get(0).text()).append("** — ");
                        for (int j = 1; j < td.size(); j++)
                        {
                            desc.append(td.get(j).text());
                            if (j < td.size() - 1)
                                    desc.append(", ");
                            else
                                desc.append("\n");
                        }
                    }
                    wikiEmbed.addField("Drop Locations", desc.toString(), false);
                }

                event.reply(wikiEmbed.build(), m ->
                {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wikiEmbed.clearFields();
                    wikiEmbed.appendDescription("\n*This message has been trimmed to save space.*");
                    m.editMessage(wikiEmbed.build()).queue();
                });
            } catch (Exception e) {
                event.replyError("No page found.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
