package com.urbantech.smartcity360.servlet;

import com.urbantech.smartcity360.interpreter.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for Part E - Interpreter Pattern Demo
 * Demonstrates parsing and evaluation of city policies defined in DSL.
 */
@WebServlet("/interpreter")
public class InterpreterServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Provide sample rules and context
        request.setAttribute("sampleRules", getSampleRules());
        request.setAttribute("sampleContext", getSampleContext());
        
        request.getRequestDispatcher("/WEB-INF/views/interpreter.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get context variables from form
        String zone = request.getParameter("zone");
        String time = request.getParameter("time");
        String noiseLevel = request.getParameter("noiseLevel");
        String parkingZone = request.getParameter("parkingZone");
        String day = request.getParameter("day");
        String vehicleSpeed = request.getParameter("vehicleSpeed");
        
        // Set defaults
        if (zone == null || zone.isEmpty()) zone = "residential";
        if (time == null || time.isEmpty()) time = "23:00";
        if (noiseLevel == null || noiseLevel.isEmpty()) noiseLevel = "55";
        if (parkingZone == null || parkingZone.isEmpty()) parkingZone = "A";
        if (day == null || day.isEmpty()) day = "Monday";
        if (vehicleSpeed == null || vehicleSpeed.isEmpty()) vehicleSpeed = "70";
        
        // Build the context
        CityContext context = new CityContext();
        context.assign("zone", zone);
        context.assign("time", time);
        context.assign("noise_level", noiseLevel);
        context.assign("parking_zone", parkingZone);
        context.assign("day", day);
        context.assign("vehicle_speed", vehicleSpeed);
        
        // Create interpreter and parse rules
        PolicyInterpreter interpreter = new PolicyInterpreter();
        
        // Parse predefined city policy rules from DSL strings
        interpreter.parseRule("NR-001",
                "IF zone = residential AND noise_level > 40 THEN enforce_noise_limit = 40dB",
                "Noise restriction in residential zones");
        
        interpreter.parseRule("NR-002",
                "IF zone = commercial AND noise_level > 70 THEN enforce_noise_limit = 70dB",
                "Noise restriction in commercial zones");
        
        interpreter.parseRule("PK-001",
                "IF parking_zone = A AND day != Sunday THEN parking_fee = 500LKR",
                "Parking fee for Zone A on weekdays");
        
        interpreter.parseRule("PK-002",
                "IF parking_zone = B OR parking_zone = C THEN parking_fee = 300LKR",
                "Parking fee for Zones B and C");
        
        interpreter.parseRule("TR-001",
                "IF zone = school AND vehicle_speed > 30 THEN issue_speed_warning",
                "Speed limit enforcement near schools");
        
        interpreter.parseRule("TR-002",
                "IF vehicle_speed > 60 AND zone = residential THEN issue_speed_fine",
                "Speed fine in residential areas");
        
        // Parse custom rule if provided
        String customRule = request.getParameter("customRule");
        String customRuleName = request.getParameter("customRuleName");
        if (customRule != null && !customRule.trim().isEmpty()) {
            try {
                if (customRuleName == null || customRuleName.isEmpty()) {
                    customRuleName = "CUSTOM-001";
                }
                interpreter.parseRule(customRuleName, customRule, "User-defined custom rule");
                request.setAttribute("customRuleStatus", "Custom rule parsed successfully!");
            } catch (Exception e) {
                request.setAttribute("customRuleStatus", "Error parsing custom rule: " + e.getMessage());
            }
        }
        
        // Evaluate all rules
        String evaluationResult = interpreter.evaluateAll(context);
        
        // Count triggered rules
        long triggeredCount = interpreter.getRules().stream()
                .filter(r -> r.isSatisfied(context))
                .count();
        
        // Set attributes for JSP
        request.setAttribute("context", context);
        request.setAttribute("evaluationResult", evaluationResult);
        request.setAttribute("totalRules", interpreter.getRules().size());
        request.setAttribute("triggeredRules", triggeredCount);
        request.setAttribute("rules", interpreter.getRules());
        
        // Preserve form values
        request.setAttribute("zone", zone);
        request.setAttribute("time", time);
        request.setAttribute("noiseLevel", noiseLevel);
        request.setAttribute("parkingZone", parkingZone);
        request.setAttribute("day", day);
        request.setAttribute("vehicleSpeed", vehicleSpeed);
        request.setAttribute("customRule", customRule);
        request.setAttribute("customRuleName", customRuleName);
        
        request.setAttribute("sampleRules", getSampleRules());
        request.setAttribute("sampleContext", getSampleContext());
        
        request.getRequestDispatcher("/WEB-INF/views/interpreter.jsp").forward(request, response);
    }
    
    private String getSampleRules() {
        return "IF zone = residential AND noise_level > 40 THEN enforce_noise_limit = 40dB\n"
             + "IF zone = commercial AND noise_level > 70 THEN enforce_noise_limit = 70dB\n"
             + "IF parking_zone = A AND day != Sunday THEN parking_fee = 500LKR\n"
             + "IF parking_zone = B OR parking_zone = C THEN parking_fee = 300LKR\n"
             + "IF zone = school AND vehicle_speed > 30 THEN issue_speed_warning\n"
             + "IF vehicle_speed > 60 AND zone = residential THEN issue_speed_fine";
    }
    
    private String getSampleContext() {
        return "zone = residential, time = 23:00, noise_level = 55,\n"
             + "parking_zone = A, day = Monday, vehicle_speed = 70";
    }
}
