package org.unq.parser.lleca.grammar.lleca.parser;


import org.unq.parser.lleca.grammar.lleca.model.Expansion;
import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.model.Keyword;
import org.unq.parser.lleca.grammar.lleca.model.Symbol;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mrivero on 17/10/17.
 */
public class ParseHelper {

    public static final String KEYWORDS = "KEYWORDS";
    public static final String SYMBOLS = "SYMBOLS";

    public Map<String, List<String>> getKeywordsAndSymbols(Grammar grammar){
        Map<String, List<String>> keywordsAndSymbols = Collections.emptyMap();
        keywordsAndSymbols.put(KEYWORDS, Collections.EMPTY_LIST);
        keywordsAndSymbols.put(SYMBOLS, Collections.EMPTY_LIST);

        grammar.getRules().forEach(rule -> {
            rule.getProductions().forEach(p -> {
                p.getExpantion().ifPresent(expansion -> {
                    getStrings(expansion);
                    keywordsAndSymbols.get(KEYWORDS).
                            addAll(getStrings(expansion).filter(k -> k.chars().allMatch(Character::isLetter)).collect(Collectors.toList()));
                    keywordsAndSymbols.get(SYMBOLS).
                            addAll(getStrings(expansion).filter(k -> !k.chars().allMatch(Character::isLetter)).collect(Collectors.toList()));
                });
            });

        });

        return keywordsAndSymbols;

    }

    private Stream<String> getStrings(Expansion expansion) {
        List<Symbol> collect = expansion.getSymbols().stream().
                filter(s -> s.isKeyword() && s.getKeyword().isPresent()).collect(Collectors.toList());

        return collect.stream().map(s -> s.getString_().get());
    }

}

