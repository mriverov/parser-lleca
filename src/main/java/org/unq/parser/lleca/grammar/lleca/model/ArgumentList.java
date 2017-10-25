package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class ArgumentList {

    private Term term;
    private Optional<ArgumentListCont> argumentListCont;

    public ArgumentList(Term term, Optional<ArgumentListCont> argumentListCont){
        this.term = term;
        this.argumentListCont = argumentListCont;
    }

    public Term getTerm() {
        return term;
    }

    public int getArgumentsSize(){
        int n = 1;
        if (argumentListCont.isPresent()){
            n +=argumentListCont.get().getTerm().size();
        }

        return n;
    }

    public Optional<ArgumentListCont> getArgumentListCont() {
        return argumentListCont;
    }
}
