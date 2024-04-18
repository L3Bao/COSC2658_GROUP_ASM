package kdtree;

public class Node {
    int dimension;
    int[] values;
    Node left = null;
    Node right = null;

    public Node(int dimension, int[] values) {
        super();
        this.dimension = dimension;
        this.values = values;
    }

    @Override
    public String toString() {
        return "Node [" +
               "values = " + arrayToString(values) +
               ", left = " + left +
               ", right = " + right +
               "]";
    }

    // Convert array of integers to a string representation
    private String arrayToString(int[] array) {
        if (array == null || array.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
