package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.model.Production;
import org.unq.parser.lleca.grammar.lleca.model.Symbol;

import java.util.*;

public class FirstCalculator {

    private HashMap<String, List<String>> result = new HashMap<>();

    public FirstCalculator() {

    }


    public void calculateFirst(Grammar grammar, List<String> nonTerminals, List<String> terminals){

        nonTerminals.forEach(nonTerm -> {
            List<String> firsts = this.getFirsts(nonTerm, grammar, nonTerminals, terminals);
            result.put(nonTerm, new ArrayList<String>());
        });




    }
/*
    private List<String> getFirsts(String nonTerm, Grammar grammar, List<String> nonTerminals) {
        ArrayList<String> first = new ArrayList<>();
        Boolean changes = true;

        while (changes){
            grammar.getRules().forEach(rule -> {
                String nt = rule.getIdentifier().getValue();

            });
        }
        return null;
    }

*/


    private List<String> getFirsts(String nonTerm, Grammar grammar, List<String> nonTerminals, List<String> terminals) {
        ArrayList<String> first = new ArrayList<>();
        grammar.getRules().forEach(rule -> {
            if (nonTerm.equals(rule.getIdentifier().toString())){
                rule.getProductions().forEach(prod -> {
                    first.addAll(getFirstsFromProd(prod));
                });
            }
        });
        return null;
    }

    private List<String> getFirstsFromProd(Production prod) {

        return null;
    }



    /*prod.getExpantion().ifPresent(expansion -> {
                        Optional<Symbol> sym = expansion.getSymbols().stream().findFirst();
                        if(sym.isPresent()){
                            if (nonTerminals.contains(sym.get().getCurrentValue())){
                                //si es un noterminal, puede pasar que sea anulable: en ese caso tengo que buscar los firsts del sig simbolo
                                // si no fuera anulable: tengo que agregar sus FIRST
                                first.addAll(getFirsts(sym.get().getCurrentValue(), grammar, nonTerminals));
                            } else first.add(sym.get().getCurrentValue());
                        }else{
                            first.add("EPSILON");
                        }
                    })*/


    private boolean isNullable(String nonTerm, Grammar grammar){
        grammar.getRules().forEach(rule -> {
            if (nonTerm.equals(rule.getIdentifier().toString())){
                rule.getIdentifier();
            }

        });

        return false;
    }
}