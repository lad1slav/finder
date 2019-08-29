package en.ladislav.finderapi.services;

import en.ladislav.finderapi.utility.parser.ParserList;

import java.util.Set;

public interface FinderService {
    String find(String findQuery);

    String find(String findQuery, Set<ParserList> parsers);
}
