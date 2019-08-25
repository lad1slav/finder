import utility.Item;
import utility.parsers.RozetkaParser;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("starting...");

        Item i = new Item();
        i.setItemPrice(3.65558);
        System.out.println(i.getItemPrice());

        RozetkaParser rozetkaParser = new RozetkaParser();

        Date startDate = new Date();
        Long time = System.currentTimeMillis();

        ArrayList<Item> result = rozetkaParser.find("miband");
        System.out.println(result.size());

        Date endDate = new Date();
        time = System.currentTimeMillis() - time;
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(time);
    }
}
