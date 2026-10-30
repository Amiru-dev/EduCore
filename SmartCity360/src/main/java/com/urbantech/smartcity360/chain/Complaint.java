package com.urbantech.smartcity360.chain;

/**
 * Represents a citizen complaint with severity level and details.
 */
public class Complaint {
    
    public enum Severity {
        LOW(1, "Low"),
        MEDIUM(2, "Medium"),
        HIGH(3, "High"),
        CRITICAL(4, "Critical");
        
        private final int level;
        private final String label;
        
        Severity(int level, String label) {
            this.level = level;
            this.label = label;
        }
        
        public int getLevel() {
            return level;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    private final String complaintId;
    private final String residentName;
    private final String description;
    private final String category;
    private final Severity severity;
    private String resolution;
    private String handledBy;
    private final StringBuilder escalationLog;
    
    public Complaint(String complaintId, String residentName, String description, 
                     String category, Severity severity) {
        this.complaintId = complaintId;
        this.residentName = residentName;
        this.description = description;
        this.category = category;
        this.severity = severity;
        this.resolution = "PENDING";
        this.handledBy = "Unassigned";
        this.escalationLog = new StringBuilder();
    }
    
    public void addToLog(String entry) {
        escalationLog.append("→ ").append(entry).append("\n");
    }
    
    // Getters and setters
    public String getComplaintId() { return complaintId; }
    public String getResidentName() { return residentName; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Severity getSeverity() { return severity; }
    
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    
    public String getHandledBy() { return handledBy; }
    public void setHandledBy(String handledBy) { this.handledBy = handledBy; }
    
    public String getEscalationLog() { return escalationLog.toString(); }
    
    @Override
    public String toString() {
        return String.format("Complaint[%s] by %s | Severity: %s | Category: %s | Status: %s | Handler: %s",
                complaintId, residentName, severity.getLabel(), category, resolution, handledBy);
    }
}
