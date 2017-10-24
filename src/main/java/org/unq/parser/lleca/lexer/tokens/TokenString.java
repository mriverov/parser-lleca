package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.Chain;
import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;
import org.unq.parser.lleca.grammar.lleca.model.Term;

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
    public Term getLeaf() {
        return null;
    }


    public String toString(){
        return "<cadena> "+ value;
    }
}
