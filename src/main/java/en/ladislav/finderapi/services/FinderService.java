package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.utility.Item;
import en.ladislav.finderapi.utility.parser.ParserList;

import java.util.List;
import java.util.Set;

public interface FinderService {
    List<ItemDto> find(String findQuery);

    List<ItemDto> find(String findQuery, Set<ParserList> parsers);
}
