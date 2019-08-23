package utility.parsers;

import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
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

    @Override
    protected Document connect(String url) {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\manzhv\\IdeaProjects\\finder\\src\\main\\resources\\geckodriver.exe");
        WebDriver webDriver = new FirefoxDriver();

        try {
            webDriver.navigate().to(this.URL + url);

            return Jsoup.parse(webDriver.getPageSource());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            webDriver.close();
        }
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

        Element price = desc.getElementsByAttributeValue("name", "price").last();
        price = price.getElementsByClass("g-price-uah").last();
        price = price.getElementsByTag("span").first();

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

        elements.addAll(this.parse(document));
//        while (document != null) {
//            System.out.println(pageNum + " || " + document.title());
//
//            elements.addAll(this.parse(document));
//
//            pageNum = 34;
//
//            document = this.connect(url + "&p=" + ++pageNum);
//        }

        return elements;
    }
}
