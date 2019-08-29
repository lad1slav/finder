package en.ladislav.finderapi.api.controller;

import en.ladislav.finderapi.api.dto.ItemDto;
import en.ladislav.finderapi.services.FinderService;
import en.ladislav.finderapi.utility.parser.ParserList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/find")
@AllArgsConstructor
@Validated
public class FinderController {

    @Autowired
    private FinderService finderService;

    @GetMapping("/{query}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> find(@PathVariable("query") String findQuery) {
        return finderService.find(findQuery);
    }

    @GetMapping("/{query}")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> find(@PathVariable("query") String findQuery,
                              @RequestParam("resources")Set<ParserList> parsers) {
        return finderService.find(findQuery, parsers);
    }
}
