package en.ladislav.finderapi.utility.parser;


import en.ladislav.finderapi.utility.Item;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Locale;

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

        while (document.getElementsByClass("x-empty-results").isEmpty() && pageNum < 2) {
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
    public Elements getElementsFromPage(Document document) {
        Elements desc = document.getElementsByClass("js-productad");

        return desc;
    }

    @Override
    protected Boolean elementExist(Element element) {
        Element wrapStatus = element.getElementsByAttributeValue("data-qaid","product_presence").first();

        if(wrapStatus == null || (!wrapStatus.text().equals("В наявності") && !wrapStatus.text().equals("Закінчується")
        && !wrapStatus.text().toLowerCase().contains("доставка"))) { return false; }

        return true;
    }

    @Override
    protected String getElementName(Element element) {
        Element name = element.getElementsByAttributeValue("data-qaid", "product_name").last();

        return name.text();
    }

    @Override
    protected String getElementPrice(Element element) {
        Element price = element.getElementsByAttributeValue("data-qaid", "product_price").last();

        return price == null ? "" : price.attributes().get("data-qaprice");
    }

    @Override
    protected String getElementURL(Element element) {
        Element url = element.getElementsByClass("js-productad").first();

        return url == null ? "" : url.attributes().get("data-advtracking-click-url");
    }

    @Override
    protected String getElementPhotoURL(Element element) {
        Element image = element.getElementsByAttributeValue("data-qaid", "image_link").first();
//        log.info(element.toString());
//        try {
//            Thread.sleep(999999L);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return image == null ? "" : image.children().first().attributes().get("src");
    }

    @Override
    public Boolean getElementExistFromPage(Document element) {
        Element existStatus = element.getElementsByAttributeValue("data-qaid", "main_product_info").first();

        return this.elementExist(existStatus);
    }
}
