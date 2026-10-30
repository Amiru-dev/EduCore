package com.urbantech.smartcity360.composite;

import java.util.List;

/**
 * PART B - Composite Pattern: Hierarchical Structure of Smart Zones
 * 
 * Component Interface - defines the common interface for both
 * individual zones (leaves) and composite zones.
 */
public interface ZoneComponent {
    
    /**
     * Shuts down power in this zone and all sub-zones.
     * @return status message describing the shutdown result
     */
    String shutdownPower();
    
    /**
     * Restores power in this zone and all sub-zones.
     * @return status message describing the restore result
     */
    String restorePower();
    
    /**
     * Reports the total energy usage for this zone and all sub-zones.
     * @return total energy usage in kWh
     */
    double reportUsage();
    
    /**
     * Gets the name of this zone.
     * @return zone name
     */
    String getName();
    
    /**
     * Gets the type of this zone (City, District, Street, Building).
     * @return zone type as string
     */
    String getZoneType();
    
    /**
     * Checks if power is currently active.
     * @return true if power is on
     */
    boolean isPowerOn();
    
    /**
     * Gets the hierarchical level of this zone (0=City, 1=District, etc.)
     * @return level number
     */
    int getLevel();
    
    /**
     * Gets all children of this zone (empty list for leaves).
     * @return list of child zone components
     */
    List<ZoneComponent> getChildren();
    
    /**
     * Returns a detailed report as a structured string.
     * @param indent the current indentation level
     * @return formatted report string
     */
    String getReport(String indent);
}
