package utility.parsers;

import org.jsoup.select.Elements;
import utility.ParserRealisation;

import javax.swing.text.Document;
import javax.swing.text.Element;

public class RozetkaParserRealisation implements ParserRealisation {
    @Override
    public Elements getElementsFromPage(Document document) {
        return null;
    }

    @Override
    public String getElementName(Element element) {
        return null;
    }

    @Override
    public String getElementPrice(Element element) {
        return null;
    }

    @Override
    public String getElementURL(Element element) {
        return null;
    }

    @Override
    public String getElementPhotoURL(Element element) {
        return null;
    }
}
