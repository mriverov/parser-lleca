package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.*;

import java.util.*;

/**
 * Created by mtejeda on 20/10/17.
 */
public class FollowCalculator {

    private Grammar grammar;
    private List<String> nullables;

    public FollowCalculator(Grammar grammar, List<String> nullables ) {
        this.grammar = grammar;
        this.nullables = nullables;
    }

    public HashMap<String, Set<String>> getFollow(HashMap<String, Set<String>> first){
        HashMap<String, Set<String>> follow = new HashMap<String, Set<String>>();

        Boolean isFirstRule = Boolean.TRUE;

        for(Rule rule: grammar.getRules()){
            Set followResult = getFollow(rule.getIdentifier().getValue(), first);
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

    private Set<String> getFollow(String rule, HashMap<String, Set<String>> first){
        Set<String> follow = new HashSet<>();

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
                                follow.addAll(getFollow(currentRule.getIdentifier().getValue(), first));
                            }
                        }
                        next = sy.getCurrentValue().equals(rule);
                    }

                    if(next && !rule.equals(currentRule.getIdentifier().getValue())){
                        boolean isFirst = grammar.getRules().get(0).getIdentifier().getValue().equals(currentRule.getIdentifier().getValue());
                        if(isFirst){
                            follow.add("$");
                        }
                        follow.addAll(getFollow(currentRule.getIdentifier().getValue(), first));
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
