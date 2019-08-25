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
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public abstract class Parser
{
    public final String URL;
    protected final ParserRealisation parserRealisation;

    protected Parser(String url, ParserRealisation parserRealisation) {
        this.URL = url;
        this.parserRealisation = parserRealisation;
    }

    protected Document connect(String url) {
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

    public abstract Elements parseAllPages(String url);

    public abstract Elements parse(Document document);

    public abstract Item parse(Element element);
}
