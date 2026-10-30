package com.urbantech.smartcity360.flyweight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Part D: Flyweight Pattern Tests")
public class FlyweightPatternTest {

    @BeforeEach
    void setUp() {
        StreetLightFactory.clearCache();
    }

    @Test
    @DisplayName("D.1: Factory reuses identical flyweight types (same reference)")
    void testFlyweightSharing() {
        StreetLightType type1 = StreetLightFactory.getStreetLightType(
                "SL-3000", 150, 4000, "UrbanLight", "v2.0", "Dusk to Dawn");
        StreetLightType type2 = StreetLightFactory.getStreetLightType(
                "SL-3000", 150, 4000, "UrbanLight", "v2.0", "Dusk to Dawn");

        // Must be the exact same object reference in memory!
        assertSame(type1, type2, "Factory must return the shared flyweight instance");
        assertEquals(1, StreetLightFactory.getTypeCount());
    }

    @Test
    @DisplayName("D.2: Distinct configurations create separate flyweights")
    void testDistinctFlyweights() {
        StreetLightType led = StreetLightFactory.getStreetLightType(
                "SL-3000 LED", 150, 4000, "UrbanLight", "v2.0", "Dusk to Dawn");
        StreetLightType solar = StreetLightFactory.getStreetLightType(
                "SL-2000 Solar", 100, 3500, "SolarTech", "v1.5", "6PM to 6AM");

        assertNotSame(led, solar);
        assertEquals(2, StreetLightFactory.getTypeCount());
    }

    @Test
    @DisplayName("D.3: Extrinsic state is independent per context while sharing flyweight")
    void testExtrinsicStateIndependence() {
        StreetLightType sharedType = StreetLightFactory.getStreetLightType(
                "SL-4000", 200, 5000, "SmartGlow", "v3.0", "Adaptive");

        StreetLightContext light1 = new StreetLightContext("SL-001", "(6.9271, 79.8612)", "Main St", sharedType);
        StreetLightContext light2 = new StreetLightContext("SL-002", "(6.9300, 79.8700)", "Oak Ave", sharedType);

        // Intrinsic type is shared
        assertSame(light1.getType(), light2.getType());

        // Extrinsic state is independent
        light1.setBrightness(100);
        light2.setBrightness(40);
        light2.turnOff();

        assertEquals(100, light1.getBrightness());
        assertTrue(light1.isOn());

        assertEquals(0, light2.getBrightness());
        assertFalse(light2.isOn());
    }

    @Test
    @DisplayName("D.4: Energy calculation combines intrinsic wattage with extrinsic brightness and hours")
    void testEnergyCalculation() {
        StreetLightType type = StreetLightFactory.getStreetLightType(
                "SL-Eco", 100, 3000, "GreenCo", "v1.0", "Night");
        StreetLightContext light = new StreetLightContext("SL-010", "Loc-1", "St-1", type);

        // 100W, 100% brightness, 10 hours -> 100 * 1.0 * 10 / 1000 = 1.0 kWh
        light.setBrightness(100);
        light.setHoursOperatedToday(10.0);
        assertEquals(1.0, light.getEnergyConsumed(), 0.001);

        // 50% brightness -> 0.5 kWh
        light.setBrightness(50);
        assertEquals(0.5, light.getEnergyConsumed(), 0.001);
    }

    @Test
    @DisplayName("D.5: Massive scaling creates thousands of lights with minimal flyweight instances")
    void testLargeScaleFlyweights() {
        String[][] sampleConfigs = {
            {"Type-A", "100", "4000"},
            {"Type-B", "150", "5000"},
            {"Type-C", "200", "3000"}
        };

        for (int i = 0; i < 5000; i++) {
            String[] c = sampleConfigs[i % sampleConfigs.length];
            StreetLightType type = StreetLightFactory.getStreetLightType(
                    c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]),
                    "Corp", "v1", "Schedule");
            new StreetLightContext("L-" + i, "loc", "street", type);
        }

        // Even with 5,000 lights, exactly 3 flyweight types exist!
        assertEquals(3, StreetLightFactory.getTypeCount());
    }
}
