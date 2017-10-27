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
import org.unq.parser.lleca.status.Result;

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

        //Leemos la gramatica
        File gramatica = new File("./tests_lleca/robot/robot.ll");

        //Tokenizamos la gramatica
        Lexer lexer = new Lexer(gramatica);
        Pair<ParseResult,List<Token>> tokens = lexer.tokenize(llecaS, llecaK);
        Parser p = new Parser(tokens.getValue());

        //Armamos la gramatica en una estructura segun las regalas de la gramatica lleca
        Grammar grammar = p.parse();

        //Validamos que esa gramatica sea LL1
        GenericParser gp = new GenericParser(grammar);
        gp.parseGrammar();

        //Obtenemos los simbolos y palabras reservadas
        Map<String, List<String>>  result = ParseHelper.getKeywordsAndSymbols(grammar);
        ReservedKeywords rkwc = new ReservedKeywords(result.get(KEYWORDS));
        ReservedSymbols symc = new ReservedSymbols(result.get(SYMBOLS));

        //Leemos el programa y lo tokenizamos
        File input = new File("./tests_lleca/robot/esquina.input");
        Tokenizer tokenCcacha = new Tokenizer(input,  symc, rkwc);

        Pair<ParseResult,List<Token>> cucaracha =  tokenCcacha.tokenize();

        if(Result.OK.equals(cucaracha.getKey().getResult())){
            cucaracha.getValue().forEach(val -> {
                System.out.println(val.toString());
            });
            gp.parseInput(cucaracha.getValue());
        }else{
            System.out.print("Error parsing input file with the following error: "+ cucaracha.getKey().toString());
        }
    }



}
