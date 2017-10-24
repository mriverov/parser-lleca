package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.generic.model.Hole;
import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.model.Struct;
import org.unq.parser.lleca.grammar.lleca.model.*;
import org.unq.parser.lleca.grammar.lleca.parser.ParseHelper;
import org.unq.parser.lleca.lexer.tokens.Token;

import java.util.*;
import java.util.stream.Collectors;

import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.KEYWORDS;
import static org.unq.parser.lleca.grammar.lleca.parser.ParseHelper.SYMBOLS;

public class GenericParser {


    private Grammar grammar;
    private List<String> nonTerminals = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();
    private List<String> nullables = new ArrayList<>();
    private FirstCalculator fc;
    private FollowCalculator foc;
    private ParseHelper ph = new ParseHelper();
    private String initialSymbol;
    Map<String, List<ProductionTerminalVO>> ll1Table = new HashMap<>();

    public GenericParser(Grammar grammar) {
        this.grammar = grammar;
        this.initialSymbol = grammar.getRules().get(0).getIdentifier().toString();
        this.calculateNonTerminals();
        this.calculateTerminals();
        this.calculateNullables(grammar);
        this.fc = new FirstCalculator(grammar, nullables);
        this.foc = new FollowCalculator(grammar, nullables);
    }

    public void parseGrammar(){
        validateLL1(grammar);
    }

    public void parseInput(List<Token> tokens){
        analize(initialSymbol,  tokens);
    }

    /*
    * Algoritmo recursivo que arma el árbol AST
    * */
    private STerm analize(String symbol, List<Token> tokens) {
        //TODO: ver el caso base de la lista vacía.
        if(tokens.isEmpty()){
            return new Hole();
        }
        if (terminals.contains(symbol)){
            Token b = tokens.get(0);
            if(b.value().equals(symbol)){
                return b.getLeaf(symbol, ll1Table);
            }
            else {
                System.out.println("Error: Se esperaba leer "+symbol+" pero se encontró "+b.value());
            }
        }
        //Si es un NO Terminal
        else {
            Token b = tokens.get(0);
            if (tableContainsProduction(symbol,b.value())){
                Production p = tableGetProduction(symbol, b.value());
                if (p.getExpantion().isPresent()){
                    int sSize = p.getExpantion().get().getSymbolsSize();
                    List<Symbol> symbols = p.getExpantion().get().getSymbols();
                    List<STerm> args = new ArrayList<>();
                    for (int i = 0; i < sSize; i++) {
                        tokens.remove(0);
                        args.add(analize(symbols.get(i).getCurrentValue(), tokens));
                        return null;
                        // return new Struct(symbol, Optional.of(args));
                    }
                }
            }
            else {
                System.out.println("Error: Se esperba leer "+symbol+" pero se encontró "+b.value());
            }
        }
        return null;

    }

    public Boolean tableContainsProduction(String nonTerminal, String terminal){
        if(ll1Table.containsKey(nonTerminal)){
            List<ProductionTerminalVO> prods = ll1Table.get(nonTerminal);
            for (ProductionTerminalVO prod:
                 prods) {
                if (prod.getTerminal().equals(terminal)) return true;
            }
        }
        return false;
    }

    public Production tableGetProduction(String nonTerminal, String terminal){
        List<ProductionTerminalVO> prods = ll1Table.get(nonTerminal);
        for (ProductionTerminalVO prod: prods){
            if (prod.getTerminal().equals(terminal)) return prod.getProduction();
        }
        return null;
    }


    public void validateLL1(Grammar grammar ){
        HashMap<String, Set<String>> first = this.fc.calculateFirst(this.nonTerminals, this.terminals);
        HashMap<String, Set<String>> follow = this.foc.getFollow(first);


        grammar.getRules().forEach(rule -> {
            String currentRule = rule.getIdentifier().getValue();
            List<ProductionTerminalVO> productionsByRule = new ArrayList<>();
            Set<String> currentFirst = first.get(currentRule);
            Set<String> currentFollow = follow.get(currentRule);

            rule.getProductions().forEach(production -> {
                if(production.getExpantion().isPresent()){
                    Expansion expansion = production.getExpantion().get();
                    if(expansion.getSymbols().size()>0){
                        String currentExpansionSymbol = expansion.getSymbols().get(0).getCurrentValue();

                        // its terminal and is first
                        if(currentFirst.contains(currentExpansionSymbol)){
                            productionsByRule.add(new ProductionTerminalVO(currentExpansionSymbol,
                                    production));
                        }else{
                            //is not terminal
                            if(nonTerminals.contains(currentExpansionSymbol)){
                                Set<String> firstOfNoTerminal = first.get(currentExpansionSymbol);
                                firstOfNoTerminal.forEach(f -> {
                                    if(currentFirst.contains(f)){
                                        if(f.equals("EPSILON")){
                                            productionsByRule.addAll(addFollowToTable(production, currentFollow));
                                        }else{
                                            productionsByRule.add(new ProductionTerminalVO(f,
                                                    production));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }else{
                    if(currentFirst.contains("EPSILON")){
                        productionsByRule.addAll(addFollowToTable(production, currentFollow));
                    }
                }
            });

            ll1Table.put(rule.getIdentifier().getValue(), productionsByRule);

            //validate that for each no termial there is only one VO
            validateTable(productionsByRule, currentFirst);
            validateTable(productionsByRule, currentFollow);
        });

        System.out.println("Validation result: The grammar is LL1");
    }

    private void validateTable(List<ProductionTerminalVO> productionsByRule, Set<String> terminals){
        terminals.forEach(cf -> {
            List<ProductionTerminalVO> prodToValid = productionsByRule.stream().filter(pru -> pru.getTerminal().equals(cf)).collect(Collectors.toList());
            if (prodToValid.size() > 1) {
                System.out.println("Validation result: The grammar is invalid, there is not LL1");
                throw new RuntimeException("Error building LL(1) table there is more that one production for " + cf);
            }
        });
    }

    private List<ProductionTerminalVO> addFollowToTable(Production production, Set<String> follow) {
        List<ProductionTerminalVO> prod = new ArrayList<>();
        follow.forEach(fo -> {
            prod.add(new ProductionTerminalVO(fo,
                    production));
        });

        return prod;
    }


    /*
    * Calculo los símbolos no terminales
    * Se asume que son los símbolos que dan nombre a las reglas.
    * */
    private void calculateNonTerminals(){
        this.grammar.getRules().forEach(rule -> {
            nonTerminals.add(rule.getIdentifier().toString());
        });
    }

    /*
    * Calculo los símbolos terminales.
    * Serán las palabras clave y los símbolos dentro de las expansiones de cada producción
    * que no sean palabras clave ni no terminales.
    * */
    private void calculateTerminals(){
        Map<String, List<String>>   x = ph.getKeywordsAndSymbols(this.grammar);
        x.get(KEYWORDS).forEach(keyword -> {
            terminals.add(keyword);
        });
        x.get(SYMBOLS).forEach(symbol -> {
            terminals.add(symbol);
        });


        terminals.add(Keyword.NUM.getValue());
        terminals.add(Keyword.ID.getValue());
        terminals.add(Keyword.STRING.getValue());


    }

    /*
       * Método que calcula los símbolos anulables.
       * */
    private void calculateNullables(Grammar grammar) {
        grammar.getRules().forEach(rule -> {
            rule.getProductions().forEach(prod -> {
                if (!prod.getExpantion().isPresent()){
                    nullables.add(rule.getIdentifier().getValue());
                };
            });
        });
    }
}
