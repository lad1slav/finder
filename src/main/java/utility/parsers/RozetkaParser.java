package utility.parsers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import soft.Validator;
import utility.Finder;
import utility.Item;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Item item = this.parse(element);

            if (item != null) {
                items.add(item);
            }
        });

        return items;
    }

    @Override
    public Elements parse(Document document) {
        Elements desc = this.getElementsFromPage(document);

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

    @Override
    protected Elements getElementsFromPage(Document document) {
        Elements desc = document.getElementsByClass("g-i-tile-i-box-desc");

        return desc;
    }

    @Override
    protected boolean elementExist(Element element) {
        Element wrapStatus = element.getElementsByClass("g-i-status-wrap").last();

        if(!wrapStatus.getElementsByClass("unavailable").isEmpty()) { return false; }

        return true;
    }

    @Override
    protected String getElementName(Element element) {
        Element name = element.getElementsByClass("g-i-tile-i-title").last();
        name = name.getElementsByTag("a").last();

        return name.ownText();
    }

    @Override
    protected String getElementPrice(Element element) {
        Element price = element.getElementsByAttributeValue("name", "prices_active_element_original").last();
        price = price.getElementsByTag("script").last();

        Pattern pattern = Pattern.compile("(?:%22price%22:)([0-9]*\\.?[0-9]+)", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(price.html().split("\n")[0]);
        matcher.find();

        return matcher.group(1);
    }

    @Override
    protected String getElementURL(Element element) {
        Element url = element.getElementsByClass("g-i-tile-i-title").last();
        url = url.getElementsByTag("a").last();

        return url.attributes().get("href");
    }

    @Override
    protected String getElementPhotoURL(Element element) {
        Element image = element.getElementsByClass("g-i-tile-i-image").last();
        image = image.getElementsByTag("img").last();
        try {
            String lazyLoadSrc = image.attributes().get("data-rz-lazy-load-src");
            Validator.isCorrectURL(lazyLoadSrc);
            return lazyLoadSrc;
        } catch (IllegalArgumentException e) {
            return image.attributes().get("src");
        }
    }
}
