package com.urbantech.smartcity360.chain;

/**
 * Concrete Handler - Supervisor handles MEDIUM severity complaints.
 * Examples: road damage, water supply issues, repeated noise violations.
 */
public class SupervisorHandler extends ComplaintHandler {
    
    public SupervisorHandler(String supervisorName) {
        super(supervisorName, "Supervisor", 2); // Can handle severity <= 2 (MEDIUM)
    }
    
    @Override
    protected String processComplaint(Complaint complaint) {
        complaint.setHandledBy(handlerName + " (Supervisor)");
        complaint.setResolution("RESOLVED by Supervisor");
        complaint.addToLog("✓ " + handlerName + " (Supervisor) resolved the complaint.");
        complaint.addToLog("Action: Initiated investigation and assigned repair crew.");
        
        return "✅ Complaint " + complaint.getComplaintId() 
               + " resolved by Supervisor: " + handlerName
               + "\n   Action: Initiated investigation and assigned repair crew for '"
               + complaint.getDescription() + "'";
    }
}
