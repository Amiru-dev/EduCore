package com.urbantech.smartcity360.interpreter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Part E: Interpreter Pattern Tests")
public class InterpreterPatternTest {

    @Test
    @DisplayName("E.1: Terminal ComparisonExpression evaluates numeric and string operators")
    void testComparisonExpressions() {
        CityContext context = new CityContext();
        context.assign("zone", "residential");
        context.assign("noise_level", "65");
        context.assign("time", "23:00");

        // String equality
        Expression eqZone = new ComparisonExpression("zone", "=", "residential");
        assertTrue(eqZone.interpret(context));

        Expression neqZone = new ComparisonExpression("zone", "!=", "commercial");
        assertTrue(neqZone.interpret(context));

        // Numeric comparison
        Expression noiseGt = new ComparisonExpression("noise_level", ">", "50");
        assertTrue(noiseGt.interpret(context));

        Expression noiseLt = new ComparisonExpression("noise_level", "<", "50");
        assertFalse(noiseLt.interpret(context));

        // Time comparison (HH:MM converted to minutes)
        Expression timeGt = new ComparisonExpression("time", ">", "22:00");
        assertTrue(timeGt.interpret(context));
    }

    @Test
    @DisplayName("E.2: Logical Non-Terminal Expressions (AND, OR, NOT)")
    void testLogicalExpressions() {
        CityContext context = new CityContext();
        context.assign("zone", "school");
        context.assign("vehicle_speed", "45");

        Expression isSchool = new ComparisonExpression("zone", "=", "school");
        Expression isSpeeding = new ComparisonExpression("vehicle_speed", ">", "30");
        Expression isFastSpeeding = new ComparisonExpression("vehicle_speed", ">", "60");

        // AND
        Expression andExpr = new AndExpression(isSchool, isSpeeding);
        assertTrue(andExpr.interpret(context));

        Expression andFalse = new AndExpression(isSchool, isFastSpeeding);
        assertFalse(andFalse.interpret(context));

        // OR
        Expression orExpr = new OrExpression(isFastSpeeding, isSchool);
        assertTrue(orExpr.interpret(context));

        // NOT
        Expression notExpr = new NotExpression(isFastSpeeding);
        assertTrue(notExpr.interpret(context));
    }

    @Test
    @DisplayName("E.3: PolicyInterpreter parses and evaluates DSL rules")
    void testDslRuleParsingAndEvaluation() {
        PolicyInterpreter interpreter = new PolicyInterpreter();
        PolicyRule rule = interpreter.parseRule("NR-001",
                "IF zone = residential AND noise_level > 40 THEN enforce_noise_limit = 40dB",
                "Night residential noise restriction");

        assertEquals("NR-001", rule.getRuleName());
        assertEquals("enforce_noise_limit = 40dB", rule.getAction());

        // Context satisfying rule
        CityContext highNoiseCtx = new CityContext();
        highNoiseCtx.assign("zone", "residential");
        highNoiseCtx.assign("noise_level", "55");
        assertTrue(rule.isSatisfied(highNoiseCtx));

        String report = rule.evaluate(highNoiseCtx);
        assertTrue(report.contains("Result: TRUE ✓"));
        assertTrue(report.contains("Action Triggered: enforce_noise_limit = 40dB"));

        // Context NOT satisfying rule
        CityContext quietCtx = new CityContext();
        quietCtx.assign("zone", "residential");
        quietCtx.assign("noise_level", "30");
        assertFalse(rule.isSatisfied(quietCtx));
    }

    @Test
    @DisplayName("E.4: Multi-rule evaluation in CityContext")
    void testMultiRuleEvaluation() {
        PolicyInterpreter interpreter = new PolicyInterpreter();
        interpreter.parseRule("PK-001", "IF parking_zone = A AND day != Sunday THEN parking_fee = 500", "Zone A parking");
        interpreter.parseRule("TR-001", "IF vehicle_speed > 60 THEN issue_fine", "Speeding fine");

        CityContext ctx = new CityContext();
        ctx.assign("parking_zone", "A");
        ctx.assign("day", "Monday");
        ctx.assign("vehicle_speed", "75");

        String report = interpreter.evaluateAll(ctx);
        assertTrue(report.contains("parking_fee = 500"));
        assertTrue(report.contains("issue_fine"));
        assertTrue(report.contains("2/2 rules triggered"));
    }
}
