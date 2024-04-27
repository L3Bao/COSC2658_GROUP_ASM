package rectangle;

import place.Place;

public class Rectangle {
    private double x, y; // Center of the rectangle
    private double w, h; // Width and height of the rectangle

    public Rectangle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // Calculate edges on-demand to save memory
    public double getLeft() {
        return x - w / 2;
    }

    public double getRight() {
        return x + w / 2;
    }

    public double getTop() {
        return y - h / 2;
    }

    public double getBottom() {
        return y + h / 2;
    }

    public boolean contains(Place point) {
        return getLeft() <= point.getX() && point.getX() <= getRight() &&
                getTop() <= point.getY() && point.getY() <= getBottom();
    }

    public boolean intersects(Rectangle range) {
        return !(getRight() < range.getLeft() || range.getRight() < getLeft() ||
                getBottom() < range.getTop() || range.getBottom() < getTop());
    }

    public Rectangle subdivide(String quadrant) {
        double halfW = this.w / 2;
        double halfH = this.h / 2;
        double newX, newY;

        switch (quadrant) {
            case "ne":
                newX = this.x + halfW / 2;
                newY = this.y - halfH / 2;
                break;
            case "nw":
                newX = this.x - halfW / 2;
                newY = this.y - halfH / 2;
                break;
            case "se":
                newX = this.x + halfW / 2;
                newY = this.y + halfH / 2;
                break;
            case "sw":
                newX = this.x - halfW / 2;
                newY = this.y + halfH / 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid quadrant specified");
        }
        return new Rectangle(newX, newY, halfW, halfH);
    }

    @Override
    public String toString() {
        return String.format("Rectangle[x=%.2f, y=%.2f, w=%.2f, h=%.2f, left=%.2f, right=%.2f, top=%.2f, bottom=%.2f]",
                x, y, w, h, getLeft(), getRight(), getTop(), getBottom());
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }
}
