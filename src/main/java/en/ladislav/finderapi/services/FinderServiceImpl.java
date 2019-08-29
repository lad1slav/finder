package en.ladislav.finderapi.services;

import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.RozetkaParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class FinderServiceImpl implements FinderService {
    @Override
    public String find(String findQuery) {
        System.out.println("starting...");

        RozetkaParser rozetkaParser = new RozetkaParser();

        Date startDate = new Date();
        Long time = System.currentTimeMillis();

        ArrayList<Item> result = rozetkaParser.find(findQuery);
        System.out.println(result.size());

        Date endDate = new Date();
        time = System.currentTimeMillis() - time;
        System.out.println(startDate);
        System.out.println(endDate);
        System.out.println(time);

        return String.valueOf(result.size());
    }
}
