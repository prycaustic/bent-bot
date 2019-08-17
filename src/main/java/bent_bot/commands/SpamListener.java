package bent_bot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class SpamListener extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        if (!event.getAuthor().isBot() && getSpam() != null)
        {
                event.getChannel().sendMessage(getSpam()).queue();
        }

        //internet noob
        if (event.getMessage().getContentRaw().equals("my internet is back"))
        {
            //just absolutely own the retard who talks about his internet
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("nobody cares about your internet.").queue();
        }
    }

    private static String getSpam()
    {
        Random random = new Random();

        if (random.nextDouble() < .002)
        {
            int rand = random.nextInt(2);

            if (rand == 1)
            {
                String arabicMess = "ما ألمّ التبرعات الى, جُل لم ليبين الصينية الجنوبي, لم بال السفن حاملات. تم قبل يطول التاريخ،. يكن وانتهاءً الرئيسية الأمريكية في. إعلان انذار العسكري أخر ثم. كل المسرح الساحل دول, وتم كردة استبدال الأعمال عل, وتم تجهيز العصبة قد.\n" +
                        "ثم على بوابة العسكري لتقليعة, انه قد غضون ترتيب البرية. ٣٠ الله باستخدام بحث, مع إعمار لهيمنة نفس. تنفّس والتي بحث ثم, الثقيل تزامناً قد مما. وبغطاء الهجوم لم لها, ان حدى نقطة سياسة سليمان،. عل فهرست بولندا، والنرويج كلّ, والعتاد وبلجيكا، ومن و. بلاده لهيمنة قد غير, ويتّفق مقاطعة الوزراء مع الا, بشكل لفشل العالمية مما في.\n" +
                        "دون بريطانيا بالمطالبة ٣٠, أفاق الهادي ثم دول. وقوعها، المبرمة ٣٠ جهة, وشعار الخاصّة بالسيطرة دار أن. أي فبعد الحرة الشتاء دول, مكن إذ ونتج شعار الأرض. أخر هو بحشد والمعدات. دون تنفّس وتنصيب ثم.\n" +
                        "تمهيد للجزر يتم ان, وبداية واستمر مكثّفة يتم إذ. يعبأ سليمان، الجنوبي كل دنو, عُقر إستعمل الضغوط لها إذ, بداية علاقة شيء كل. عل فقد وبعد العمليات, مع هناك أصقاع مكن, كلّ بقعة بلاده الستار ٣٠. بـ أسر منتصف تكاليف, أي به، لعدم بالرّغم. ألمّ والقرى البشريةً ٣٠ أما.\n" +
                        "جنوب اليها الشّعبين بين و, في ومن نقطة كانتا. بوابة انتصارهم ما بها, ٣٠ كلا بهيئة المتّبعة, لمحاكم معاملة النزاع حدى مع. لغات الجنرال الإتفاقية ذات تم, الشرق، العالم الأوروبية، مع أسر. حدى بسبب تاريخ إعلان ثم, بحث هو ساعة الصفحات.\n";

                String[] arabicArray = arabicMess.split(" ");

                //shuffle the array
                arabicArray = shuffleArray(arabicArray);

                //remove some random entries
                for (int i = 0; i < arabicArray.length; i++)
                {
                    if (random.nextDouble() < .2)
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
                return arabicStr;
            }
            else if (rand == 0)
            {
                String chinaSpam1 = "I love China :flag_cn: Hong Kong is part of China forever :flag_cn: 相信国家 反对暴力 希望中国香港平安:flag_cn:";
                String chinaSpam2 = "动态网自由门 天安門 天安门 法輪功 李洪志 Free Tibet 六四天安門事件 The Tiananmen Square protests of 1989 天安門大屠殺 " +
                        "The Tiananmen Square Massacre 反右派鬥爭 The Anti-Rightist Struggle 大躍進政策 The Great Leap Forward 文化大革命 " +
                        "The Great Proletarian Cultural Revolution 人權 Human Rights 民運 Democratization 自由 Freedom 獨立 Independence 多黨制 " +
                        "Multi-party system 台灣 臺灣 Taiwan Formosa 中華民國 Republic of China 西藏 土伯特 唐古特 Tibet 達賴喇嘛 Dalai Lama 法輪功 " +
                        "Falun Dafa 新疆維吾爾自治區 The Xinjiang Uyghur Autonomous Region 諾貝爾和平獎 Nobel Peace Prize 劉暁波 Liu Xiaobo " +
                        "民主 言論 思想 反共 反革命 抗議 運動 騷亂 暴亂 騷擾 擾亂 抗暴 平反 維權 示威游行 李洪志 法輪大法 大法弟子 強制斷種 強制堕胎 " +
                        "民族淨化 人體實驗 肅清 胡耀邦 趙紫陽 魏京生 王丹 還政於民 和平演變 激流中國 北京之春 大紀元時報 九評論共産黨 獨裁 專制 壓制 統一 " +
                        "監視 鎮壓 迫害 侵略 掠奪 破壞 拷問 屠殺 活摘器官 誘拐 買賣人口 遊進 走私 毒品 賣淫 春畫 賭博 六合彩 天安門 天安门 法輪功 李洪志 " +
                        "Winnie the Pooh 劉曉波动态网自由门";

                if (random.nextBoolean())
                    return chinaSpam1;
                else
                    return chinaSpam2;
            }
        }
        return null;
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
