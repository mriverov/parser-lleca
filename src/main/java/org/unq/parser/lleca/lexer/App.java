package org.unq.parser.lleca.lexer;

import javafx.util.Pair;
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


        //test(llecaKeyWords, llecaSymbols);


        ReservedSymbols llecaS = new ReservedSymbols(llecaSymbols);

        File gramatica = new File("./files/alumnos.ll");
        Tokenizer elLexer = new Tokenizer(gramatica, llecaS, llecaK);

        Pair<ParseResult,List<Token>> tokens = elLexer.tokenize();
        tokens.getValue().forEach(token-> System.out.println(token));

        System.out.println(tokens.getKey());

        Parser p = new Parser(tokens.getValue());
        Grammar grammar = p.parse();
        Map result = ParseHelper.getKeywordsAndSymbols(grammar);
        System.out.println(result);





        /*String pep = "/*";
        String juan = "hola soy un /*comen";
        System.out.println(juan.startsWith(pep));

    /*    Pattern p = Pattern.compile("^\\||");
        Matcher m = p.matcher("||hola");
        m.find();
        String token = m.group( 0 );
*/

    /*    String identifiersRegex = "(^[a-zA-Z_][a-zA-Z0-9_])*\\w+";

        String iden = "expresion | termino expresion1 => _";


        Pattern p = Pattern.compile(identifiersRegex);
        Matcher m = p.matcher(iden);
        List<String> tokens = new LinkedList<>();

        while(m.find()) {
            String token = m.group( 0 );
            //group 0 is always the entire match
            //k es el resto
            System.out.printf(token +"\n");
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
