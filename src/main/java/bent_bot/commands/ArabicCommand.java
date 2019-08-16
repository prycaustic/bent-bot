package bent_bot.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class ArabicCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        double random  = Math.random();

        if (random < .002 && !event.getAuthor().isBot())
        {
            String arabicMess = "ما ألمّ التبرعات الى, جُل لم ليبين الصينية الجنوبي, لم بال السفن حاملات. تم قبل يطول التاريخ،. يكن وانتهاءً الرئيسية الأمريكية في. إعلان انذار العسكري أخر ثم. كل المسرح الساحل دول, وتم كردة استبدال الأعمال عل, وتم تجهيز العصبة قد.\n" +
                    "\n" +
                    "ثم على بوابة العسكري لتقليعة, انه قد غضون ترتيب البرية. ٣٠ الله باستخدام بحث, مع إعمار لهيمنة نفس. تنفّس والتي بحث ثم, الثقيل تزامناً قد مما. وبغطاء الهجوم لم لها, ان حدى نقطة سياسة سليمان،. عل فهرست بولندا، والنرويج كلّ, والعتاد وبلجيكا، ومن و. بلاده لهيمنة قد غير, ويتّفق مقاطعة الوزراء مع الا, بشكل لفشل العالمية مما في.\n" +
                    "\n" +
                    "دون بريطانيا بالمطالبة ٣٠, أفاق الهادي ثم دول. وقوعها، المبرمة ٣٠ جهة, وشعار الخاصّة بالسيطرة دار أن. أي فبعد الحرة الشتاء دول, مكن إذ ونتج شعار الأرض. أخر هو بحشد والمعدات. دون تنفّس وتنصيب ثم.\n" +
                    "\n" +
                    "تمهيد للجزر يتم ان, وبداية واستمر مكثّفة يتم إذ. يعبأ سليمان، الجنوبي كل دنو, عُقر إستعمل الضغوط لها إذ, بداية علاقة شيء كل. عل فقد وبعد العمليات, مع هناك أصقاع مكن, كلّ بقعة بلاده الستار ٣٠. بـ أسر منتصف تكاليف, أي به، لعدم بالرّغم. ألمّ والقرى البشريةً ٣٠ أما.\n" +
                    "\n" +
                    "جنوب اليها الشّعبين بين و, في ومن نقطة كانتا. بوابة انتصارهم ما بها, ٣٠ كلا بهيئة المتّبعة, لمحاكم معاملة النزاع حدى مع. لغات الجنرال الإتفاقية ذات تم, الشرق، العالم الأوروبية، مع أسر. حدى بسبب تاريخ إعلان ثم, بحث هو ساعة الصفحات.\n" +
                    "\n" +
                    "بالتوقيع اليابانية دول عل, إعلان الأرواح عل كلّ, عل كان شمال الضروري. ثم تسبب تجهيز بها. بـ الا محاولات اليابانية. تكبّد وصافرات الى كل, جعل للإتحاد البولندي أم.\n" +
                    "\n" +
                    "كلّ بحشد واتّجه تكاليف هو. أخذ فشكّل وحلفاؤها ٣٠, أسيا الثالث عرض بـ, أي يتم ترتيب ليرتفع. ومن ٣٠ سياسة الآخر, تم الجو أوسع فرنسا لكل, أي وفي واستمر تغييرات مليارات. شيء دارت اليابان ان. تعديل مليارات عن الا, يكن بل دارت إحكام والنفيس.\n" +
                    "\n" +
                    "حيث ما لإنعدام الأوروبي التقليدية, وقرى الفرنسي عن ذات. بلا أن بتطويق وتزويده بالرّغم. بحث ٣٠ الطرفين الأرواح والكوري, أي جعل مشاركة العسكري. مدن قتيل، المواد بل, ان واستمرت التجارية غير, يبق عن وانهاء والنفيس. ذات جمعت الانجليزية قد, وعُرفت وبعدما أي كلا.\n" +
                    "\n" +
                    "ما مدن هامش وبحلول إستيلاء. أي ضرب قدما للجزر, ضرب و دخول بهيئة عملية. لم أسر حالية أوراقهم الاندونيسية, ان أهّل تاريخ ألمانيا لمّ. ثم بتخصيص والإتحاد ومن. معارضة لليابان بمباركة ما تلك, بها أجزاء للسيطرة أن, بقعة مدينة الوزراء كلا عل.\n" +
                    "\n" +
                    "كل إحكام وسمّيت تحت, لفشل ماليزيا، لمّ ٣٠. ٣٠ إختار سليمان، الباهضة على, حقول إستعمل تكتيكاً كل مدن, بال غضون لغزو الأرضية قد. لعدم قامت وبالرغم ثم ومن. الساحة الإقتصادية دنو بل, وباءت واتّجه العالمي أن ذلك. أن قائمة تحرّكت أخذ, تغييرات لبلجيكا، الأوروبية هو بحق.";

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

            //send that shit
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(arabicStr).queue();
        }

        if (event.getMessage().getContentRaw().equals("my internet is back"))
        {
            //just absolutely own the retard who talks about his internet
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("nobody cares about your internet.").queue();
        }
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
