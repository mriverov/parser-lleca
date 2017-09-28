package org.unq.parser.lleca.lexer;

import org.unq.parser.lleca.lexer.tokens.*;
import org.unq.parser.lleca.lexer.tokens.reserved.GlobalSymbols;
import org.unq.parser.lleca.lexer.tokens.reserved.Keywords;
import org.unq.parser.lleca.lexer.tokens.reserved.Symbols;
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
import static java.util.stream.Collectors.toSet;

public class Tokenizer {
    private File file;
    private List<Token> tokens = new ArrayList<>();
    private Symbols symbols;
    private Keywords keywords;



    public Tokenizer(File file, Symbols symbols, Keywords keywords) {
        this.file = file;
        this.symbols = symbols;
        this.keywords = keywords;
    }

    public ParseResult tokenize(){
        if(file == null){
            return new ParseResult(Result.ERROR, "File not defined", Optional.of("Trying to get file to tokenize"));
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String lines = "";
            for(String line; (line = reader.readLine()) != null; ) {
                lines = lines+line+" ";
            }
            processFile(lines);
        } catch (IOException e) {
            return new ParseResult(Result.ERROR, "Could not read file", Optional.of("Trying to read file to tokenize"));
        }

        tokens.forEach(token-> System.out.println(token));
        return new ParseResult(Result.OK, "Lexer finish successfully", Optional.empty());
    }

    private void processFile(String file) {
        //List<String> keyWordsRegex = this.keywords.getReservedKeywords().stream().map(keyWord -> "^" + keyWord).collect(toList());
        List<String> reservedSymbolsRegex = this.symbols.getReservedSymbols().stream().map(symbol -> "^" + symbol).collect(toList());
        String identifiersRegex = "^[a-zA-Z_][a-zA-Z0-9_]+";
        String numberRegex = "^[0-9]+";


       while (!"".equals(file)){
            if (matches(numberRegex,file)){
                String[] result = this.split(numberRegex, file);
                tokens.add(new TokenNumeric(result[0]));
                file=result[1];
                continue;
            }
            if (matches(identifiersRegex,file)){
               String[] result = this.split(identifiersRegex, file);
               if (isAKeyword(result[0])){
                   tokens.add(new TokenLiteral(result[0]));
               }
               else tokens.add(new TokenIdentifier(result[0]));
               file=result[1];
               continue;
           }
           else file = "";

        }
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

    private boolean isReservedSymbol(String character, Symbols symbols) {
        return symbols.getReservedSymbols().contains(character);
    }

    public List<Token> getTokens()
    {
        return tokens;
    }

}
