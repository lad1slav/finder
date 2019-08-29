package en.ladislav.finderapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private String itemId;
    private String itemName;
    private String itemURL;
    private String itemPhotoURL;
    private double itemPrice;
    private String itemSource;
}