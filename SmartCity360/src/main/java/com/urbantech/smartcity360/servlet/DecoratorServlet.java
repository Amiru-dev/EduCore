package com.urbantech.smartcity360.servlet;

import com.urbantech.smartcity360.decorator.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for Part A - Decorator Pattern Demo
 * Demonstrates dynamic security wrappers for energy meter data streams.
 */
@WebServlet("/decorator")
public class DecoratorServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/decorator.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String meterId = request.getParameter("meterId");
        String inputData = request.getParameter("inputData");
        boolean useEncryption = "on".equals(request.getParameter("encryption"));
        boolean useAuth = "on".equals(request.getParameter("authentication"));
        boolean useCompression = "on".equals(request.getParameter("compression"));
        String authToken = request.getParameter("authToken");
        
        if (meterId == null || meterId.isEmpty()) meterId = "EM-001";
        if (inputData == null || inputData.isEmpty()) inputData = "kWh=1250.7;voltage=230;current=5.4";
        if (authToken == null || authToken.isEmpty()) authToken = "SMART-TOKEN-2024";
        
        // Create the base stream (Concrete Component)
        DataStream stream = new EnergyMeterStream(meterId);
        
        List<String> appliedLayers = new ArrayList<>();
        appliedLayers.add("Base: EnergyMeterStream");
        
        // Dynamically wrap with decorators based on user selection
        if (useAuth) {
            stream = new AuthenticationDecorator(stream, authToken);
            appliedLayers.add("+ Authentication Layer");
        }
        if (useEncryption) {
            stream = new EncryptionDecorator(stream, "AES-256");
            appliedLayers.add("+ Encryption Layer (AES-256)");
        }
        if (useCompression) {
            stream = new CompressionDecorator(stream, "GZIP");
            appliedLayers.add("+ Compression Layer (GZIP)");
        }
        
        // Write data through the decorated stream
        stream.writeData(inputData);
        
        // Read data back through the decorated stream
        String readResult = stream.readData();
        String description = stream.getDescription();
        
        // Set attributes for JSP
        request.setAttribute("meterId", meterId);
        request.setAttribute("inputData", inputData);
        request.setAttribute("readResult", readResult);
        request.setAttribute("description", description);
        request.setAttribute("appliedLayers", appliedLayers);
        request.setAttribute("useEncryption", useEncryption);
        request.setAttribute("useAuth", useAuth);
        request.setAttribute("useCompression", useCompression);
        request.setAttribute("authToken", authToken);
        
        request.getRequestDispatcher("/WEB-INF/views/decorator.jsp").forward(request, response);
    }
}
