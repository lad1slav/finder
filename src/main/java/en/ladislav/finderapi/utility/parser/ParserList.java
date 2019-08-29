package en.ladislav.finderapi.utility.parser;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum ParserList {
    ROZETKA_PARSER(new RozetkaParser());

    private final Parser parser;

    public static final List<Parser> allParsers = Lists.newArrayList(ROZETKA_PARSER.getParser());

    public static final Set<ParserList> allParsersIdentifier = Sets.newHashSet(ParserList.values());
}