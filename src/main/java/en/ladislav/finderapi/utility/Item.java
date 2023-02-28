package en.ladislav.finderapi.utility;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.utility.parser.ParserList;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@AllArgsConstructor
@Table("item")
@Getter
@EqualsAndHashCode
public class Item {
    @Id
    @Column("id")
    @EqualsAndHashCode.Exclude
    private Integer itemId;
    @Column("name")
    @Setter
    private String itemName;
    @Column("url")
    private String itemURL;
    @Column("photo_url")
    @Setter
    private String itemPhotoURL;
    @Column("price")
    private double itemPrice;
    @Column("company")
    @Setter
    private String itemSource;
    @Column("is_presence")
    @Setter
    private Boolean itemPresence;
    @Column("query")
    private Integer query;

    public void setItemPrice(double itemPrice) {
//        Validator.isNotNegative(itemPrice);
        this.itemPrice = Math.round(itemPrice * 100.0) / 100.0;
    }

    public void setItemURL(String itemURL) {
//        Validator.isCorrectURL(itemURL);
        this.itemURL = itemURL;
    }

    public Item(String itemName, String itemURL, String itemPhotoURL, double itemPrice, String itemSource, Boolean itemPresence) {
//        this.itemId = UUID.randomUUID().toString();

        this.setItemName(itemName);
        this.setItemURL(itemURL);
        this.setItemPhotoURL(itemPhotoURL);
        this.setItemPrice(itemPrice);
        this.setItemSource(itemSource);
        this.setItemPresence(itemPresence);
    }

    public Item(ItemDto itemDto) {
        this.itemId = itemDto.getItemId();

        this.setItemName(itemDto.getItemName());
        this.setItemPrice(itemDto.getItemPrice());
        this.setItemURL(itemDto.getItemURL());
        this.setItemPhotoURL(itemDto.getItemPhotoURL());
        this.setItemSource(itemDto.getItemSource());
        this.setItemPresence(itemDto.getItemPresence());
    }

    public Item() {
//        this.itemId = UUID.randomUUID().toString();
    }
}
