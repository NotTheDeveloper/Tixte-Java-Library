import dev.blocky.library.tixte.internal.RawResponseData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends RawResponseData
{

    public static void main(String[] args)
    {
        Pattern pattern = Pattern.compile("^([a-zA-Z\\d_-])+.([a-zA-Z\\d_-])+.([a-zA-Z\\d])+$");
        Matcher matcher = pattern.matcher("lol-.cool.lol");

        if (matcher.find())
        {
            System.out.println("found");
        }
        else
        {
            System.out.println("not found");
        }
    }
}
