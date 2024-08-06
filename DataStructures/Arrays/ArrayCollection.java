package assign03;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Daniel Kopta and Kira Halls and Braden Campbell
 * Implements the Collection interface using an array as storage.
 * The array must grow as needed.
 * An ArrayCollection can not contain duplicates.
 * All methods should be implemented as defined by the Java API, unless otherwise specified.
 * 
 * It is your job to fill in the missing implementations and to comment this class. 
 * Every method should have comments (Javadoc-style prefered). 
 * The comments should at least indicate what the method does, 
 * what the arguments mean, and what the returned value is. 
 * It should specify any special cases that the method
 * handles. Any code that is not self-explanatory should be commented.
 *
 * @param <T> - generic type placeholder
 */
public class ArrayCollection<T> implements Collection<T> {

	private T data[]; // Storage for the items in the collection
	private int size; // Keep track of how many items this collection holds

	// There is no clean way to convert between T and Object, so we suppress the warning.
	@SuppressWarnings("unchecked")  
	public ArrayCollection() {
		size = 0;
		// We can't instantiate an array of unknown type T, so we must create an Object array, and typecast
		data = (T[]) new Object[10]; // Start with an initial capacity of 10
	}

	/**
	 * This is a helper method specific to ArrayCollection
	 * Doubles the size of the data storage array, retaining its current contents.
	 */
	@SuppressWarnings("unchecked")
	private void grow() {
		T newData[] = (T[]) new Object[this.data.length * 2];
		for (int i = 0; i < this.size; i++) {
			newData[i] = this.data[i];
		}
		data = newData;
	}
	
	/**
	 *This method adds the specified T object to the data array
	 *of the ArrayCollection calling the method. Does not add
	 *the object if already contained in the ArrayCollection
	 *
	 *@param arg0 -The object to be added to the ArrayCollection
	 *@return True if the object was added to the ArrayCollection, 
	 *		false if the object was already in the ArrayCollection
	 */
	public boolean add(T arg0) {
		if (this.contains(arg0)) {
			return false;
		}
		if (this.size == this.data.length) {
			this.grow();
		}
		this.data[size] = arg0;
		this.size++;
		return true;
	}

	/**
	 *This method loops over the given parameter and adds 
	 *each value not already in this.data[] to the ArrayCollection.
	 *Calls the Add method to do so.
	 *
	 *@param arg0 - the collection of data we want to add to the 
	 *ArrayCollection object calling the method
	 *@return true if it adds at least one object to the ArrayCollection
	 *data[], false if it doesn't add any.
	 */
	public boolean addAll(Collection<? extends T> arg0) {
		boolean itemAdded = false;
		boolean didItemAdd = false;
		for (T value : arg0) {
			if (this.add(value) == true) {
				itemAdded = true;
			}
		}
		return itemAdded;
	}

	/**
	 *Delete all objects in the current ArrayCollection data object
	 *by assigning it to a new array of the same length as the previous
	 *data array. Set the ArrayCollection size back to 0.
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		this.data = (T[]) new Object[this.data.length];
		this.size = 0;
	}

	/**
	 *Loop over the current ArrayCollection data variable 
	 *to check if it contains the specified value. 
	 *
	 *@param arg0 - the object we want to check for 
	 *@return true if this.data contains arg0, false if not
	 */
	public boolean contains(Object arg0) {
		for (T value : data) {
			if (value != null) {
				if (value.equals(arg0)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *This method loops over the collection of data
	 *to add and checks it against the current ArrayCollection
	 *data to see if every item is contained in this.data.
	 *
	 *@param arg0 - the collection of data to check for
	 *@return true if every item in arg0 is contained in this.data
	 *false if not.
	 */
	public boolean containsAll(Collection<?> arg0) {
		for (Object value : arg0) {
			if (! this.contains(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 *Method to check if the specified ArrayCollection is empty
	 *
	 *@return true if empty, false if it isn't
	 */
	public boolean isEmpty() {
		if (this.size == 0) {
			return true;
		}
		return false;
	}

	/**
	 *Creates a new ArrayCollectionIterator Object
	 *
	 *@return ArrayCollectionIterator
	 */
	public Iterator<T> iterator() {
		ArrayCollectionIterator iterator = new ArrayCollectionIterator();
		return iterator;
	}

	/**
	 *Remove the specified object from the ArrayCollection
	 *
	 *@param arg0 - the object to be removed
	 *@return true if the item has been removed, false if the object was not in the ArrayCollection
	 */
	@SuppressWarnings("unchecked")
	public boolean remove(Object arg0) {
		Iterator<T> iterator = this.iterator();
		T newArray[] = (T[]) new Object[this.data.length];
		T currentValue = null;
		int currentIndex = 0;
		if (this.contains(arg0) == false) {
			return false;
		}
		else {
			while (iterator.hasNext()) {
				currentValue = iterator.next();
				if ( ! currentValue.equals(arg0)) {
					newArray[currentIndex++] = currentValue;
				}
			}
		}
		this.size += -1;
		data = newArray;
		return true;
	}

	/**
	 *Remove every item in the given collection parameter from 
	 *the ArrayCollection that calls this method 
	 *
	 *@param arg0 - the collection of items to be removed
	 *@return true if an item was removed from the ArrayCollection,
	 *false if nothing was removed
	 */
	public boolean removeAll(Collection<?> arg0) {
		boolean itemRemoved = false;
		for (Object value : arg0) {
			if (this.remove(value)) {
				itemRemoved = true;
			}
		}
		return itemRemoved;
	}

	/**
	 *Remove every item in the ArrayCollection that 
	 *is not also included in the given Collection. Essentially 
	 *creates a combined Collection that only includes values that overlap
	 *between this.ArrayCollection and the parameter collection 
	 *
	 *@param arg0 - the collection to check against this.ArrayCollection 
	 *@return true if we remove an item from this.arrayCollection, false 
	 *if nothing is removed
	 */
	public boolean retainAll(Collection<?> arg0) {
		boolean itemRemoved = false;
		ArrayCollection<T> combinedCollection = new ArrayCollection();
		Iterator<T> thisIterator = this.iterator();
		T currentThisValue = null;
		while (thisIterator.hasNext()) {
			currentThisValue = thisIterator.next();
			if (arg0.contains(currentThisValue)) {
				combinedCollection.add(currentThisValue);
			}
			else {
				itemRemoved = true;
			}
		}
		this.data = combinedCollection.getData();
		this.size = combinedCollection.getSize();
		return itemRemoved;
	}

	/**
	 *Gets the size of the ArrayCollection 
	 *that calls this method
	 *
	 *@return the size of the ArrayCollection that calls the method
	 */
	public int size() {
		return this.size;
	}

	/**
	 *Returns a new array that contains all the 
	 *items in this.data[]. The new array is the exact size 
	 *of the data in the array
	 *
	 *@return an object array that contains only the data in this.data[]
	 */
	public Object[] toArray() {
		Object[] array = new Object[this.size];
		for (int i = 0; i < this.size; i++) {
			array[i] = this.data[i];
		}
		return array;
	}

	/* 
	 * Don't implement this method (unless you want to).
	 * It must be here to complete the Collection interface.
	 * We will not test this method.
	 */
	public <T> T[] toArray(T[] arg0) {
		return null;
	}

	/**
	 * Getter for the data[] array in the ArrayCollection calling 
	 * this method
	 * 
	 * @return this.data;
	 */
	public T[] getData() {
		return this.data;
	}
	
	/**
	 * Get the length of the data array. Used for testing purposes
	 * 
	 * @return an int that is the length of the data array
	 */
	public int getDataLength() {
		return this.data.length;
	}
	
	/**
	 * Getter for the size of the ArrayCollection 
	 * that calls the getter
	 * 
	 * @return the size of the ArrayCollection
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 *Custom toString method for testing purposes
	 *
	 *@return the string conversion of the ArrayCollection calling 
	 *the method
	 */
	public String collectionToString() {
		String arrayToString = "{";
		for(int i = 0; i < this.data.length; i++) {
			if(i == this.data.length - 1) {
				arrayToString += this.data[i] + "}";
			}
			else {
				arrayToString += this.data[i] + ", ";
			}
		}
		return arrayToString;
	}
	
	/**
	 * A toString method used for testing purposes
	 * 
	 * @param array - the array to convert to a string
	 * @return - the string conversion of the parameter
	 */
	public String toString(Object[] array) {
		String toString = "";
		for (Object value : array) {
			toString += value;
			toString += " ";
		}
		return toString;
	}
	
	/**
	 * Sorting method specific to ArrayCollection - not part of the Collection interface.
     	Must implement a selection sort (see Assignment 2 for code).
     	Must not modify this ArrayCollection.
	 * @param cmp - the comparator that defines item ordering
	 * @return - the sorted list
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<T> toSortedList(Comparator<? super T> cmp) {
		ArrayList<T> sortedArray = new ArrayList<T>();
		for(int i = 0; i < this.size; i++) {
			sortedArray.add(this.data[i]);
		}
		T temp;
		for(int i = 0; i < this.size; i++) {
			int j, minIndex;
			for(j = i + 1, minIndex = i; j < this.size(); j++)
				if(cmp.compare(sortedArray.get(j), sortedArray.get(minIndex)) < 0)
					minIndex = j;
			temp = sortedArray.get(i);
			sortedArray.set(i, sortedArray.get(minIndex));
			sortedArray.set(minIndex, temp);
		}
		return sortedArray;
	}

	/**
	 * 
	 * @author Kira Halls and Braden Campbell
	 * Describe your ArrayCollectionIterator class here.
	 *
	 */
	private class ArrayCollectionIterator implements Iterator<T> {
		private int currentIndex = 0;
		private boolean isIllegalToRemove = true;
		
		public ArrayCollectionIterator() {
		}

		/**
		 *Checks to see if the ArrayCollection has another piece of data
		 */
		public boolean hasNext() {
			if (size < currentIndex + 1) {
				return false;
			}
			return true;
		}

		/**
		 *Gets the next value of the ArrayCollection
		 */
		public T next() {
			isIllegalToRemove = false;
			return data[currentIndex++];
		}

		/**
		 *Checks if you can remove the last seen item 
		 */
		public void remove() {
			if (isIllegalToRemove == true) {
				throw new IllegalStateException();
			}
			isIllegalToRemove = true;
		}
	}
	
	protected class StringComparator implements Comparator<String> {

		/**
		 *Uses the string compareTo() method to sort two String objects
		 *
		 */
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2) > 0 ? 1 : o1.compareTo(o2) < 0 ? -1 : 0;
		}
	}
	
	protected class IntegerComparator implements Comparator<Integer> {
		
		/**
		 *Uses the compareTo() method to sort to Integer objects
		 */
		@Override 
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2) > 0 ? 1 : o1.compareTo(o2) < 0 ? -1 : 0;
		}
	}
}
