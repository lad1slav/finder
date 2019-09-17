package en.ladislav.finderapi.utility;

import en.ladislav.finderapi.model.Item;

import java.util.ArrayList;

public interface Finder {
    ArrayList<Item> find(String phrase);
}
