package en.ladislav.finderapi.utility.property;

import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.Item;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;


public class SearchProperties {
    Properties properties = new Properties();

    public String getProperty(PropertyKey key) {
        return this.properties.getProperty(String.valueOf(key));
    }

    public Object setProperty(PropertyKey key, String value) {
        return this.properties.setProperty(String.valueOf(key), value);
    }

    public int size() {
        return this.properties.size();
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    public Enumeration propertyNames() {
        return this.properties.propertyNames();
    }

    public ArrayList<Item> findIn(ArrayList<Item> items) {
        if (this.isEmpty()) { return items; }

        ArrayList<Item> resultItemList = new ArrayList<>(items);

        Enumeration propertyNames = this.propertyNames();
        while (propertyNames.hasMoreElements()) {
            PropertyKey propertyName = PropertyKey.valueOf((String) propertyNames.nextElement());
            String propertyValue = this.getProperty(propertyName);

            switch (propertyName) {
                case MIN_PRICE:
                    items.forEach((item -> {
                        if (item.getItemPrice() < Double.parseDouble(propertyValue)) {
                            resultItemList.remove(item);
                        }
                    }));

                    break;

                case MAX_PRICE:
                    items.forEach((item -> {
                        if (item.getItemPrice() > Double.parseDouble(propertyValue)) {
                            resultItemList.remove(item);
                        }
                    }));

                    break;

                case WITHOUT_SOURCE:
                    String[] sources = propertyValue.split(" ");
                    ArrayList<ParserList> identifiers = new ArrayList<ParserList>();
                    for (String source : sources) { identifiers.add(ParserList.valueOf(source)); }

                    items.forEach((item -> {
                        identifiers.forEach(identifier -> {
                            if (item.getItemSource().equals(identifier.getParser().URL)) {
                                resultItemList.remove(item);
                            }
                        });
                    }));

                    break;
            }
        }

        return resultItemList;
    }
}
