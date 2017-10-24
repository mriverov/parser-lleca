package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;
import org.unq.parser.lleca.grammar.lleca.model.Term;

import java.util.List;
import java.util.Map;

/**
 * Created by mrivero on 24/9/17.
 */
public interface Token {

    String value();

    Term getLeaf();
}
