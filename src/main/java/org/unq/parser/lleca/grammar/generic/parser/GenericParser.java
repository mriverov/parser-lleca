package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.model.Keyword;
import org.unq.parser.lleca.grammar.lleca.parser.ParseHelper;

import java.util.*;

import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.KEYWORDS;
import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.SYMBOLS;

public class GenericParser {


    private Grammar grammar;
    private List<String> nonTerminals = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();
    private List<String> nullables = new ArrayList<>();
    private FirstCalculator fc;
    private FollowCalculator foc;
    private ParseHelper ph = new ParseHelper();
    private String initialSymbol;

    public GenericParser(Grammar grammar) {
        this.grammar = grammar;
        this.initialSymbol = grammar.getRules().get(0).getIdentifier().toString();
    }


    /*
    * Calculo los símbolos no terminales
    * Se asume que son los símbolos que dan nombre a las reglas.
    * */
    public void calculateNonTerminals(){
        this.grammar.getRules().forEach(rule -> {
            nonTerminals.add(rule.getIdentifier().toString());
        });
    }

    /*
    * Calculo los símbolos terminales.
    * Serán las palabras clave y los símbolos dentro de las expansiones de cada producción
    * que no sean palabras clave ni no terminales.
    * */
    public void calculateTerminals(){
        Map<String, List<String>>   x = ph.getKeywordsAndSymbols(this.grammar);
        x.get(KEYWORDS).forEach(keyword -> {
            terminals.add(keyword);
        });
        x.get(SYMBOLS).forEach(symbol -> {
            terminals.add(symbol);
        });
        /*this.grammar.getRules().forEach(rule -> {
            rule.getProductions().forEach(production -> {
                production.getExpantion().ifPresent(expansion -> {
                    expansion.getSymbols().forEach(symbol -> {
                        if(symbol.isKeyword()){
                            if (!terminals.contains(symbol.getKeyword().get().toString()))
                            terminals.add(symbol.getKeyword().get().toString());
                        }
                    });
                });
            });
        });*/

        terminals.add(Keyword.NUM.getValue());
        terminals.add(Keyword.ID.getValue());
        terminals.add(Keyword.STRING.getValue());


    }

    public void parseGrammar(){
        this.calculateNonTerminals();
        this.calculateTerminals();
        this.calculateNullables(grammar);

        this.fc = new FirstCalculator(grammar, nullables);

        HashMap<String, Set<String>> first = this.fc.calculateFirst(this.nonTerminals, this.terminals);

        this.foc = new FollowCalculator(grammar, first, nullables);
        this.foc.getFollow();

    }

    /*
       * Método que calcula los símbolos anulables.
       * */
    private void calculateNullables(Grammar grammar) {
        grammar.getRules().forEach(rule -> {
            rule.getProductions().forEach(prod -> {
                if (!prod.getExpantion().isPresent()){
                    nullables.add(rule.getIdentifier().getValue());
                };
            });
        });
    }
}
