package utility.parsers;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.Finder;
import utility.Item;
import utility.Parser;

import java.io.IOException;
import java.util.ArrayList;

public class RozetkaParser extends Parser implements Finder {

    private static final String ROZETKA_URL = "https://rozetka.com.ua/";

    public RozetkaParser() {
        super(ROZETKA_URL);
    }

    public ArrayList<Item> find(String phrase) {
        String findExpressionURL = "search/?text=" + phrase.replace(" ", "+");
        Elements elements = parseAllPages(findExpressionURL);

        ArrayList<Item> items = new ArrayList<>();
        elements.forEach(element -> {
            Item item = parse(element);

            if (item != null) {
                items.add(item);
            }
        });

        return null;
    }

    @Override
    public Item parse(Element element) {
        Item item = new Item();

        Element desc = element.getElementsByClass("g-i-tile-i-box-desc").last();

        Element name = desc.getElementsByClass("g-i-tile-i-title").last();
        name = name.getElementsByTag("a").last();

        Element price = desc.getElementsByAttributeValue("name", "prices_active_element_original").last();
        price = price.getElementsByTag("script").first();

//        Element price = desc.getElementsByAttributeValue("name", "price").last();
//        price = price.getElementsByClass("g-price-uah").last();
//        price = price.getElementsByTag("span").first();

        System.out.println(name.ownText());
        System.out.println(price.ownText());

        return null;
    }

    @Override
    public Elements parse(Document document) {
        Elements searchList = document.getElementsByAttributeValue("name", "search_list");
        Elements foundItems = searchList.last().getElementsByAttributeValue("data-location", "SearchResults");

        return foundItems;
    }

    @Override
    public Elements parseAllPages(String url) {
        int pageNum = 1;

        Elements elements = new Elements();
        Document document = this.connect(url + "&p=" + pageNum);

        while (document != null) {
            System.out.println(pageNum + " || " + document.title());

            elements.addAll(this.parse(document));

            document = this.connect(url + "&p=" + ++pageNum);
        }

        return elements;
    }
}
