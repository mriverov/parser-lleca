package org.unq.parser.lleca.lexer.tokens;

import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.parser.ProductionTerminalVO;

import java.util.List;
import java.util.Map;

/**
 * Created by mrivero on 24/9/17.
 */
public interface Token {

    String value();

    STerm getLeaf(String symbol, Map<String, List<ProductionTerminalVO>> ll1Table);
}
