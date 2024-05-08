package rectangle;

public class Rectangle {
    private int x, y, w, h;

    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    // Calculate boundaries based on the bottom-left origin
    public int getLeft() {
        return x;
    }

    public int getRight() {
        return x + w;
    }

    public int getTop() {
        return y;
    }

    public int getBottom() {
        return y + h;
    }

    public boolean contains(int px, int py) {
        return px >= getLeft() && px < getRight() && py >= getTop() && py < getBottom();
    }
    

    public boolean intersects(Rectangle other) {
        return this.getLeft() < other.getRight() && this.getRight() > other.getLeft() &&
                this.getTop() < other.getBottom() && this.getBottom() > other.getTop();
    }

    @Override
    public String toString() {
        return String.format("Rectangle[x=%d, y=%d, w=%d, h=%d, left=%d, right=%d, top=%d, bottom=%d]",
                x, y, w, h, getLeft(), getRight(), getTop(), getBottom());
    }
}
