package org.unq.parser.lleca.grammar.lleca.model;

import java.util.List;

/**
 * Created by mtejeda on 06/10/17.
 */
public class Grammar {

    List<Rule> rules;

    public Grammar(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRules(){
        return rules;
    }
}
