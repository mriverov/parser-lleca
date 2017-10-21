package org.unq.parser.lleca.grammar.lleca.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtejeda on 06/10/17.
 */
public class Rule {

    // simbolo no termina y producciones
    Identifier identifier;
    List<Production> productions;

    public Rule(Identifier identifier, List<Production> productions){
        this.identifier = identifier;
        this.productions = productions;
    }

    public List<Production> getProductions(){
        return productions;
    }

    public Identifier getIdentifier() {
        return identifier;
    }


    public List<String> toListString(){
        List<String> rule = new ArrayList<>();
        rule.add(this.identifier.getValue());

        this.getProductions().forEach(prod ->{
             rule.add(prod.toString());
        });
       return rule;

    }
}
