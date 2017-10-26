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

    public Optional<String> getHole() {
        return hole;
    }

    public void setHole(Optional<String> hole) {
        this.hole = hole;
    }

    public Optional<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Optional<Identifier> identifier) {
        this.identifier = identifier;
    }

    public Optional<Argument> getArgument() {
        return argument;
    }

    public void setArgument(Optional<Argument> argument) {
        this.argument = argument;
    }

    public Optional<String> getString_() {
        return string_;
    }

    public void setString_(Optional<String> string_) {
        this.string_ = string_;
    }

    public Optional<Integer> getNum() {
        return num;
    }

    public void setNum(Optional<Integer> num) {
        this.num = num;
    }

    public Optional<String> getDolarSign() {
        return dolarSign;
    }

    public void setDolarSign(Optional<String> dolarSign) {
        this.dolarSign = dolarSign;
    }

    public Optional<Substitution> getSubstitution() {
        return substitution;
    }

    public void setSubstitution(Optional<Substitution> substitution) {
        this.substitution = substitution;
    }


    public Boolean isIdentifierAndArgument() {
        return isIdentifierAndArgument;
    }

    public Boolean isString() {
        return isString;
    }

    public Boolean isNumeric() {
        return isNumeric;
    }

    public Boolean isNumericAndSubstitution() {
        return isNumericAndSubstitution;
    }


    public void print() {
       if(isNumeric){
           System.out.println(this.getNum().get());
       }
       if(isString){
           System.out.println(this.getString_());
       }
    }

    public String toString() {
        if(isNumeric){
            return this.getNum().get().toString();
        }else {
            return this.getString_().orElse("");
        }
    }
}