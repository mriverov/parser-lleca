package org.unq.parser.lleca.grammar.lleca.model;


import java.util.List;

/*
* Se crea esta clase para crear un término con sus argumentos/sustituciones
* */
public class ActionSubstTerm extends Term{


    private Term term;
    private List<Term> args;

    public ActionSubstTerm(Term term, List<Term> args) {
        this.term = term;
        this.args = args;
    }


    public String parseASTerm() {
        String ret = "";
        if (term.isNumericAndSubstitution()){
            int fst = term.getNum().get();
            ret+=args.get(fst).toString();

            if (term.getSubstitution().isPresent()){
                ret+=term.toString();
            }
        }


        return ret;
    }

    public void print(){
        System.out.println(this.parseASTerm());
    }

}
