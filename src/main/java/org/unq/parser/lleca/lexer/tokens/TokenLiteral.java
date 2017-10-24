package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.model.Struct;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mrivero on 24/9/17.
 */
public class TokenLiteral implements Token {
    private String value;

    public TokenLiteral(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public STerm getLeaf(String symbol, Map<String, List<ProductionTerminalVO>> ll1Table) {
        return new Struct(value, Optional.empty());
    }

    public String toString(){
        return " \" " + value + " \" ";
    }
}
