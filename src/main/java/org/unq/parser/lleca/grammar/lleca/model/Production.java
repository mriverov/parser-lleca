package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mtejeda on 06/10/17.
 */
public class Production {

    private String pipe = "|";
    private Optional<Expansion> expansion = Optional.empty();
    private String arrow = "=>";
    private Term term;


    public Production(Optional<Expansion> expansion, Term term){
        this.expansion = expansion;
        this.term = term;
    }

    public Optional<Expansion> getExpantion(){
        return expansion;
    }


    public Term getTerm() {
        return term;
    }


}
