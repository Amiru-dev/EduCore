package com.urbantech.smartcity360.servlet;

import com.urbantech.smartcity360.chain.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for Part C - Chain of Responsibility Pattern Demo
 * Demonstrates complaint escalation from local staff to city manager.
 */
@WebServlet("/chain")
public class ChainServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/chain.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String residentName = request.getParameter("residentName");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String severityStr = request.getParameter("severity");
        
        if (residentName == null || residentName.isEmpty()) residentName = "John Doe";
        if (description == null || description.isEmpty()) description = "General complaint";
        if (category == null || category.isEmpty()) category = "General";
        
        Complaint.Severity severity;
        switch (severityStr != null ? severityStr : "LOW") {
            case "MEDIUM": severity = Complaint.Severity.MEDIUM; break;
            case "HIGH": severity = Complaint.Severity.HIGH; break;
            case "CRITICAL": severity = Complaint.Severity.CRITICAL; break;
            default: severity = Complaint.Severity.LOW; break;
        }
        
        // Generate complaint ID
        String complaintId = "CMP-" + System.currentTimeMillis() % 10000;
        
        // Create the complaint
        Complaint complaint = new Complaint(complaintId, residentName, description, category, severity);
        
        // Build the Chain of Responsibility
        ComplaintHandler localStaff = new LocalStaffHandler("Mr. Perera");
        ComplaintHandler supervisor = new SupervisorHandler("Mrs. Fernando");
        ComplaintHandler cityManager = new CityManagerHandler("Dr. Wickramasinghe");
        
        // Link the chain: LocalStaff → Supervisor → CityManager
        localStaff.setNext(supervisor);
        supervisor.setNext(cityManager);
        
        // Process the complaint through the chain
        complaint.addToLog("Complaint submitted by " + residentName + " [Severity: " + severity.getLabel() + "]");
        String result = localStaff.handle(complaint);
        
        // Set attributes for JSP
        request.setAttribute("complaint", complaint);
        request.setAttribute("result", result);
        request.setAttribute("escalationLog", complaint.getEscalationLog());
        request.setAttribute("chainDescription", 
            "Mr. Perera (Local Staff) → Mrs. Fernando (Supervisor) → Dr. Wickramasinghe (City Manager)");
        
        request.getRequestDispatcher("/WEB-INF/views/chain.jsp").forward(request, response);
    }
}
