package quadTree;

import place.Place;
import place.ServiceRegistry;
import rectangle.*;
// import rectangle.Rectangle.Quadrant;

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

    private int getCurrentCapacity() {
        return INITIAL_CAPACITY + (int) (Math.log(depth + 1) * INITIAL_CAPACITY);
    }

    private void subdivide() {
        if (divided || points.isEmpty()) return; // Avoid unnecessary subdivision if already divided or no points to subdivide
    
        divided = true;  // Mark as divided early to prevent multiple subdivisions in recursive insertions
    
        float halfWidth = boundary.getW() / 2;
        float halfHeight = boundary.getH() / 2;
        float x = boundary.getX();
        float y = boundary.getY();
    
        // Initialize child quadrants lazily
        northeast = new QuadTree(new Rectangle(x + halfWidth, y + halfHeight, halfWidth, halfHeight), depth + 1);
        northwest = new QuadTree(new Rectangle(x, y + halfHeight, halfWidth, halfHeight), depth + 1);
        southeast = new QuadTree(new Rectangle(x + halfWidth, y, halfWidth, halfHeight), depth + 1);
        southwest = new QuadTree(new Rectangle(x, y, halfWidth, halfHeight), depth + 1);
    
        // Distribute existing points into appropriate quadrants
        redistributePoints();
    }
    
    private void redistributePoints() {
        ArrayList<Place> tempPoints = new ArrayList<>(); // Copy to a temporary list to avoid ConcurrentModificationException
        points.clear(); // Clear original points list since they'll be reinserted into child quadrants
    
        iterable.Iterator<Place> iterator = tempPoints.iterator();
        while (iterator.hasNext()) {
            Place point = iterator.next();
            if (!insert(point)) { // Reinsert points into the correct quadrant
                System.err.println("Failed to reinsert point during subdivision: " + point);
            }
        }
    }
    
    

    public boolean insert(Place point) {
        // Check if the point is within the boundary of the current quad
        if (!boundary.contains(point.getX(), point.getY())) {
            return false;  // Skip insertion if point is out of bounds
        }
    
        // Ensure capacity and subdivide if necessary
        if (points.size() >= getCurrentCapacity() && !divided) {
            subdivide();
        }
    
        // Directly add the point if not divided
        if (!divided) {
            points.add(point);
            return true;
        }
    
        // Recursively insert the point into the appropriate subdivided quadrant
        if (northeast != null && northeast.insert(point)) return true;
        if (northwest != null && northwest.insert(point)) return true;
        if (southeast != null && southeast.insert(point)) return true;
        if (southwest != null && southwest.insert(point)) return true;
    
        // If no quadrant could insert the point, it's an error (this should not normally happen)
        System.err.println("Insertion failed for point: " + point + " in all quadrants.");
        return false;
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
        }
        Iterator<Place> iterator = batch.iterator();
        while (iterator.hasNext()) {
            Place point = iterator.next();
            if (!insert(point)) {
                System.err.println("Failed to insert point: " + point);
            }
        }
    }

    public static void main(String[] args) {
        // Create a QuadTree with a large boundary covering the whole map.
        Rectangle boundary = new Rectangle(0, 0, 10000000, 10000000);
        QuadTree tree = new QuadTree(boundary, 0);
        Random random = new Random();
        ArrayList<Place> batch = new ArrayList<>();
        int batchSize = 1000; // Define an optimal batch size
        int numberOfPoints = 100_000_000; // Total number of points to insert
    
        // Define the center of the query area
        float areaSize = 100000;

        long startInsertTime = System.nanoTime();
    
        // Generate points
        for (int i = 0; i < numberOfPoints; i++) {
            // Randomly distribute points across the entire boundary to ensure a uniform spread
            float x = random.nextFloat() * boundary.getW();
            float y = random.nextFloat() * boundary.getH();
            int serviceBitmask = generateServiceBitmask(random, ServiceRegistry.getServiceTypes().length);
    
            // Create a new place and add to batch
            Place newPlace = new Place(x, y, serviceBitmask);
            batch.add(newPlace);
    
            // Perform batch insertion when batch size is reached
            if (batch.size() == batchSize) {
                tree.insertBatch(batch);
                batch.clear(); // Clear the batch after insertion to free up memory
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
    }
    
    private static int generateServiceBitmask(Random random, int serviceTypeCount) {
        int serviceBitmask = 0;
        int countServices = random.nextInt(3) + 1;
        for (int j = 0; j < countServices; j++) {
            serviceBitmask |= 1 << random.nextInt(serviceTypeCount);
        }
        return serviceBitmask;
    }
    

}
