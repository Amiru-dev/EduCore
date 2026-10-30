package com.urbantech.smartcity360.servlet;

import com.urbantech.smartcity360.flyweight.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Servlet for Part D - Flyweight Pattern Demo
 * Demonstrates efficient memory management for thousands of smart street lights.
 */
@WebServlet("/flyweight")
public class FlyweightServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/flyweight.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String countStr = request.getParameter("lightCount");
        int lightCount = 100; // Default
        
        try {
            lightCount = Integer.parseInt(countStr);
            lightCount = Math.min(lightCount, 50000); // Cap at 50000
            lightCount = Math.max(lightCount, 10);    // Minimum 10
        } catch (NumberFormatException e) {
            lightCount = 100;
        }
        
        // Clear factory cache for fresh demo
        StreetLightFactory.clearCache();
        
        // Define the streetlight types (only 5 types for all lights)
        String[][] types = {
            {"SL-3000 LED", "150", "4000", "UrbanLight Corp", "v2.4.1", "Dusk to Dawn"},
            {"SL-2000 Solar", "100", "3500", "SolarTech Inc", "v1.8.3", "6PM to 6AM"},
            {"SL-4000 Smart", "200", "5000", "SmartGlow Ltd", "v3.1.0", "Adaptive"},
            {"SL-1500 Eco", "75", "3000", "GreenLight Co", "v2.0.5", "7PM to 5AM"},
            {"SL-5000 Pro", "250", "4500", "ProLight Systems", "v4.0.0", "Sensor-Based"}
        };
        
        String[] streets = {"Galle Road", "Duplication Road", "Marine Drive", 
                           "Havelock Road", "Baseline Road", "High Level Road",
                           "Bauddhaloka Mawatha", "Wijerama Mawatha"};
        
        Random rand = new Random(42); // Fixed seed for reproducibility
        
        // Create lights using Flyweight pattern
        List<StreetLightContext> lights = new ArrayList<>();
        long startTime = System.nanoTime();
        
        for (int i = 0; i < lightCount; i++) {
            String[] typeData = types[i % types.length];
            
            StreetLightType type = StreetLightFactory.getStreetLightType(
                    typeData[0], Integer.parseInt(typeData[1]),
                    Integer.parseInt(typeData[2]), typeData[3], typeData[4], typeData[5]);
            
            String street = streets[i % streets.length];
            String location = String.format("(%.4f, %.4f)", 
                    6.9 + rand.nextDouble() * 0.1, 79.8 + rand.nextDouble() * 0.1);
            
            StreetLightContext light = new StreetLightContext(
                    "SL-" + String.format("%05d", i + 1),
                    location, street, type);
            light.setBrightness(50 + rand.nextInt(51));
            light.setHoursOperatedToday(4 + rand.nextDouble() * 8);
            
            if (rand.nextInt(10) == 0) {
                light.turnOff(); // 10% chance of being off
            }
            
            lights.add(light);
        }
        
        long endTime = System.nanoTime();
        double creationTimeMs = (endTime - startTime) / 1_000_000.0;
        
        // Memory analysis
        int uniqueTypes = StreetLightFactory.getTypeCount();
        long intrinsicMemoryPerType = 200; // Estimated bytes
        long extrinsicMemoryPerLight = 50; // Estimated bytes
        
        long naiveMemory = (long) lightCount * (intrinsicMemoryPerType + extrinsicMemoryPerLight);
        long flyweightMemory = (uniqueTypes * intrinsicMemoryPerType) + ((long) lightCount * extrinsicMemoryPerLight);
        long memorySaved = naiveMemory - flyweightMemory;
        double savingsPercent = (memorySaved * 100.0) / naiveMemory;
        
        // Get sample lights for display (first 20)
        List<StreetLightContext> sampleLights = lights.subList(0, Math.min(20, lights.size()));
        
        // Count statistics
        long onCount = lights.stream().filter(StreetLightContext::isOn).count();
        double totalEnergy = lights.stream().mapToDouble(StreetLightContext::getEnergyConsumed).sum();
        
        // Set attributes
        request.setAttribute("lightCount", lightCount);
        request.setAttribute("uniqueTypes", uniqueTypes);
        request.setAttribute("sampleLights", sampleLights);
        request.setAttribute("flyweightTypes", StreetLightFactory.getAllTypes());
        request.setAttribute("naiveMemory", formatBytes(naiveMemory));
        request.setAttribute("flyweightMemory", formatBytes(flyweightMemory));
        request.setAttribute("memorySaved", formatBytes(memorySaved));
        request.setAttribute("savingsPercent", String.format("%.1f", savingsPercent));
        request.setAttribute("creationTimeMs", String.format("%.2f", creationTimeMs));
        request.setAttribute("onCount", onCount);
        request.setAttribute("offCount", lightCount - onCount);
        request.setAttribute("totalEnergy", String.format("%.2f", totalEnergy));
        
        request.getRequestDispatcher("/WEB-INF/views/flyweight.jsp").forward(request, response);
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1048576) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / 1048576.0);
    }
}
