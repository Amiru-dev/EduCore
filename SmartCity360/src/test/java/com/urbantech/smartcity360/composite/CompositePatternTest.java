package com.urbantech.smartcity360.composite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Part B: Composite Pattern Tests")
public class CompositePatternTest {

    @Test
    @DisplayName("B.1: Building leaf node behavior")
    void testBuildingLeaf() {
        Building b1 = new Building("Town Hall", 150.0);
        assertEquals("Town Hall", b1.getName());
        assertEquals("Building", b1.getZoneType());
        assertEquals(3, b1.getLevel());
        assertTrue(b1.isPowerOn());
        assertEquals(150.0, b1.reportUsage());
        assertTrue(b1.getChildren().isEmpty());

        // Shutdown power
        String shutdownMsg = b1.shutdownPower();
        assertFalse(b1.isPowerOn());
        assertEquals(0.0, b1.reportUsage(), "Usage should be 0 when power is off");
        assertTrue(shutdownMsg.contains("Power OFF"));

        // Restore power
        String restoreMsg = b1.restorePower();
        assertTrue(b1.isPowerOn());
        assertEquals(150.0, b1.reportUsage());
        assertTrue(restoreMsg.contains("Power ON"));
    }

    @Test
    @DisplayName("B.2: CompositeZone aggregates children and reports usage")
    void testCompositeAggregation() {
        CompositeZone street = new CompositeZone("Lotus Road", "Street", 2);
        Building b1 = new Building("Mall", 200.0);
        Building b2 = new Building("Cinema", 100.0);

        street.add(b1);
        street.add(b2);

        assertEquals(2, street.getChildren().size());
        assertEquals(300.0, street.reportUsage(), "Total usage should sum all children");

        street.remove(b2);
        assertEquals(1, street.getChildren().size());
        assertEquals(200.0, street.reportUsage());
    }

    @Test
    @DisplayName("B.3: Recursive power shutdown and restore throughout hierarchy")
    void testRecursivePowerControl() {
        CompositeZone district = new CompositeZone("District North", "District", 1);
        CompositeZone street = new CompositeZone("Main St", "Street", 2);
        Building b1 = new Building("Office Tower", 350.0);
        Building b2 = new Building("Data Center", 650.0);

        street.add(b1);
        street.add(b2);
        district.add(street);

        assertEquals(1000.0, district.reportUsage());

        // Shutdown district - should recursively shut down street and all buildings
        String shutdownLog = district.shutdownPower();
        assertFalse(district.isPowerOn());
        assertFalse(street.isPowerOn());
        assertFalse(b1.isPowerOn());
        assertFalse(b2.isPowerOn());
        assertEquals(0.0, district.reportUsage());
        assertTrue(shutdownLog.contains("Office Tower"));
        assertTrue(shutdownLog.contains("Data Center"));

        // Restore power
        district.restorePower();
        assertTrue(b1.isPowerOn());
        assertTrue(b2.isPowerOn());
        assertEquals(1000.0, district.reportUsage());
    }

    @Test
    @DisplayName("B.4: CityHierarchyFactory builds valid full city tree")
    void testSampleCityHierarchy() {
        CompositeZone city = CityHierarchyFactory.createSampleCity();
        assertEquals("Colombo", city.getName());
        assertEquals("City", city.getZoneType());
        assertEquals(2, city.getChildren().size(), "Should have 2 districts");

        double totalCityUsage = city.reportUsage();
        assertTrue(totalCityUsage > 0, "Total usage must be strictly positive");

        String report = city.getReport("");
        assertTrue(report.contains("Colombo"));
        assertTrue(report.contains("Cinnamon Gardens"));
        assertTrue(report.contains("Kollupitiya"));
        assertTrue(report.contains("City Hospital"));
    }
}
