package map2D;

import place.*;
import rectangle.Rectangle;
import arrayList.ArrayList;
import quadTree.*;

public class Map2D {
    private QuadTree root;
    private static final float MAP_WIDTH = 10000000;  // Example map width
    private static final float MAP_HEIGHT = 10000000; // Example map height

    public Map2D() {
        this.root = new QuadTree(new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT), 0);
    }

    // Add a place to the map
    public void addPlace(float x, float y, int serviceBitmask) {
        Place newPlace = new Place(x, y, serviceBitmask);
        root.insert(newPlace);
    }

    // Edit services of a place
    public void editPlaceServices(float x, float y, String action, String serviceType) {
        Place place = root.findPlace(x, y);
        if (place == null) {
            System.out.println("Place not found at coordinates (" + x + ", " + y + ")");
            return;
        }
        int serviceIndex = ServiceRegistry.getServiceTypeIndex(serviceType);
        boolean hasService = place.isServiceEnabled(serviceIndex);

        if ("add".equalsIgnoreCase(action) && !hasService) {
            place.toggleService(serviceIndex, true);
            System.out.println("Service '" + serviceType + "' added. Updated Place: " + place);
        } else if ("remove".equalsIgnoreCase(action) && hasService) {
            place.toggleService(serviceIndex, false);
            System.out.println("Service '" + serviceType + "' removed. Updated Place: " + place);
        } else {
            System.out.println("No changes made. Place details: " + place);
        }
    }

    // Remove a place from the map
    public void removePlace(float x, float y) {
        boolean success = root.removePlace(x, y);
        if (!success) {
            System.out.println("Failed to remove place at coordinates (" + x + ", " + y + ")");
        }
    }

    // Search for places within a specified area and optional service filter
    public void search(Rectangle area, Integer serviceBitmask) {
        ArrayList<Place> results = new ArrayList<>();
        root.query(area, results, serviceBitmask);
        for (int i = 0; i < results.size(); i++) {
            System.out.println(results.get(i));
        }
    }

    public static void main(String[] args) {
        Map2D map = new Map2D();
        // Adding places with various services
        map.addPlace(50000, 50000, 0b101); // Place with multiple services
        map.addPlace(40000, 40000, 0b010); // Another place

        // Searching within a large area
        Rectangle searchArea = new Rectangle(0, 0, 100000, 100000);
        System.out.println("Search Results:");
        map.search(searchArea, null); // Search without service filter

        // Editing a service
        System.out.println("Editing Services:");
        map.editPlaceServices(50000, 50000, "add", "Hospital");

        // Removing a place
        System.out.println("Removing a Place:");
        map.removePlace(50000, 50000);

        System.out.println("New search results:");
        map.search(searchArea, null);
    }
}
