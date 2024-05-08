package map2D;

import place.*;
import rectangle.Rectangle;
import arrayList.ArrayList;
import quadTree.*;
import java.util.Random;
import java.util.Scanner;

public class Map2D {
    private QuadTree root;
    private ArrayList<Place> batch;
    private static final int BATCH_SIZE = 10000;
    private static final int MAP_WIDTH = 10000000;  // Example map width
    private static final int MAP_HEIGHT = 10000000; // Example map height

    public Map2D() {
        this.root = new QuadTree(new Rectangle(0, 0, MAP_WIDTH, MAP_HEIGHT), 0);
        this.batch = new ArrayList<>();
    }

    // Add a place to the map
    public void addPlace(int x, int y, int serviceBitmask) {
        Place newPlace = new Place(x, y, serviceBitmask);
        batch.add(newPlace);
        if (batch.size() == BATCH_SIZE) {
            flushBatch(); // Insert the batch to the QuadTree and clear it
        }
    }

    public void flushBatch() {
        if (!batch.isEmpty()) {
            root.insertBatch(batch);
            batch.clear(); // Clear the batch after insertion to free up memory
        }
    }

    // Edit services of a place
    public void editPlaceServices(int x, int y, String action, String serviceType) {
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
    public void removePlace(int x, int y) {
        boolean success = root.removePlace(x, y);
        if (!success) {
            System.out.println("Failed to remove place at coordinates (" + x + ", " + y + ")");
        }
    }

    // Search for places within a specified area and optional service filter
    public void search(Rectangle area, Integer serviceBitmask) {
        ArrayList<Place> results = new ArrayList<>();
        root.query(area, results, serviceBitmask);
        System.out.println("Displaying up to 50 places: ");
        for (int i = 0; i < Math.min(50, results.size()); i++) {
            System.out.println(results.get(i));
        }
        System.out.println(results.size() + " places found!");
    }

    public static void main(String[] args) {
        Map2D map = new Map2D();

        // Adding places with various services
        Random random = new Random();
        for (int i = 0; i < 10_000_000; i++) {
            // Randomly distribute points across the entire boundary to ensure a uniform spread
            int x = random.nextInt(Map2D.MAP_WIDTH);
            int y = random.nextInt(Map2D.MAP_HEIGHT) ;
            int serviceBitmask = QuadTree.generateServiceBitmask(random, ServiceRegistry.getServiceTypes().length);

            map.addPlace(x, y, serviceBitmask);
        }

        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Place");
            System.out.println("2. Edit Place");
            System.out.println("3. Remove Place");
            System.out.println("4. Search");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter X coordinate: ");
                    int x = scanner.nextInt();
                    System.out.print("Enter Y coordinate: ");
                    int y = scanner.nextInt();
                    System.out.println("Enter service type (1-10): ");
                    System.out.println("1. Cafe");
                    System.out.println("2. Restaurant");
                    System.out.println("3. Gas Station");
                    System.out.println("4. Library");
                    System.out.println("5. Hospital");
                    System.out.println("6. School");
                    System.out.println("7. Store");
                    System.out.println("8. Park");
                    System.out.println("9. Hotel");
                    System.out.println("10. Gym");
                    int serviceTypeIndex = scanner.nextInt() - 1;
                    map.addPlace(x, y, 1 << serviceTypeIndex);
                    System.out.println("Do you want to add more service types ?: ");
                    System.out.println("(1 to continue, any other number to stop)");
                    int addMoreServiceType = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    while (addMoreServiceType == 1) {
                        System.out.println("Enter service type (1-10): ");
                        System.out.println("1. Cafe");
                        System.out.println("2. Restaurant");
                        System.out.println("3. Gas Station");
                        System.out.println("4. Library");
                        System.out.println("5. Hospital");
                        System.out.println("6. School");
                        System.out.println("7. Store");
                        System.out.println("8. Park");
                        System.out.println("9. Hotel");
                        System.out.println("10. Gym");
                        int addServiceTypeIndex = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        map.editPlaceServices(x, y, "add", ServiceRegistry.getServiceType(addServiceTypeIndex));
                        System.out.println("Do you want to add more service types ?: ");
                        System.out.println("(1 to continue, any other number to stop)");
                        addMoreServiceType = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                    }
                    break;
                case 2:
                    System.out.print("Enter X coordinate: ");
                    int editX = scanner.nextInt();
                    System.out.print("Enter Y coordinate: ");
                    int editY = scanner.nextInt();
                    System.out.println("Enter action (1 for 'add', 2 for 'remove'): ");
                    int editAction = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    System.out.println("Enter service type (1-10): ");
                    System.out.println("1. Cafe");
                    System.out.println("2. Restaurant");
                    System.out.println("3. Gas Station");
                    System.out.println("4. Library");
                    System.out.println("5. Hospital");
                    System.out.println("6. School");
                    System.out.println("7. Store");
                    System.out.println("8. Park");
                    System.out.println("9. Hotel");
                    System.out.println("10. Gym");
                    int editServiceTypeIndex = scanner.nextInt() - 1;
                    String action = (editAction == 1) ? "add" : "remove";
                    map.editPlaceServices(editX, editY, action, ServiceRegistry.getServiceType(editServiceTypeIndex));
                    break;
                case 3:
                    System.out.print("Enter X coordinate: ");
                    int removeX = scanner.nextInt();
                    System.out.print("Enter Y coordinate: ");
                    int removeY = scanner.nextInt();
                    map.removePlace(removeX, removeY);
                    break;
                case 4:
                    System.out.print("Enter X coordinate of search area: ");
                    int searchX = scanner.nextInt();
                    System.out.print("Enter Y coordinate of search area: ");
                    int searchY = scanner.nextInt();
                    System.out.print("Enter width of search area: ");
                    int width = scanner.nextInt();
                    System.out.print("Enter height of search area: ");
                    int height = scanner.nextInt();
                    Rectangle searchArea = new Rectangle(searchX, searchY, width, height);
                    System.out.println("Do you want to search with service filters ?");
                    System.out.println("(1 for yes, any other number for no): ");
                    int filterServiceType = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character
                    if (filterServiceType == 1) {
                        System.out.println("Enter service type (1-10): ");
                        System.out.println("1. Cafe");
                        System.out.println("2. Restaurant");
                        System.out.println("3. Gas Station");
                        System.out.println("4. Library");
                        System.out.println("5. Hospital");
                        System.out.println("6. School");
                        System.out.println("7. Store");
                        System.out.println("8. Park");
                        System.out.println("9. Hotel");
                        System.out.println("10. Gym");
                        int searchServiceTypeIndex = scanner.nextInt() - 1;
                        scanner.nextLine(); // Consume newline character
                        System.out.println("Search Results:");
                        map.search(searchArea, 1 << searchServiceTypeIndex); // Search with service filter
                        break;
                    } else {
                        System.out.println("Search Results:");
                        map.search(searchArea, null); // Search with service filter
                        break;
                    }
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

//        // Searching within a large area
//        Rectangle searchArea = new Rectangle(0, 0, 100000, 100000);
//        System.out.println("Search Results:");
//        map.search(searchArea, null); // Search without service filter
//
//        // Editing a service
//        System.out.println("Editing Services:");
//        map.editPlaceServices(50000, 50000, "add", "Hospital");
//
//        // Removing a place
//        System.out.println("Removing a Place:");
//        map.removePlace(50000, 50000);
//
//        System.out.println("New search results:");
//        map.search(searchArea, null);
    }
