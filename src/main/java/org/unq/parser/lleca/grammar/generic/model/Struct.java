package org.unq.parser.lleca.grammar.generic.model;

import org.unq.parser.lleca.grammar.lleca.model.ArgumentList;

import java.util.List;
import java.util.Optional;

public class Struct  extends STerm{

    String value;
    Optional<ArgumentList> terms = Optional.empty();

    public Struct(String value, Optional<ArgumentList> terms) {
        this.value = value;
        this.terms = terms;
    }

}
