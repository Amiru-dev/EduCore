package com.urbantech.smartcity360.interpreter;

/**
 * Policy Rule - associates a condition (Expression) with an action.
 * When the condition evaluates to true, the action is triggered.
 * 
 * Example: IF zone = "residential" AND time > 22:00 THEN noise_limit = 40
 */
public class PolicyRule {
    
    private final String ruleName;
    private final Expression condition;
    private final String action;
    private final String description;
    
    public PolicyRule(String ruleName, Expression condition, String action, String description) {
        this.ruleName = ruleName;
        this.condition = condition;
        this.action = action;
        this.description = description;
    }
    
    /**
     * Evaluates the rule against the given context.
     * @param context the city context
     * @return evaluation result with details
     */
    public String evaluate(CityContext context) {
        boolean result = condition.interpret(context);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Rule: ").append(ruleName).append("\n");
        sb.append("  Description: ").append(description).append("\n");
        sb.append("  Condition: ").append(condition.toExpressionString()).append("\n");
        sb.append("  Result: ").append(result ? "TRUE ✓" : "FALSE ✗").append("\n");
        
        if (result) {
            sb.append("  → Action Triggered: ").append(action).append("\n");
        } else {
            sb.append("  → No action taken (condition not met).\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Checks if the rule condition is satisfied.
     * @param context the city context
     * @return true if condition is met
     */
    public boolean isSatisfied(CityContext context) {
        return condition.interpret(context);
    }
    
    // Getters
    public String getRuleName() { return ruleName; }
    public Expression getCondition() { return condition; }
    public String getAction() { return action; }
    public String getDescription() { return description; }
}
