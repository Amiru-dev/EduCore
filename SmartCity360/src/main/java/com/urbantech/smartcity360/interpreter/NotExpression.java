package com.urbantech.smartcity360.interpreter;

/**
 * Non-Terminal Expression - logical NOT operation.
 * Negates the result of the wrapped expression.
 */
public class NotExpression implements Expression {
    
    private final Expression expression;
    
    public NotExpression(Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public boolean interpret(CityContext context) {
        return !expression.interpret(context);
    }
    
    @Override
    public String toExpressionString() {
        return "NOT(" + expression.toExpressionString() + ")";
    }
    
    public Expression getExpression() { return expression; }
}
