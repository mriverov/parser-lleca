package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.*;

import java.util.*;

/**
 * Created by mtejeda on 20/10/17.
 */
public class FollowCalculator {

    private Grammar grammar;
    private HashMap<String, Set<String>> first;
    private List<String> nullables;

    public FollowCalculator(Grammar grammar, HashMap<String, Set<String>> first, List<String> nullables ) {
        this.grammar = grammar;
        this.first = first;
        this.nullables = nullables;
    }

    public HashMap<String, List<String>> getFollow(){
        HashMap<String, List<String>> follow = new HashMap<String, List<String>>();

        Boolean isFirstRule = Boolean.TRUE;

        for(Rule rule: grammar.getRules()){
            List followResult = getFollow(rule.getIdentifier().getValue());
            if(isFirstRule){
                followResult.add("$");

            }
            if (!followResult.isEmpty()) {
                follow.put(rule.getIdentifier().getValue(), followResult);
            }
            isFirstRule = Boolean.FALSE;
        }

        return follow;
    }

    private List<String> getFollow(String rule){
        List<String> follow = new ArrayList<>();

        grammar.getRules().forEach(currentRule -> {
            currentRule.getProductions().forEach(production -> {
                List<Symbol> symbols = getExpansions(production, rule);
                if(!symbols.isEmpty()){

                    boolean next = false;
                    for(Symbol sy: symbols){
                        if(next){
                            if(!sy.isIdentifier()){
                                follow.add(sy.getCurrentValue());
                            }else{
                                follow.addAll(first.get(sy.getCurrentValue()));
                            }
                            if(nullables.contains(sy.getCurrentValue())){
                                boolean isFirst = grammar.getRules().get(0).getIdentifier().getValue().equals(currentRule.getIdentifier().getValue());
                                if(isFirst){
                                    follow.add("$");
                                }
                                follow.addAll(getFollow(currentRule.getIdentifier().getValue()));
                            }
                        }
                        next = sy.getCurrentValue().equals(rule);
                    }

                    if(next && !rule.equals(currentRule.getIdentifier().getValue())){
                        boolean isFirst = grammar.getRules().get(0).getIdentifier().getValue().equals(currentRule.getIdentifier().getValue());
                        if(isFirst){
                            follow.add("$");
                        }
                        follow.addAll(getFollow(currentRule.getIdentifier().getValue()));
                    }
                }
            });
        });

        return follow;
    }

    private List<Symbol> getExpansions(Production p, String rule){
        List<Symbol> result = new ArrayList<>();
        p.getExpantion().ifPresent(expansion -> {
            if(expansion.getSymbols().stream().anyMatch(symbol -> symbol.getCurrentValue().equals(rule))){
                result.addAll(expansion.getSymbols());
            }
        });

        return result;
    }


}
