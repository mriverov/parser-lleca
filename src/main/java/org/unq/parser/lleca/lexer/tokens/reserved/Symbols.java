package org.unq.parser.lleca.lexer.tokens.reserved;

import java.util.Comparator;
import java.util.List;

/**
 * Created by mrivero on 24/9/17.
 */
public class Symbols {

    private List<String> reservedSymbols;

    public Symbols(List<String> reservedSymbols){
        reservedSymbols.sort((s1,s2) -> s2.length() - s1.length());
        this.reservedSymbols = reservedSymbols;
    }


    public List<String> getReservedSymbols() {
        return reservedSymbols;
    }

    public void setReservedSymbols(List<String> reservedSymbols) {
        reservedSymbols.sort((s1,s2) -> s2.length() - s1.length());
        this.reservedSymbols = reservedSymbols;
    }

}
