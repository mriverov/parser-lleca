package org.unq.parser.lleca.lexer;

import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.unq.parser.lleca.lexer.tokens.*;
import org.unq.parser.lleca.lexer.tokens.reserved.GlobalSymbols;
import org.unq.parser.lleca.lexer.tokens.reserved.ReservedKeywords;
import org.unq.parser.lleca.lexer.tokens.reserved.ReservedSymbols;
import org.unq.parser.lleca.status.ParseResult;
import org.unq.parser.lleca.status.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Tokenizer {
    private File file;
    private List<Token> tokens = new ArrayList<>();
    private ReservedSymbols symbols;
    private ReservedKeywords keywords;
    private GlobalSymbols globalSymbols = new GlobalSymbols();



    public Tokenizer(File file, ReservedSymbols symbols, ReservedKeywords keywords) {
        this.file = file;
        this.symbols = symbols;
        this.keywords = keywords;
    }

    public Pair<ParseResult, List<Token>> tokenize(){
        if(file == null){
            return new Pair<>(new ParseResult(Result.ERROR, "File not defined", Optional.of("Trying to get file to tokenize")), new ArrayList<>());
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String lines = "";
            for(String line; (line = reader.readLine()) != null; ) {
                lines = lines+line+" ";
            }
            ParseResult result = processFile(lines);
            return new Pair<>(result, tokens);

        } catch (IOException e) {
            return new Pair<>(new ParseResult(Result.ERROR, "Could not read file", Optional.of("Trying to read file to tokenize")), new ArrayList<Token>());
        }
    }

    private ParseResult processFile(String file) {
        //List<String> keyWordsRegex = this.keywords.getReservedKeywords().stream().map(keyWord -> "^" + keyWord).collect(toList());
        //List<String> reservedSymbolsRegex = this.symbols.getReservedSymbols().stream().map(symbol -> "^" + symbol).collect(toList());
        //List<String> globalSymbolsRegex = this.globalSymbols.getGsymbols().stream().map(symbol -> "^" + symbol).collect(toList());
        //String identifiersRegex = "^[a-zA-Z_][a-zA-Z0-9_]+";
        String identifiersRegex = "(^[a-zA-Z_][a-zA-Z0-9_])*\\w+";
        String numberRegex = "^[0-9]+";
        String quotationMarksRegex = "^([\"'])(?:(?=(\\\\?))\\2.)*?\\1";


       while (!"".equals(file)){
            //matchear los comentarios
           Character ch = file.charAt(0);
            if (StringUtils.isEmpty(file) ||  ch==' ' || ch=='\t' || ch=='\n' || ch=='\r' ){
                file = file.substring(1);
                continue;
            }
            else if (file.startsWith("/*")){
                while (!file.startsWith("*/")){
                    file = file.substring(1);
                }
                file = file.substring(2);
                continue;
            }
            else if (matches(numberRegex,file)){
                String[] result = this.split(numberRegex, file);
                tokens.add(new TokenNumeric(result[0]));
                file=result[1];
                continue;
            }
            else if (matches(identifiersRegex,file)){
               String[] result = this.split(identifiersRegex, file);
               if (isAKeyword(result[0])){
                   tokens.add(new TokenLiteral(result[0]));
               }
               else tokens.add(new TokenIdentifier(result[0]));
               file=result[1];
               continue;
           }
           //TODO: quitar las barras invertidas que queden dentro.
           else if (matches(quotationMarksRegex,file)){
               String[] result = this.split(quotationMarksRegex, file);
               tokens.add(new TokenString(result[0]));
               file=result[1];
               continue;
           }
           else if (matchesReservedSymbols(this.symbols.getReservedSymbols(), file)){
               String[] result = this.splitSymbols(this.symbols.getReservedSymbols(), file);
               tokens.add(new TokenLiteral(result[0]));
               file=result[1];
               continue;
           }


           else {
               int limit = file.length() > 10 ? 10 : file.length();
                return new ParseResult(Result.ERROR,  "Could not parse file", Optional.of("There is a lexical error near "+file.substring(0,limit)));
            }

        }
        return new ParseResult(Result.OK, "Lexer finish successfully", Optional.empty());
    }

    private String[] splitSymbols(List<String> reservedSymbols, String file) {
        for (String symbol: reservedSymbols) {
            if (file.startsWith(symbol)){
                String sy = file.substring(0,symbol.length());
                String rest = file.substring(symbol.length());
                return new String[]{sy, rest};
            }
        }
        return null;
    }


    private boolean matchesReservedSymbols(List<String> reservedSymbols, String file) {
        for (String symbol: reservedSymbols) {
            if (file.startsWith(symbol)){
                return true;
            }
        }
        return false;
    }

    private String[] split(String regex, String file) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(file);
        m.find();
        String token = m.group( 0 );
        String restOfFIle = file.substring(token.length(),file.length());
        return new String[]{token, restOfFIle};

        /*
        while(m.find()) {
            String token = m.group( 0 );
            String k = file.substring(token.length(),file.length());
        }
        return new String[0];*/
    }

    private boolean matches(String regex, String file){
        return file.matches(regex+".*");
    }


    private boolean isAKeyword(String token) {
        return this.keywords.getReservedKeywords().contains(token);
    }

    private boolean isReservedSymbol(String character, ReservedSymbols symbols) {
        return symbols.getReservedSymbols().contains(character);
    }

    public List<Token> getTokens()
    {
        return tokens;
    }

}
