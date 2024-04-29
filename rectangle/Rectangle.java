package rectangle;

import place.Place;

public class Rectangle {
    private double x, y; // Center of the rectangle
    private double w, h; // Width and height of the rectangle
    private double left, right, top, bottom; // Cached boundaries
    private boolean boundariesNeedUpdate = true; // Flag to track when boundaries need recalculating

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        updateBoundaries();
    }

    private void updateBoundaries() {
        this.left = x - w / 2;
        this.right = x + w / 2;
        this.top = y - h / 2;
        this.bottom = y + h / 2;
        boundariesNeedUpdate = false;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLeft() {
        if (boundariesNeedUpdate) updateBoundaries();
        return left;
    }

    public double getRight() {
        if (boundariesNeedUpdate) updateBoundaries();
        return right;
    }

    public double getTop() {
        if (boundariesNeedUpdate) updateBoundaries();
        return top;
    }

    public double getBottom() {
        if (boundariesNeedUpdate) updateBoundaries();
        return bottom;
    }

    public boolean contains(Place point) {
        return getLeft() <= point.getX() && point.getX() <= getRight() &&
                getTop() <= point.getY() && point.getY() <= getBottom();
    }

    public boolean intersects(Rectangle range) {
        return !(getRight() < range.getLeft() || range.getRight() < getLeft() ||
                getBottom() < range.getTop() || range.getBottom() < getTop());
    }

    public enum Quadrant {
        NE, NW, SE, SW
    }

    public Rectangle subdivide(Quadrant quadrant) {
        double newW = w / 2;
        double newH = h / 2;
        double newX, newY;
    
        switch (quadrant) {
            case NE:
                newX = x + newW / 2;
                newY = y - newH / 2;
                break;
            case NW:
                newX = x - newW / 2;
                newY = y - newH / 2;
                break;
            case SE:
                newX = x + newW / 2;
                newY = y + newH / 2;
                break;
            case SW:
                newX = x - newW / 2;
                newY = y + newH / 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid quadrant specified");
        }
        return new Rectangle(newX, newY, newW, newH);
    }
    
    @Override
    public String toString() {
        return String.format("Rectangle[x=%.2f, y=%.2f, w=%.2f, h=%.2f, left=%.2f, right=%.2f, top=%.2f, bottom=%.2f]",
                x, y, w, h, getLeft(), getRight(), getTop(), getBottom());
    }

    // Setters that mark boundaries for update
    public void setX(double x) {
        this.x = x;
        boundariesNeedUpdate = true;
    }

    public void setY(double y) {
        this.y = y;
        boundariesNeedUpdate = true;
    }

    public void setW(double w) {
        this.w = w;
        boundariesNeedUpdate = true;
    }

    public void setH(double h) {
        this.h = h;
        boundariesNeedUpdate = true;
    }
}
