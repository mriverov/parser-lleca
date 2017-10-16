package org.unq.parser.lleca.grammar.lleca.model;

import java.util.Optional;

/**
 * Created by mtejeda on 06/10/17.
 */
public class Production {

    // barra vertical, expansion seguida por accion (esta dada por una flecha y un termino)
    private String pipe = "|";
    private Optional<Expansion> expansion = Optional.empty();
    private String arrow = "=>";
    private Term term;

    public Production(Expansion expansion, Term term){
        this.expansion = Optional.of(expansion);
        this.term = term;
    }
}
