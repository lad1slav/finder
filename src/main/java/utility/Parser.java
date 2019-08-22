package utility;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public abstract class Parser
{
    public final String URL;

    protected Parser(String url) {
        URL = url;
    }

    protected Document connect(String url) {
        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            HtmlPage myPage = webClient.getPage(this.URL + url);

            try{
                FileWriter fw=new FileWriter("testout.txt");
                fw.write(myPage.asXml());
                fw.close();
            }catch(Exception e){System.out.println(e);}

            return Jsoup.parse(myPage.asXml());
        } catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();

            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract Elements parseAllPages(String url);

    public abstract Elements parse(Document document);

    public abstract Item parse(Element element);
}
