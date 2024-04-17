package quadTree;

import place.Place;
import rectangle.Rectangle;
import arrayList.ArrayList; // Assuming this is a list for holding Place objects in each node

public class QuadTree {
    private static final int MAX_CAPACITY = 4;  // Maximum items per QuadTree node
    private Rectangle boundary;  // Spatial boundary of this node
    private ArrayList<Place> points;  // Points contained in this node
    private boolean divided;  // Indicates if this node has been subdivided
    private QuadTree northeast;
    private QuadTree northwest;
    private QuadTree southeast;
    private QuadTree southwest;

    public QuadTree(Rectangle boundary) {
        this.boundary = boundary;
        this.points = new ArrayList<>();
        this.divided = false;
    }

    // Insert a point into the QuadTree
    public boolean insert(Place point) {
        if (!this.boundary.contains(point)) {
            return false;  // Ignore points that do not belong in this boundary
        }

        if (points.size() < MAX_CAPACITY) {
            points.insertAt(points.size(), point);
            return true;
        }

        if (!divided) {
            subdivide();  // Subdivide the QuadTree further into 4
        }

        // Insert the point into the appropriate quadrant
        if (northeast.insert(point)) return true;
        if (northwest.insert(point)) return true;
        if (southeast.insert(point)) return true;
        if (southwest.insert(point)) return true;

        // Should not reach here in normal circumstances
        return false;
    }

    // Subdivides the current QuadTree into four quadrants
    private void subdivide() {
        northeast = new QuadTree(boundary.subdivide("ne"));
        northwest = new QuadTree(boundary.subdivide("nw"));
        southeast = new QuadTree(boundary.subdivide("se"));
        southwest = new QuadTree(boundary.subdivide("sw"));
        divided = true;
    }

    // Query the QuadTree for points within a specific range
    public ArrayList<Place> query(Rectangle range, ArrayList<Place> found) {
        if (!this.boundary.intersects(range)) {
            return found;  // Empty list if there is no intersection
        }

        for (int i = 0; i < points.size(); i++) {
            Place p = points.get(i);
            if (range.contains(p)) {
                found.insertAt(found.size(), p);
            }
        }

        if (divided) {
            northeast.query(range, found);
            northwest.query(range, found);
            southeast.query(range, found);
            southwest.query(range, found);
        }

        return found;
    }

    public static void main(String[] args) {
        // Define the boundary of the whole quadtree (e.g., a map boundary)
        Rectangle boundary = new Rectangle(0, 0, 100, 100);
        QuadTree tree = new QuadTree(boundary);
    
        // Add some places to the quadtree
        tree.insert(new Place(10, 20, "Cafe"));
        tree.insert(new Place(30, 40, "Library"));
        tree.insert(new Place(70, 80, "Restaurant"));
    
        // Define a query range and find POIs within that range
        Rectangle searchArea = new Rectangle(25, 25, 50, 50);
        ArrayList<Place> found = tree.query(searchArea, new ArrayList<>());
        System.out.println("Found within the range: " + found.size());
    
        // Iterate over found items and print them
        for (int i = 0; i < found.size(); i++) {
            System.out.println(found.get(i));
        }
    }
    
}
