package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.model.Struct;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;
import org.unq.parser.lleca.grammar.lleca.model.Term;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public STerm getLeaf(String symbol, Map<String, List<ProductionTerminalVO>> ll1Table) {
        List<ProductionTerminalVO> p = ll1Table.get(symbol);
        for (ProductionTerminalVO prod: p){
            if(prod.getTerminal().equals(value)){
                Term t = prod.getProduction().getTerm();
                if (t.getArgument().isPresent()) {
                    return new Struct(t.getIdentifier().get().getValue(), Optional.of(t.getArgument().get().getArgumentList().get()));
                }
            }
        }
        return null;
    }

    public String toString(){
        return "<identificador> "+ value;
    }
}
