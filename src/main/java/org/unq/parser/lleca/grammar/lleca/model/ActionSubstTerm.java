package org.unq.parser.lleca.grammar.lleca.model;


import java.util.List;
import java.util.Optional;

/*
* Se crea esta clase para crear un término con sus argumentos/sustituciones
* */
public class ActionSubstTerm extends Term{


    private Term term;
    private List<Term> args;



    public ActionSubstTerm(Integer num, Optional<Substitution> substitution, Term term, List<Term> args) {
        super(num, substitution);
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
