package en.ladislav.finderapi.api.controller;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.services.SorterService;
import en.ladislav.finderapi.utility.sort.SortKey;
import en.ladislav.finderapi.utility.sort.Sorter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/sort")
@Slf4j
@AllArgsConstructor
@Validated
public class SorterController {

    @Autowired
    private SorterService sorterService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> sort(@Valid @RequestBody List<ItemDto> searchableItems,
                              @RequestParam(value = "property") @NotNull SortKey property) {

        return sorterService.sort(searchableItems, new Sorter(property));
    }

}
