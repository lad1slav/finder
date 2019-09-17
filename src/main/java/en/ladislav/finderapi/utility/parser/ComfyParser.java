package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

@Slf4j
public class ComfyParser extends  Parser {

    private static final String COMFY_URL = "https://comfy.ua/";

    ComfyParser() {
        super(COMFY_URL);
    }

    @Override
    public ArrayList<Item> find(String phrase) {
        String findExpressionURL = "catalogsearch/result?q=" + phrase.replace(" ", "+");

        return super.find(findExpressionURL);
    }

    @Override
    Document connect(String url) {
        //robot check
        return super.connect(url);
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
        Document document = this.connect(url + "&p=" + pageNum);

        System.out.println(document.text());

        while (document.getElementsByClass("category__empty").isEmpty()) {
            if(!document.getElementsByClass("search-empty").isEmpty()) {
                log.warn("Nothing was found: [{}]", document.location());
                break;
            }

            log.info("Connected to [{}] [{}] [{}]", document.location(), pageNum, document.title());

            elements.addAll(this.parse(document));

            document = this.connect(url + "&p=" + ++pageNum);
        }

        return elements;
    }

    @Override
    protected Elements getElementsFromPage(Document document) {
        Elements desc = document.getElementsByClass("product-item__i");

        return desc;
    }

    @Override
    protected boolean elementExist(Element element) {
        Element wrapStatus = element.getElementsByClass("js-item-informer-wrap").last();

        if(!wrapStatus.getElementsByClass("sms-notification").isEmpty()) { return false; }

        return true;
    }

    @Override
    protected String getElementName(Element element) {
        Element name = element.getElementsByClass("product-item__name").last();
        name = name.getElementsByTag("a").last();

        return name.ownText();
    }

    @Override
    protected String getElementPrice(Element element) {
        Element price = element.getElementsByClass("product-item__price").last();
        price = price.getElementsByClass("price-value").last();

        return price.ownText();
    }

    @Override
    protected String getElementURL(Element element) {
        Element url = element.getElementsByClass("product-item__name").last();
        url = url.getElementsByTag("a").last();

        return url.attributes().get("href");
    }

    @Override
    protected String getElementPhotoURL(Element element) {
        Element image = element.getElementsByClass("product-item__img").last();
        image = image.getElementsByTag("img").last();

        return image.attributes().get("src");
    }
}
