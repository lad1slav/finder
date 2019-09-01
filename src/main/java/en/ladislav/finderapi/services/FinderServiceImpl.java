package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.api.exception.BadRequestException;
import en.ladislav.finderapi.mapper.ItemMapper;
import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.PropertyKey;
import en.ladislav.finderapi.utility.property.SearchProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FinderServiceImpl implements FinderService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<ItemDto> findIn(List<ItemDto> itemDtos, SearchProperties properties) {
        log.info("Searching for [{}] items", itemDtos.size());
        Long time = System.currentTimeMillis();

        ArrayList<Item> items = new ArrayList<>();
        itemDtos.forEach(itemDto -> {
//            try {
                items.add(new Item(itemDto));
//            } catch (IllegalArgumentException e) {
//                throw new BadRequestException(e.getMessage());
//            }
        });

//        log.info("Wrapped ItemDto to Item: left [{}] items", items.size());

        List<Item> result = properties.findIn(items);

        time = System.currentTimeMillis() - time;
        log.info("Found [{}] items in [{}] ms", result.size(), time);

        return itemMapper.itemsToItemDtos(result);
    }

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
