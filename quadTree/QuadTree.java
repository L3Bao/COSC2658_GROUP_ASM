package quadTree;

import place.Place;
import place.ServiceRegistry;
import rectangle.*;

import java.util.Random;

import arrayList.ArrayList;
import iterable.Iterator;

public class QuadTree {
    private static final int INITIAL_CAPACITY = 10;
    private Rectangle boundary;
    private ArrayList<Place> points;
    private boolean divided;
    private QuadTree northeast, northwest, southeast, southwest;
    private int depth;

    public QuadTree(Rectangle boundary, int depth) {
        this.boundary = boundary;
        this.points = new ArrayList<>();
        this.divided = false;
        this.depth = depth;
    }

    public Rectangle getBoundary() {
        return boundary;
    }

    private int getCurrentCapacity() {
        return INITIAL_CAPACITY + (int) (Math.log(depth + 1) * INITIAL_CAPACITY);
    }

    private void subdivide() {
        divided = true;  // Mark as divided
    
        int halfWidth = boundary.getW() / 2;
        int halfHeight = boundary.getH() / 2;
        int x = boundary.getX();
        int y = boundary.getY();
    
        // Initialize child quadrants lazily
        northeast = new QuadTree(new Rectangle(x + halfWidth, y, halfWidth, halfHeight), depth + 1);
        northwest = new QuadTree(new Rectangle(x, y, halfWidth, halfHeight), depth + 1);
        southeast = new QuadTree(new Rectangle(x + halfWidth, y + halfHeight, halfWidth, halfHeight), depth + 1);
        southwest = new QuadTree(new Rectangle(x, y + halfHeight, halfWidth, halfHeight), depth + 1);
    
        // Distribute existing points into appropriate quadrants
        Iterator<Place> iterator = points.iterator();
        while (iterator.hasNext()) {
            Place point = iterator.next();
            QuadTree quadrant = getQuadrant(point);
            if (quadrant != null) {
                quadrant.insert(point);
            }
        }
        points.clear();
    }
    
    
    private QuadTree getQuadrant(Place point) {
        boolean rightHalf = point.getX() >= boundary.getX() + boundary.getW() / 2;
        boolean topHalf = point.getY() < boundary.getY() + boundary.getH() / 2; // Assuming top origin


        if (rightHalf) {
            return topHalf ? northeast : southeast;
        } else {
            return topHalf ? northwest : southwest;
        }
    }
    

    public boolean insert(Place point) {
        // Special handling for points on the exclusive right and bottom boundary of the entire space
        if ((point.getX() == getBoundary().getX() + getBoundary().getW()) || 
            (point.getY() == getBoundary().getY() + getBoundary().getH())) {
            // Adjust these points to be considered in-bounds by reducing x or y by a minimal value
            // This is a common technique used to handle boundary conditions in computational problems
            if (point.getX() == getBoundary().getX() + getBoundary().getW()) {
                point.setX(point.getX() - 1); // Decrement x to move the point to the left side
            }
            if (point.getY() == getBoundary().getY() + getBoundary().getH()) {
                point.setY(point.getY() - 1); // Decrement y to move the point upwards
            }
        }
    
        if (!boundary.contains(point.getX(), point.getY())) {
            System.err.println("Point out of bounds: " + point);
            return false;
        }
    
        if (!divided && points.size() >= getCurrentCapacity()) {
            subdivide();
        }
    
        if (!divided) {
            points.add(point);
            return true;
        } else {
            QuadTree quadrant = getQuadrant(point);
            if (quadrant != null) {
                return quadrant.insert(point);
            } else {
                return false;
            }
        }
    }
    
    
    
    public void query(Rectangle range, ArrayList<Place> found, Integer serviceBitmask) {
        if (!boundary.intersects(range))
            return;
        Iterator<Place> iterator = points.iterator();
        while (iterator.hasNext()) {
            Place point = iterator.next();
            if (range.contains(point.getX(), point.getY())
                    && (serviceBitmask == null || hasAnyService(point, serviceBitmask))) {
                found.add(point);
            }
        }
        if (divided) {
            if (northeast != null && northeast.boundary.intersects(range))
                northeast.query(range, found, serviceBitmask);
            if (northwest != null && northwest.boundary.intersects(range))
                northwest.query(range, found, serviceBitmask);
            if (southeast != null && southeast.boundary.intersects(range))
                southeast.query(range, found, serviceBitmask);
            if (southwest != null && southwest.boundary.intersects(range))
                southwest.query(range, found, serviceBitmask);
        }
    }

    private boolean hasAnyService(Place place, int serviceBitmask) {
        return (place.getServiceBitmask() & serviceBitmask) != 0;
    }

    public void insertBatch(ArrayList<Place> batch) {
        if (batch.isEmpty()) return;
    
    
        if (!divided && points.size() + batch.size() > getCurrentCapacity()) {
            
            subdivide();
        }
        
        if (!divided) {
            points.addAll(batch);
            return;
        } else {
            Iterator<Place> iterator = batch.iterator();
            while (iterator.hasNext()) {
                Place point = iterator.next();
                boolean inserted = insert(point);
                if (!inserted) {
                    System.err.println("Failed to insert point: " + point + " at boundary: " + boundary);
                }
            }
        }
    
        
    }

    public void editPlaceService(int x, int y, String action, String serviceType) {
        // Find the place in the QuadTree
        Place place = findPlace(x, y);
        if (place == null) {
            System.out.println("Place not found at coordinates (" + x + ", " + y + ")");
            return;
        }

        // Determine the service index
        int serviceIndex = ServiceRegistry.getServiceTypeIndex(serviceType);

        // Check if the place already has the specified service
        boolean hasService = place.isServiceEnabled(serviceIndex);

        // Update the place's service bitmask based on the action
        if (action.equalsIgnoreCase("add")) {
            if (hasService) {
                System.out.println("Place already has service '" + serviceType + "'");
            } else {
                place.toggleService(serviceIndex, true);
                System.out.println("Service '" + serviceType + "' added to the place at coordinates (" + x + ", " + y + ")");
            }
        } else if (action.equalsIgnoreCase("remove")) {
            if (!hasService) {
                System.out.println("Place does not have service '" + serviceType + "'");
            } else {
                place.toggleService(serviceIndex, false);
                System.out.println("Service '" + serviceType + "' removed from the place at coordinates (" + x + ", " + y + ")");
            }
        } else {
            System.out.println("Invalid action. Please specify 'add' or 'remove'.");
        }
    }


    public boolean removePlace(int x, int y) {
        // Find the place in the QuadTree
        Place place = findPlace(x, y);
        if (place == null) {
            System.out.println("Place not found at coordinates (" + x + ", " + y + ")");
            return false;
        }

        // Remove the place from the QuadTree
        if (remove(place)) {
            System.out.println("Place removed successfully at coordinates (" + x + ", " + y + ")");
            return true;
        } else {
            System.out.println("Failed to remove place at coordinates (" + x + ", " + y + ")");
        }
        return false;
    }

    // Helper method to find a place in the QuadTree based on coordinates
    public Place findPlace(int x, int y) {
        ArrayList<Place> found = new ArrayList<>();
        query(new Rectangle(x, y, 1, 1), found, null); // Search a small area around the point
        for (int i = 0; i < found.size(); i++) {
            if (found.get(i).getX() == x && found.get(i).getY() == y) {
                return found.get(i);
            }
        }
        return null; // Place not found
    }

    // Helper method to remove a place from the QuadTree
    private boolean remove(Place place) {
        // Find the QuadTree node containing the place
        QuadTree containingNode = findContainingNode(place.getX(), place.getY(), this);
        if (containingNode == null) {
            return false; // Place not found in the QuadTree
        }

        // Remove the place from the containing node
        containingNode.points.remove(place);

        // Merge nodes if necessary
        containingNode.tryMerge();

        return true;
    }

    // Helper method to find the QuadTree node containing a specific point
    private QuadTree findContainingNode(int x, int y, QuadTree node) {
        if (node.boundary.contains(x, y)) {
            if (!node.divided) {
                return node; // Found leaf node containing the point
            }
            // Recursively search child nodes
            if (node.northeast != null && node.northeast.boundary.contains(x, y)) {
                return findContainingNode(x, y, node.northeast);
            }
            if (node.northwest != null && node.northwest.boundary.contains(x, y)) {
                return findContainingNode(x, y, node.northwest);
            }
            if (node.southeast != null && node.southeast.boundary.contains(x, y)) {
                return findContainingNode(x, y, node.southeast);
            }
            if (node.southwest != null && node.southwest.boundary.contains(x, y)) {
                return findContainingNode(x, y, node.southwest);
            }
        }
        return null; // Point not found in any node
    }

    // Helper method to try merging child nodes into the parent node
    private void tryMerge() {
        if (!divided) {
            return; // No child nodes to merge
        }
        // Check if all child nodes are empty
        if (northeast.isEmpty() && northwest.isEmpty() && southeast.isEmpty() && southwest.isEmpty()) {
            // Merge child nodes into the parent node
            northeast = null;
            northwest = null;
            southeast = null;
            southwest = null;
            divided = false;
        }
    }

    // Helper method to check if a node's point list is empty
    private boolean isEmpty() {
        return points.isEmpty();
    }


    public static void main(String[] args) {
        // Create a QuadTree with a large boundary covering the whole map.
        Rectangle boundary = new Rectangle(0, 0, 10_000_000, 10_000_000);
        QuadTree tree = new QuadTree(boundary, 0);
        ArrayList<Place> batch = new ArrayList<>();
        int batchSize = 10_000; // Batch size for batch insertion
        Random random = new Random();
        int numberOfPoints = 10_000_000; // Total number of points to insert
    
        // Define the center of the query area
        int areaSize = 10_000_000;

        long startInsertTime = System.nanoTime();
    
        // Generate points
        for (int i = 0; i < numberOfPoints; i++) {
            int quadrant = i % 4; // Randomly choose quadrant: 0, 1, 2, or 3
            int x = 0 , y = 0;
        
            int midX = boundary.getW() / 2;
            int midY = boundary.getH() / 2;

            switch (quadrant) {
                case 0: // Northwest
                    x = random.nextInt(midX);
                    y = random.nextInt(midY);
                    break;
                case 1: // Northeast
                    x = midX + random.nextInt(boundary.getW() - midX);
                    y = random.nextInt(midY);
                    break;
                case 2: // Southwest
                    x = random.nextInt(midX);
                    y = midY + random.nextInt(boundary.getH() - midY);
                    break;
                case 3: // Southeast
                    x = midX + random.nextInt(boundary.getW() - midX);
                    y = midY + random.nextInt(boundary.getH() - midY);
                    break;
            }
        
            int serviceBitmask = generateServiceBitmask(random, ServiceRegistry.getServiceTypes().length);
        
            // Create a new place and add to batch
            Place newPlace = new Place(x, y, serviceBitmask);
            batch.add(newPlace);
        
            // Perform batch insertion when batch size is reached
            if (batch.size() == batchSize) {
                tree.insertBatch(batch);
                batch.clear();  // Clear the batch after insertion to free up memory
            }
        }
        
        // Insert any remaining points in the batch
        if (!batch.isEmpty()) {
            tree.insertBatch(batch);
            batch.clear();
        }
        
        

        long endInsertTime = System.nanoTime();
        System.out.printf("Insertion of %d places completed in %.2f ms.%n", numberOfPoints, (endInsertTime - startInsertTime) / 1e6);
    
        // Define the query area as a smaller rectangle within the map
        Rectangle searchArea = new Rectangle(0, 0, areaSize, areaSize);
        ArrayList<Place> found = new ArrayList<>();
    
        // Time the query performance
        long startTime = System.nanoTime();
        tree.query(searchArea, found, null);
        long endTime = System.nanoTime();
    
        // Output the results of the query
        System.out.printf("Query within [%d, %d, %d, %d] completed in %.2f ms.%n",
            (int)searchArea.getX(), (int)searchArea.getY(), (int)searchArea.getW(), (int)searchArea.getH(),
            (endTime - startTime) / 1e6);
        System.out.printf("Found %d places within the search area.%n", found.size());
    
        // Optionally print some of the found places for verification
        for (int i = 0; i < Math.min(50, found.size()); i++) {
            System.out.println(found.get(i));
        }

        System.out.println("The lucky chosen place to be edit and remove is:\n" + found.get(35));

        // Test editing a place's services
        int testX = found.get(35).getX();
        int testY = found.get(35).getY();
        String action = "add"; // or "remove"
        String serviceType = "Restaurant"; // or any other service type
        tree.editPlaceService(testX, testY, action, serviceType);

        System.out.println("The chosen place after service edit:\n" + found.get(35));
        System.out.println("Removing the chosen place...");

        tree.removePlace(testX, testY);

        System.out.println("Query the same area again to check if the place have been remove or not: ");

        ArrayList<Place> foundTest = new ArrayList<>();
        tree.query(searchArea, foundTest, null);

        System.out.printf("Found %d places within the search area.%n", foundTest.size());
    }
    
    public static int generateServiceBitmask(Random random, int serviceTypeCount) {
        int serviceBitmask = 0;
        int countServices = random.nextInt(3) + 1;
        for (int j = 0; j < countServices; j++) {
            serviceBitmask |= 1 << random.nextInt(serviceTypeCount);
        }
        return serviceBitmask;
    }
}
