package org.unq.parser.lleca.grammar.lleca.model;

/**
 * Created by mtejeda on 06/10/17.
 */
public class Production {

    // barra vertical, expansion seguida por accion (esta dada por una flecha y un termino)
    private String pipe = "|";
    private Expansion expansion;
    private String arrow = "=>";
    private Term term;

    public Production(Expansion expansion, Term term){
        this.expansion = expansion;
        this.term = term;
    }
}
