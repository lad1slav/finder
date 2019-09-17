package en.ladislav.finderapi.mapper;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.model.Item;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDto itemToItemDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> items);
}

