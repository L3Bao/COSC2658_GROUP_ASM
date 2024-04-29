package arrayList;
import iterable.Iterable;
import iterable.Iterator;

public class ArrayList<T> implements Iterable<T> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_CAPACITY = 4;

    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    // Constructor with initial capacity to prevent resizing at early stages if the expected size is known
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            elements = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            elements = new Object[0];
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - elements.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - Integer.MAX_VALUE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        elements = java.util.Arrays.copyOf(elements, newCapacity);
    }

    private int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) { // Overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > Integer.MAX_VALUE) ? Integer.MAX_VALUE : Integer.MAX_VALUE - 8;
    }

    public boolean add(T element) {
        try {
            ensureCapacity(size + 1);  // Ensure there is enough space
            elements[size++] = element;  // Add the element
            return true;  // Return true as the operation is always successful
        } catch (OutOfMemoryError e) {
            // Handle memory errors gracefully
            return false;
        }
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) elements[index];
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    public void addAll(ArrayList<T> other) {
        ensureCapacity(size + other.size());  // Ensure there is enough space for all elements
        System.arraycopy(other.elements, 0, this.elements, size, other.size());  // Efficient array copying
        size += other.size();  // Update size after adding all elements
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException("No more elements");
            }
            return (T) elements[currentIndex++];
        }
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(elements[i].toString());
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
