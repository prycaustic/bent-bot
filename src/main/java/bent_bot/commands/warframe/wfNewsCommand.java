package bent_bot.commands.warframe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@CommandInfo(
        name = {"Warframe News"},
        description = "Returns the latest Warframe news"
)

@Author("Elon (stronous)")
public class wfNewsCommand extends Command
{

    public wfNewsCommand()
    {
        this.name = "wfnews";
        this.help = "returns the latest Warframe news";
        this.guildOnly = false;
        this.aliases = new String[]{"wfn"};
        this.category = new Category("Warframe");
    }

    @Override
    protected void execute(CommandEvent event)
    {
        try {

            String wfURL = "https://api.warframestat.us/pc";
            String catURL = "https://aws.random.cat/meow";

            URL url = new URL(catURL);
            URLConnection request = url.openConnection();
            request.connect();

            //convert to a JSON object
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootobj = root.getAsJsonObject();

            String link = rootobj.get("file").getAsString();

            event.reply(link);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
