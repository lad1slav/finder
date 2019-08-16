package utility;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public abstract class Parser
{
    public abstract ArrayList<Elements> parse();

    public abstract Document connect(String url);
}
