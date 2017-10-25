package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.Number;
import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;
import org.unq.parser.lleca.grammar.lleca.model.Term;

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
    public Term getLeaf() {
        return new Term(Integer.valueOf(value));
    }


    public String toString(){
        return "<número> "+ value;
    }
}
