package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mrivero on 15/10/17.
 */
public class Term {

    private Optional<String> hole = Optional.of("_");
    private Optional<Identifier> identifier = Optional.empty();
    private Optional<Argument> argument = Optional.empty();
    private Optional<String> string_ = Optional.empty();
    private Optional<Integer> num = Optional.empty();
    private Optional<String> dolarSign = Optional.of("$");
    private Optional<Substitution> substitution = Optional.empty();

    private Boolean isHole = Boolean.FALSE;
    private Boolean isIdentifierAndArgument = Boolean.FALSE;
    private Boolean isString = Boolean.FALSE;
    private Boolean isNumeric = Boolean.FALSE;
    private Boolean isNumericAndSubstitution = Boolean.FALSE;



    public Term(){
        isHole = Boolean.TRUE;
    }

    public Term(Identifier identifier, Optional<Argument> argument){
        this.identifier = Optional.of(identifier);
        this.argument = argument;
        isIdentifierAndArgument = Boolean.TRUE;
    }

    public Term(String string_) {
        this.string_ = Optional.of(string_);
        isString = Boolean.TRUE;
    }

    public Term(Integer num) {
        this.num = Optional.of(num);
        isNumeric = Boolean.TRUE;
    }

    public Term(Integer num, Optional<Substitution> substitution) {
        this.num = Optional.of(num);
        this.substitution = substitution;
        isNumericAndSubstitution = Boolean.TRUE;
    }
}