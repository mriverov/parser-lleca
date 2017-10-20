package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.parser.ParseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.KEYWORDS;
import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.SYMBOLS;

public class GenericParser {


    private Grammar grammar;
    private List<String> nonTerminals = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();
    private FirstCalculator fc = new FirstCalculator();
    private FollowCalculator foc = new FollowCalculator();
    private ParseHelper ph = new ParseHelper();
    private String initialSymbol ;

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

        terminals.add("NUM");
        terminals.add("ID");
        terminals.add("STRING");


    }

    public void parseGrammar(){
        this.calculateNonTerminals();
        this.calculateTerminals();

        this.fc.calculateFirst(this.grammar, this.nonTerminals, this.terminals);

    }
}
