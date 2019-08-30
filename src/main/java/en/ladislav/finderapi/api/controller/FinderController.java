package en.ladislav.finderapi.api.controller;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.services.FinderService;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.PropertyKey;
import en.ladislav.finderapi.utility.property.SearchProperties;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/find")
@Slf4j
@AllArgsConstructor
@Validated
public class FinderController {

    @Autowired
    private FinderService finderService;

    @GetMapping("/{query}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> find(@PathVariable("query") String findQuery,
                              @RequestParam(value = "resources", required = false) Set<ParserList> parserIdentifiers,
                              @RequestParam(value = "minPrice", required = false) @PositiveOrZero Integer minPrice,
                              @RequestParam(value = "maxPrice", required = false) @PositiveOrZero Integer maxPrice) {
        SearchProperties properties = new SearchProperties();

        if (minPrice != null) { properties.setProperty(PropertyKey.MIN_PRICE, minPrice.toString()); }
//      maxPrice != null && !maxPrice.isEmpty()
        if (maxPrice != null) { properties.setProperty(PropertyKey.MAX_PRICE, maxPrice.toString()); }

        if (parserIdentifiers == null || parserIdentifiers.isEmpty()) {
            return finderService.find(findQuery, properties);
        } else {
            parserIdentifiers.remove(null);

            return finderService.find(findQuery, properties, parserIdentifiers);
        }
    }
}