package com.urbantech.smartcity360.chain;

/**
 * Concrete Handler - Local Staff handles LOW severity complaints.
 * Examples: noise complaints, minor littering, street light issues.
 */
public class LocalStaffHandler extends ComplaintHandler {
    
    public LocalStaffHandler(String staffName) {
        super(staffName, "Local Staff", 1); // Can handle severity <= 1 (LOW)
    }
    
    @Override
    protected String processComplaint(Complaint complaint) {
        complaint.setHandledBy(handlerName + " (Local Staff)");
        complaint.setResolution("RESOLVED by Local Staff");
        complaint.addToLog("✓ " + handlerName + " (Local Staff) resolved the complaint.");
        complaint.addToLog("Action: Dispatched local maintenance team.");
        
        return "✅ Complaint " + complaint.getComplaintId() 
               + " resolved by Local Staff: " + handlerName
               + "\n   Action: Dispatched local maintenance team for '"
               + complaint.getDescription() + "'";
    }
}
