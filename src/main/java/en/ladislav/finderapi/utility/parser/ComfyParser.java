package en.ladislav.finderapi.utility.parser;

import en.ladislav.finderapi.utility.Item;
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
        return null;
    }

    @Override
    Document connect(String url) {
        return super.connect(url);
    }

    @Override
    Item parse(Element element) {
        return super.parse(element);
    }

    @Override
    protected Elements parseAllPages(String url) {
        return null;
    }

    @Override
    protected Elements parse(Document document) {
        return null;
    }

    @Override
    protected Elements getElementsFromPage(Document document) {
        return null;
    }

    @Override
    protected String getElementName(Element element) {
        return null;
    }

    @Override
    protected String getElementPrice(Element element) {
        return null;
    }

    @Override
    protected String getElementURL(Element element) {
        return null;
    }

    @Override
    protected String getElementPhotoURL(Element element) {
        return null;
    }

    @Override
    protected boolean elementExist(Element element) {
        return false;
    }
}
