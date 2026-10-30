package com.urbantech.smartcity360.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Composite class - represents a zone that can contain sub-zones.
 * Used for City, District, and Street levels which contain children.
 */
public class CompositeZone implements ZoneComponent {
    
    private final String name;
    private final String zoneType;
    private final int level;
    private boolean powerOn;
    private final List<ZoneComponent> children;
    
    public CompositeZone(String name, String zoneType, int level) {
        this.name = name;
        this.zoneType = zoneType;
        this.level = level;
        this.powerOn = true;
        this.children = new ArrayList<>();
    }
    
    /**
     * Adds a child zone to this composite zone.
     * @param component the child zone to add
     */
    public void add(ZoneComponent component) {
        children.add(component);
    }
    
    /**
     * Removes a child zone from this composite zone.
     * @param component the child zone to remove
     */
    public void remove(ZoneComponent component) {
        children.remove(component);
    }
    
    @Override
    public String shutdownPower() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(zoneType).append("] ").append(name)
          .append(" - Shutting down power...\n");
        
        this.powerOn = false;
        
        // Recursively shut down all children
        for (ZoneComponent child : children) {
            sb.append("  ".repeat(level + 1));
            sb.append(child.shutdownPower());
        }
        
        sb.append("  ".repeat(level));
        sb.append("✓ ").append(name).append(" power shutdown complete.\n");
        return sb.toString();
    }
    
    @Override
    public String restorePower() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(zoneType).append("] ").append(name)
          .append(" - Restoring power...\n");
        
        this.powerOn = true;
        
        for (ZoneComponent child : children) {
            sb.append("  ".repeat(level + 1));
            sb.append(child.restorePower());
        }
        
        sb.append("  ".repeat(level));
        sb.append("✓ ").append(name).append(" power restored.\n");
        return sb.toString();
    }
    
    @Override
    public double reportUsage() {
        double totalUsage = 0;
        for (ZoneComponent child : children) {
            totalUsage += child.reportUsage();
        }
        return totalUsage;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getZoneType() {
        return zoneType;
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
        return Collections.unmodifiableList(children);
    }
    
    @Override
    public String getReport(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("[").append(zoneType).append("] ")
          .append(name).append(" | Power: ").append(powerOn ? "ON" : "OFF")
          .append(" | Usage: ").append(String.format("%.1f", reportUsage())).append(" kWh")
          .append(" | Sub-zones: ").append(children.size()).append("\n");
        
        for (ZoneComponent child : children) {
            sb.append(child.getReport(indent + "    "));
        }
        return sb.toString();
    }
}
