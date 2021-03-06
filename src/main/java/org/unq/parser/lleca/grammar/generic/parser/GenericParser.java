package org.unq.parser.lleca.grammar.generic.parser;

import org.unq.parser.lleca.grammar.generic.model.Hole;
import org.unq.parser.lleca.grammar.generic.model.STerm;
import org.unq.parser.lleca.grammar.generic.model.Struct;
import org.unq.parser.lleca.grammar.lleca.model.*;
import org.unq.parser.lleca.grammar.lleca.parser.ParseHelper;
import org.unq.parser.lleca.lexer.tokens.Token;
import org.unq.parser.lleca.lexer.tokens.TokenIdentifier;
import org.unq.parser.lleca.lexer.tokens.TokenNumeric;
import org.unq.parser.lleca.lexer.tokens.TokenString;

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
        Term h = analize(initialSymbol,  tokens);
        h.print();
    }

    /*
    * Algoritmo recursivo que arma el árbol AST
    * */
    private Term analize(String symbol, List<Token> tokens) {
        if(tokens.isEmpty() && accepts(symbol)){
            Production p = tableGetProduction(symbol, "$");
            String finalSign = p.getTerm().getIdentifier().get().toString();
            return new Term(finalSign);
        }
        if (terminals.contains(symbol)){
            Token b = tokens.get(0);
            if (b instanceof TokenNumeric && ("NUM").equals(symbol) || b instanceof TokenIdentifier && ("ID").equals(symbol)
            || b instanceof TokenString && ("STRING").equals(symbol) || symbol.equals(b.value())){
                tokens.remove(0);
                return b.getLeaf();
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
                List<Term> args = new ArrayList<>();
                if (p.getExpantion().isPresent()){
                    int sSize = p.getExpantion().get().getSymbolsSize();
                    List<Symbol> symbols = p.getExpantion().get().getSymbols();
                    for (int i = 0; i < sSize; i++) {
                        args.add(analize(symbols.get(i).getCurrentValue(), tokens));
                    }
                    return getAction(symbol, b.value(), args);
                }else{
                    //este es el caso cuando no hay expansión: la prod deriva desde EPSILON, --UNSOLVED
                   // String finalSign = p.getTerm().getIdentifier().get().toString();
                    //return new Term(finalSign);
                    return getAction(symbol, b.value(), args);
                }
            }

            else {
                System.out.println("Error: Se esperba leer "+symbol+" pero se encontró "+b.value());
            }
        }
        return null;

    }

    private Production tableGetEpsilonProduction(String symbol) {
        return null;
    }

    /*
    * Indica si el sìmbolo llega al estado de aceptación
    * */
    private boolean accepts(String symbol) {
        return (tableContainsProduction(symbol, "$"));
    }


    /*
    * Devuelve la derivación a EPSILON para un símbolo (UNUSED)
    * */
    private Term getExpsilonDerivationFor(String symbol) {
        List<Rule> rules = grammar.getRules();
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getIdentifier().equals(symbol)){
                List<Production> prods = rules.get(i).getProductions();
                for (int j = 0; j < prods.size(); j++) {
                    if (!prods.get(j).getExpantion().isPresent())
                        return new Term(prods.get(j).getTerm().toString());
                }
            }
        }
        return null;
    }

    /*
    * Verifica si un símbolo deriva a EPSILON
    * */
    private boolean derivesEpsilon(String symbol) {
        List<Rule> rules = grammar.getRules();
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).getIdentifier().getValue().equals(symbol)){
                List<Production> prods = rules.get(i).getProductions();
                for (int j = 0; j < prods.size(); j++) {
                    if (!prods.get(j).getExpantion().isPresent())
                        return true;
                }
            }
        }
        return false;
    }


    /*
    * Método que retorna la acción de un símbolo dado el un terminal y una lista de argumentos
    * */
    private Term getAction(String symbol, String b, List<Term> args) {
        Production prod = tableGetProduction(symbol, b);
        Term terms = prod.getTerm();


        if (!prod.getExpantion().isPresent()){
            if (terms.isIdentifierAndArgument()){
                return new Term(terms.getIdentifier().get().toString());
            }
            else return new Term(terms.toString());
        }
        if (terms.isString() || terms.isNumeric()){
            return terms;
        }
        if (terms.isIdentifierAndArgument()){
            return new ActionArgTerm(terms.getIdentifier().get(), terms.getArgument(), terms, args);
        }

        if (terms.isNumericAndSubstitution()){
            return new ActionSubstTerm(terms.getNum().get(), terms.getSubstitution(), terms, args);
        }

        return terms;
    }


    /*
    * Chequea si en la tabla LL1, un no terminal tiene una producción que arranque con el terminal dado.
    * */
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

    /*
    * Devuelve la producción de la tabla LL1 que relaciona el No terminal y el terminal dados
    * */
    public Production tableGetProduction(String nonTerminal, String terminal){
        List<ProductionTerminalVO> prods = ll1Table.get(nonTerminal);
        for (ProductionTerminalVO prod: prods){
            if (prod.getTerminal().equals(terminal)) return prod.getProduction();
        }
        return null;
    }


    /*
    * Valida que la tabla armada sea LL1
    * */
    public void validateLL1(Grammar grammar ){
        System.out.print("Start validating grammar");
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
