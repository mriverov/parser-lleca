package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.Chain;
import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;

import java.util.List;
import java.util.Map;

/**
 *  Palabras que empiezan con "". Se puede escapear la comilla con /" y escribir // se toma como una sola barra.
 */
public class    TokenString implements Token{

    private String value;

    public TokenString(String value){
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public STerm getLeaf(String symbol, Map<String, List<ProductionTerminalVO>> ll1Table) {
        return new Chain(value);
    }

    public String toString(){
        return "<cadena> "+ value;
    }
}
