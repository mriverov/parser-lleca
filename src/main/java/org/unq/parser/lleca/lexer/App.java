package org.unq.parser.lleca.lexer;

import javafx.util.Pair;
import org.unq.parser.lleca.grammar.generic.parser.GenericParser;
import org.unq.parser.lleca.grammar.lleca.model.Grammar;
import org.unq.parser.lleca.grammar.lleca.parser.ParseHelper;
import org.unq.parser.lleca.grammar.lleca.parser.Parser;
import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.reserved.ReservedKeywords;
import org.unq.parser.lleca.lexer.tokens.reserved.ReservedSymbols;
import org.unq.parser.lleca.status.ParseResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.KEYWORDS;
import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.SYMBOLS;

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

        ReservedKeywords llecaK = new ReservedKeywords(llecaKeyWords);

        List<String> llecaSymbols = new ArrayList<>();
        llecaSymbols.add("|");
        llecaSymbols.add("=>");
        llecaSymbols.add("$");
        llecaSymbols.add("(");
        llecaSymbols.add(")");
        llecaSymbols.add(",");
        llecaSymbols.add("[");
        llecaSymbols.add("]");

        ReservedSymbols llecaS = new ReservedSymbols(llecaSymbols);

        //File gramatica = new File("./files/alumnos.ll");
        File gramatica = new File("./files/robot.ll");

        Tokenizer elLexer = new Tokenizer(gramatica, llecaS, llecaK);
        Pair<ParseResult,List<Token>> tokens = elLexer.tokenize();
        Parser p = new Parser(tokens.getValue());

        Grammar grammar = p.parse();

        GenericParser gp = new GenericParser(grammar);
        gp.parseGrammar();


        Map<String, List<String>>  result = ParseHelper.getKeywordsAndSymbols(grammar);

        List<String> kw = result.get(KEYWORDS);
        ReservedKeywords rkwc = new ReservedKeywords(kw);


        List<String> sy = result.get(SYMBOLS);
        ReservedSymbols symc = new ReservedSymbols(sy);
        //File input = new File("./tests_lleca/cucaracha/test29.input");
        File input = new File("./files/esquina.input");
        Tokenizer tokenCcacha = new Tokenizer(input,  symc, rkwc);

        Pair<ParseResult,List<Token>> cucaracha =  tokenCcacha.tokenize();

        cucaracha.getValue().forEach(val -> {
            System.out.println(val.toString());
        });
        //gp.parseInput(cucaracha.getValue());

        /*
        Pair<ParseResult,List<Token>> tokens = elLexer.tokenize();
        tokens.getValue().forEach(token-> System.out.println(token));

        System.out.println(tokens.getKey());

        Parser p = new Parser(tokens.getValue());
        Grammar grammar = p.parse();
        Map<String, List<String>>  result = ParseHelper.getKeywordsAndSymbols(grammar);
        System.out.println(result);


        List<String> kw = result.get(KEYWORDS);
        ReservedKeywords rkwc = new ReservedKeywords(kw);


        List<String> sy = result.get(SYMBOLS);
        ReservedSymbols symc = new ReservedSymbols(sy);
        File input = new File("./tests_lleca/cucaracha/test29.input");
       //File input = new File("./files/esquina.input");
        Tokenizer tokenCcacha = new Tokenizer(input,  symc, rkwc);

        Pair<ParseResult,List<Token>> cucaracha =  tokenCcacha.tokenize();
        cucaracha.getValue().forEach(token-> System.out.println(token));


        GenericParser gp = new GenericParser(grammar);
        gp.parseGrammar();
        */
    }



}
