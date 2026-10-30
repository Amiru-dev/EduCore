package com.urbantech.smartcity360.composite;

/**
 * Factory class that creates a sample city hierarchy for demonstration.
 * Builds: Colombo → 2 Districts → Streets → Buildings
 */
public class CityHierarchyFactory {
    
    /**
     * Creates a complete sample city hierarchy.
     * @return the root CompositeZone representing the city
     */
    public static CompositeZone createSampleCity() {
        // Level 0: City
        CompositeZone city = new CompositeZone("Colombo", "City", 0);
        
        // Level 1: Districts
        CompositeZone district1 = new CompositeZone("Colombo-07 (Cinnamon Gardens)", "District", 1);
        CompositeZone district2 = new CompositeZone("Colombo-03 (Kollupitiya)", "District", 1);
        
        // Level 2: Streets in District 1
        CompositeZone street1 = new CompositeZone("Maitland Place", "Street", 2);
        CompositeZone street2 = new CompositeZone("Ward Place", "Street", 2);
        
        // Level 2: Streets in District 2
        CompositeZone street3 = new CompositeZone("Galle Road", "Street", 2);
        CompositeZone street4 = new CompositeZone("Duplication Road", "Street", 2);
        
        // Level 3: Buildings on Maitland Place
        street1.add(new Building("City Hospital", 450.5));
        street1.add(new Building("Central Library", 120.3));
        street1.add(new Building("Tech Park Tower", 380.7));
        
        // Level 3: Buildings on Ward Place
        street2.add(new Building("Government Office", 210.0));
        street2.add(new Building("Smart School Complex", 175.2));
        
        // Level 3: Buildings on Galle Road
        street3.add(new Building("Shopping Mall", 520.8));
        street3.add(new Building("Hotel Grand", 410.0));
        street3.add(new Building("Convention Center", 330.5));
        
        // Level 3: Buildings on Duplication Road
        street4.add(new Building("Residential Tower A", 95.4));
        street4.add(new Building("Residential Tower B", 88.6));
        
        // Assemble hierarchy
        district1.add(street1);
        district1.add(street2);
        
        district2.add(street3);
        district2.add(street4);
        
        city.add(district1);
        city.add(district2);
        
        return city;
    }
}
