package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.config.ThreadConfig;
import en.ladislav.finderapi.utility.Item;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import en.ladislav.finderapi.soft.Validator;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class RozetkaParser extends Parser {

    private static final String ROZETKA_URL = "https://rozetka.com.ua/";

    RozetkaParser() {
        super(ROZETKA_URL);
    }

    public ArrayList<Item> find(String phrase) {
        String findExpressionURL = "search/?text=" + phrase.replace(" ", "+");
        Elements elements = parseAllPages(findExpressionURL);

        ArrayList<Item> items = new ArrayList<Item>();
        elements.forEach(element -> {
            Item item = this.parse(element);

            if (item != null) {
                items.add(item);
            }
        });

        return items;
    }

    @Override
    protected Elements parse(Document document) {
        Elements desc = this.getElementsFromPage(document);

        return desc;
    }

    @Override
    protected Elements parseAllPages(String url) {
        int pageNum = 1;
        ArrayList<String> urls = new ArrayList<>();
        while (pageNum < 33) {
            urls.add(url + "&redirected=1&p=" + pageNum++);
        }

        Elements elements = new Elements();
        ExecutorService executor = Executors.newFixedThreadPool(32);
        RozetkaParser parser = this;

        List<Callable<Elements>> callableTasks = new ArrayList<>();
        urls.forEach((urlPath) -> { callableTasks.add(() -> parser.parsePage(urlPath)); });

        try {
            List<Future<Elements>> futures = executor.invokeAll(callableTasks);
            for (Future future : futures) { elements.addAll((Elements) future.get()); }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return elements;
    }

    @Override
    @Async
    protected Elements parsePage(String url) {
        Elements elements = new Elements();
        Document document = this.connect(url);

        if (document != null) {
            if(!document.getElementsByClass("search-container-nothing").isEmpty()) {
                log.warn("Nothing was found: [{}]", document.location());
            } else {
                log.info("Connected to [{}] [{}]", document.location(), document.title());
                elements.addAll(this.parse(document));
            }
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
