package bent_bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

@CommandInfo(
        name = {"Archillect"},
        description = "Returns a random or specified Archillect post"
)

@Author("Elon (stronous)")
public class ArchillectCommand extends Command
{
    private int id;

    public ArchillectCommand() {
        this.name = "archillect";
        this.help = "returns a random or specified archillect post";
        this.arguments = "<id>";
        this.guildOnly = false;
        this.aliases = new String[]{"ar","arch"};
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try {
            EmbedBuilder archillect = new EmbedBuilder()
                    .setColor(Color.white)
                    .setTitle("Archillect");

            if (event.getArgs().isEmpty())
            {
                archillect.setImage(getRandomImage())
                        .setDescription("["+id+"]");
                event.reply(archillect.build());
            }
            else
            {
                String[] arguments = event.getArgs().split("\\s+");

                if (arguments.length > 1)
                {
                    event.replyWarning("Please input an ID.");
                    return;
                }
                else if (arguments[0].equalsIgnoreCase("l"))
                {
                    archillect.setImage(getLatestImage())
                            .setDescription("["+id+"]");
                }
                //if they don't want a random post try searching for a post
                else
                {
                    if (getImageById(arguments[0]) == null)
                    {
                        event.replyWarning("Please enter a valid ID.");
                        return;
                    }
                    else
                    {
                        archillect.setImage(getImageById(arguments[0]))
                                .setDescription("["+id+"]");
                    }
                }

                event.reply(archillect.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Web scraper to grab the Archillect homepage
     *
     * @return      a Document object containing the Archillect homepage
     */
    private Document scraper() throws IOException
    {
        return Jsoup.connect("http://archillect.com/").get();
    }

    /**
     * Gets the subpage of a specific post
     *
     * @param inputId   the id of the desired post
     * @return          a Document object containing the subpage of the post specified by {@code inputId}
     */
    private Document getPost(Integer inputId) throws IOException
    {
        return Jsoup.connect("http://archillect.com/" + inputId).get();
    }

    /**
     * Gets the image link of the latest post listed on the Archillect homepage
     *
     * @return          a String containing a link to the latest post
     */
    private String getLatestImage() throws IOException
    {
        id = getLatestImageId();

        return getImageLink(getPost(id));
    }

    /**
     * Gets the id of the latest post listed on the Archillect homepage
     * 
     * @return          an Integer with the id of the latest archillect post
     */
    private Integer getLatestImageId() throws IOException
    {
        return Integer.parseInt(scraper().getElementById("posts").child(0).attr("href").replace("/", ""));
    }

    /**
     * Gets the image link from the post with the specified Document objet
     * 
     * @param doc       the Document object of the desired post
     * @return          a String containing a link to the image in the post specified by {@code doc}
     */
    private String getImageLink(Document doc)
    {
        return doc.getElementById("imgnav").getElementsByTag("img").attr("src");
    }

    /**
     * Gets a random post from http://archillect.com/
     * 
     * @return          a String containing a link to the post with the generated id
     */
    private String getRandomImage() throws IOException
    {
        Random rand = new Random();

        //get a random id
        id = rand.nextInt(getLatestImageId() + 1);
        
        return getImageLink(getPost(id));
    }

    /**
     * Gets an image from Archillect by its id
     * 
     * @param args      the String of arguments from the current event {@link #execute(CommandEvent)} )}
     * @return          a String containing a link to the post specified by {@code args}
     */
    private String getImageById(String args) throws IOException
    {
        try {
            id = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            return null;
        }

        //check that the id exists
        if (id > getLatestImageId())
        {
            return null;
        }

        return getImageLink(getPost(id));
    }
}
