package com.urbantech.smartcity360.flyweight;

/**
 * Context class - stores the extrinsic (unique) state for each streetlight instance.
 * References a shared StreetLightType flyweight for intrinsic state.
 * 
 * This is a lightweight object — it only stores location-specific data.
 */
public class StreetLightContext {
    
    // Extrinsic state - unique to each streetlight instance
    private final String lightId;
    private final String location;         // GPS coordinates or street address
    private final String streetName;
    private int brightness;                // 0-100 percent
    private boolean isOn;
    private double hoursOperatedToday;
    
    // Reference to shared intrinsic state (Flyweight)
    private final StreetLightType type;
    
    public StreetLightContext(String lightId, String location, String streetName,
                              StreetLightType type) {
        this.lightId = lightId;
        this.location = location;
        this.streetName = streetName;
        this.type = type;
        this.brightness = 100;
        this.isOn = true;
        this.hoursOperatedToday = 0;
    }
    
    /**
     * Operates this streetlight using the shared type's behavior.
     * @return operation status combining intrinsic + extrinsic state
     */
    public String operate() {
        return type.operate(location, brightness, isOn);
    }
    
    /**
     * Calculates energy consumed by this specific light.
     * @return energy in kWh
     */
    public double getEnergyConsumed() {
        return type.calculateEnergy(brightness, hoursOperatedToday);
    }
    
    /**
     * Turns the light on.
     */
    public void turnOn() {
        this.isOn = true;
    }
    
    /**
     * Turns the light off.
     */
    public void turnOff() {
        this.isOn = false;
        this.brightness = 0;
    }
    
    /**
     * Adjusts brightness level.
     * @param level brightness percentage (0-100)
     */
    public void adjustBrightness(int level) {
        this.brightness = Math.max(0, Math.min(100, level));
    }

    /**
     * Sets brightness level.
     * @param brightness brightness percentage (0-100)
     */
    public void setBrightness(int brightness) {
        this.brightness = Math.max(0, Math.min(100, brightness));
    }
    
    // Getters
    public String getLightId() { return lightId; }
    public String getLocation() { return location; }
    public String getStreetName() { return streetName; }
    public int getBrightness() { return brightness; }
    public boolean isOn() { return isOn; }
    public double getHoursOperatedToday() { return hoursOperatedToday; }
    public StreetLightType getType() { return type; }
    
    public void setHoursOperatedToday(double hours) { this.hoursOperatedToday = hours; }
    
    @Override
    public String toString() {
        return String.format("Light[%s] at %s | %s | Brightness: %d%% | %s | Energy: %.2f kWh",
                lightId, location, type.getModel(), brightness,
                isOn ? "ON" : "OFF", getEnergyConsumed());
    }
}
