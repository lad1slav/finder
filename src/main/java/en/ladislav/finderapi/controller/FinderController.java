package en.ladislav.finderapi.controller;

import en.ladislav.finderapi.services.FinderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/find")
@AllArgsConstructor
@Validated
public class FinderController {
    @Autowired
    private FinderService finderService;

    @GetMapping("/{findQuery}")
    @ResponseStatus(HttpStatus.OK)
    public String find(@PathVariable("findQuery") String findQuery) {
        //RequestParam Set<ParserList>
        return finderService.find(findQuery);
    }
}
