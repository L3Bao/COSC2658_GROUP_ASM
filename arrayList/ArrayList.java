package arrayList;

public class ArrayList<T> implements List<T> {
  private int size;
  private int pointer;
  private static int CAPACITY = 1000;
  private T[] items;

  public ArrayList() {
    size = 0;
    pointer = 0;
    items = (T[]) new Object[CAPACITY];
  }

  // Big-O time complexity of insertAt: O(n)
  @Override
  public boolean insertAt(int index, T value) {
    if (index > size) {
      return false;
    }
    shiftRight(index);
    items[index] = value;
    size++;
    return true;
  }

  // Big-O time complexity of insertBefore: O(n)
  @Override
  public boolean insertBefore(T searchValue, T value) {
    for (int i = 0; i < size; i++) {
      if (items[i].equals(searchValue)) {
        return insertAt(i, value);
      }
    }
    return false;
  }

  // Big-O time complexity of insertAfter: O(n)
  @Override
  public boolean insertAfter(T searchValue, T value) {
    for (int i = 0; i < size; i++) {
      if (items[i].equals(searchValue)) {
        return insertAt(i + 1, value);
      }
    }
    return false;
  }

  // Big-O time complexity of removeAt: O(n)
  @Override
  public boolean removeAt(int index) {
    if (index >= size) {
      return false;
    }
    shiftLeft(index);
    size--;
    return true;
  }

  // Big-O time complexity of remove: O(n)
  @Override
  public boolean remove(T value) {
    for (int i = 0; i < size; i++) {
      if (items[i].equals(value)) {
        return removeAt(i);
      }
    }
    return false;
  }

  // Big-O time complexity of contains: O(n)
  @Override
  public boolean contains(T value) {
    for (int i = 0; i < size; i++) {
      if (items[i].equals(value)) {
        return true;
      }
    }
    return false;
  }

  // Big-O time complexity of size: O(1)
  @Override
  public int size() {
    return size;
  }

  // Big-O time complexity of reset: O(1)
  @Override
  public void reset() {
    pointer = 0;
  }

  // Big-O time complexity of get: O(1)
  @Override
  public T get(int index) {
    if (index >= size) {
      return null;
    }
    return items[index];
  }

  // Big-O time complexity of hasNext: O(1)
  @Override
  public boolean hasNext() {
    return (pointer < size);
  }

  // Big-O time complexity of next: O(1)
  @Override
  public T next() {
    pointer++;
    return items[pointer - 1];
  }

  // Big-O time complexity of replaceValue: O(n)
  public boolean replaceValue(T valueToBeReplaced, T valueToReplace) {
    for (int i = 0; i < size; i++) {
      if (items[i].equals(valueToBeReplaced)) {
        items[i] = valueToReplace;
        return true;
      }
    }
    return false;
  }

  // Big-O time complexity of replaceAt: O(1)
  public boolean replaceAt(int index, T value) {
    if (index >= size) {
      return false;
    }
    items[index] = value;
    return true;
  }

  // shift all elements from index one position to the right
  private void shiftRight(int index) {
    for (int i = size; i > index; i--) {
      items[i] = items[i - 1];
    }
  }

  // shift all elements from the end one position to the left
  // until index
  private void shiftLeft(int index) {
    for (int i = index + 1; i < size; i++) {
      items[i - 1] = items[i];
    }
  }

  public static void main(String[] args) {
    List<String> names = new ArrayList<>();
    names.insertAt(0, "World");  // World
    names.insertAt(0, "Hello");  // Hello, World
    names.insertAt(0, "RMIT");  // RMIT, Hello, World
    System.out.println("-------First Test-------");
    names.reset();
    while (names.hasNext()) {
      System.out.println(names.next());
    }
    names.insertBefore("RMIT", "SSET");  // SSET, RMIT, Hello, World
    names.insertAfter("World", "4.0");  // SSET, RMIT, Hello, World, 4.0
    names.insertAfter("Alice", "Wonderland");  // SSET, RMIT, Hello, World, 4.0 (no change)
    System.out.println("-------Second Test-------");
    names.reset();
    while (names.hasNext()) {
      System.out.println(names.next());
    }
    names.removeAt(1);  // // SSET, Hello, World, 4.0
    names.remove("4.0");  // // SSET, Hello, World
    System.out.println("-------Third Test-------");
    names.reset();
    while (names.hasNext()) {
      System.out.println(names.next());
    }
    System.out.println("-------More Test-------");
    System.out.println("Value at index 1: " + names.get(1));  // Hello
    System.out.println("Alice exists in the list? " + names.contains("Alice"));  // false
    System.out.println("SSET exists in the list? " + names.contains("SSET"));  // true

    // Test replaceValue and replaceAt
    names.replaceValue("SSET", "ABC");  // Replace "SSET" with "ABC"
    names.replaceAt(2, "XYZ"); // Replace element at index 2 with "XYZ"
    names.reset();
    System.out.println("-------Replacement Test-------");
    while (names.hasNext()) {
      System.out.println(names.next());
    }
  }
}

