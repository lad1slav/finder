package en.ladislav.finderapi.api.controller;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.services.FinderService;
import en.ladislav.finderapi.utility.parser.ParserList;
import en.ladislav.finderapi.utility.property.PropertyKey;
import en.ladislav.finderapi.utility.property.SearchProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.util.ArrayList;
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
                              @RequestParam(value = "withSource", required = false) Set<ParserList> parserIdentifiers,
                              @RequestParam(value = "minPrice", required = false) @PositiveOrZero Integer minPrice,
                              @RequestParam(value = "maxPrice", required = false) @PositiveOrZero Integer maxPrice) {

//        try {
//            Document document = Jsoup.connect("https://prom.ua/search?page=1&search_term=miband").get();
//
//            System.out.println(document);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        SearchProperties properties = new SearchProperties();

        if (minPrice != null) { properties.setProperty(PropertyKey.MIN_PRICE, minPrice.toString()); }
//      maxPrice != null && !maxPrice.isEmpty()
        if (maxPrice != null) { properties.setProperty(PropertyKey.MAX_PRICE, maxPrice.toString()); }

        if (parserIdentifiers == null || parserIdentifiers.isEmpty()) {
            return finderService.find(findQuery, properties);
        } else {
            parserIdentifiers.remove(null);

            if (parserIdentifiers == null || parserIdentifiers.isEmpty()) { return finderService.find(findQuery, properties); }

            return finderService.find(findQuery, properties, parserIdentifiers);
        }

//        return new ArrayList<ItemDto>();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> findIn(@Valid @RequestBody List<ItemDto> searchableItems,
                                @RequestParam(value = "withoutSource", required = false) Set<ParserList> parserIdentifiers,
                                @RequestParam(value = "minPrice", required = false) @PositiveOrZero Integer minPrice,
                                @RequestParam(value = "maxPrice", required = false) @PositiveOrZero Integer maxPrice) {

        SearchProperties properties = new SearchProperties();

        if (minPrice != null) { properties.setProperty(PropertyKey.MIN_PRICE, minPrice.toString()); }

        if (maxPrice != null) { properties.setProperty(PropertyKey.MAX_PRICE, maxPrice.toString()); }

        if (parserIdentifiers != null && !parserIdentifiers.isEmpty()) {
            parserIdentifiers.remove(null);

            if (parserIdentifiers != null && !parserIdentifiers.isEmpty()) {
                properties.setProperty(PropertyKey.WITHOUT_SOURCE, "");
                parserIdentifiers.forEach(identifier -> {
                    properties.setProperty(PropertyKey.WITHOUT_SOURCE, properties.getProperty(PropertyKey.WITHOUT_SOURCE) + identifier.toString() + " ");
                });
            }
        }

        return finderService.findIn(searchableItems, properties);
    }

}