package com.urbantech.smartcity360.chain;

/**
 * Concrete Handler - City Manager handles HIGH and CRITICAL severity complaints.
 * Examples: public safety hazards, major infrastructure failure, emergencies.
 */
public class CityManagerHandler extends ComplaintHandler {
    
    public CityManagerHandler(String managerName) {
        super(managerName, "City Manager", 4); // Can handle severity <= 4 (CRITICAL)
    }
    
    @Override
    protected String processComplaint(Complaint complaint) {
        complaint.setHandledBy(handlerName + " (City Manager)");
        complaint.setResolution("RESOLVED by City Manager - Priority Action Taken");
        complaint.addToLog("✓ " + handlerName + " (City Manager) resolved the complaint.");
        
        if (complaint.getSeverity() == Complaint.Severity.CRITICAL) {
            complaint.addToLog("⚠ CRITICAL: Emergency response team deployed.");
            complaint.addToLog("Action: Emergency meeting scheduled, public advisory issued.");
            return "🚨 Complaint " + complaint.getComplaintId() 
                   + " CRITICAL - Handled by City Manager: " + handlerName
                   + "\n   Action: Emergency response team deployed for '"
                   + complaint.getDescription() + "'";
        } else {
            complaint.addToLog("Action: Authorized budget allocation and assigned senior team.");
            return "✅ Complaint " + complaint.getComplaintId() 
                   + " resolved by City Manager: " + handlerName
                   + "\n   Action: Authorized budget and assigned senior team for '"
                   + complaint.getDescription() + "'";
        }
    }
}
