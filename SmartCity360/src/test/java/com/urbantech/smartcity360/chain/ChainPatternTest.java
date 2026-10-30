package com.urbantech.smartcity360.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Part C: Chain of Responsibility Pattern Tests")
public class ChainPatternTest {

    private ComplaintHandler chain;

    @BeforeEach
    void setUp() {
        ComplaintHandler localStaff = new LocalStaffHandler("Staff Alice");
        ComplaintHandler supervisor = new SupervisorHandler("Supervisor Bob");
        ComplaintHandler cityManager = new CityManagerHandler("Manager Carol");

        localStaff.setNext(supervisor).setNext(cityManager);
        this.chain = localStaff;
    }

    @Test
    @DisplayName("C.1: Low severity complaint resolved by Local Staff")
    void testLowSeverityHandling() {
        Complaint complaint = new Complaint("CMP-001", "Citizen John", "Litter on sidewalk",
                "Sanitation", Complaint.Severity.LOW);

        String result = chain.handle(complaint);

        assertEquals("RESOLVED by Local Staff", complaint.getResolution());
        assertTrue(complaint.getHandledBy().contains("Staff Alice"));
        assertTrue(result.contains("resolved by Local Staff"));
        assertTrue(complaint.getEscalationLog().contains("Staff Alice (Local Staff) resolved"));
    }

    @Test
    @DisplayName("C.2: Medium severity complaint escalated and resolved by Supervisor")
    void testMediumSeverityEscalation() {
        Complaint complaint = new Complaint("CMP-002", "Citizen Mary", "Broken water pipe on street",
                "Utilities", Complaint.Severity.MEDIUM);

        String result = chain.handle(complaint);

        assertEquals("RESOLVED by Supervisor", complaint.getResolution());
        assertTrue(complaint.getHandledBy().contains("Supervisor Bob"));
        assertTrue(result.contains("resolved by Supervisor"));
        // Check audit log shows escalation through Staff Alice first
        assertTrue(complaint.getEscalationLog().contains("Staff Alice (Local Staff) cannot handle"));
        assertTrue(complaint.getEscalationLog().contains("Supervisor Bob (Supervisor) resolved"));
    }

    @Test
    @DisplayName("C.3: High severity complaint escalated to City Manager")
    void testHighSeverityEscalation() {
        Complaint complaint = new Complaint("CMP-003", "Citizen Dave", "Major power grid transformer failure",
                "Energy", Complaint.Severity.HIGH);

        String result = chain.handle(complaint);

        assertTrue(complaint.getResolution().contains("City Manager"));
        assertTrue(complaint.getHandledBy().contains("Manager Carol"));
        assertTrue(result.contains("resolved by City Manager"));
        assertTrue(complaint.getEscalationLog().contains("Staff Alice (Local Staff) cannot handle"));
        assertTrue(complaint.getEscalationLog().contains("Supervisor Bob (Supervisor) cannot handle"));
        assertTrue(complaint.getEscalationLog().contains("Manager Carol (City Manager) resolved"));
    }

    @Test
    @DisplayName("C.4: Critical severity triggers emergency response from City Manager")
    void testCriticalSeverityEmergency() {
        Complaint complaint = new Complaint("CMP-004", "Citizen Elena", "Chemical spill near school zone",
                "Safety", Complaint.Severity.CRITICAL);

        String result = chain.handle(complaint);

        assertTrue(result.contains("🚨"));
        assertTrue(result.contains("Emergency response team deployed"));
        assertTrue(complaint.getEscalationLog().contains("CRITICAL: Emergency response team deployed"));
    }

    @Test
    @DisplayName("C.5: Incomplete chain leaves complaint unhandled gracefully")
    void testUnresolvedComplaint() {
        // Chain with only local staff
        ComplaintHandler singleStaff = new LocalStaffHandler("Staff Solo");
        Complaint complaint = new Complaint("CMP-005", "Citizen Frank", "Bridge structural crack",
                "Infrastructure", Complaint.Severity.CRITICAL);

        String result = singleStaff.handle(complaint);

        assertEquals("UNRESOLVED", complaint.getResolution());
        assertTrue(result.contains("could not be resolved"));
        assertTrue(complaint.getEscalationLog().contains("No handler available"));
    }
}
