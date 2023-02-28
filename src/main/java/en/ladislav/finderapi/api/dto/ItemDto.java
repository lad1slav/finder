package en.ladislav.finderapi.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ItemDto {
    @NotBlank
    private Integer itemId;
    @NotBlank
    private String itemName;
    @NotBlank
    @URL
    private String itemURL;
    @NotNull
    private String itemPhotoURL;
    @NotNull
    @PositiveOrZero
    private double itemPrice;
    @NotBlank
    private String itemSource;
    private Boolean itemPresence;
}
