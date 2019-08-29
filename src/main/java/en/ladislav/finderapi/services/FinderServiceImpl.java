package en.ladislav.finderapi.services;

import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.Parser;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.parser.RozetkaParser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@Service
public class FinderServiceImpl implements FinderService {

    @Override
    public String find(String findQuery, Set<ParserList> parsers) {
        return null;
    }

    @Override
    public String find(String findQuery) {
        Parser rozetkaParser = ParserList.ROZETKA_PARSER.getParser();

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
