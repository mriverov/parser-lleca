package org.unq.parser.lleca.grammar.lleca.model;

/**
 * Created by mrivero on 15/10/17.
 */
public class Substitution {

    private String startSquareBracket = "[";
    private String closeSquareBracket = "]";
    private Term term;

    public Substitution(Term term){
        this.term = term;
    }


}
