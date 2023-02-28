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
    ROZETKA_PARSER(new RozetkaParser()),
    PROM_PARSER(new PromParser()),
    COMFY_PARSER(new ComfyParser());

    private final Parser parser;

    public static final List<Parser> allParsers = Lists.newArrayList(COMFY_PARSER.getParser(), PROM_PARSER.getParser());

    public static final Set<ParserList> allParserIdentifiers = Sets.newHashSet(ParserList.values());
}
