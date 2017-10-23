package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.Expansion;
import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.model.Keyword;
import org.unq.parser.lleca.grammar.lleca.model.Production;
import org.unq.parser.lleca.grammar.lleca.parser.ParseHelper;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        this.calculateNonTerminals();
        this.calculateTerminals();
        this.calculateNullables(grammar);

        this.fc = new FirstCalculator(grammar, nullables);
        this.foc = new FollowCalculator(grammar, nullables);
    }

    public void parseGrammar(){
        validateLL1(grammar);
    }

    public void validateLL1(Grammar grammar ){
        HashMap<String, Set<String>> first = this.fc.calculateFirst(this.nonTerminals, this.terminals);
        HashMap<String, Set<String>> follow = this.foc.getFollow(first);

        Map<String, List<ProductionTerminalVO>> ll1Table = new HashMap<>();

        grammar.getRules().forEach(rule -> {
            String currentRule = rule.getIdentifier().getValue();
            List<ProductionTerminalVO> productionsByRule = new ArrayList<>();
            Set<String> currentFirst = first.get(currentRule);
            Set<String> currentFollow = follow.get(currentRule);

            rule.getProductions().forEach(production -> {
                if(production.getExpantion().isPresent()){
                    Expansion expansion = production.getExpantion().get();
                    if(expansion.getSymbols().size()>0){
                        String currentExpansionSymbol = expansion.getSymbols().get(0).getCurrentValue();

                        // its terminal and is first
                        if(currentFirst.contains(currentExpansionSymbol)){
                            productionsByRule.add(new ProductionTerminalVO(currentExpansionSymbol,
                                    production));
                        }else{
                            //is not terminal
                            if(nonTerminals.contains(currentExpansionSymbol)){
                                Set<String> firstOfNoTerminal = first.get(currentExpansionSymbol);
                                firstOfNoTerminal.forEach(f -> {
                                    if(currentFirst.contains(f)){
                                        if(f.equals("EPSILON")){
                                            productionsByRule.addAll(addFollowToTable(production, currentFollow));
                                        }else{
                                            productionsByRule.add(new ProductionTerminalVO(f,
                                                    production));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }else{
                    if(currentFirst.contains("EPSILON")){
                        productionsByRule.addAll(addFollowToTable(production, currentFollow));
                    }
                }
            });

            ll1Table.put(rule.getIdentifier().getValue(), productionsByRule);

            //validate that for each no termial there is only one VO
            validateTable(productionsByRule, currentFirst);
            validateTable(productionsByRule, currentFollow);
        });

        System.out.println("Validation result: The grammar is LL1");
    }

    private void validateTable(List<ProductionTerminalVO> productionsByRule, Set<String> terminals){
        terminals.forEach(cf -> {
            List<ProductionTerminalVO> prodToValid = productionsByRule.stream().filter(pru -> pru.getTerminal().equals(cf)).collect(Collectors.toList());
            if (prodToValid.size() > 1) {
                System.out.println("Validation result: The grammar is invalid, there is not LL1");
                throw new RuntimeException("Error building LL(1) table there is more that one production for " + cf);
            }
        });
    }

    private List<ProductionTerminalVO> addFollowToTable(Production production, Set<String> follow) {
        List<ProductionTerminalVO> prod = new ArrayList<>();
        follow.forEach(fo -> {
            prod.add(new ProductionTerminalVO(fo,
                    production));
        });

        return prod;
    }


    /*
    * Calculo los símbolos no terminales
    * Se asume que son los símbolos que dan nombre a las reglas.
    * */
    private void calculateNonTerminals(){
        this.grammar.getRules().forEach(rule -> {
            nonTerminals.add(rule.getIdentifier().toString());
        });
    }

    /*
    * Calculo los símbolos terminales.
    * Serán las palabras clave y los símbolos dentro de las expansiones de cada producción
    * que no sean palabras clave ni no terminales.
    * */
    private void calculateTerminals(){
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
