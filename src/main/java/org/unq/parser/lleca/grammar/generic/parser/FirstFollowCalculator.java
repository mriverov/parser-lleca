package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class FirstFollowCalculator {

    private Grammar grammar;

    public FirstFollowCalculator(Grammar grammar) {
        this.grammar = grammar;
    }


    public Map<String, List<String>> getFrist(){
        Map<String, List<String>> firstSet = new HashMap<>();


        grammar.getRules().forEach(rule -> {
            List<String> first = new ArrayList<String>();
            rule.getProductions().forEach(p -> {
                p.getExpantion().ifPresent(expansion -> {
                    Optional<Symbol> sym = expansion.getSymbols().stream().findFirst();
                    if(sym.isPresent()){
                        first.add(sym.get().getCurrentValue());
                    }else{
                        first.add("EPSILON");
                    }
                });
            });
            if(!first.isEmpty()){
                firstSet.put(rule.getIdentifier().getValue(), first);
            }

        });

        return firstSet;
    }


    public Map<String, List<String>> getFollow(){
        Map<String, List<String>> followSet = new HashMap<>();
        Boolean isFirstRule = Boolean.TRUE;

        for(Rule rule: grammar.getRules()){
            List follow = getFollow(rule, isFirstRule);
            if (!follow.isEmpty()) {
                followSet.put(rule.getIdentifier().getValue(), follow);
            }

            isFirstRule = Boolean.FALSE;
        }


        return followSet;
    }

    private List<String> getFollow(Rule rule, Boolean isFrist){
        List<String> follow = new ArrayList<String>();
        rule.getProductions().forEach(p -> {
            p.getExpantion().ifPresent(expansion -> {
                getSymbolFromExpansion(expansion, rule.getIdentifier()).stream();
                //TODO aca habria que ubicar la posicion dentro de la lista de symbols correspondiente al identificador de la rule (esa lista ya te la da getSymbolFromExpansion)
                //luego de ver la posicion, el siguiente seria el follow, hay que ver tambien el tipo de posicion, si es aAb, o aA, para saber si el follow tambien incluye el follow
                // de su respectiva rule.
            });


        });
        if(!follow.isEmpty() && isFrist){
            follow.add("$");

        }

        return follow;
    }

    public List<Symbol> getSymbolFromExpansion(Expansion ex,  Identifier rule){
        List<Symbol> symbolListResult = ex.getSymbols().stream().filter(symbol ->
                symbol.isIdentifier() && symbol.getCurrentValue().equals(rule.getValue())
        ).collect(Collectors.toList());

        if(symbolListResult.isEmpty()){
            return Collections.emptyList();
        }

        return ex.getSymbols();
    }


    
}
