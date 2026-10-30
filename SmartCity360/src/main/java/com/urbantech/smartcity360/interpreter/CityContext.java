package com.urbantech.smartcity360.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * PART E - Interpreter Pattern: Interpreting City Policies Defined in DSL
 * 
 * Context class - holds the runtime variables and their values
 * against which policy expressions are evaluated.
 */
public class CityContext {
    
    private final Map<String, Object> variables;
    
    public CityContext() {
        this.variables = new HashMap<>();
    }
    
    /**
     * Assigns a value to a variable in the context.
     * @param name variable name
     * @param value variable value
     */
    public void assign(String name, Object value) {
        variables.put(name.toLowerCase(), value);
    }
    
    /**
     * Looks up the value of a variable.
     * @param name variable name
     * @return the value, or null if not found
     */
    public Object lookup(String name) {
        return variables.get(name.toLowerCase());
    }
    
    /**
     * Checks if a variable exists in the context.
     * @param name variable name
     * @return true if the variable exists
     */
    public boolean contains(String name) {
        return variables.containsKey(name.toLowerCase());
    }
    
    /**
     * Returns all variables in the context.
     * @return map of variable name to value
     */
    public Map<String, Object> getAllVariables() {
        return new HashMap<>(variables);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CityContext {\n");
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            sb.append("  ").append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
