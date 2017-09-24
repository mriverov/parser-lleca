package org.unq.parser.lleca.lexer;

import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.reserved.Keywords;
import org.unq.parser.lleca.lexer.tokens.reserved.Symbols;
import org.unq.parser.lleca.status.ParseResult;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by mrivero on 24/9/17.
 */
public class Lexer {

    private File file;
    private List<Token> tokens = Collections.emptyList();

    public Lexer(File fileToTokenize){
        this.file = fileToTokenize;
    }

    public ParseResult tokenize(Symbols symbols, Keywords keywords){
        //TODO

        return null; // return a ParserResult with ok or an error
    }


    public List<Token> getTokens(){
        return tokens;
    }

}
