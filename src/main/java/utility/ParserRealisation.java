package utility;

import org.jsoup.select.Elements;

import javax.swing.text.Document;
import javax.swing.text.Element;

public interface ParserRealisation {
    Elements getElementsFromPage(Document document);

    String getElementName(Element element);

    String getElementPrice(Element element);

    String getElementURL(Element element);

    String getElementPhotoURL(Element element);
}
