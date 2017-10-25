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
    public Term getLeaf() {
        return new Term(value);
    }


    public String toString(){
        return "<identificador> "+ value;
    }
}
