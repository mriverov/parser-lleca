package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class ArgumentListCont {

    private String comma = ",";
    private Term term;
    private Optional<ArgumentListCont> argumentListCont = Optional.empty();

    public ArgumentListCont(Term term, ArgumentListCont argumentListCont){
        this.term = term;
        this.argumentListCont = Optional.of(argumentListCont);
    }
}
