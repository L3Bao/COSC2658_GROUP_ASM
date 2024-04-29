package rectangle;

public class Rectangle {
    private float x, y, w, h;

    public Rectangle(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    // Calculate boundaries based on the bottom-left origin
    public float getLeft() {
        return x;
    }

    public float getRight() {
        return x + w;
    }

    public float getTop() {
        return y + h;
    }

    public float getBottom() {
        return y;
    }

    public boolean contains(float px, float py) {
        return px >= getLeft() && px <= getRight() && py >= getBottom() && py <= getTop();
    }

    public boolean intersects(Rectangle other) {
        return this.getLeft() < other.getRight() && this.getRight() > other.getLeft() &&
                this.getTop() > other.getBottom() && this.getBottom() < other.getTop();
    }

    @Override
    public String toString() {
        return String.format("Rectangle[x=%.2f, y=%.2f, w=%.2f, h=%.2f, left=%.2f, right=%.2f, top=%.2f, bottom=%.2f]",
                x, y, w, h, getLeft(), getRight(), getTop(), getBottom());
    }
}
