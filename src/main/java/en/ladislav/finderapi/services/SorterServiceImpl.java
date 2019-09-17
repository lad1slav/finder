package en.ladislav.finderapi.services;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.mapper.ItemMapper;
import en.ladislav.finderapi.model.Item;
import en.ladislav.finderapi.utility.sort.Sorter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SorterServiceImpl implements SorterService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
    public List<ItemDto> sort(List<ItemDto> itemDtos, Sorter sorter) {
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

        List<Item> result = sorter.sort(items);

        time = System.currentTimeMillis() - time;
        log.info("Sorted [{}] items in [{}] ms", result.size(), time);

        return itemMapper.itemsToItemDtos(result);
    }
}
