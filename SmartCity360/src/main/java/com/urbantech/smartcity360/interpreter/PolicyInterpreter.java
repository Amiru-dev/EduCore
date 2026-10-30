package com.urbantech.smartcity360.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Policy Interpreter - parses DSL rule strings and evaluates them.
 * Manages a collection of PolicyRules and evaluates them against a context.
 * 
 * DSL Syntax:
 *   IF <condition> THEN <action>
 *   Conditions: <variable> <operator> <value>
 *   Logical: AND, OR, NOT
 *   Operators: =, !=, >, <, >=, <=
 * 
 * Examples:
 *   IF zone = "residential" AND time > 22:00 THEN noise_limit = 40
 *   IF parking_zone = "A" AND day != "Sunday" THEN parking_fee = 500
 */
public class PolicyInterpreter {
    
    private final List<PolicyRule> rules;
    
    public PolicyInterpreter() {
        this.rules = new ArrayList<>();
    }
    
    /**
     * Parses a DSL rule string and adds it to the rule set.
     * Format: IF <condition> THEN <action>
     * 
     * @param ruleName name/identifier for the rule
     * @param dslRule the DSL rule string
     * @param description human-readable description
     * @return the parsed PolicyRule
     */
    public PolicyRule parseRule(String ruleName, String dslRule, String description) {
        // Remove leading "IF " and split on " THEN "
        String normalized = dslRule.trim();
        
        if (normalized.toUpperCase().startsWith("IF ")) {
            normalized = normalized.substring(3).trim();
        }
        
        String[] parts = normalized.split("(?i)\\s+THEN\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid rule syntax. Expected: IF <condition> THEN <action>");
        }
        
        String conditionStr = parts[0].trim();
        String action = parts[1].trim();
        
        Expression condition = parseCondition(conditionStr);
        PolicyRule rule = new PolicyRule(ruleName, condition, action, description);
        rules.add(rule);
        
        return rule;
    }
    
    /**
     * Parses a condition string into an Expression tree.
     * Supports AND, OR operators and simple comparisons.
     */
    private Expression parseCondition(String conditionStr) {
        // Handle OR (lowest precedence)
        String upperStr = conditionStr.toUpperCase();
        int orIndex = findOperator(conditionStr, " OR ");
        if (orIndex != -1) {
            String left = conditionStr.substring(0, orIndex).trim();
            String right = conditionStr.substring(orIndex + 4).trim();
            return new OrExpression(parseCondition(left), parseCondition(right));
        }
        
        // Handle AND
        int andIndex = findOperator(conditionStr, " AND ");
        if (andIndex != -1) {
            String left = conditionStr.substring(0, andIndex).trim();
            String right = conditionStr.substring(andIndex + 5).trim();
            return new AndExpression(parseCondition(left), parseCondition(right));
        }
        
        // Handle NOT
        if (conditionStr.toUpperCase().startsWith("NOT ")) {
            String inner = conditionStr.substring(4).trim();
            return new NotExpression(parseCondition(inner));
        }
        
        // Handle parentheses
        if (conditionStr.startsWith("(") && conditionStr.endsWith(")")) {
            return parseCondition(conditionStr.substring(1, conditionStr.length() - 1).trim());
        }
        
        // Terminal expression: variable operator value
        return parseComparison(conditionStr);
    }
    
    /**
     * Finds the index of a logical operator, ignoring those inside quotes.
     */
    private int findOperator(String str, String operator) {
        String upper = str.toUpperCase();
        String upperOp = operator.toUpperCase();
        int depth = 0;
        boolean inQuotes = false;
        
        for (int i = 0; i <= upper.length() - upperOp.length(); i++) {
            char c = str.charAt(i);
            if (c == '"') inQuotes = !inQuotes;
            if (c == '(') depth++;
            if (c == ')') depth--;
            
            if (!inQuotes && depth == 0 && upper.substring(i, i + upperOp.length()).equals(upperOp)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Parses a simple comparison expression.
     * Format: <variable> <operator> <value>
     */
    private ComparisonExpression parseComparison(String comparison) {
        String[] operators = {">=", "<=", "!=", "=", ">", "<", "=="};
        
        for (String op : operators) {
            int idx = comparison.indexOf(op);
            if (idx != -1) {
                // Make sure we don't split on != when looking for =
                if (op.equals("=") && idx > 0 && comparison.charAt(idx - 1) == '!') {
                    continue;
                }
                if (op.equals("=") && idx > 0 && comparison.charAt(idx - 1) == '<') {
                    continue;
                }
                if (op.equals("=") && idx > 0 && comparison.charAt(idx - 1) == '>') {
                    continue;
                }
                
                String variable = comparison.substring(0, idx).trim();
                String value = comparison.substring(idx + op.length()).trim();
                return new ComparisonExpression(variable, op, value);
            }
        }
        
        throw new IllegalArgumentException("Cannot parse comparison: " + comparison);
    }
    
    /**
     * Evaluates all rules against the given context.
     * @param context the city context
     * @return combined evaluation results
     */
    public String evaluateAll(CityContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Policy Evaluation Report ===\n");
        sb.append("Context: ").append(context.toString()).append("\n\n");
        
        int triggered = 0;
        for (PolicyRule rule : rules) {
            sb.append(rule.evaluate(context));
            if (rule.isSatisfied(context)) triggered++;
            sb.append("\n");
        }
        
        sb.append("=== Summary: ").append(triggered).append("/").append(rules.size())
          .append(" rules triggered ===\n");
        
        return sb.toString();
    }
    
    /**
     * Adds a pre-built rule to the interpreter.
     * @param rule the policy rule to add
     */
    public void addRule(PolicyRule rule) {
        rules.add(rule);
    }
    
    /**
     * Returns all rules.
     * @return list of policy rules
     */
    public List<PolicyRule> getRules() {
        return new ArrayList<>(rules);
    }
    
    /**
     * Clears all rules.
     */
    public void clearRules() {
        rules.clear();
    }
}
