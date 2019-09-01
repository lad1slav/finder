package en.ladislav.finderapi.utility;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.utility.parser.ParserList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Item {
    @EqualsAndHashCode.Exclude
    private String itemId;
    @Setter
    private String itemName;
    private String itemURL;
    @Setter
    private String itemPhotoURL;
    private double itemPrice;
    @Setter
    private String itemSource;

    public void setItemPrice(double itemPrice) {
        Validator.isNotNegative(itemPrice);
        this.itemPrice = Math.round(itemPrice * 100.0) / 100.0;
    }

    public void setItemURL(String itemURL) {
        Validator.isCorrectURL(itemURL);
        this.itemURL = itemURL;
    }

    public Item(String itemName, String itemURL, String itemPhotoURL, double itemPrice, String itemSource) {
        this.itemId = UUID.randomUUID().toString();

        this.setItemName(itemName);
        this.setItemURL(itemURL);
        this.setItemPhotoURL(itemPhotoURL);
        this.setItemPrice(itemPrice);
        this.setItemSource(itemSource);
    }

    public Item(ItemDto itemDto) {
        this.itemId = itemDto.getItemId();

        this.setItemName(itemDto.getItemName());
        this.setItemPrice(itemDto.getItemPrice());
        this.setItemURL(itemDto.getItemURL());
        this.setItemPhotoURL(itemDto.getItemPhotoURL());
        this.setItemSource(itemDto.getItemSource());
    }

    public Item() {
        this.itemId = UUID.randomUUID().toString();
    }
}
