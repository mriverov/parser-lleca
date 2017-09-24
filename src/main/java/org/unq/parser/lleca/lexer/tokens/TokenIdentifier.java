package org.unq.parser.lleca.lexer.tokens;

/**
 * Created by mrivero on 24/9/17.
 */
public class TokenIdentifier implements Token {

    private String value;

    public TokenIdentifier(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }
}
