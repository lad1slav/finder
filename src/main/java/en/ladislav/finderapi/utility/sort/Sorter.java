package en.ladislav.finderapi.utility.sort;

import en.ladislav.finderapi.model.Item;

import java.util.*;

public class Sorter {
    SortKey property;

    public Sorter(SortKey property) {
        this.property = property;
    }

    public ArrayList<Item> sort(ArrayList<Item> items) {
        if (property == null) { return items; }

        ArrayList<Item> resultItemList = new ArrayList<>(items);

        switch (property) {
            case PRICE:
                resultItemList.sort(Comparator.comparing(Item::getItemPrice));

                break;

            case REVERSE_PRICE:
                resultItemList.sort(Comparator.comparing(Item::getItemPrice).reversed());

                break;

            case ALPHABET:
                resultItemList.sort(Comparator.comparing(Item::getItemName));

                break;

            case REVERSE_ALPHABET:
                resultItemList.sort(Comparator.comparing(Item::getItemName).reversed());

                break;
        }

        return resultItemList;
    }
}
