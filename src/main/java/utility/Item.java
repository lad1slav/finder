package utility;

import lombok.Getter;
import lombok.Setter;
import soft.Validator;

@Getter
@Setter
public class Item {
    private long itemId;
    private String itemName;
    private String itemDescription;
    private double itemPrice;

    public void setItemId(long itemId) {
        Validator.isNotNegative(itemId);
        this.itemId = itemId;
    }

    public void setItemPrice(double itemPrice) {
        Validator.isNotNegative(itemPrice);
        this.itemPrice = Math.round(itemPrice * 100.0) / 100.0;
    }
}
