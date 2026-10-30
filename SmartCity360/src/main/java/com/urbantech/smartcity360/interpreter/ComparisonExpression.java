package com.urbantech.smartcity360.interpreter;

/**
 * Terminal Expression - evaluates a simple comparison between
 * a context variable and a specified value.
 * 
 * Supports operators: =, !=, >, <, >=, <=
 * Examples: "zone = residential", "noise_level > 40", "time >= 22:00"
 */
public class ComparisonExpression implements Expression {
    
    private final String variableName;
    private final String operator;
    private final String value;
    
    public ComparisonExpression(String variableName, String operator, String value) {
        this.variableName = variableName.trim().toLowerCase();
        this.operator = operator.trim();
        this.value = value.trim().replace("\"", "");
    }
    
    @Override
    public boolean interpret(CityContext context) {
        Object contextValue = context.lookup(variableName);
        
        if (contextValue == null) {
            return false;
        }
        
        String contextStr = contextValue.toString().trim();
        
        switch (operator) {
            case "=":
            case "==":
                return contextStr.equalsIgnoreCase(value);
            case "!=":
                return !contextStr.equalsIgnoreCase(value);
            case ">":
                return toDouble(contextStr) > toDouble(value);
            case "<":
                return toDouble(contextStr) < toDouble(value);
            case ">=":
                return toDouble(contextStr) >= toDouble(value);
            case "<=":
                return toDouble(contextStr) <= toDouble(value);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    /**
     * Converts a string to double, handling time format (HH:MM) as well.
     */
    private double toDouble(String s) {
        try {
            // Handle time format like "22:00" → convert to minutes since midnight
            if (s.contains(":") && s.length() <= 5) {
                String[] parts = s.split(":");
                return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
            }
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return s.hashCode(); // Fallback for non-numeric comparisons
        }
    }
    
    @Override
    public String toExpressionString() {
        return variableName + " " + operator + " " + value;
    }
    
    // Getters
    public String getVariableName() { return variableName; }
    public String getOperator() { return operator; }
    public String getValue() { return value; }
}
