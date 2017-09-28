package org.unq.parser.lleca.lexer;

import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.reserved.Keywords;
import org.unq.parser.lleca.lexer.tokens.reserved.Symbols;
import org.unq.parser.lleca.status.ParseResult;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Created by mtejeda on 26/09/17.
 */
public class App {

    /*Sólo para probar voy a crear un lexer con las palabras reservadas de lleca*/
    public static void main(String []args) {

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


        //test(llecaKeyWords, llecaSymbols);


        Symbols llecaS = new Symbols(llecaSymbols);

        File gramatica = new File("./files/lleca.ll");
        Tokenizer elLexer = new Tokenizer(gramatica, llecaS, llecaK);

        ParseResult res = elLexer.tokenize();
        System.out.println(res);

/*
        String identifiersRegex = "^[a-zA-Z_][a-zA-Z0-9_]+";

        String iden = "soyunindet03495840958390532";


        Pattern p = Pattern.compile(identifiersRegex);
        Matcher m = p.matcher(iden);
        List<String> tokens = new LinkedList<>();
        while(m.find()) {
            String token = m.group( 0 );
            //group 0 is always the entire match
            //k es el resto
            String k =iden.substring(token.length(),iden.length());
            //token es el string del token
            tokens.add(token);
        }
*/


        // Tokenizer de Java
        /* StringTokenizer st =  new StringTokenizer("\"Hola \"mundo\"\"");


        while (st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }*/


    }

    private static void test(List<String> llecaKeyWords, List<String> llecaSymbols) {


        Set<String> keyWordsRegex = llecaKeyWords.stream().map(keyWord -> "^" + keyWord + ".*").collect(toSet());
        Set<String> reservedSymbolsRegex = llecaSymbols.stream().map(symbol -> "^" + symbol+ ".*").collect(toSet());
        keyWordsRegex.add("/\\*([^*]|[\\r\\n])*\\*/");


        String palabra = "=> Cons($1";


        for (String wordsRegex : reservedSymbolsRegex) {
            System.out.println(palabra.matches(wordsRegex));
        }




        palabra = "";


    }

}
