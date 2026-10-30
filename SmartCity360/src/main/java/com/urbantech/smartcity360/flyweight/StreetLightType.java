package com.urbantech.smartcity360.flyweight;

/**
 * PART D - Flyweight Pattern: Efficient Management of Smart Street Lights
 * 
 * Flyweight class - stores shared intrinsic state that is common
 * across many streetlight instances of the same type.
 * This object is IMMUTABLE and shared among all streetlights of the same type.
 */
public class StreetLightType {
    
    // Intrinsic state - shared across all streetlights of this type
    private final String model;
    private final int wattage;
    private final int colorTemperature; // in Kelvin
    private final String manufacturer;
    private final String firmwareVersion;
    private final String defaultSchedule;
    
    public StreetLightType(String model, int wattage, int colorTemperature,
                           String manufacturer, String firmwareVersion, String defaultSchedule) {
        this.model = model;
        this.wattage = wattage;
        this.colorTemperature = colorTemperature;
        this.manufacturer = manufacturer;
        this.firmwareVersion = firmwareVersion;
        this.defaultSchedule = defaultSchedule;
    }
    
    /**
     * Operates the streetlight using its intrinsic properties combined
     * with extrinsic state passed from the context.
     * 
     * @param location GPS coordinates (extrinsic)
     * @param brightness current brightness level (extrinsic)
     * @param isOn whether the light is on (extrinsic)
     * @return operation status message
     */
    public String operate(String location, int brightness, boolean isOn) {
        return String.format("[%s] at %s | %dW | %dK | Brightness: %d%% | Status: %s | Schedule: %s",
                model, location, wattage, colorTemperature, brightness,
                isOn ? "ON" : "OFF", defaultSchedule);
    }
    
    /**
     * Calculates energy consumption based on wattage and brightness.
     * @param brightness current brightness percentage (extrinsic)
     * @param hoursOn hours the light has been on (extrinsic)
     * @return energy consumed in kWh
     */
    public double calculateEnergy(int brightness, double hoursOn) {
        return (wattage * (brightness / 100.0) * hoursOn) / 1000.0;
    }
    
    // Getters for intrinsic state
    public String getModel() { return model; }
    public int getWattage() { return wattage; }
    public int getColorTemperature() { return colorTemperature; }
    public String getManufacturer() { return manufacturer; }
    public String getFirmwareVersion() { return firmwareVersion; }
    public String getDefaultSchedule() { return defaultSchedule; }
    
    @Override
    public String toString() {
        return String.format("StreetLightType[%s | %dW | %dK | %s | FW:%s]",
                model, wattage, colorTemperature, manufacturer, firmwareVersion);
    }
}
