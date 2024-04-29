package quadTree;

import place.Place;
import place.ServiceRegistry;
import rectangle.*;
// import rectangle.Rectangle.Quadrant;

import java.util.Random;

import arrayList.ArrayList;
import iterable.Iterator;

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
        if (!divided) {
            // System.out.println("Starting subdivision for boundary: " + boundary);
            divided = true;

            // Define the quadrants' boundaries
            Rectangle neRect = boundary.subdivide(Rectangle.Quadrant.NE);
            Rectangle nwRect = boundary.subdivide(Rectangle.Quadrant.NW);
            Rectangle seRect = boundary.subdivide(Rectangle.Quadrant.SE);
            Rectangle swRect = boundary.subdivide(Rectangle.Quadrant.SW);

            // System.out.println("NE Rect: " + neRect);
            // System.out.println("NW Rect: " + nwRect);
            // System.out.println("SE Rect: " + seRect);
            // System.out.println("SW Rect: " + swRect);

            // Initialize subtrees even if they end up empty
            northeast = new QuadTree(neRect);
            northwest = new QuadTree(nwRect);
            southeast = new QuadTree(seRect);
            southwest = new QuadTree(swRect);

            // Distribute existing points into the new quadrants
            Iterator<Place> iterator = points.iterator();
            while (iterator.hasNext()) {
                Place point = iterator.next();
                // Ensure each point is only placed into one quadrant.
                if (neRect.contains(point)) {
                    northeast.points.add(point);
                } else if (nwRect.contains(point)) {
                    northwest.points.add(point);
                } else if (seRect.contains(point)) {
                    southeast.points.add(point);
                } else if (swRect.contains(point)) {
                    southwest.points.add(point);
                } else {
                    System.err.println("Error: Point " + point + " could not be placed in any quadrant.");
                }
            }

            points.clear(); // Clear the current node's points since they have been redistributed
        } else {
            System.out.println("Subdivision skipped as already divided for boundary: " + boundary);
        }
    }

    public boolean insert(Place point) {
        if (!boundary.contains(point)) {
            return false; // Point is outside the QuadTree boundary
        }

        if (points.size() < MAX_CAPACITY && !divided) {
            points.add(point); // Add point to this node
            return true;
        }

        // If we're here, the node is at capacity or already divided; ensure subdivision
        // has occurred
        if (!divided) {
            subdivide(); // This will only happen once
        }

        // Now that we've ensured this node is divided, attempt to insert into the
        // appropriate subtree
        if (northeast.insert(point))
            return true;
        if (northwest.insert(point))
            return true;
        if (southeast.insert(point))
            return true;
        if (southwest.insert(point))
            return true;

        // The point should have been added to one of the subtrees; if it hasn't,
        // something is wrong
        System.err.println("Failed to insert point into subtrees: " + point);
        return false; // Point was not added anywhere, return false
    }

    public void query(Rectangle range, ArrayList<Place> found, Integer serviceBitmask) {
        if (!boundary.intersects(range)) {
            return;
        }

        Iterator<Place> iterator = points.iterator();
        while (iterator.hasNext()) {
            Place point = iterator.next();
            if (range.contains(point) && (serviceBitmask == null || hasAnyService(point, serviceBitmask))) {
                found.add(point);
            }
        }

        // Recurse into child quadrants only if they intersect the search range
        if (divided) {
            if (northeast != null && northeast.boundary.intersects(range)) {
                northeast.query(range, found, serviceBitmask);
            }
            if (northwest != null && northwest.boundary.intersects(range)) {
                northwest.query(range, found, serviceBitmask);
            }
            if (southeast != null && southeast.boundary.intersects(range)) {
                southeast.query(range, found, serviceBitmask);
            }
            if (southwest != null && southwest.boundary.intersects(range)) {
                southwest.query(range, found, serviceBitmask);
            }
        }
    }

    private boolean hasAnyService(Place place, int serviceBitmask) {
        return (place.getServiceBitmask() & serviceBitmask) != 0;
    }

    public void insertBatch(ArrayList<Place> batch) {
        if (!divided && points.size() + batch.size() > MAX_CAPACITY) {
            subdivide();
        }

        // Use an iterator to go through the batch after subdivision
        Iterator<Place> iterator = batch.iterator();
        while (iterator.hasNext()) {
            Place point = iterator.next();
            boolean inserted = insert(point); // Call insert which will handle subtree insertion if necessary
            if (!inserted) {
                System.err.println("Failed to insert point: " + point);
            }
        }
    }

    public static void main(String[] args) {
        Rectangle boundary = new Rectangle(0, 0, 10000000, 10000000);
        QuadTree tree = new QuadTree(boundary);
        Random random = new Random();
        int serviceTypeCount = ServiceRegistry.getServiceTypes().length;
        int numberOfPlaces = 10000000; // Total number of places to insert
        int batchSize = 1000; // Number of places per batch

        long startTime = System.currentTimeMillis();

        ArrayList<Place> batch = new ArrayList<>();

        for (int i = 0; i < numberOfPlaces; i++) {
            double x = random.nextDouble() * boundary.getW() + boundary.getLeft();
            double y = random.nextDouble() * boundary.getH() + boundary.getTop();
            int serviceBitmask = 0;
            for (int j = 0; j < random.nextInt(3) + 1; j++) {
                serviceBitmask |= (1 << random.nextInt(serviceTypeCount));
            }
            batch.add(new Place(x, y, serviceBitmask));

            if (batch.size() == batchSize) {
                tree.insertBatch(batch);
                batch.clear(); // Clear the batch after insertion
            }
        }

        if (!batch.isEmpty()) {
            tree.insertBatch(batch); // Insert any remaining points
            batch.clear();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Inserted " + numberOfPlaces + " places in " + (endTime - startTime) + " ms.");

        // Define a search area and perform a query
        Rectangle searchArea = new Rectangle(0, 0, 5000000, 5000000);
        ArrayList<Place> found = new ArrayList<>();
        startTime = System.currentTimeMillis();
        tree.query(searchArea, found, null);
        endTime = System.currentTimeMillis();

        System.out.println("Query completed in " + (endTime - startTime) + " ms.");
        System.out.println("Found " + found.size() + " places with any services in the search area:");
        for (int i = 0; i < found.size() && i < 50; i++) {
            System.out.println(found.get(i));
        }

        // Rectangle boundary = new Rectangle(0, 0, 1000, 1000); // Adjust boundary as
        // needed
        // QuadTree tree = new QuadTree(boundary);

        // // Sample places to insert
        // Place[] samplePlaces = {
        // new Place(100, 100, 5), // x=100, y=100, serviceBitmask=5
        // new Place(200, 300, 3), // x=200, y=300, serviceBitmask=3
        // new Place(500, 700, 6), // x=500, y=700, serviceBitmask=6
        // new Place(800, 200, 4) // x=800, y=200, serviceBitmask=4
        // // Add more sample places as needed
        // };

        // // Insert sample places
        // for (Place place : samplePlaces) {
        // tree.insert(place);
        // }

        // // Define a search area
        // Rectangle searchArea = new Rectangle(0, 0, 1000, 1000); // Adjust search area
        // as needed

        // // Perform a query
        // ArrayList<Place> found = new ArrayList<>();
        // tree.query(searchArea, found, null); // Pass null for serviceBitmask to query
        // all services

        // // Display results
        // System.out.println("Places found in the search area:");
        // Iterator<Place> iterator = found.iterator();
        // while (iterator.hasNext()) {
        // Place place = iterator.next();
        // System.out.println(place + " - Services: " + place.getServiceTypeNames(new
        // ServiceRegistry()));
        // }
    }

}
