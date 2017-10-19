package org.unq.parser.lleca.grammar.lleca.model;

/**
 * Created by mrivero on 15/10/17.
 */
public enum Keyword {

    ID("ID"), NUM("NUM"), STRING("STRING");

    private String value;

    Keyword(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }


}
