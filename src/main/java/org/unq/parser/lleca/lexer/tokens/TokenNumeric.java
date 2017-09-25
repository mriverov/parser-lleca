package org.unq.parser.lleca.lexer.tokens;

/**
 * Números
 */
public class TokenNumeric implements Token {
    private String value;

    public TokenNumeric(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString(){
        return "<número> "+ value;
    }
}
