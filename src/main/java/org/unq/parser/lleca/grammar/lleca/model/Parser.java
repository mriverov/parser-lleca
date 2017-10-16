package org.unq.parser.lleca.grammar.lleca.model;

import org.unq.parser.lleca.lexer.tokens.Token;

import java.util.List;

public class Parser {

    private List<Token> token;

    public Parser(List<Token> tokens){
        this.token = tokens;
    }

    public Grammar parse(){
        return null;
    }
}
