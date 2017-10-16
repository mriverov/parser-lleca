package org.unq.parser.lleca.grammar.lleca.model;

import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.TokenIdentifier;
import org.unq.parser.lleca.lexer.tokens.TokenNumeric;
import org.unq.parser.lleca.lexer.tokens.TokenString;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Parser {

    private List<Token> tokens;

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
        List rules = Collections.EMPTY_LIST;
        int currentTokenIndex;
        for(currentTokenIndex= 0; currentTokenIndex<= tokens.size() - 1; currentTokenIndex++){
            Token currentToken = tokens.get(currentTokenIndex);

            if(currentToken instanceof TokenIdentifier){
                List<Production> productions = getProductions(currentTokenIndex);
                Rule currentRule = new Rule(new Identifier(currentToken.value()), productions);
                rules.add(currentRule);
                currentTokenIndex++;
            }
        }

        return new Grammar(rules);
    }

    /**
     * Build production
     * Defintion:
     * production ->
     *              "|" <expansion> "=>" <termino>
     * @param currentTokenIndex
     * @return List<Production>
     */
    private List<Production> getProductions(int currentTokenIndex) {
        List productions = Collections.EMPTY_LIST;
        int tokenIndex;
        for(tokenIndex = currentTokenIndex +1; tokenIndex <= tokens.size() - 1; tokenIndex++){
            Token currentToken = tokens.get(tokenIndex);
            if("|".equals(currentToken.value())){
                List symbols = Collections.EMPTY_LIST;
                tokenIndex++;
                currentToken = tokens.get(tokenIndex);
                if("=>".equals(currentToken.value())){
                    // in this case we process a term because expansion is empty
                    tokenIndex++;
                    Term term = getTerm(tokenIndex);
                    productions.add(new Production(Optional.empty(), term));
                }else{
                    // case expansion exists
                    tokenIndex++;
                    Term term = getTerm(tokenIndex);

                    tokenIndex++;

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
                            symbols.add(new Symbol(new Identifier(currentToken.value())));
                        }else if (currentToken instanceof  TokenIdentifier) {
                            symbols.add(new Symbol(String.valueOf(currentToken.value())));
                        }else{
                            symbolExist = false;
                        }

                        tokenIndex++;
                        currentToken = tokens.get(tokenIndex);
                    }
                    Expansion expansion = new Expansion(symbols);
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
     * @param tokenIndex
     * @return
     */
    private Term getTerm(int tokenIndex) {
        Token currentToken = tokens.get(tokenIndex);

        if("_".equals(currentToken)){
            return new Term();
        }else if(currentToken instanceof  TokenIdentifier){
            Identifier identifier = new Identifier(currentToken.value());
            tokenIndex++;
            currentToken = tokens.get(tokenIndex);
            //find arguments
            if("(".equals(currentToken.value())){
                tokenIndex++;
                currentToken = tokens.get(tokenIndex);
                if(")".equals(currentToken.value())){
                    // case that argument list is empty
                    return new Term(identifier, Optional.empty());
                }else{
                    // process argument list
                    tokenIndex++;
                    Term term = getTerm(tokenIndex);
                    // process argument list cont
                    tokenIndex++;
                    currentToken = tokens.get(tokenIndex);
                    if(",".equals(currentToken.value())){
                        List<Term> terms = Collections.EMPTY_LIST;

                        while(",".equals(currentToken.value())){
                            // get all terms separete by ,
                            tokenIndex++;
                            terms.add(getTerm(tokenIndex));
                            tokenIndex++;
                        }
                        // check that argument list ends
                        if(")".equals(currentToken.value())){
                            ArgumentListCont argumentListCont = new ArgumentListCont(terms);

                            ArgumentList argumentList = new ArgumentList(term, Optional.of(argumentListCont));
                            Argument argument = new Argument(argumentList);
                            return new Term(identifier, Optional.of(argument));
                        }
                    }else{
                        // in this case argument list is empty
                        ArgumentList argumentList = new ArgumentList(term, Optional.empty());
                        Argument argument = new Argument(argumentList);
                        return new Term(identifier, Optional.of(argument));
                    }

                }


            }
        }else if(currentToken instanceof TokenString){
            return new Term(currentToken.value());
        }else if(currentToken instanceof TokenNumeric){
            return new Term(Integer.valueOf(currentToken.value()));
        }else{
            if("$".equals(currentToken.value())){
                tokenIndex++;
                currentToken = tokens.get(tokenIndex);
                if(currentToken instanceof TokenNumeric){
                    Integer numericValue = Integer.valueOf(currentToken.value());
                    tokenIndex++;
                    currentToken = tokens.get(tokenIndex);
                    if("[".equals(currentToken.value())){
                        tokenIndex++;
                        Term substitutionTerm = getTerm(tokenIndex);
                        tokenIndex++;
                        if("]".equals(currentToken.value())){

                            return new Term(numericValue, Optional.of(new Substitution(substitutionTerm)));
                        }
                    }else{
                        // case for numeric but empty substitution
                        return new Term(numericValue, Optional.empty());
                    }
                }
                throw new RuntimeException("Error trying to parse for token "+currentToken.value());
            }else{
                throw new RuntimeException("Error trying to parse for token "+currentToken.value());
            }
        }

        throw new RuntimeException("Error trying to parse for token "+currentToken.value());
    }
}
