package com.urbantech.smartcity360.chain;

/**
 * PART C - Chain of Responsibility Pattern: Escalation of Citizen Complaints
 * 
 * Abstract Handler - defines the interface for handling complaints
 * and maintains a reference to the next handler in the chain.
 */
public abstract class ComplaintHandler {
    
    protected ComplaintHandler nextHandler;
    protected final String handlerName;
    protected final String role;
    protected final int maxSeverityLevel; // Maximum severity this handler can process
    
    public ComplaintHandler(String handlerName, String role, int maxSeverityLevel) {
        this.handlerName = handlerName;
        this.role = role;
        this.maxSeverityLevel = maxSeverityLevel;
    }
    
    /**
     * Sets the next handler in the chain.
     * @param nextHandler the next handler to escalate to
     * @return the next handler (for chaining setups)
     */
    public ComplaintHandler setNext(ComplaintHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }
    
    /**
     * Handles the complaint or passes it to the next handler.
     * @param complaint the complaint to handle
     * @return result message describing the handling outcome
     */
    public String handle(Complaint complaint) {
        if (canHandle(complaint)) {
            return processComplaint(complaint);
        } else {
            complaint.addToLog(handlerName + " (" + role + ") cannot handle severity " 
                    + complaint.getSeverity().getLabel() + " - escalating...");
            
            if (nextHandler != null) {
                return nextHandler.handle(complaint);
            } else {
                complaint.addToLog("⚠ No handler available - complaint unresolved!");
                complaint.setResolution("UNRESOLVED");
                return "⚠ Complaint " + complaint.getComplaintId() 
                       + " could not be resolved. No handler in chain.";
            }
        }
    }
    
    /**
     * Determines whether this handler can process the complaint.
     * @param complaint the complaint to evaluate
     * @return true if this handler can handle the complaint
     */
    protected boolean canHandle(Complaint complaint) {
        return complaint.getSeverity().getLevel() <= maxSeverityLevel;
    }
    
    /**
     * Processes the complaint. Implemented by each concrete handler.
     * @param complaint the complaint to process
     * @return result message
     */
    protected abstract String processComplaint(Complaint complaint);
    
    public String getHandlerName() { return handlerName; }
    public String getRole() { return role; }
    public int getMaxSeverityLevel() { return maxSeverityLevel; }
}
