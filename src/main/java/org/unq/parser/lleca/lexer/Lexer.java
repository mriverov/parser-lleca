package org.unq.parser.lleca.lexer;

import org.apache.commons.lang3.StringUtils;
import org.unq.parser.lleca.lexer.tokens.*;
import org.unq.parser.lleca.lexer.tokens.reserved.GlobalSymbols;
import org.unq.parser.lleca.lexer.tokens.reserved.Keywords;
import org.unq.parser.lleca.lexer.tokens.reserved.Symbols;
import org.unq.parser.lleca.status.ParseResult;
import org.unq.parser.lleca.status.Result;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ParseResult tokenize(Symbols symbols, Keywords keywords){
       Tokenizer tokenizer = new Tokenizer(file, symbols, keywords);
       return tokenizer.tokenize();
    }


    public List<Token> getTokens()
    {
        return tokens;
    }

}
