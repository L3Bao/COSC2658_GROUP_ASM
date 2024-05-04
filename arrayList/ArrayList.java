package arrayList;
import iterable.Iterable;
import iterable.Iterator;
import comparable.*;

public class ArrayList<T extends Comparabling<T>> implements Iterable<T> {
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
        elements = copyOf(elements, newCapacity);
    }

    private Object[] copyOf(Object[] original, int newLength) {
        Object[] copy = new Object[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }


    public boolean add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
        return true;
    }

    @SuppressWarnings("unchecked")
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

    public boolean remove(T element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                // Shift elements to the left to fill the gap
                System.arraycopy(elements, i + 1, elements, i, size - i - 1);
                elements[--size] = null; // Clear the reference to the removed element
                return true;
            }
        }
        return false; // Element not found
    }


    public void addAll(ArrayList<T> other) {
        ensureCapacity(size + other.size());  // Ensure there is enough space for all elements
        System.arraycopy(other.elements, 0, this.elements, size, other.size());  // Efficient array copying
        size += other.size();  // Update size after adding all elements
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void sort() {
        quickSort(0, size - 1);
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);

            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        @SuppressWarnings("unchecked")
        T pivot = (T) elements[high];  // Safely cast to T
        int i = (low - 1);
    
        for (int j = low; j < high; j++) {
            @SuppressWarnings("unchecked")
            T currentElement = (T) elements[j];  // Safely cast to T before comparing
    
            if (currentElement.compareTo(pivot) <= 0) {
                i++;
                // Swap elements[i] and elements[j]
                Object temp = elements[i];
                elements[i] = elements[j];
                elements[j] = temp;
            }
        }
    
        // Swap elements[i+1] and elements[high] (or pivot)
        Object temp = elements[i + 1];
        elements[i + 1] = elements[high];
        elements[high] = temp;
    
        return i + 1;
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
            return get(currentIndex++);
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
