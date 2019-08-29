package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.mapper.ItemMapper;
import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.Parser;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.parser.RozetkaParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FinderServiceImpl implements FinderService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<ItemDto> find(String findQuery, Set<ParserList> parsers) {
        return null;
    }

    @Override
    public List<ItemDto> find(String findQuery) {
        Parser rozetkaParser = ParserList.ROZETKA_PARSER.getParser();

        log.info("Searching for query \"{}\"", findQuery);
        Long time = System.currentTimeMillis();

        ArrayList<Item> result = rozetkaParser.find(findQuery);
        log.info("Found {} items", result.size());

        time = System.currentTimeMillis() - time;
        log.info("Searching for query \"{}\" was finished in {} ms", findQuery, time);

        return itemMapper.itemsToItemDtos(result);
    }
}
