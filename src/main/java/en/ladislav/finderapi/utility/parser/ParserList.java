package en.ladislav.finderapi.utility.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public enum ParserList {
    ROZETKA_PARSER(new RozetkaParser());

    private final Parser parser;

    private static final List<Parser> allParsers = Lists.newArrayList(ROZETKA_PARSER.getParser());
}
