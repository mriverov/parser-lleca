package org.unq.parser.lleca.lexer;

import javafx.util.Pair;
import org.unq.parser.lleca.lexer.tokens.*;
import org.unq.parser.lleca.lexer.tokens.reserved.GlobalSymbols;
import org.unq.parser.lleca.lexer.tokens.reserved.ReservedKeywords;
import org.unq.parser.lleca.lexer.tokens.reserved.ReservedSymbols;
import org.unq.parser.lleca.status.ParseResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrivero on 24/9/17.
 */
public class Lexer {

    private File file;
    private List<Token> tokens = new ArrayList<>();


    private GlobalSymbols globalSymbols = new GlobalSymbols();

    public Lexer(File fileToTokenize){
        this.file = fileToTokenize;
    }

    public Pair<ParseResult, List<Token>> tokenize(ReservedSymbols symbols, ReservedKeywords keywords){
       Tokenizer tokenizer = new Tokenizer(file, symbols, keywords);
       return tokenizer.tokenize();
    }


    public List<Token> getTokens()
    {
        return tokens;
    }

}
