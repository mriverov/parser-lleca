package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.model.Symbol;

import java.util.*;

public class FirstCalculator {

    private List<String> nullables = new ArrayList<>();
    private Grammar grammar;

    public FirstCalculator(Grammar grammar, List<String> nullables) {
        this.grammar = grammar;
        this.nullables = nullables;
    }


    public HashMap<String, Set<String>> calculateFirst(List<String> nonTerminals, List<String> terminals){
        HashMap<String, Set<String>> result = new HashMap<String, Set<String>>();
        nonTerminals.forEach(nt ->{
            result.put(nt, new HashSet<>());
        });

        int before = 0;
        int after = 1000;
        while (before != after){

            before = result.values().stream().mapToInt(list -> list.size()).sum();
            grammar.getRules().forEach(rule -> {
                rule.getProductions().forEach(production -> {
                    String ident = rule.getIdentifier().getValue();
                    if (!production.getExpantion().isPresent()){

                        result.get(ident).add("EPSILON");
                    }
                    else{
                        production.getExpantion().ifPresent(expansion -> {
                            int ss = expansion.getSymbolsSize();
                            for (int i = 0; i < ss; i++) {
                                String currValue = expansion.getSymbols().get(i).getCurrentValue();
                                if(!nullables.contains(currValue)
                                        && terminals.contains(currValue)){

                                    result.get(ident).add(currValue);
                                        break;

                                }
                                if (!nullables.contains(currValue)
                                        && nonTerminals.contains(currValue)){
                                    result.get(ident).addAll(result.get(currValue));
                                    break;
                                }
                                if (i == ss-1){
                                    if(nullables.contains(currValue)){

                                        result.get(ident).add("EPSILON");
                                    }
                                }
                            }
                        });
                    }
                });
            });
            after =  result.values().stream().mapToInt(list -> list.size()).sum();
        }

        /*result.forEach((s,l) -> {
            System.out.printf("Terminal: ");
            System.out.println(s);
            System.out.println("FIRST:");
            l.forEach(ll -> {
                System.out.println(ll);
            });
        });*/


        return result;
    }

}