package quadTree;

import place.Place;
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

    public void query(Rectangle range, ArrayList<Place> found) {
        if (!boundary.intersects(range)) {
            return;
        }

        for (Place point : points.toArray(new Place[0])) {
            if (range.contains(point)) {
                found.add(point);
            }
        }

        if (divided) {
            northeast.query(range, found);
            northwest.query(range, found);
            southeast.query(range, found);
            southwest.query(range, found);
        }
    }

    public static void main(String[] args) {
        // Define the boundary for the entire QuadTree
        Rectangle boundary = new Rectangle(500000, 500000, 10000000, 10000000);
        QuadTree tree = new QuadTree(boundary);

        // Random number generator for place coordinates and service types
        Random random = new Random();
        int serviceTypeCount = 10; // Total number of different service types
        int numberOfPlaces = 100_000_000; // Total number of places to insert

        // Inserting a large number of randomly generated places into the QuadTree
        for (int i = 0; i < numberOfPlaces; i++) {
            double x = random.nextDouble() * boundary.getW() + boundary.getLeft();
            double y = random.nextDouble() * boundary.getH() + boundary.getTop();
            int[] serviceTypeIndexes = random.ints(0, serviceTypeCount).distinct().limit(random.nextInt(3) + 1).toArray();
            tree.insert(new Place(x, y, serviceTypeIndexes));
        }

        System.out.println("Inserted " + numberOfPlaces + " places.");

        // Define a search area within the boundary of the QuadTree
        Rectangle searchArea = new Rectangle(5500000, 5500000, 1000000, 1000000);
        ArrayList<Place> found = new ArrayList<>();
        tree.query(searchArea, found);

        System.out.println("Found " + found.size() + " places in the search area:");
        for (int i = 0; i < found.size() && i < 50; i++) { // Print up to 50 places for brevity
            System.out.println(found.get(i));
        }
    }

}
