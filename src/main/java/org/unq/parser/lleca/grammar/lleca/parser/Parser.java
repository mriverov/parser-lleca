package org.unq.parser.lleca.grammar.lleca.parser;

import org.unq.parser.lleca.grammar.lleca.model.*;
import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.TokenIdentifier;
import org.unq.parser.lleca.lexer.tokens.TokenNumeric;
import org.unq.parser.lleca.lexer.tokens.TokenString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Parser {

    private List<Token> tokens;
    private static int globalTokenIndex = 0;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    /**
     * Build the grammar reading a .ll file
     * Defintion:
     * gramatica -> epsilon
     *              <regla> <gramatica>
     * @return Grammar
     */
    public Grammar parse(){
        List<Rule> rules = new ArrayList<>();
        int currentTokenIndex;
        //for(currentTokenIndex= globalTokenIndex; currentTokenIndex<= tokens.size() - 1; currentTokenIndex++){
        while(globalTokenIndex < tokens.size()){
            Token currentToken = tokens.get(globalTokenIndex);

            if(currentToken instanceof TokenIdentifier){
                globalTokenIndex++;
                List<Production> productions = getProductions();
                Rule currentRule = new Rule(new Identifier(currentToken.value()), productions);
                rules.add(currentRule);
               // currentTokenIndex++;
            }
        }

        return new Grammar(rules);
    }

    /**
     * Build production
     * Defintion:
     * production ->
     *              "|" <expansion> "=>" <termino>
     * @return List<Production>
     */
    private List<Production> getProductions() {
        List<Production> productions = new ArrayList<>();

        while(globalTokenIndex < tokens.size()){
            Token currentToken = tokens.get(globalTokenIndex);
            if(currentToken instanceof TokenIdentifier){

                return productions;
            }
            if("|".equals(currentToken.value())){
                List<Symbol> symbols = new ArrayList<>();
                globalTokenIndex++;
                currentToken = tokens.get(globalTokenIndex);
                if("=>".equals(currentToken.value())){
                    // in this case we process a term because expansion is empty
                    globalTokenIndex++;
                    Term term = getTerm();
                    productions.add(new Production(Optional.empty(), term));
                }else{
                    // case expansion exists
                    //tokenIndex++;


                    // get all symbols
                    boolean symbolExist = true;
                    while(symbolExist){
                        if ("ID".equals(currentToken.value())){
                            symbols.add(new Symbol(Keyword.ID));
                        }else if ("STRING".equals(currentToken.value())){
                            symbols.add(new Symbol(Keyword.STRING));
                        }else if ("NUM".equals(currentToken.value())){
                            symbols.add(new Symbol(Keyword.NUM));
                        }else if (currentToken instanceof  TokenString){
                            symbols.add(new Symbol(String.valueOf(currentToken.value())));
                        }else if (currentToken instanceof  TokenIdentifier) {
                            symbols.add(new Symbol(new Identifier(currentToken.value())));
                        }else{
                            symbolExist = false;
                        }

                        globalTokenIndex++;
                        currentToken = tokens.get(globalTokenIndex);
                    }

                    Expansion expansion = new Expansion(symbols);
                    Term term = getTerm();

                    //globalTokenIndex++;
                    productions.add(new Production(Optional.of(expansion), term));
                }

            }
        }

        return productions;
    }

    /**
     * Build term. Look the definition
     * termino -> "_"
     *            <identificador> <argumentos>
     *            <cadena>
     *            <numero>
     *            "$" <numero> <substitution>
     * @return
     */
    private Term getTerm() {
        Token currentToken = tokens.get(globalTokenIndex);

        if("_".equals(currentToken.value())){
            globalTokenIndex++;

            return new Term();
        }else if(currentToken instanceof  TokenIdentifier){
            Identifier identifier = new Identifier(currentToken.value());
            globalTokenIndex++;
            if(globalTokenIndex >= tokens.size()){
                return new Term(identifier, Optional.empty());
            }
            currentToken = tokens.get(globalTokenIndex);
            if ("|".equals(currentToken.value())){
                return new Term(identifier, Optional.empty());
            }
            if(")".equals(currentToken.value())){
                return new Term(identifier, Optional.empty());
            }
            //find arguments
            if("(".equals(currentToken.value())){
                globalTokenIndex++;
                currentToken = tokens.get(globalTokenIndex);
                if(")".equals(currentToken.value())){
                    // case that argument list is empty
                    return new Term(identifier, Optional.empty());
                }else{
                    // process argument list
                    //tokenIndex++;
                    //acá el término retorna un numeric+substitution, pero es un argumento...
                    Term term = getTerm();
                    // process argument list cont
                    currentToken = tokens.get(globalTokenIndex);
                    List<Term> terms = new ArrayList<>();

                    if(",".equals(currentToken.value()) || ")".equals(currentToken.value())){

                        while(",".equals(currentToken.value())){
                            // get all terms separete by ,
                            globalTokenIndex++;
                            terms.add(getTerm());
                            //globalTokenIndex++;
                            currentToken = tokens.get(globalTokenIndex);
                        }

                        // check that argument list ends
                        if(")".equals(currentToken.value())){

                            ArgumentListCont argumentListCont = new ArgumentListCont(terms);

                            ArgumentList argumentList = new ArgumentList(term, Optional.of(argumentListCont));
                            Argument argument = new Argument(argumentList);
                            globalTokenIndex++;
                            return new Term(identifier, Optional.of(argument));
                        }

                    }

                    else{
                        // in this case argument list is empty
                        ArgumentList argumentList = new ArgumentList(term, Optional.empty());
                        Argument argument = new Argument(argumentList);
                        return new Term(identifier, Optional.of(argument));
                    }


                }


            }else if (currentToken instanceof TokenIdentifier){
                return new Term(identifier, Optional.empty());
            }
        }else if(currentToken instanceof TokenString){
            globalTokenIndex++;
            return new Term(currentToken.value());
        }else if(currentToken instanceof TokenNumeric){
            globalTokenIndex++;
            return new Term(Integer.valueOf(currentToken.value()));
        } else{
            if("$".equals(currentToken.value())){
                globalTokenIndex++;
                currentToken = tokens.get(globalTokenIndex);
                if(currentToken instanceof TokenNumeric){
                    Integer numericValue = Integer.valueOf(currentToken.value());
                    globalTokenIndex++;
                    currentToken = tokens.get(globalTokenIndex);
                    if("[".equals(currentToken.value())){
                        globalTokenIndex++;
                        Term substitutionTerm = getTerm();
                        currentToken = tokens.get(globalTokenIndex);
                        //globalTokenIndex++;
                        if("]".equals(currentToken.value())){
                            globalTokenIndex++;
                            return new Term(numericValue, Optional.of(new Substitution(substitutionTerm)));
                        }
                    }else{
                        // case for numeric but empty substitution
                        return new Term(numericValue, Optional.empty());
                    }
                }
                throw new RuntimeException("Error trying to parse for token "+currentToken.value() + " " + globalTokenIndex);
            }else{
                throw new RuntimeException("Error trying to parse for token "+currentToken.value()+ " " + globalTokenIndex);
            }
        }

        throw new RuntimeException("Error trying to parse for token "+currentToken.value()+ " " + globalTokenIndex);
    }
}
