/*package bent_bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import bent_bot.*;

import java.io.IOException;
import java.util.Random;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        double random = Math.random();
        JDA jda = event.getJDA();

        //event specific variables these are provided in every event
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();            //this could be a textChannel, PrivateChannel, or Group


        String msg = message.getContentDisplay();           //a human readable version of the message
        String[] args = message.getContentRaw().split(" ");         //split the message into a string array


        boolean bot = author.isBot();           //is the message sent by a bot?

        //guild messages
        if (event.isFromType(ChannelType.TEXT))
        {
            //since we know the message is from a guild we can store guild specific information about the event
            Guild guild = event.getGuild();
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();          //guild specific information about the member
                                                        //this is different from a User!!!
            String name;            //name of the user
            //check if the message is a webhook
            if (message.isWebhookMessage())
            {
                name = author.getName();            //if it is a webhook there is no user associated; we use the author
            }
            else
            {
                name = member.getEffectiveName();           //nickname if the member has one; username if not
            }

            //print the message to the console
            System.out.printf("(%s)[%s]<%s>: %s\n", guild.getName(), textChannel.getName(), name, msg);

            //guild only command handler
            if (!bot)
            {
                if (msg.startsWith(bent_bot.prefix + "ping"))
                    pingCommand(textChannel);
                else if (msg.startsWith(bent_bot.prefix + "info"))
                    infoCommand(textChannel, member);
                else if (msg.startsWith(bent_bot.prefix + "pfp"))
                    pfpCommand(textChannel, member, args, message);
                else if (msg.startsWith("my internet is back"))
                    internetCommand(channel);
                else if (msg.startsWith(bent_bot.prefix + "shutdown")) {
                    try {
                        shutdownCommand(textChannel, member, args, event);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //randomly send arabic like an actual retard
                if (random < .002)
                {
                    arabicCommand(textChannel);
                }
            }
        }
        else if (event.isFromType(ChannelType.PRIVATE))
        {
            //the message was sent in a private channel
            PrivateChannel privateChannel = event.getPrivateChannel();

            System.out.printf("[PRIV]<%s>: %s\n", author.getName(), msg);

            //private only command handler
            if (msg.startsWith(bent_bot.prefix + "ping"))
                pingCommand(channel);
            else if (msg.startsWith("my internet is back"))
                internetCommand(channel);
        }
    }

    private static void pingCommand(MessageChannel channel)
    {
        channel.sendTyping().queue();
        channel.sendMessage("pong!").queue();
    }

    public void infoCommand(MessageChannel channel, Member member)
    {
        //create the embed
        EmbedBuilder info = new EmbedBuilder()
                .setTitle("Information")
                .setDescription("Just another bot so I can learn some more. I hope she's good.")
                .setFooter("Made by Elon (@stronous#3792)", "https://cdn.discordapp.com/avatars/124620492098240513/fe72def697653b6113ede3e22c2b143f.png")
                .setColor(member.getColor());

        //send the message after building the embed
        channel.sendTyping().queue();
        channel.sendMessage(info.build()).queue();

        info.clear();
    }

    private static void pfpCommand(MessageChannel channel, Member member, String[] args, Message message)
    {
        Member targetMember = null;

        //get avatar
        //gets the avatar of a pinged user and sends it as an embed

        //check how many arguments there are
        if (args.length < 2)
        {
            //if they only send the command then set the target as the user who sent the command and build the embed
            targetMember = member;
        }
        else
        {
            //check if they even ping anyone
            if (message.getMentionedMembers().size() < 1)
            {
                //if nobody is pinged then check if they want help
                if (args[1].equalsIgnoreCase("help"))
                {
                    //help them if they do
                    channel.sendTyping().queue();
                    channel.sendMessage("``pfp <@user>``\n" +
                            "this command gets the picture of a user (the user of the command if nobody is mentioned)");
                }
                else
                {
                    //if they are putting in random arguments then complain
                    channel.sendTyping().queue();
                    channel.sendMessage("please use the command properly").queue();
                }
            }
            else
            {
                //if anyone is mentioned then set the target as the first member
                targetMember = message.getMentionedMembers().get(0);
            }
        }

        //if someone is mentioned then send their profile picture
        //if multiple people are mentioned it will just send the first person that was mentioned
        EmbedBuilder pfp = new EmbedBuilder()
                .setImage(targetMember.getUser().getEffectiveAvatarUrl() + "?size=2048")
                .setColor(targetMember.getColor());

        //send the embed
        channel.sendTyping().queue();
        channel.sendMessage(pfp.build()).queue();

        pfp.clear();
    }

    public static void internetCommand(MessageChannel channel)
    {
        //just absolutely own the retard who talks about his internet
        channel.sendTyping().queue();
        channel.sendMessage("nobody cares about your internet.").queue();
    }

    public static void shutdownCommand(TextChannel channel, Member member, String[] args, MessageReceivedEvent event) throws IOException
    {
        if (args.length == 1)
        {
            if (member.getId().equals(bent_bot.ownerID))
            {
                //shut her down :(
                channel.sendMessage("Shutting down...").queue();
                bent_bot.shutdown(event.getJDA());
            }
            else
            {
                channel.sendTyping().queue();
                channel.sendMessage("sorry only the owner can use this command").queue();
            }
        }
    }

    public static void arabicCommand(TextChannel channel)
    {

        double random = Math.random();
        String arabicMess = "واُسدل استدعى التّ حول انه أم, حتى تم تحرّكت الخار  جية. بس بب  انتصارهم  عل ذات, ٣٠ سقط" +
                "ت الث الث  كان,  بسبب جسيمة انتهت كل بال.   دون ثمّة عرفها العالمية من, بال مرمى  والحز ب كل, حين ثم" +
                " بالرغم الحكومة الإنذار،. بحث أن  دارت الدو ل, س اعة  تصرّف الإيطالية أن  ضرب,  ب ل تعد  أصقاع الساحل. ذلك" +
                " بـ الطر يق للمجه ود, حلّت  معزّزة مع ذلك. كل هذا فرنسا الفرنسية الأبرياء, وصل  أم حصدت فكان ت العسكري.";

        String[] arabicArray = arabicMess.split(" ");

        //shuffle the array
        arabicArray = shuffleArray(arabicArray);

        //remove some random entries
        for (int i = 0; i < arabicArray.length; i++)
        {
            random = Math.random();
            if (random < .2)
            {
                arabicArray[i] = " ";
            }
        }

        //turn it back into a string
        String arabicStr = String.join("", arabicArray);

        //clean it up
        arabicStr.replace("[", "");
        arabicStr.replace("]", "");

        channel.sendMessage(arabicStr).queue();
    }

    private static String[] shuffleArray(String[] array)
    {
        int index;
        String temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        return array;
    }
}
*/