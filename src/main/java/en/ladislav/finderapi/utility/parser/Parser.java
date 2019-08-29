package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.utility.Finder;
import en.ladislav.finderapi.utility.Item;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;

public abstract class Parser implements Finder
{
    public final String URL;
    public final ParserList IDENTIFIER;

    Parser(String url, ParserList identifier) {
        this.URL = url;
        this.IDENTIFIER = identifier;
    }

    Document connect(String url) {
        try {
            System.out.println(this.URL + url);

            return Jsoup.connect(this.URL + url).get();
        } catch (HttpStatusException e) {
            e.printStackTrace();

            return null;
        } catch (SocketTimeoutException e) {
          e.printStackTrace();

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
