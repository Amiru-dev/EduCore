package com.urbantech.smartcity360.composite;

import java.util.Collections;
import java.util.List;

/**
 * Leaf class - represents an individual building (the smallest zone unit).
 * Buildings do not contain sub-zones; they execute operations directly.
 */
public class Building implements ZoneComponent {
    
    private final String name;
    private final double energyUsage; // in kWh
    private boolean powerOn;
    private final int level;
    
    public Building(String name, double energyUsage) {
        this.name = name;
        this.energyUsage = energyUsage;
        this.powerOn = true;
        this.level = 3; // Buildings are at level 3 (City=0, District=1, Street=2, Building=3)
    }
    
    @Override
    public String shutdownPower() {
        this.powerOn = false;
        return "[Building] " + name + " - Power OFF ⚡\n";
    }
    
    @Override
    public String restorePower() {
        this.powerOn = true;
        return "[Building] " + name + " - Power ON ✓\n";
    }
    
    @Override
    public double reportUsage() {
        return powerOn ? energyUsage : 0;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getZoneType() {
        return "Building";
    }
    
    @Override
    public boolean isPowerOn() {
        return powerOn;
    }
    
    @Override
    public int getLevel() {
        return level;
    }
    
    @Override
    public List<ZoneComponent> getChildren() {
        return Collections.emptyList();
    }
    
    @Override
    public String getReport(String indent) {
        return indent + "[Building] " + name 
               + " | Power: " + (powerOn ? "ON" : "OFF")
               + " | Usage: " + String.format("%.1f", reportUsage()) + " kWh\n";
    }
}
