package ece351.f.rdescent;

import ece351.util.CommandLine;
import ece351.util.Lexer;
import ece351.util.Todo351Exception;
import ece351.vhdl.VConstants;

public final class FRecursiveDescentRecognizer implements VConstants {
   
    private final Lexer lexer;

    public static void main(final String arg) {
    	main(new String[]{arg});
    }
    
    public static void main(final String[] args) {
    	final CommandLine c = new CommandLine(args);
        final Lexer lexer = new Lexer(c.readInputSpec());
        final FRecursiveDescentRecognizer r = new FRecursiveDescentRecognizer(lexer);
        r.recognize();
    }

    public FRecursiveDescentRecognizer(final Lexer lexer) {
        this.lexer = lexer;
    }

    public void recognize() {
        program();
    }

    void program() {
        while (!lexer.inspectEOF()) {
            formula();
        }
        lexer.consumeEOF();
    }

    void formula() {
        var();
        lexer.consume("<=");
        expr();
        lexer.consume(";");
    }
    
    void expr() { 
        term();
        while(lexer.inspect(OR)){
            lexer.consume(OR);
            term();
        }
    
    }
    void term() {
        factor();
        while(lexer.inspect(AND)){
            lexer.consume(AND);
            factor();
        }
    }
    void factor() { 
        if(lexer.inspect(NOT)){
        	lexer.consume(NOT);
            factor();
        }else if(lexer.inspect("(")){
            lexer.consume("(");
            expr();
            lexer.consume(")");
        }else if(peekConstant()){// we have a constant
            constant();
        }else{
            var();
        }
    }
    void var() { 
        lexer.consumeID();
    }
    void constant() { 
        lexer.consume("'");
        lexer.consume("0","1");
        lexer.consume("'");
    }
    // helper functions
    private boolean peekConstant() {
        final boolean result = lexer.inspect("'"); //constants start (and end) with single quote
    	return result;
    }

}

