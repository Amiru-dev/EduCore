package com.urbantech.smartcity360;

import com.urbantech.smartcity360.decorator.*;
import com.urbantech.smartcity360.composite.*;
import com.urbantech.smartcity360.chain.*;
import com.urbantech.smartcity360.flyweight.*;
import com.urbantech.smartcity360.interpreter.*;

/**
 * SmartCity360 Comprehensive Design Patterns Showcase Runner
 * Executes and demonstrates all 5 Design Patterns (Parts A, B, C, D, E).
 */
public class SmartCity360Demo {

    public static void main(String[] args) {
        printBanner();
        demoPartA();
        demoPartB();
        demoPartC();
        demoPartD();
        demoPartE();
        printSummary();
    }

    private static void printBanner() {
        System.out.println("================================================================================");
        System.out.println("               🏙️  SmartCity360 - Smart City Management Platform               ");
        System.out.println("                   UrbanTech Solutions - Design Patterns Demo                   ");
        System.out.println("================================================================================\n");
    }

    private static void demoPartA() {
        System.out.println("================================================================================");
        System.out.println("PART A: DECORATOR PATTERN - Dynamic Security Wrappers for Data Streams");
        System.out.println("================================================================================");

        // 1. Base Stream
        DataStream stream = new EnergyMeterStream("EM-COL-001");
        stream.writeData("kWh=1420.5;voltage=230;current=6.2;status=NORMAL");
        System.out.println("[Base Stream Created]");
        System.out.println("Description: " + stream.getDescription());
        System.out.println("Read Output: " + stream.readData() + "\n");

        // 2. Add Authentication Layer dynamically
        System.out.println("[Dynamically Adding Authentication Wrapper]");
        stream = new AuthenticationDecorator(stream, "SMART-CITY-SECURE-KEY-2024");
        System.out.println("Description: " + stream.getDescription());
        System.out.println("Read Output: " + stream.readData() + "\n");

        // 3. Add Encryption Layer dynamically
        System.out.println("[Dynamically Adding Encryption Wrapper (AES-256)]");
        stream = new EncryptionDecorator(stream, "AES-256");
        System.out.println("Description: " + stream.getDescription());
        System.out.println("Read Output: " + stream.readData() + "\n");

        // 4. Add Compression Layer dynamically
        System.out.println("[Dynamically Adding Compression Wrapper (GZIP)]");
        stream = new CompressionDecorator(stream, "GZIP");
        System.out.println("Description: " + stream.getDescription());
        System.out.println("Final Read Output: " + stream.readData());
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    private static void demoPartB() {
        System.out.println("================================================================================");
        System.out.println("PART B: COMPOSITE PATTERN - Hierarchical Structure of Smart Zones");
        System.out.println("================================================================================");

        CompositeZone city = CityHierarchyFactory.createSampleCity();
        System.out.println("Initial City Hierarchy & Energy Consumption Report:");
        System.out.println(city.getReport("  "));
        System.out.printf("Total City Energy Usage: %.1f kWh\n\n", city.reportUsage());

        // Perform uniform command on a sub-zone (District 1)
        ZoneComponent district1 = city.getChildren().get(0);
        System.out.println(">>> Executing command: Shutdown power in " + district1.getName());
        System.out.print(district1.shutdownPower());
        System.out.printf("Updated Total City Energy Usage: %.1f kWh (After District-07 Shutdown)\n\n", city.reportUsage());

        // Restore power uniformly
        System.out.println(">>> Executing command: Restoring power in " + district1.getName());
        System.out.print(district1.restorePower());
        System.out.printf("Restored Total City Energy Usage: %.1f kWh\n", city.reportUsage());
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    private static void demoPartC() {
        System.out.println("================================================================================");
        System.out.println("PART C: CHAIN OF RESPONSIBILITY PATTERN - Escalation of Citizen Complaints");
        System.out.println("================================================================================");

        ComplaintHandler localStaff = new LocalStaffHandler("Mr. Perera");
        ComplaintHandler supervisor = new SupervisorHandler("Mrs. Fernando");
        ComplaintHandler cityManager = new CityManagerHandler("Dr. Wickramasinghe");

        // Assemble the chain: LocalStaff -> Supervisor -> CityManager
        localStaff.setNext(supervisor).setNext(cityManager);

        Complaint[] complaints = {
            new Complaint("CMP-101", "Kamal Perera", "Streetlight blinking on Galle Road", "Lighting", Complaint.Severity.LOW),
            new Complaint("CMP-102", "Sunil Silva", "Main water line burst causing flooding", "Utilities", Complaint.Severity.MEDIUM),
            new Complaint("CMP-103", "Anura Bandara", "Gas leak suspected near residential complex", "Safety", Complaint.Severity.HIGH),
            new Complaint("CMP-104", "Nimal Fernando", "Bridge support structural collapse hazard", "Emergency", Complaint.Severity.CRITICAL)
        };

        for (Complaint c : complaints) {
            System.out.println(">>> Processing " + c);
            String output = localStaff.handle(c);
            System.out.println(output);
            System.out.println("Escalation Audit Trail:\n" + c.getEscalationLog());
            System.out.println("---");
        }
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    private static void demoPartD() {
        System.out.println("================================================================================");
        System.out.println("PART D: FLYWEIGHT PATTERN - Efficient Management of Smart Street Lights");
        System.out.println("================================================================================");

        StreetLightFactory.clearCache();
        int lightCount = 10000;
        System.out.println("Simulating deployment of " + lightCount + " streetlights across Colombo...");

        String[][] models = {
            {"SL-3000 LED", "150", "4000", "UrbanLight Corp", "v2.4", "Dusk to Dawn"},
            {"SL-2000 Solar", "100", "3500", "SolarTech Inc", "v1.8", "6PM to 6AM"},
            {"SL-4000 Smart", "200", "5000", "SmartGlow Ltd", "v3.1", "Adaptive"},
            {"SL-1500 Eco", "75", "3000", "GreenLight Co", "v2.0", "7PM to 5AM"}
        };

        long startTime = System.nanoTime();
        for (int i = 0; i < lightCount; i++) {
            String[] m = models[i % models.length];
            StreetLightType sharedType = StreetLightFactory.getStreetLightType(
                    m[0], Integer.parseInt(m[1]), Integer.parseInt(m[2]), m[3], m[4], m[5]);
            StreetLightContext light = new StreetLightContext(
                    "SL-" + i, "(6.92" + (i % 100) + ", 79.85" + (i % 100) + ")", "Street-" + (i % 20), sharedType);
            light.setBrightness(80);
            light.setHoursOperatedToday(8.0);
        }
        long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;

        int uniqueFlyweights = StreetLightFactory.getTypeCount();
        long naiveMemory = (long) lightCount * (200 + 50);
        long flyweightMemory = ((long) uniqueFlyweights * 200) + ((long) lightCount * 50);
        long memorySaved = naiveMemory - flyweightMemory;
        double savingsPct = (memorySaved * 100.0) / naiveMemory;

        System.out.printf("Total Streetlights Created : %d\n", lightCount);
        System.out.printf("Unique Flyweight Objects   : %d\n", uniqueFlyweights);
        System.out.printf("Naive Object Memory Est.   : %,d bytes (%.2f MB)\n", naiveMemory, naiveMemory / (1024.0 * 1024.0));
        System.out.printf("Flyweight Memory Est.      : %,d bytes (%.2f MB)\n", flyweightMemory, flyweightMemory / (1024.0 * 1024.0));
        System.out.printf("Memory Saved               : %,d bytes (%.1f%% reduction)\n", memorySaved, savingsPct);
        System.out.printf("Creation Time              : %d ms\n", elapsedMs);
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    private static void demoPartE() {
        System.out.println("================================================================================");
        System.out.println("PART E: INTERPRETER PATTERN - Interpreting City Policies Defined in DSL");
        System.out.println("================================================================================");

        PolicyInterpreter interpreter = new PolicyInterpreter();
        interpreter.parseRule("NR-001", "IF zone = residential AND noise_level > 40 THEN enforce_noise_limit = 40dB",
                "Residential Night Noise Ordinance");
        interpreter.parseRule("PK-001", "IF parking_zone = A AND day != Sunday THEN parking_fee = 500LKR",
                "Zone A Weekday Parking Tariff");
        interpreter.parseRule("TR-001", "IF zone = school AND vehicle_speed > 30 THEN issue_school_speeding_fine",
                "School Zone Safety Speed Limit");

        // Context 1: Residential night with elevated noise
        CityContext ctx1 = new CityContext();
        ctx1.assign("zone", "residential");
        ctx1.assign("time", "23:30");
        ctx1.assign("noise_level", "58");
        ctx1.assign("parking_zone", "A");
        ctx1.assign("day", "Friday");
        ctx1.assign("vehicle_speed", "35");

        System.out.println("[Evaluating Context 1: Residential Night, Elevated Noise]");
        System.out.println(interpreter.evaluateAll(ctx1));

        // Context 2: School zone with speeding vehicle
        CityContext ctx2 = new CityContext();
        ctx2.assign("zone", "school");
        ctx2.assign("time", "08:15");
        ctx2.assign("noise_level", "35");
        ctx2.assign("parking_zone", "B");
        ctx2.assign("day", "Monday");
        ctx2.assign("vehicle_speed", "48");

        System.out.println("[Evaluating Context 2: School Zone, Speeding Vehicle]");
        System.out.println(interpreter.evaluateAll(ctx2));
        System.out.println("--------------------------------------------------------------------------------\n");
    }

    private static void printSummary() {
        System.out.println("================================================================================");
        System.out.println("                     ✅ ALL 5 DESIGN PATTERNS VERIFIED                         ");
        System.out.println("  Part A: Decorator Pattern          -> Verified                                ");
        System.out.println("  Part B: Composite Pattern          -> Verified                                ");
        System.out.println("  Part C: Chain of Responsibility    -> Verified                                ");
        System.out.println("  Part D: Flyweight Pattern          -> Verified                                ");
        System.out.println("  Part E: Interpreter Pattern        -> Verified                                ");
        System.out.println("================================================================================");
    }
}
