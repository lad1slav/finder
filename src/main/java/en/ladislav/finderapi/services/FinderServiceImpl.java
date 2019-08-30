package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.mapper.ItemMapper;
import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.SearchProperties;
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
    public List<ItemDto> find(String findQuery, SearchProperties properties, Set<ParserList> parserIdentifiers) {
        ArrayList<Item> result = new ArrayList<>();

        log.info("Searching for query \"{}\"", findQuery);
        Long time = System.currentTimeMillis();

        parserIdentifiers.forEach(identifier -> {
            result.addAll(identifier.getParser().find(findQuery));
        });

        log.info("Found {} items", result.size());

        time = System.currentTimeMillis() - time;
        log.info("Searching for query \"{}\" was finished in {} ms", findQuery, time);

        return itemMapper.itemsToItemDtos(properties.findIn(result));
    }

    @Override
    public List<ItemDto> find(String findQuery, SearchProperties properties) {
        return this.find(findQuery, properties, ParserList.allParserIdentifiers);
    }
}
