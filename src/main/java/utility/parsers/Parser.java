package utility.parsers;

import com.sun.deploy.net.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.misc.IOUtils;
import utility.Item;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

public abstract class Parser
{
    public final String URL;

    Parser(String url) {
        this.URL = url;
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

    public abstract Elements parseAllPages(String url);

    public abstract Elements parse(Document document);

    protected abstract Elements getElementsFromPage(Document document);

    protected abstract String getElementName(Element element);

    protected abstract String getElementPrice(Element element);

    protected abstract String getElementURL(Element element);

    protected abstract String getElementPhotoURL(Element element);

    protected abstract boolean elementExist(Element element);
}
