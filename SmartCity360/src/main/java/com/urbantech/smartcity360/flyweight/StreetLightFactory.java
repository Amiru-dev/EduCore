package com.urbantech.smartcity360.flyweight;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Flyweight Factory - creates and manages shared StreetLightType flyweight objects.
 * Ensures that only one instance of each type exists, shared across all streetlights.
 */
public class StreetLightFactory {
    
    private static final Map<String, StreetLightType> lightTypes = new HashMap<>();
    
    /**
     * Returns a shared StreetLightType flyweight, creating one if it doesn't exist.
     * @param model the model identifier
     * @param wattage power consumption in watts
     * @param colorTemp color temperature in Kelvin
     * @param manufacturer manufacturer name
     * @param firmware firmware version
     * @param schedule default operating schedule
     * @return the shared StreetLightType flyweight object
     */
    public static StreetLightType getStreetLightType(String model, int wattage, 
            int colorTemp, String manufacturer, String firmware, String schedule) {
        
        String key = model + "-" + wattage + "-" + colorTemp;
        
        if (!lightTypes.containsKey(key)) {
            StreetLightType newType = new StreetLightType(
                    model, wattage, colorTemp, manufacturer, firmware, schedule);
            lightTypes.put(key, newType);
        }
        
        return lightTypes.get(key);
    }
    
    /**
     * Returns the number of unique flyweight types created.
     * @return count of shared types
     */
    public static int getTypeCount() {
        return lightTypes.size();
    }
    
    /**
     * Returns all created flyweight types.
     * @return unmodifiable map of flyweight types
     */
    public static Map<String, StreetLightType> getAllTypes() {
        return Collections.unmodifiableMap(lightTypes);
    }
    
    /**
     * Clears all cached types (for testing/reset).
     */
    public static void clearCache() {
        lightTypes.clear();
    }
}
