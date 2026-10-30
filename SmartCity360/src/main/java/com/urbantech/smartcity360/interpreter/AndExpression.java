package com.urbantech.smartcity360.interpreter;

/**
 * Non-Terminal Expression - logical AND operation.
 * Evaluates to true only if BOTH left and right expressions are true.
 */
public class AndExpression implements Expression {
    
    private final Expression left;
    private final Expression right;
    
    public AndExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean interpret(CityContext context) {
        return left.interpret(context) && right.interpret(context);
    }
    
    @Override
    public String toExpressionString() {
        return "(" + left.toExpressionString() + " AND " + right.toExpressionString() + ")";
    }
    
    public Expression getLeft() { return left; }
    public Expression getRight() { return right; }
}
