package utility.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utility.Finder;
import utility.Item;
import utility.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class RozetkaParser extends Parser implements Finder {

    public static final String URL = "https://rozetka.com.ua/";

    @Override
    public Document connect(String url) {
        try {
            return Jsoup.connect(URL + url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Item> find(String phrase) {


        return null;
    }

    @Override
    public ArrayList<Elements> parse() {
        return null;
    }
}
