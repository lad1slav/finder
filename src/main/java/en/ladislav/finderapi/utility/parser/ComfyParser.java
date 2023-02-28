package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.utility.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Slf4j
public class ComfyParser extends  Parser {

    private static final String COMFY_URL = "https://comfy.ua/";

    ComfyParser() {
        super(COMFY_URL);
    }

    @Override
    public ArrayList<Item> find(String phrase) {
        String findExpressionURL = "search/cat_120/?q=" + phrase.replace(" ", "+");

        return super.find(findExpressionURL);
    }

    @Override
    public Document connect(String url) {
        //robot check
        return super.connect(url);
    }

    @Override
    protected Elements parse(Document document) {
        Elements desc = this.getElementsFromPage(document);

//        log.info(desc.toString());
//        try {
//            Thread.sleep(999999L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return desc;
    }

    @Override
    protected Elements parseAllPages(String url) {
        int pageNum = 1;

        Elements elements = new Elements();
        Document document = this.connect(url);
        if (!isNull(document)) {

//        System.out.println(document.text());

//        while (document.getElementsByClass("category__empty").isEmpty()) {
//            if(!document.getElementsByClass("search-empty").isEmpty()) {
//                log.warn("Nothing was found: [{}]", document.location());
//                break;
//            }

            log.info("Connected to [{}] [{}] [{}]", document.location(), pageNum, document.title());

            elements.addAll(this.parse(document));

//            document = this.connect(url + "&p=" + ++pageNum);
//        }
        }

        return elements;
    }

    @Override
    public Elements getElementsFromPage(Document document) {
        Elements desc = document.getElementsByClass("products-list-item");

        return desc;
    }

    @Override
    protected Boolean elementExist(Element element) {
        Element wrapStatus = element.getElementsByClass("products-list-item__annotation-title").last();

        if(!isNull(wrapStatus) && wrapStatus.text().contains("Товар закончился")) { return false; }

        return true;
    }

    @Override
    protected String getElementName(Element element) {
        Element name = element.getElementsByClass("products-list-item__info").first();
        name = name.getElementsByTag("a").first();

        return name.ownText();
    }

    @Override
    protected String getElementPrice(Element element) {
        Element price = element.getElementsByClass("products-list-item__actions-price-current").last();

        return price.ownText().replace(" ", "");
    }

    @Override
    protected String getElementURL(Element element) {
        Element url = element.getElementsByClass("products-list-item__info").first();
        url = url.getElementsByTag("a").last();

        return url.attributes().get("href");
    }

    @Override
    protected String getElementPhotoURL(Element element) {
        Element image = element.getElementsByClass("products-list-item__img").last();
        image = image.getElementsByTag("img").last();

        return image.attributes().get("src");
    }

    @Override
    public Boolean getElementExistFromPage(Document element) {
        Element wrapStatus = element.getElementsByClass("products-list-item__annotation-title").last();

        if(!isNull(wrapStatus) && wrapStatus.text().contains("Товар закончился")) { return false; }

        return true;
    }
}
