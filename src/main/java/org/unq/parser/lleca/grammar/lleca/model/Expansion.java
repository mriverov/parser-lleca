package org.unq.parser.lleca.grammar.lleca.model;

import java.util.List;

/**
 * Created by mtejeda on 06/10/17.
 */
public class Expansion {

    // lista de simbolos que pueden ser cadenas o identificadores
    List<Symbol> symbols;

    public Expansion(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public List<Symbol> getSymbols(){
        return symbols;
    }

    public int getSymbolsSize(){
        return this.symbols.size();
    }
}
