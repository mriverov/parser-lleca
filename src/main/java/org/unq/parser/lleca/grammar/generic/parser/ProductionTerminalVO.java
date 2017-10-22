package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.lleca.model.Production;

/**
 * Created by mrivero on 22/10/17.
 */
public class ProductionTerminalVO {

    private String terminal;
    private Production production;

    public ProductionTerminalVO(String terminal, Production production) {
        this.terminal = terminal;
        this.production = production;
    }

    public String getTerminal() {
        return terminal;
    }

    public Production getProduction() {
        return production;
    }
}
