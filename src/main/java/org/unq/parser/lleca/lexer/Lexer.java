package org.unq.parser.lleca.lexer;

import org.apache.commons.lang3.StringUtils;
import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.reserved.Keywords;
import org.unq.parser.lleca.lexer.tokens.reserved.Symbols;
import org.unq.parser.lleca.status.ParseResult;
import org.unq.parser.lleca.status.Result;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by mrivero on 24/9/17.
 */
public class Lexer {

    private File file;
    private List<Token> tokens = Collections.emptyList();

    /* Flags to control the type of token */
    private Boolean comment = Boolean.FALSE;
    private Boolean numeric = Boolean.FALSE;
    private Boolean string = Boolean.FALSE;
    private Boolean identifier = Boolean.FALSE;
    private Boolean literal = Boolean.FALSE;
    private Boolean ignore = Boolean.FALSE;


    public Lexer(File fileToTokenize){
        this.file = fileToTokenize;
    }

    public ParseResult tokenize(Symbols symbols, Keywords keywords){
        if(file == null){
            return new ParseResult(Result.ERROR, "File not defined", Optional.of("Trying to get file to tokenize"));
        }

        Reader reader = getReaderFromFile();
        if(reader ==  null){
            return new ParseResult(Result.ERROR, "Could not read file", Optional.of("Trying to read file to tokenize"));
        }

        int r;
        String actualValue = "";
        boolean scaped = false;
        try {
            while ((r = reader.read()) != -1) {
                char ch = (char) r;
                String character = String.valueOf(ch);

                // Ignore characters between /* */ because are comments
                if(!ignore){
                    if("/".equals(character)){
                        comment = Boolean.TRUE;
                        actualValue = actualValue.concat(character);
                    }else {
                        if("*".equals(character) && comment) {
                            comment = Boolean.FALSE;
                            ignore = Boolean.TRUE;
                            actualValue = "";
                        }else{
                            comment = Boolean.FALSE;
                            ignore = Boolean.FALSE;

                            if(StringUtils.isEmpty(character) ||  ch==' ' || ch=='\t' || ch=='\n' || ch=='\r' ) {
                                // in case of string (cadenas), ignore blank space
                                if(!string){
                                    processToken(actualValue);
                                    actualValue = "";
                                    numeric = Boolean.FALSE;
                                    string = Boolean.FALSE;
                                    identifier = Boolean.FALSE;
                                    literal = Boolean.FALSE;
                                }
                            }else{
                                // contact value to create token
                                actualValue = actualValue.concat(character);

                                if(Character.isDigit(ch) && !string && !identifier && !literal){ // process tokens
                                    numeric = Boolean.TRUE;
                                    string = Boolean.FALSE;
                                    identifier = Boolean.FALSE;
                                    literal = Boolean.FALSE;
                                }else if ((Character.isLetter(ch) || "_".equals(character)) && !numeric && !string && !literal ){ // find if isLetter retuns true when is " and for _
                                    identifier = Boolean.TRUE;
                                    numeric = Boolean.FALSE;
                                    string = Boolean.FALSE;
                                    literal = Boolean.FALSE;
                                }else if ("\"".equals(character) && !identifier && !numeric && !literal){
                                    if (scaped && string) {
                                        string = Boolean.TRUE;
                                    }else{
                                        string = Boolean.FALSE;
                                        scaped = Boolean.FALSE;
                                    }
                                    identifier = Boolean.FALSE;
                                    numeric = Boolean.FALSE;
                                    literal = Boolean.FALSE;
                                }else if("\\".equals(character) && string && !identifier && !numeric && !literal){
                                    scaped = true;
                                }else if (isReservedSymbol(character, symbols) && !string && !identifier && !numeric && !literal){
                                    literal = Boolean.TRUE;
                                }else {
                                    return new ParseResult(Result.ERROR, "Syntax error: unknown character", Optional.of("Trying to read character "+ character));
                                }
                            }

                        }
                    }
                }else{
                    // Find close comments
                    if("*".equals(character)){
                        comment = Boolean.TRUE;
                    }else if("\\".equals(character) && comment){
                        ignore = Boolean.FALSE;
                    }else{
                        comment = Boolean.FALSE;
                    }
                }
            }

        } catch (IOException e) {
            return new ParseResult(Result.ERROR, "Could not read file", Optional.of("Trying to read file to tokenize"));
        }


        return new ParseResult(Result.OK, "Lexer finish successfully", Optional.empty());
    }

    //Depending on the type of token, create the token and add to the list
    //In case of indentifier, define if its inlude in keywords first, if its true, create a Literal token
    private void processToken(String actualValue) {
        //TODO
    }

    private boolean isReservedSymbol(String character, Symbols symbols) {
        //TODO
        return false;
    }

    private Reader getReaderFromFile() {
        try (InputStream in = new FileInputStream(file);
             Reader reader = new InputStreamReader(in);
             Reader buffer = new BufferedReader(reader)) {
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Token> getTokens(){
        return tokens;
    }

}
