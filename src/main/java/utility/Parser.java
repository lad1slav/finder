package utility;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Parser
{
    public final String URL;

    protected Parser(String url) {
        URL = url;
    }

    protected Document connect(String url) {
        try {
            return Jsoup.connect(URL + url).get();
        } catch (HttpStatusException e) {
            e.printStackTrace();

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract Elements parseAllPages(String url);

    public abstract Elements parse(Document document);

    public abstract Item parse(Element element);
}
