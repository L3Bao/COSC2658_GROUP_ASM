package quadTree;

import place.Place;
import rectangle.Rectangle;
import java.util.Random;
import arrayList.ArrayList;

public class QuadTree {
    private static final int MAX_CAPACITY = 4;  // Maximum items per QuadTree node
    private Rectangle boundary;  // Spatial boundary of this node
    private ArrayList<Place> points;  // Points contained in this node
    private boolean divided;  // Indicates if this node has been subdivided
    private QuadTree northeast, northwest, southeast, southwest;
    private String path;  // To track the path of subdivision

    public QuadTree(Rectangle boundary, String path) {
        this.boundary = boundary;
        this.points = new ArrayList<>();
        this.divided = false;
        this.path = path;
    }

    private void subdivide() {
        double halfWidth = boundary.getW() / 2.0;
        double halfHeight = boundary.getH() / 2.0;
        double x = boundary.getX();
        double y = boundary.getY();
    
        northeast = new QuadTree(new Rectangle(x + halfWidth / 2, y - halfHeight / 2, halfWidth, halfHeight), path + " NE");
        northwest = new QuadTree(new Rectangle(x - halfWidth / 2, y - halfHeight / 2, halfWidth, halfHeight), path + " NW");
        southeast = new QuadTree(new Rectangle(x + halfWidth / 2, y + halfHeight / 2, halfWidth, halfHeight), path + " SE");
        southwest = new QuadTree(new Rectangle(x - halfWidth / 2, y + halfHeight / 2, halfWidth, halfHeight), path + " SW");
    
        System.out.println("Subdividing " + path + " into NE, NW, SE, SW quadrants");
    
        divided = true;
    }
    
    public boolean insert(Place point) {
        System.out.printf("Debug: Attempting to insert point %s into %s\n", point, path);

        if (!boundary.contains(point)) {
            System.out.printf("Debug: Point %s is outside the boundaries of %s\n", point, path);
            return false;
        }

        if (points.size() < MAX_CAPACITY && !divided) {
            points.add(point);
            System.out.printf("Debug: Point %s added directly to node within %s\n", point, path);
            return true;
        }

        if (!divided) {
            subdivide();
        }

        if (northeast.boundary.contains(point)) {
            return northeast.insert(point);
        } else if (northwest.boundary.contains(point)) {
            return northwest.insert(point);
        } else if (southeast.boundary.contains(point)) {
            return southeast.insert(point);
        } else if (southwest.boundary.contains(point)) {
            return southwest.insert(point);
        }

        return false;  // This case should never be reached due to boundary checks
    }

    

    // Query for points within a given rectangle
    public void query(Rectangle range, ArrayList<Place> found) {
        queryHelper(range, found);
        System.out.println("Debug: Total places found: " + found.size());
    }

    // Recursive helper method for querying within subdivisions
    private void queryHelper(Rectangle range, ArrayList<Place> found) {
        if (!boundary.intersects(range)) {
            return; // Early exit if the range does not intersect this quadrant
        }

        for (int i = 0; i < points.size(); i++) {
            Place p = points.get(i);
            if (range.contains(p)) {
                found.add(p);
            }
        }

        if (divided) {
            northeast.queryHelper(range, found);
            northwest.queryHelper(range, found);
            southeast.queryHelper(range, found);
            southwest.queryHelper(range, found);
        }
    }

    public static void main(String[] args) {
        // Define a smaller boundary for the QuadTree
        Rectangle boundary = new Rectangle(100, 100, 200, 200);
        QuadTree tree = new QuadTree(boundary, "Root"); // Initializing with "Root" as the path for the initial rectangle
    
        Random random = new Random();
        String[] serviceTypes = {"Cafe", "Restaurant", "Gas Station", "Library", "Hospital", "School", "Store", "Park", "Hotel", "Gym"};
    
        // Start performance tracking
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
    
        // Insert only 10 places to see how well the subdivision works on a small scale
        for (int i = 0; i < 10; i++) {
            double x = boundary.getLeft() + random.nextDouble() * boundary.getW(); // Correctly position points within the boundary
            double y = boundary.getTop() + random.nextDouble() * boundary.getH();
            String allServiceTypes = "";
            int numberOfServices = random.nextInt(3) + 1;
            for (int n = 0; n < numberOfServices; n++) {
                int serviceIndex = random.nextInt(serviceTypes.length);
                String serviceType = serviceTypes[serviceIndex];
                if (!allServiceTypes.contains(serviceType)) {
                    allServiceTypes += serviceType + (n < numberOfServices - 1 ? "," : "");
                }
            }
            tree.insert(new Place(x, y, allServiceTypes));
        }
    
        // Query a small area within the map to demonstrate the targeted retrieval capabilities
        Rectangle searchArea = new Rectangle(100, 100, 200, 200); // A more focused central area within the map
        ArrayList<Place> found = new ArrayList<>();
        tree.query(searchArea, found);
    
        // Performance tracking end
        long endTime = System.nanoTime();
        runtime.gc();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();
    
        long memoryUsed = startMemory - endMemory;
        double duration = (endTime - startTime) / 1_000_000_000.0;
    
        // Output results
        System.out.println("Displaying found places:");
        for (int i = 0; i < found.size(); i++) {
            System.out.println(found.get(i));
        }
    
        System.out.println("Total places found: " + found.size());
        System.out.println("Test duration: " + duration + " seconds");
        System.out.println("Memory used: " + memoryUsed + " bytes");
    }
        
}
