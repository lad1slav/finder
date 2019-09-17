package en.ladislav.finderapi.utility.parser;


import en.ladislav.finderapi.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

@Slf4j
public class PromParser extends Parser {

    private static final String PROM_URL = "https://prom.ua/";

    PromParser() {
        super(PROM_URL);
    }

    @Override
    public ArrayList<Item> find(String phrase) {
        String findExpressionURL = "/search?search_term=" + phrase.replace(" ", "+");

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
        Document document = this.connect(url + "&page=" + pageNum);

        while (document.getElementsByClass("x-empty-results").isEmpty() && pageNum < 17) {
            log.info("Connected to [{}] [{}] [{}]", document.location(), pageNum, document.title());

            elements.addAll(this.parse(document));

            document = this.connect(url + "&page=" + ++pageNum);
        }

        if(elements.isEmpty()) {
            log.warn("Nothing was found: [{}]", document.location());
        }

        return elements;
    }

    @Override
    protected Elements getElementsFromPage(Document document) {
        Elements desc = document.getElementsByClass("x-gallery-tile");

        return desc;
    }

    @Override
    protected boolean elementExist(Element element) {
        Element wrapStatus = element.getElementsByClass("x-product-presence").last();

        if(wrapStatus == null || wrapStatus.attributes().get("class").split(" ").length > 1) { return false; }

        return true;
    }

    @Override
    protected String getElementName(Element element) {
        Element name = element.getElementsByClass("x-gallery-tile__name").last();

        return name.ownText();
    }

    @Override
    protected String getElementPrice(Element element) {
        Element price = element.getElementsByClass("x-gallery-tile__price").last();

        return price == null ? "" : price.attributes().get("data-qaprice");
    }

    @Override
    protected String getElementURL(Element element) {
        Element url = element.getElementsByClass("x-gallery-tile__name").last();

        return url.attributes().get("href");
    }

    @Override
    protected String getElementPhotoURL(Element element) {
        Element image = element.getElementsByClass("x-image-holder__img").last();

        return image.attributes().get("src");
    }
}
