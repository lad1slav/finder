package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.utility.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import en.ladislav.finderapi.soft.Validator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RozetkaParser extends Parser {

    private static final String ROZETKA_URL = "https://rozetka.com.ua/";

    RozetkaParser() {
        super(ROZETKA_URL);
    }

    public ArrayList<Item> find(String phrase) {
        String findExpressionURL = "search/?text=" + phrase.replace(" ", "+");

        return super.find(findExpressionURL);
    }

    @Override
    protected Elements parse(Document document) {
        Elements desc = this.getElementsFromPage(document);

        return desc;
    }

    @Override
    protected Elements parseAllPages(String url) {
        int pageNum = 1;

        Elements elements = new Elements();
        Document document = this.connect(url + "&redirected=1&p=" + pageNum);

        log.info("parsed document:");
        log.info(document.toString());

        while (document != null && pageNum < 10) {
            if(!document.getElementsByClass("search-container-nothing").isEmpty()) {
                //sdfg test

                log.warn("Nothing was found: [{}]", document.location());
                break;
            }

            log.info("Connected to [{}] [{}] [{}]", document.location(), pageNum, document.title());

            elements.addAll(this.parse(document));

            document = this.connect(url + "&redirected=1&p=" + ++pageNum);
        }

        return elements;
    }

    @Override
    public Elements getElementsFromPage(Document document) {
        Elements desc = document.getElementsByClass("content_type_catalog");
        log.info("Size: " + desc.size());

        return desc;
    }

    @Override
    protected Boolean elementExist(Element element) {
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

    @Override
    public Boolean getElementExistFromPage(Document element) {
        Element image = element.getElementsByAttributeValue("data-qaid", "image_link").first();
//        log.info(element.toString());
//        try {
//            Thread.sleep(999999L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        return image == null ? "" : image.children().first().attributes().get("src");
        return true;
    }
}
