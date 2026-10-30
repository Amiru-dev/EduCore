package com.urbantech.smartcity360.interpreter;

/**
 * Non-Terminal Expression - logical OR operation.
 * Evaluates to true if EITHER left or right expression is true.
 */
public class OrExpression implements Expression {
    
    private final Expression left;
    private final Expression right;
    
    public OrExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean interpret(CityContext context) {
        return left.interpret(context) || right.interpret(context);
    }
    
    @Override
    public String toExpressionString() {
        return "(" + left.toExpressionString() + " OR " + right.toExpressionString() + ")";
    }
    
    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }
}
