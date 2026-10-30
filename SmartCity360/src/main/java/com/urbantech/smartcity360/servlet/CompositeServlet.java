package com.urbantech.smartcity360.servlet;

import com.urbantech.smartcity360.composite.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for Part B - Composite Pattern Demo
 * Demonstrates hierarchical structure of smart zones with recursive operations.
 */
@WebServlet("/composite")
public class CompositeServlet extends HttpServlet {
    
    private CompositeZone city;
    
    @Override
    public void init() throws ServletException {
        // Initialize the city hierarchy once
        city = CityHierarchyFactory.createSampleCity();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Reset city for fresh view
        city = CityHierarchyFactory.createSampleCity();
        
        String report = city.getReport("");
        double totalUsage = city.reportUsage();
        
        request.setAttribute("city", city);
        request.setAttribute("report", report);
        request.setAttribute("totalUsage", String.format("%.1f", totalUsage));
        
        request.getRequestDispatcher("/WEB-INF/views/composite.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String targetZone = request.getParameter("targetZone");
        
        String operationResult = "";
        
        if ("shutdown".equals(action)) {
            if ("city".equals(targetZone)) {
                operationResult = city.shutdownPower();
            } else {
                // Find and shutdown specific zone
                for (ZoneComponent district : city.getChildren()) {
                    if (district.getName().contains(targetZone)) {
                        operationResult = district.shutdownPower();
                        break;
                    }
                    for (ZoneComponent street : district.getChildren()) {
                        if (street.getName().contains(targetZone)) {
                            operationResult = street.shutdownPower();
                            break;
                        }
                    }
                }
            }
        } else if ("restore".equals(action)) {
            if ("city".equals(targetZone)) {
                operationResult = city.restorePower();
            } else {
                for (ZoneComponent district : city.getChildren()) {
                    if (district.getName().contains(targetZone)) {
                        operationResult = district.restorePower();
                        break;
                    }
                    for (ZoneComponent street : district.getChildren()) {
                        if (street.getName().contains(targetZone)) {
                            operationResult = street.restorePower();
                            break;
                        }
                    }
                }
            }
        }
        
        String report = city.getReport("");
        double totalUsage = city.reportUsage();
        
        request.setAttribute("city", city);
        request.setAttribute("report", report);
        request.setAttribute("totalUsage", String.format("%.1f", totalUsage));
        request.setAttribute("operationResult", operationResult);
        request.setAttribute("lastAction", action);
        request.setAttribute("lastTarget", targetZone);
        
        request.getRequestDispatcher("/WEB-INF/views/composite.jsp").forward(request, response);
    }
}
