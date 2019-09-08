package bent_bot.commands.warframe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@CommandInfo(
        name = {"Warframe News"},
        description = "Returns the latest Warframe news"
)

@Author("Elon (stronous)")
public class wfNewsCommand extends Command
{
    private EventWaiter waiter;

    public wfNewsCommand(EventWaiter waiter)
    {
        this.name = "wfnews";
        this.help = "returns the latest Warframe news";
        this.guildOnly = false;
        this.aliases = new String[]{"wfn"};
        this.category = new Category("Warframe");
        this.waiter = waiter;
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try {
            JsonArray news = connect().getAsJsonArray("news");

            if (event.getArgs().isEmpty())
            {
                EmbedBuilder newsEmbed = new EmbedBuilder()
                        .setColor(0x04506F)
                        .setTitle("Warframe News", "https://www.warframe.com/")
                        .setFooter("via https://hub.warframestat.us/", "https://hub.warframestat.us/img/icons/favicon-32x32.png");

                newsEmbed.appendDescription("**Choose an option for more info.**");

                for (int i = 1; i < news.getAsJsonArray().size(); i++) {
                    newsEmbed.appendDescription("\n\n``" + i + ":``  " + news.get(i).getAsJsonObject().get("message").getAsString());
                }

                waiter.waitForEvent(
                        MessageReceivedEvent.class,
                        predicate -> predicate.getAuthor().equals(event.getAuthor()) && event.getChannel().equals(event.getChannel()),
                        action -> sendPostEmbed(event, action.getMessage().getContentRaw(), news),
                        30,
                        TimeUnit.SECONDS,
                        null
                );

                event.reply(newsEmbed.build());
            }
            else
            {
                sendPostEmbed(event, event.getArgs(), news);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPostEmbed(CommandEvent event, String m, JsonArray news)
    {
        try {
            int postnum = Integer.parseInt(m);

            String message = news.get(postnum).getAsJsonObject().get("message").getAsString();
            String link = news.get(postnum).getAsJsonObject().get("link").getAsString();
            String imageLink = news.get(postnum).getAsJsonObject().get("imageLink").getAsString();
            String eta = news.get(postnum).getAsJsonObject().get("eta").getAsString();

            EmbedBuilder postEmbed = new EmbedBuilder()
                    .setColor(0x04506F)
                    .setTitle(message, link)
                    .setDescription(eta)
                    .setImage(imageLink)
                    .setFooter("via https://hub.warframestat.us/", "https://hub.warframestat.us/img/icons/favicon-32x32.png");

            event.reply(postEmbed.build());
        } catch (NumberFormatException nfe) {
            event.replyError("Not a valid post.");
        }
    }

    static JsonObject connect() throws IOException
    {
        URL url = new URL("https://api.warframestat.us/pc");
        HttpURLConnection request = (HttpURLConnection) url.openConnection();

        if (request.getResponseCode() != 200)
            throw new RuntimeException("Failed : HTTP Error Code : " + request.getResponseCode());

        //convert to a JSON object
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(request.getInputStream()));

        return root.getAsJsonObject();
    }
}
