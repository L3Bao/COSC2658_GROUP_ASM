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

    public QuadTree(Rectangle boundary) {
        this.boundary = boundary;
        this.points = new ArrayList<>();
        this.divided = false;
    }

    public boolean insert(Place point) {
        if (!this.boundary.contains(point)) {
            return false;
        }
        if (points.size() < MAX_CAPACITY) {
            points.insertAt(points.size(), point);
            return true;
        }
        if (!divided) {
            subdivide();
        }
        if (northeast.insert(point)) return true;
        if (northwest.insert(point)) return true;
        if (southeast.insert(point)) return true;
        if (southwest.insert(point)) return true;
        return false;
    }

    private void subdivide() {
        double halfWidth = boundary.getW() / 2.0;
        double halfHeight = boundary.getH() / 2.0;
        double x = boundary.getX();
        double y = boundary.getY();
        northeast = new QuadTree(new Rectangle(x + halfWidth, y, halfWidth, halfHeight));
        northwest = new QuadTree(new Rectangle(x, y, halfWidth, halfHeight));
        southeast = new QuadTree(new Rectangle(x + halfWidth, y + halfHeight, halfWidth, halfHeight));
        southwest = new QuadTree(new Rectangle(x, y + halfHeight, halfWidth, halfHeight));
        divided = true;
    }

    public ArrayList<Place> query(Rectangle range, ArrayList<Place> found) {
        int[] totalFound = new int[1]; // Use an array to hold the count to be mutable in recursion
        queryHelper(range, found, totalFound);
        System.out.println("Total places found: " + totalFound[0]);
        return found;
    }

    private void queryHelper(Rectangle range, ArrayList<Place> found, int[] totalFound) {
        if (!boundary.intersects(range)) {
            return;
        }
        for (int i = 0; i < points.size(); i++) {
            Place p = points.get(i);
            if (range.contains(p)) {
                totalFound[0]++;
                if (found.size() < 50) {
                    found.insertAt(found.size(), p);
                }
            }
        }
        if (divided) {
            northeast.queryHelper(range, found, totalFound);
            northwest.queryHelper(range, found, totalFound);
            southeast.queryHelper(range, found, totalFound);
            southwest.queryHelper(range, found, totalFound);
        }
    }



    public static void main(String[] args) {
        Rectangle boundary = new Rectangle(5000000, 5000000, 10000000, 10000000);
        QuadTree tree = new QuadTree(boundary);

        Random random = new Random();
        String[] serviceTypes = {"Cafe", "Restaurant", "Gas Station", "Library", "Hospital", "School", "Store", "Park", "Hotel", "Gym"};

        // Start performance tracking
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        // Insert places
        for (int i = 0; i < 100000000; i++) {  // Reduced number for practical testing
            double x = random.nextDouble() * 10000000;
            double y = random.nextDouble() * 10000000;
            String serviceType = serviceTypes[random.nextInt(serviceTypes.length)];
            tree.insert(new Place(x, y, serviceType));
        }

        // Query places
        Rectangle searchArea = new Rectangle(5000000, 5000000, 100000, 100000);
        ArrayList<Place> found = tree.query(searchArea, new ArrayList<>());

        // Performance tracking end
        long endTime = System.nanoTime();
        runtime.gc();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();

        long memoryUsed = startMemory - endMemory;
        double duration = (endTime - startTime) / 1_000_000_000.0;

        // Output results
        System.out.println("Displaying up to 50 places:");
        for (int i = 0; i < Math.min(found.size(), 50); i++) {
            System.out.println(found.get(i));
        }

        System.out.println("Test duration: " + duration + " seconds");
        System.out.println("Memory used: " + memoryUsed + " bytes");
    }
}
