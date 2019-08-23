package utility.parsers;

import exceptions.HttpStatusCodeException;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import soft.URLHelper;
import utility.Finder;
import utility.Item;
import utility.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RozetkaParser extends Parser implements Finder {

    private static final String ROZETKA_URL = "https://rozetka.com.ua/";

    public RozetkaParser() {
        super(ROZETKA_URL);
    }

    @Override
    protected Document connect(String url) {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\manzhv\\IdeaProjects\\finder\\src\\main\\resources\\geckodriver.exe");
        //System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE, "true");
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");

        WebDriver webDriver = new FirefoxDriver();
        String targeUrl = this.URL + url;
        try {
            webDriver.navigate().to(targeUrl);

            int statusCode = URLHelper.getPageResponseCode(webDriver.getCurrentUrl());
            System.out.println(statusCode + " || " + targeUrl);
            if (statusCode == -1 || statusCode == 404) { throw new HttpStatusCodeException("" + statusCode); }

            return Jsoup.parse(webDriver.getPageSource());
        } catch (HttpStatusCodeException e) {
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
        ArrayList<Integer> pageNums = new ArrayList<>();

        Elements elements = new Elements();

//        Document document = this.connect(url + "&p=" + pageNum);

        Parser thisParser = this;

//        while (document != null) {
        while (pageNum < 32) {

            pageNums.add(pageNum);

            pageNum++;
        }

        ExecutorService es = Executors.newCachedThreadPool();
        pageNums.forEach((currPageNum) -> {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    elements.addAll(((RozetkaParser) thisParser).parsePages(url, currPageNum));
                }
            });
        });

        es.shutdown();
        while(true) {
            try {
                if (!!es.awaitTermination(1, TimeUnit.MINUTES)) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        return elements;
    }

    public Elements parsePages(String url, int pageNum) {
        Document document = this.connect(url + "&p=" + pageNum);
        System.out.println(pageNum + " || " + document.title());

        return this.parse(document);
    }

//    new Thread(() -> {
//        elements.addAll(thisParser.parse(document));
//        System.out.println(pageNum + " || " + document.title());
//        document = thisParser.connect(url + "&p=" + ++pageNum);
//    }).start();
}
