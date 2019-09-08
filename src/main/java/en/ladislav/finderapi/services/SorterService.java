package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.utility.property.Filter;
import en.ladislav.finderapi.utility.sort.Sorter;

import java.util.List;

public interface SorterService {
    List<ItemDto> sort(List<ItemDto> itemDtos, Sorter sorter);
}
