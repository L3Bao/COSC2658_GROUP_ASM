package arrayList;

public interface List<T> {
  // insert a value at a specific index (index starts from zero)
  // if the index is invalid, return false
  // return true in other cases
  public boolean insertAt(int index, T value);
  
  // insert a value before another value  
  // if there are multiple searchValue, the first one (from the left) is used
  // if searchValue doesn't exist, return false
  // return true in other cases
  public boolean insertBefore(T searchValue, T value);

  // insert a value after another value  
  // if there are multiple searchValue, the first one (from the left) is used
  // if searchValue doesn't exist, return false
  // return true in other cases
  public boolean insertAfter(T searchValue, T value);

  // remove a value at a specific index (index starts from zero)
  // if the index is invalid, return false
  // return true in other cases
  public boolean removeAt(int index);

  // remove a value in the list
  // if there are multiple value, remove the first one (from the left)
  // if value doesn't exist, return false
  // return true in other cases
  public boolean remove(T value);

  // replace a value at a specific index (index start from zero)
  // if the index is invalid, return false
  // return true in other cases
  public boolean replaceAt(int index, T value);

  // replace a value in the list
  // if there are multiple value, replace the first one (from the left)
  // if value doesn't exist, return false
  // return true in other cases
  public boolean replaceValue(T valueToBeReplaced, T valueToReplace);

  // return whether a value exist in the list
  public boolean contains(T value);

  // return the number of elements in the list
  public int size();

  // return whether the next value exist in the list
  public boolean hasNext();

  // return the next value in the list, and advance to the next index
  // if there is no value available, return null
  public T next();

  // reset the iteration
  public void reset();

  // return a value at a specific index
  // return null if index is invalid
  public T get(int index);
}
