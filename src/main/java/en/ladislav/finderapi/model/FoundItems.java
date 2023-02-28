package en.ladislav.finderapi.model;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.utility.Item;
import lombok.*;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FoundItems {
    Long chatId;
    String query;
    Integer messageToDel;
    List<ItemDto> foundItems;
    Double avgPrice;
    Boolean isSubscribed;
}
