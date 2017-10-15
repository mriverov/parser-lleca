package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class Symbol {

    private Optional<Identifier> identifier = Optional.empty();
    private Optional<Keyword> keyword = Optional.empty();
    private Optional<String> string_ = Optional.empty();

    private Boolean isIdentifier = Boolean.FALSE;
    private Boolean isKeyword = Boolean.FALSE;
    private Boolean isString = Boolean.FALSE;

    public Symbol(Identifier identifier){
        this.identifier = Optional.of(identifier);
        isIdentifier = Boolean.TRUE;
    }

    public Symbol(Keyword keyword){
        this.keyword = Optional.of(keyword);
        isKeyword = Boolean.TRUE;
    }

    public Symbol(String string_){
        this.string_ = Optional.of(string_);
        isString = Boolean.TRUE;
    }

    public Boolean isIdentifier(){
        return isIdentifier;
    }

    public Boolean isKeyword(){
        return isKeyword;
    }

    public Boolean isString(){
        return isString;
    }
}
