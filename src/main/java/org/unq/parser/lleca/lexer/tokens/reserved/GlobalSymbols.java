package org.unq.parser.lleca.lexer.tokens.reserved;

import java.util.ArrayList;
import java.util.List;

public class GlobalSymbols {

    public ArrayList<String> gsymbols = new ArrayList<>();

    //TODO
    /*El conjunto S de símbolos reservados debe contener únicamente símbolos formados por cualquiera de los
        siguientes caracteres:
        ( ) [ ] { } , ; : . + - * / % ! ? $ @ # | & = < > ~ ^ \

        Esta clase será una especie de biblioteca de símbolos. Es para salvar el caso de literal de símbolos reservados
        con más de un símbolo.*/
    public GlobalSymbols() {
        gsymbols.add("(");
        gsymbols.add(")");
        gsymbols.add("[");
        gsymbols.add("]");
        gsymbols.add("{");
        gsymbols.add("}");
        gsymbols.add(",");
        gsymbols.add(";");
        gsymbols.add(":");
        gsymbols.add(".");
        gsymbols.add("+");
        gsymbols.add("-");
        gsymbols.add("*");
        gsymbols.add("/");
        gsymbols.add("%");
        gsymbols.add("!");
        gsymbols.add("?");
        gsymbols.add("$");
        gsymbols.add("@");
        gsymbols.add("#");
        gsymbols.add("|");
        gsymbols.add("&");
        gsymbols.add("=");
        gsymbols.add("<");
        gsymbols.add(">");
        gsymbols.add("~");
        gsymbols.add("^");
        gsymbols.add("\\");
    }

    public boolean isGlobalSymbol(String symbol){
        return gsymbols.contains(symbol);
    }

    public ArrayList<String> getGsymbols() {
        return gsymbols;
    }
}
