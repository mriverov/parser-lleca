package org.unq.parser.lleca.lexer.tokens;

/**
 *  Palabras que empiezan con "". Se puede escapear la comilla con /" y escribir // se toma como una sola barra.
 */
public class TokenString implements Token{

    private String value;

    public TokenString(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString(){
        return "<cadena> "+ value;
    }
}
