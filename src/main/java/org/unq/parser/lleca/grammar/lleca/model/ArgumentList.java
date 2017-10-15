package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class ArgumentList {

    private Term term;
    private Optional<ArgumentListCont> argumentListCont;

    public ArgumentList(Term term, ArgumentListCont argumentListCont){
        this.term = term;
        this.argumentListCont = Optional.of(argumentListCont);
    }
}
