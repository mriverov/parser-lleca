package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class Argument {

    private String startBracket = "(";
    private String closeBracket = ")";
    private Optional<ArgumentList> argumentList = Optional.empty();

    public Argument(ArgumentList argumentList){
        this.argumentList = Optional.of(argumentList);
    }

    public Optional<ArgumentList> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(Optional<ArgumentList> argumentList) {
        this.argumentList = argumentList;
    }
}
