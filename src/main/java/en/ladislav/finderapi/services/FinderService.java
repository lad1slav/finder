package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.Filter;

import java.util.List;
import java.util.Set;

public interface FinderService {
    List<ItemDto> find(String findQuery, Filter properties);

    List<ItemDto> find(String findQuery, Filter properties, Set<ParserList> parserIdentifiers);

    List<ItemDto> trim(List<ItemDto> itemDtos, Filter properties);
}
