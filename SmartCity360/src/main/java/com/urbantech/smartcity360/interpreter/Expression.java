package com.urbantech.smartcity360.interpreter;

/**
 * Abstract Expression - the base interface for all expressions
 * in the city policy DSL grammar.
 */
public interface Expression {
    
    /**
     * Interprets (evaluates) this expression against the given context.
     * @param context the city context containing variable values
     * @return true if the expression evaluates to true
     */
    boolean interpret(CityContext context);
    
    /**
     * Returns a human-readable string representation of this expression.
     * @return the expression as a string
     */
    String toExpressionString();
}
