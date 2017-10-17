package org.unq.parser.lleca.grammar.lleca.parser;


import org.unq.parser.lleca.grammar.lleca.model.Expansion;
import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.model.Keyword;
import org.unq.parser.lleca.grammar.lleca.model.Symbol;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mrivero on 17/10/17.
 */
public class ParseHelper {

    public static final String KEYWORDS = "KEYWORDS";
    public static final String SYMBOLS = "SYMBOLS";

    public static Map<String, List<String>> getKeywordsAndSymbols(Grammar grammar){
        Map<String, List<String>> keywordsAndSymbols = new HashMap<>();
        keywordsAndSymbols.put(KEYWORDS, new ArrayList<>());
        keywordsAndSymbols.put(SYMBOLS, new ArrayList<>());

        grammar.getRules().forEach(rule -> {
            rule.getProductions().forEach(p -> {
                p.getExpantion().ifPresent(expansion -> {
                    getStrings(expansion).collect(Collectors.toList());
                    keywordsAndSymbols.get(KEYWORDS).
                            addAll(getStrings(expansion).filter(k -> k.chars().anyMatch(Character::isLetter)).collect(Collectors.toList()));
                    keywordsAndSymbols.get(SYMBOLS).
                            addAll(getStrings(expansion).filter(k -> k.chars().allMatch(h -> !Character.isLetter(h))).collect(Collectors.toList()));
                });
            });

        });

        return keywordsAndSymbols;

    }

    private static Stream<String> getStrings(Expansion expansion) {
        List<Symbol> collect = expansion.getSymbols().stream().
                filter(s -> s.isString() && s.getString_().isPresent()).collect(Collectors.toList());

        return collect.stream().map(s -> s.getString_().get());
    }

}

