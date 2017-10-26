package org.unq.parser.lleca.grammar.lleca.model;

import java.util.List;
import java.util.Optional;

/*
* Se crea esta clase para crear un término con sus argumentos/sustituciones
* */
public class ActionArgTerm extends   Term{


    private Term term;
    private List<Term> args;


    public ActionArgTerm(Identifier identifier, Optional<Argument> argument, Term term, List<Term> args) {
        super(identifier, argument);
        this.term = term;
        this.args = args;
    }



    public String parseAATerm(){
        String ret = "";

        ret+=term.getIdentifier().get().getValue()+"( ";


        if (term.getArgument().isPresent()){
            if(term.getArgument().get().getArgumentList().isPresent()){
                Term fstt = term.getArgument().get().getArgumentList().get().getTerm();
                if(fstt.isNumericAndSubstitution()){
                    Term argTerm = args.get(fstt.getNum().get() - 1);
                    ret+= argTerm.toString() +" ";
                }
                if(term.getArgument().get().getArgumentList().get().getArgumentListCont().isPresent()){
                    List<Term> t = term.getArgument().get().getArgumentList().get().getArgumentListCont().get().getTerm();
                    int as = t.size();
                    for (int i = 0; i < as; i++) {
                        Term a = t.get(i);
                        if (a.isNumericAndSubstitution()){
                            Term argTerm = args.get(a.getNum().get()-1);
                            ret+= argTerm.toString();
                        }
                    }
                }

            }
        }

        return ret+")";
    }

    public void print(){
        System.out.println(this.parseAATerm());
    }
}
