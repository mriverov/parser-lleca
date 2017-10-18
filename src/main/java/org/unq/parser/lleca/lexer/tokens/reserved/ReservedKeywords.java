package org.unq.parser.lleca.lexer.tokens.reserved;

import java.util.List;

/**
 * Created by mrivero on 24/9/17.
 */
public class ReservedKeywords {

    private List<String> reservedKeywords;

    public ReservedKeywords(List<String> reservedKeywords){
        this.reservedKeywords = reservedKeywords;
    }

    public List<String> getReservedKeywords() {
        return reservedKeywords;
    }
}
