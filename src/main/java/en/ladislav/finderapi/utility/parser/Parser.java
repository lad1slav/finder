package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.utility.Finder;
import en.ladislav.finderapi.utility.Item;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

@Slf4j
public abstract class Parser implements Finder
{
    public final String URL;

    Parser(String url) {
        this.URL = url;
    }

    @Override
    public ArrayList<Item> find(String phrase) {
        Elements elements = parseAllPages(phrase);

        ArrayList<Item> items = new ArrayList<Item>();
        elements.forEach(element -> {
            try {
                Item item = this.parse(element);

                if (item != null) {
                    items.add(item);
                }
            } catch (Exception e) {
                log.error("{}", e);
            }
        });

        return items;
    }

    Document connect(String url) {
        try {
            log.info("Connecting to [{}]", this.URL + url);

            return Jsoup.connect(this.URL + url).get();
        } catch (HttpStatusException e) {
            log.warn("{}", e.toString());

            return null;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            log.error("{}", e);

          //bad request timeout connection inspect this || return new Document();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    Item parse(Element element) {
        if (!this.elementExist(element)) { return null; }

        Item item = new Item();

        try {
            item.setItemName(this.getElementName(element));
            item.setItemPrice(Double.parseDouble(this.getElementPrice(element)));
            item.setItemURL(this.getElementURL(element));
            item.setItemPhotoURL(this.getElementPhotoURL(element));
            item.setItemSource(this.URL);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

            return null;
        }

        return item;
    }

    protected abstract Elements parseAllPages(String url);

    protected abstract Elements parse(Document document);

    protected abstract Elements getElementsFromPage(Document document);

    protected abstract String getElementName(Element element);

    protected abstract String getElementPrice(Element element);

    protected abstract String getElementURL(Element element);

    protected abstract String getElementPhotoURL(Element element);

    protected abstract boolean elementExist(Element element);
}
