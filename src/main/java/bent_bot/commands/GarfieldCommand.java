package bent_bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Random;

@CommandInfo(
        name = {"Garfield"},
        description = "Returns a random Garfield comic or one specified by date"
)


@Author("Elon (stronous)")
public class GarfieldCommand extends Command
{

    public GarfieldCommand()
    {
        this.name = "gfc";
        this.help = "returns a random garfield comic or one specified by date";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            Random rand = new Random();

            int id = rand.nextInt(getLatestComicId() + 1);

            EmbedBuilder gfc = new EmbedBuilder()
                    .setColor(0xF1A703);

            gfc.setTitle(getComicTitle(getComicById(id)), getComicLink(id));
            gfc.setImage(getComicImage(getComicById(id)));
            gfc.setDescription(getComicDescription(getComicById(id)));

            event.reply(gfc.build());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Web scraper to grab the MezzaCotta Garfield homepage
     *
     * @return  a Document object containing the homepage
     */
    private Document scraper() throws IOException
    {
        //get the garfield homepage
        return Jsoup.connect("http://www.mezzacotta.net/garfield/").get();
    }

    /**
     * Gets the id of the latest comic
     *
     * @return  an Integer with the id of the latest comic
     */
    private Integer getLatestComicId() throws IOException
    {
        return Integer.valueOf(scraper().getElementsByTag("img").get(1).attr("src").substring(17, 21));
    }

    /**
     * Gets the MezzaCotta page for a garfield comic with the id specified by {@code comicId}
     *
     * @param comicId   id for the comic
     * @return          a Document object containing the page for the comic
     */
    private Document getComicById(Integer comicId) throws IOException
    {
        return Jsoup.connect("http://www.mezzacotta.net/garfield/?comic=" + comicId).get();
    }

    /**
     * Gets the title of the comic specified by {@code doc}
     *
     * @param doc   a Document object containing the page for the comic
     * @return      a String containing the title of the comic
     */
    private String getComicTitle(Document doc)
    {
        return doc.getElementsByTag("h2").get(0).text();
    }

    /**
     * Gets the image link for the comic specified by {@code doc}
     *
     * @param doc   a Document object containing the page for the comic
     * @return      a String containing the link to the image for the comic
     */
    private String getComicImage(Document doc)
    {
        return "http://www.mezzacotta.net" + doc.getElementsByTag("img").get(1).attr("src");
    }

    /**
     * Gets the link to the comic specified by {@code comicId}
     *
     * @return      a String containing the link to the comic page
     */
    private String getComicLink(Integer comicId)
    {
        return "http://www.mezzacotta.net/garfield/?comic=" + comicId;
    }

    /**
     * Gets the author notes for a comic specified by {@code doc}
     *
     * @param doc   a Document object containing the page for the comic
     * @return      a String containing the description for the comic
     */
    private String getComicDescription(Document doc)
    {
        return doc.getElementsByTag("p").get(7).wholeText();
    }
}
