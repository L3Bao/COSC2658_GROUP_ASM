package quadTree;

import place.Place;
import place.ServiceRegistry;
import rectangle.Rectangle;

import java.util.Random;

import arrayList.ArrayList;

public class QuadTree {
    private static final int MAX_CAPACITY = 4;
    private Rectangle boundary;
    private ArrayList<Place> points;
    private boolean divided;
    private QuadTree northeast, northwest, southeast, southwest;

    public QuadTree(Rectangle boundary) {
        this.boundary = boundary;
        this.points = new ArrayList<>();
        this.divided = false;
    }

    private void subdivide() {
        double halfWidth = boundary.getW() / 2;
        double halfHeight = boundary.getH() / 2;
        double x = boundary.getX();
        double y = boundary.getY();

        northeast = new QuadTree(new Rectangle(x + halfWidth / 2, y - halfHeight / 2, halfWidth, halfHeight));
        northwest = new QuadTree(new Rectangle(x - halfWidth / 2, y - halfHeight / 2, halfWidth, halfHeight));
        southeast = new QuadTree(new Rectangle(x + halfWidth / 2, y + halfHeight / 2, halfWidth, halfHeight));
        southwest = new QuadTree(new Rectangle(x - halfWidth / 2, y + halfHeight / 2, halfWidth, halfHeight));

        divided = true;
        redistributePlaces();
    }

    private void redistributePlaces() {
        Place[] placesArray = points.toArray(new Place[0]);
        for (Place point : placesArray) {
            insertIntoSubTree(point);
        }
        points.clear(); // Clear the points list after redistribution
    }

    private boolean insertIntoSubTree(Place point) {
        if (northeast.insert(point)) return true;
        if (northwest.insert(point)) return true;
        if (southeast.insert(point)) return true;
        if (southwest.insert(point)) return true;
        return false;
    }

    public boolean insert(Place point) {
        if (!boundary.contains(point)) {
            return false;
        }
        if (points.size() < MAX_CAPACITY && !divided) {
            points.add(point);
            return true;
        }
        if (!divided) {
            subdivide();
        }
        return insertIntoSubTree(point);
    }

    public void query(Rectangle range, ArrayList<Place> found, Integer serviceBitmask) {
        if (!boundary.intersects(range)) {
            return;
        }
    
        for (Place point : points.toArray(new Place[0])) {
            if (range.contains(point)) {
                if (serviceBitmask == null || hasAnyService(point, serviceBitmask)) { // Check for optional filtering
                    found.add(point);
                }
            }
        }
    
        if (divided) {
            northeast.query(range, found, serviceBitmask);
            northwest.query(range, found, serviceBitmask);
            southeast.query(range, found, serviceBitmask);
            southwest.query(range, found, serviceBitmask);
        }
    }
    
    // Helper function to check if the Place has any of the services 
    private boolean hasAnyService(Place place, int serviceBitmask) {
        return (place.getServiceBitmask() & serviceBitmask) != 0;
    }

    public static void main(String[] args) {
        // Define the boundary for the entire QuadTree
        Rectangle boundary = new Rectangle(0, 0, 10000000, 10000000);
        QuadTree tree = new QuadTree(boundary);
    
        // Random number generator
        Random random = new Random();
        int serviceTypeCount = ServiceRegistry.getServiceTypes().length; // Get number of services from registry 
        int numberOfPlaces = 100_000; 
    
        // Inserting places with random service types
        for (int i = 0; i < numberOfPlaces; i++) {
            double x = random.nextDouble() * boundary.getW() + boundary.getLeft();
            double y = random.nextDouble() * boundary.getH() + boundary.getTop();
    
            // Generate a random service bitmask
            int serviceBitmask = 0; 
            int servicesToEnable = random.nextInt(3) + 1; // Enable 1-3 random services
            for (int j = 0; j < servicesToEnable; j++) {
                int serviceIndex = random.nextInt(serviceTypeCount); 
                serviceBitmask |= (1 << serviceIndex); 
            }
    
            tree.insert(new Place(x, y, serviceBitmask));
        }
    
        System.out.println("Inserted " + numberOfPlaces + " places.");
    
        // Define a search area 
        Rectangle searchArea = new Rectangle(0, 0, 5000000, 5000000);
    
        
    
        ArrayList<Place> found = new ArrayList<>();
        tree.query(searchArea, found, null); 
    
        System.out.println("Found " + found.size() + " places with the any services in the search area:");
        for (int i = 0; i < found.size() && i < 50; i++) { 
            System.out.println(found.get(i));
        }
    }

}
