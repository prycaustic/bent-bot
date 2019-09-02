package bent_bot.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@CommandInfo(
        name = {"Cat"},
        description = "Returns cat"
)

@Author("Elon (stronous)")
public class CatCommand extends Command
{
    public CatCommand()
    {
        this.name = "cat";
        this.help = "returns cat";
        this.aliases = new String[]{"cat"};
        this.guildOnly = false;
        this.category = new Category("CAT");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try
        {
            String catURL = "https://aws.random.cat/meow";

            URL url = new URL(catURL);
            URLConnection request = url.openConnection();
            request.connect();

            //convert to a JSON object
            JsonParser jp = new JsonParser();
            JsonObject root = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();

            EmbedBuilder catEmbed = new EmbedBuilder()
                    .setTitle("CAT")
                    .setDescription("Here is a random cat.")
                    .setImage(root.get("file").getAsString())
                    .setFooter("via https://aws.random.cat/", "https://purr.objects-us-east-1.dream.io/static/ico/favicon-96x96.png");

            event.reply(catEmbed.build());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
