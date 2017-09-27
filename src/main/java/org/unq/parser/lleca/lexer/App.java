package org.unq.parser.lleca.lexer;

import org.unq.parser.lleca.lexer.tokens.reserved.Keywords;
import org.unq.parser.lleca.lexer.tokens.reserved.Symbols;
import org.unq.parser.lleca.status.ParseResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mtejeda on 26/09/17.
 */
public class App {

    /*Sólo para probar voy a crear un lexer con las palabras reservadas de lleca*/
    public static void main(String []args){

        List<String> llecaKeyWords = new ArrayList<>();
        llecaKeyWords.add("_");
        llecaKeyWords.add("ID");
        llecaKeyWords.add("STRING");
        llecaKeyWords.add("NUM");

        Keywords llecaK = new Keywords(llecaKeyWords);

        List<String> llecaSymbols = new ArrayList<>();
        llecaSymbols.add("|");
        llecaSymbols.add("=>");
        llecaSymbols.add("$");
        llecaSymbols.add("(");
        llecaSymbols.add(")");
        llecaSymbols.add(",");
        llecaSymbols.add("[");
        llecaSymbols.add("]");

        Symbols llecaS = new Symbols(llecaSymbols);

        File gramatica = new File("./files/lleca.ll");
        Lexer elLexer = new Lexer(gramatica);

        ParseResult res = elLexer.tokenize(llecaS, llecaK);
        System.out.println(res);




    }

}
