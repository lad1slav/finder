package utility.parsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import soft.Validator;
import utility.Finder;
import utility.Item;
import utility.Parser;
import utility.ParserRealisation;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RozetkaParser extends Parser implements Finder {

    private static final String ROZETKA_URL = "https://rozetka.com.ua/";

    private static final ParserRealisation ROZETKA_PARSERREALISATION = null;

    public RozetkaParser() {
        super(ROZETKA_URL, ROZETKA_PARSERREALISATION);
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

        return items;
    }

    @Override
    public Item parse(Element element) {
        Item item = new Item();

        Element name = element.getElementsByClass("g-i-tile-i-title").last();
        name = name.getElementsByTag("a").last();


        Element image = element.getElementsByClass("g-i-tile-i-image").last();
        image = image.getElementsByTag("img").last();

        Element price = element.getElementsByAttributeValue("name", "prices_active_element_original").last();
        price = price.getElementsByTag("script").last();

        String firstStr = price.html().split("\n")[0];
        Pattern pattern = Pattern.compile("(?:%22price%22:)([0-9]*\\.?[0-9]+)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(firstStr);
        matcher.find();
        String p = matcher.group(1);

        System.out.println(p);
        System.out.println(name.ownText());
        System.out.println(name.attributes().get("href"));
        System.out.println(image.attributes().get("data-rz-lazy-load-src"));
        System.out.println(image.attributes().get("src"));

        try {
            item.setItemName(name.ownText());
            item.setItemPrice(Double.parseDouble(p));
            item.setItemURL(name.attributes().get("href"));
            try {
                Validator.isCorrectURL(image.attributes().get("data-rz-lazy-load-src"));
                item.setItemPhotoURL(image.attributes().get("data-rz-lazy-load-src"));
            } catch (IllegalArgumentException e) {
                item.setItemPhotoURL(image.attributes().get("src"));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

            return null;
        }

        return item;
    }

    @Override
    public Elements parse(Document document) {
        Elements desc = document.getElementsByClass("g-i-tile-i-box-desc");

        return desc;
    }

    @Override
    public Elements parseAllPages(String url) {
        int pageNum = 1;

        Elements elements = new Elements();
        Document document = this.connect(url + "&redirected=1&p=" + pageNum);

        while (document != null) {
            if(!document.getElementsByClass("search-container-nothing").isEmpty()) { break; }

            System.out.println(pageNum + " || " + document.title());

            elements.addAll(this.parse(document));

            document = this.connect(url + "&redirected=1&p=" + ++pageNum);
        }

        return elements;
    }
}
