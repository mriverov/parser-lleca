package org.unq.parser.lleca.lexer.tokens;

/**
 * Son simplemente nombres, palabras que contienen caracteres alfabéticos, y que puede contener numeros y guion bajo.
 */
public class TokenIdentifier implements Token {

    private String value;

    public TokenIdentifier(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString(){
        return "<identificador> "+ value;
    }
}
