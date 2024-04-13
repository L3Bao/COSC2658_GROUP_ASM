package rectangle;

class Point {
    private double x;
    private double y;
    private Object userData;

    public Point(double x, double y, Object data) {
        this.x = x;
        this.y = y;
        this.userData = data;
    }

    // Getters for the properties
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Object getUserData() {
        return userData;
    }
}


public class Rectangle {
    private double x, y; // Center of the rectangle
    private double w, h; // Width and height of the rectangle
    private double left, right, top, bottom; // Edges of the rectangle

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.left = x - w / 2;
        this.right = x + w / 2;
        this.top = y - h / 2;
        this.bottom = y + h / 2;
    }

    public boolean contains(Point point) {
        return this.left <= point.getX() && point.getX() <= this.right &&
                this.top <= point.getY() && point.getY() <= this.bottom;
    }

    public boolean intersects(Rectangle range) {
        return !(this.right < range.left || range.right < this.left ||
                this.bottom < range.top || range.bottom < this.top);
    }

    public Rectangle subdivide(String quadrant) {
        return switch (quadrant) {
            case "ne" -> new Rectangle(this.x + this.w / 4, this.y - this.h / 4, this.w / 2, this.h / 2);
            case "nw" -> new Rectangle(this.x - this.w / 4, this.y - this.h / 4, this.w / 2, this.h / 2);
            case "se" -> new Rectangle(this.x + this.w / 4, this.y + this.h / 4, this.w / 2, this.h / 2);
            case "sw" -> new Rectangle(this.x - this.w / 4, this.y + this.h / 4, this.w / 2, this.h / 2);
            default -> throw new IllegalArgumentException("Invalid quadrant specified");
        };
    }

    public double xDistanceFrom(Point point) {
        if (this.left <= point.getX() && point.getX() <= this.right) {
            return 0;
        }

        return Math.min(
                Math.abs(point.getX() - this.left),
                Math.abs(point.getX() - this.right)
        );
    }

    public double yDistanceFrom(Point point) {
        if (this.top <= point.getY() && point.getY() <= this.bottom) {
            return 0;
        }

        return Math.min(
                Math.abs(point.getY() - this.top),
                Math.abs(point.getY() - this.bottom)
        );
    }

    public double sqDistanceFrom(Point point) {
        double dx = this.xDistanceFrom(point);
        double dy = this.yDistanceFrom(point);
        return dx * dx + dy * dy;
    }

    public double distanceFrom(Point point) {
        return Math.sqrt(this.sqDistanceFrom(point));
    }
}
