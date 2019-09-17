package en.ladislav.finderapi.model;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.soft.Validator;
import en.ladislav.finderapi.utility.parser.ParserList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@Entity
@NoArgsConstructor
@Table(name = "ITEM")
public class Item {
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint", nullable = false)
    private long itemId;

    @Setter
    @Column(name = "NAME", length = 512, nullable = false)
    private String itemName;

    @Column(name = "URL", length = 512, nullable = false)
    private String itemURL;

    @Setter
    @Column(name = "PHOTO_URL", length = 512)
    private String itemPhotoURL;

    @Column(name = "PRICE", columnDefinition = "money", nullable = false)
    private double itemPrice;

    @Setter
    @Column(name = "SOURCE", length = 512, nullable = false)
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
//        this.itemId = UUID.randomUUID().toString();

        this.setItemName(itemName);
        this.setItemURL(itemURL);
        this.setItemPhotoURL(itemPhotoURL);
        this.setItemPrice(itemPrice);
        this.setItemSource(itemSource);
    }

    public Item(ItemDto itemDto) {
//        this.itemId = itemDto.getItemId();

        this.setItemName(itemDto.getItemName());
        this.setItemPrice(itemDto.getItemPrice());
        this.setItemURL(itemDto.getItemURL());
        this.setItemPhotoURL(itemDto.getItemPhotoURL());
        this.setItemSource(itemDto.getItemSource());
    }

//    public Item() {
//        this.itemId = UUID.randomUUID().toString();
//    }
}
