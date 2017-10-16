package org.unq.parser.lleca.grammar.lleca.model;

import java.util.List;
import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class ArgumentListCont {

    private String comma = ",";
    private List<Term> term;

    public ArgumentListCont(List<Term> term){
        this.term = term;
    }
}
