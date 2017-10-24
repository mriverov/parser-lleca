package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.Number;
import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;

import java.util.List;
import java.util.Map;

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

    @Override
    public STerm getLeaf(String symbol, Map<String, List<ProductionTerminalVO>> ll1Table) {
        return new Number(Integer.valueOf(value));
    }

    public String toString(){
        return "<número> "+ value;
    }
}
