package assign05;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * This class contains methods that implement sorting algorithms to sort a given ArrayList. The two sorting algorithms used are mergesort and quicksort. 
 * Each sorting method has helper methods to implement the sorting, and a driver method to initialize necessary variables and check for any errors.
 * 
 * @author Kira Halls and Braden Campbell
 *
 */
public class ArrayListSorter {
	private static final int thresholdValue = 1000;
	
	
	/**
	 * The Driver method for the mergesort method. Creates a tempArray to hold values as we sort them in the merge method.
	 * Also creates the generic comparator to sort the objects of the ArrayList. Calls the mergesort implementation method
	 * to actually implement the merge sort. This method must be generic to accommodate multiple types of ArrayList. 
	 * 
	 * @param <T> The type of objects in the ArrayList to be sorted
	 * @param arrayList The ArrayList to be sorted
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> void mergesort(ArrayList<T> arrayList) { //Driver Method
		ArrayList<T> tempArray = new ArrayList<T>(arrayList);
		tempArray.ensureCapacity(arrayList.size() * 2); //ensuring there is more than enough capacity in tempArray for the merge method
		mergesort(arrayList, tempArray, 0, arrayList.size() -1);
	}

	
	/**
	 * The mergesort method that actually implements the mergesort as called from the Driver method above. This method must 
	 * perform a mergesort (i.e. separating the array into increasingly smaller halves until it reaches the designated threshold value
	 * that determines when the array is small enough, and then calls an insertionSort helper method to organize that smaller array. After 
	 * sorting both halves of that array using an insertion sort, it calls the merge method to sort and add both arrays to the tempArray and then 
	 * copies that over into the original array). Implemented using recursive calls to mergesort until the parameter array is the size of the 
	 * thresholdValue. 
	 * 
	 * @param <T> The type of objects contained in the ArrayList to be sorted
	 * @param unsortedArray The ArrayList that is being sorted
	 * @param tempArray The ArrayList that holds enough storage to contain the array while it is being sorted
	 * @param startIndex The index at which the current section of the array being sorted begins
	 * @param endIndex The index at which the current section of the array being sorted ends
	 * @param cmp The comparator for the objects contained in the ArrayList
	 * @return The sorted ArrayList
	 */
	private static <T extends Comparable<? super T>> ArrayList<T> mergesort(ArrayList<T> unsortedArray, ArrayList<T> tempArray, int startIndex, int endIndex) {
		if ((endIndex - startIndex) <= thresholdValue) {
			insertionSort(unsortedArray, startIndex, endIndex);
		}
		else {
			int midIndex = startIndex + (endIndex - startIndex) / 2;
			mergesort(unsortedArray, tempArray, startIndex, midIndex);
			mergesort(unsortedArray, tempArray, midIndex + 1, endIndex);
			merge(unsortedArray, tempArray, startIndex, midIndex, endIndex);
		}
		return unsortedArray;
	}
	
	
	/**
	 * A helper method for mergesort. This method performs an insertion sort on a small section of the 
	 * array being sorted to sort it. This method is only called once the array from mergesort reaches 
	 * the designated thresholdValue i.e. becomes small enough for an insertion sort to be effective. 
	 * 
	 * @param <T> The type of objects contained in the ArrayList being sorted
	 * @param arrayToSort The ArrayList being sorted, created by taking a small section of the original ArrayList from mergesort
	 * @param cmp The comparator that will sort the objects contained in the ArrayList
	 */
	private static <T extends Comparable<? super T>> void insertionSort(ArrayList<T> arrayToSort, int startIndex, int endIndex) {
		T currentI;
		T currentJ;
		for (int i = startIndex + 1; i <= endIndex; i++) {
			currentI = arrayToSort.get(i);
			for (int j = i - 1; j >= startIndex; j--) {
				currentJ = arrayToSort.get(j);
				if (currentJ.compareTo(currentI) <= 0) {
					break;
				}
				else {
					arrayToSort.set(j, currentI);
					arrayToSort.set(j+1, currentJ);
				}
			}
		}
	}
	
	
	/**
	 * This helper method merges the two sorted halves of the mergesort arrays together. It loops through each array and compares 
	 * the values against each other, and places the sorted values in their correct order in the tempArray. After all values in the 
	 * two halves have been looped through, it copies all values from the tempArray and places them in the ArrayList that is being sorted.
	 * 
	 * @param <T> The type of the objects contained in the ArrayList being sorted
	 * @param arrayList The ArrayList being sorted
	 * @param tempArray The ArrayList that temporarily holds the sorted values until they are copied over to the actual ArrayList
	 * @param startIndex The index at which the current section of the ArrayList being sorted starts
	 * @param midIndex The middle index of the section of the ArrayList currently being sorted. Designates the end of the first half of this ArrayList section
	 * @param endIndex The index at which the current section of the ArrayList being sorted starts
	 * @param cmp The comparator that will sort the objects contained in the ArrayList
	 */
	private static <T extends Comparable<? super T>> void merge(ArrayList<T> arrayList, ArrayList<T> tempArray, int startIndex, int midIndex, int endIndex) {
		int i = startIndex;
		int j = midIndex + 1;
		int mergePosition = startIndex;
		
		while (i <= midIndex && j <= endIndex) {
			if ((arrayList.get(i)).compareTo(arrayList.get(j)) <= 0) {
				tempArray.set(mergePosition++, arrayList.get(i++));
			}
			else {
				tempArray.set(mergePosition++, arrayList.get(j++));
			}
		}
		
		//Two additional while loops to copy any leftover values from left array or right array over to tempArray
		while (j <= endIndex) { 
			tempArray.set(mergePosition++, arrayList.get(j++));
		}
		while (i <= midIndex) {
			tempArray.set(mergePosition++, arrayList.get(i++));
		}
		
		//Remove all old values from arrayList and copy all values from tempArray over to the arrayList
		for (int k = startIndex; k < endIndex + 1; k++) {
			arrayList.set(k, tempArray.get(k));
		}
	}
	
	
	/**
	* This helper method is designed to help choose a pivot for quicksort. This method takes the items at
	* the first index, the middle index, and end index. The median of these three values is returned
	*
	* @param <T> The type of objects being compared and chosen
	* @param arrayList The ArrayList that objects are being chosen from
	* @return The median item of the three items chosen
	**/
	private static <T extends Comparable<? super T>> int fmePivot(ArrayList<T> arrayList, int startIndex, int endIndex) {
		if (endIndex < 2) { //If the arrayList is smaller than three items, just return zero (the first index)
			return 0;
		}
		ArrayList<T> smallList = new ArrayList<T>();
		smallList.add(arrayList.get(startIndex));
		smallList.add(arrayList.get(endIndex - 1));
		smallList.add(arrayList.get((endIndex)/2));
		insertionSort(smallList, 0, 2);
		if(smallList.get(1).equals(arrayList.get(startIndex))) {
			return startIndex;
		} 
		else if(smallList.get(1).equals(arrayList.get(endIndex -1))) {
			return endIndex;
		} 
		else {
			return (endIndex)/ 2;
		}
	}
	
	
	/**
	 * This helper method is designed to help choose a pivot for quicksort. This method generates a random
	 * number that will function as the index for the pivot. It then returns the value held at that index in the 
	 * ArrayList and that value is the chosen pivot. 
	 * 
	 * @param <T> The type of objects being compared and chosen
	 * @param arrayList The ArrayList that objects are chosen from
	 * @return The value contained at the randomly generated index, this value will become the pivot for the quicksort
	 */
	private static <T extends Comparable<? super T>> int randomSinglePivot(ArrayList<T> arrayList, int startIndex, int endIndex) {
		if (endIndex == 0) {
			return 0;
		}
		Random random = new Random();
		return random.nextInt(startIndex, endIndex + 1);
	}
	
	
	/**
	 * This helper method is designed to help choose a pivot for quicksort. It generates three random 
	 * indexes, retrieves the values at those indexes from arrayList, and adds them to an objectArray. 
	 * The objectArray then calls insertion sort and returns the middle value, thus returning the median of 
	 * the three randomly chosen values. 
	 * 
	 * @param <T> The type of objects being compared and chosen
	 * @param arrayList The arrayList that objects are chosen from
	 * @return The median of the three random values chosen, this value will become the pivot for the quicksort
	 */
	private static <T extends Comparable<? super T>> int randomSampleMedian(ArrayList<T> arrayList, int startIndex, int endIndex) {
		if (endIndex < 2 || endIndex == startIndex) { //If the arrayList is smaller than three items, just return zero (the first index)
			return 0;
		}
		Random random = new Random();
		ArrayList<T> objectArray = new ArrayList<T>();
		int firstObjectIndex = random.nextInt(startIndex, endIndex);
		T firstObject = arrayList.get(firstObjectIndex);
		int secondObjectIndex = random.nextInt(startIndex, endIndex);
		T secondObject = arrayList.get(secondObjectIndex);
		int thirdObjectIndex = random.nextInt(startIndex, endIndex);
		T thirdObject = arrayList.get(thirdObjectIndex);
		objectArray.add(firstObject);
		objectArray.add(secondObject);
		objectArray.add(thirdObject);
		insertionSort(objectArray, 0, 2);
		//if statements to return the specified index of the median value, not the median value itself
		if (objectArray.get(1).equals(firstObject)) { 
			return firstObjectIndex;
		}
		else if (objectArray.get(1).equals(secondObject)) {
			return secondObjectIndex;
		}
		else {
			return thirdObjectIndex;
		}
	}
	
	
	/**
	 * The driver method for quicksort. This method creates a new comparator object to compare the items to each other, sets the start index to 0 and the 
	 * end index to the length of the list - 1, and selects the first pivot index. It then calls the quicksort helper method to actually complete the sorting 
	 * algorithm. 
	 * 
	 * @param <T> Ensures the generic nature of the code. All item types contained in the arrayList must extend Comparable
	 * @param arrayList The arrayList to be sorted
	 */
	public static <T extends Comparable<? super T>> void quicksort(ArrayList<T> arrayList) { //Driver Method
		int startIndex = 0;
		int endIndex = arrayList.size() - 1;
		if (arrayList.size() == 0) { //check for empty arrayList 
			int zeroPivotIndex = 0;
			quicksort(arrayList, startIndex, endIndex, zeroPivotIndex);
		}
		else {
			//int pivotIndex = randomSampleMedian(arrayList, startIndex, endIndex + 1); //commented out lines below allow for different pivot selection strategies
			//int pivotIndex = fmePivot(arrayList, startIndex, endIndex);
			int pivotIndex = randomSinglePivot(arrayList, startIndex, endIndex);
			quicksort(arrayList, startIndex, endIndex, pivotIndex);
		}
	}
	
	
	/**
	 * The helper method that recursively recalls quicksort until the full array is sorted. Partitions the array around the pivot, with the left side 
	 * being all values less than the pivot value, and the right side being all values greater than the pivot value. To do so, it starts at the leftmost index
	 * and the rightmost index, and moves each towards each other until they both land on a value that is on the wrong side of the array (i.e. a value smaller than 
	 * pivot is on right side and a value greater than pivot is on left side), and takes those values and swaps them. Once the left and right index values are the same, 
	 * they check it against the pivot value and if it is greater than or equal to the pivot, they swap that value with the pivot. Otherwise, they swap it with the value at 
	 * the index one above the current index. After that partition, it recursively calls 
	 * quicksort for each of the remaining halves of the array, until the startIndex is greater than or equal to the endIndex. 
	 * 
	 * @param <T> Ensures the generic nature of the code. The type of objects contained in ArrayList must extend comparable
	 * @param arrayList The ArrayList to be sorted
	 * @param startIndex The beginning index of the portion of the arrayList currently being sorted
	 * @param endIndex The end index of the portion of the arrayList currently being sorted
	 * @param pivotIndex The index of the pivot value
	 * @param cmp The comparator that compares two objects in an array to see if they must be swapped
	 */
	public static <T extends Comparable <? super T>> void quicksort(ArrayList<T> arrayList, int startIndex, int endIndex, int pivotIndex) {
		if (!(startIndex >= endIndex)) {
			T pivot = arrayList.get(pivotIndex);
			swap(arrayList, pivotIndex, endIndex);
			int rightIndex = endIndex - 1;
			int leftIndex = startIndex;
			while (leftIndex < rightIndex) {
				while(((arrayList.get(leftIndex)).compareTo(pivot) <= 0) && leftIndex < rightIndex) {
					leftIndex ++;
				}
				while(((arrayList.get(rightIndex).compareTo(pivot) >= 0) && rightIndex > leftIndex)) {
					rightIndex --;
				}
				if (leftIndex < rightIndex) {
					swap(arrayList, leftIndex, rightIndex);
				}
			}
			if ((arrayList.get(leftIndex)).compareTo(pivot) < 0) {
				swap(arrayList, leftIndex + 1, endIndex);
			}
			else {
				swap(arrayList, leftIndex, endIndex);
			}
			//int leftPivotIndex = randomSampleMedian(arrayList, startIndex, leftIndex); //commented out lines below allow for different pivot selection strategies
			int leftPivotIndex = randomSinglePivot(arrayList, startIndex, leftIndex);
			//int leftPivotIndex = fmePivot(arrayList, startIndex, leftIndex);
			quicksort(arrayList, startIndex, leftIndex, leftPivotIndex);
			//int rightPivotIndex = randomSampleMedian(arrayList, leftIndex + 1, endIndex + 1); //commented out lines below allow for different pivot selection strategies
			int rightPivotIndex = randomSinglePivot(arrayList, leftIndex + 1, endIndex);
			//int rightPivotIndex = fmePivot(arrayList, leftIndex + 1, endIndex);
			quicksort(arrayList, leftIndex + 1, endIndex, rightPivotIndex);
		}
	}
	
	
	/**
	* This helper method takes two objects in an ArrayList and swaps their positions. Used to aid in quicksort.
	*
	* @param arrayList - the ArrayList with the two objects to be switched
	* @param leftIndex - the index of the item on the left side of the list
	* @param rightIndex - the index of the item on the right side of the list
	**/
	private static <T extends Comparable<? super T>> void swap(ArrayList<T> arrayList, int leftIndex, int rightIndex) {
		T object1 = arrayList.get(leftIndex);
		T object2 = arrayList.get(rightIndex);
		arrayList.set(leftIndex, object2);
		arrayList.set(rightIndex, object1);
	}
	
	
	/**
	* This method creates an ArrayList of integers in accending order from 1 to the specified size
	*
	* @param size desired size of the generated array
	* @return ArrayList<Integer> with values 1 to size, increasing by one
	**/
	public static ArrayList<Integer> generateAscending(int size) {
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++) {
			intList.add(i);
		}
		return intList;
	}
	
	
	/**
	 * This method creates an ArrayList of integers from one to size in a random order. 
	 * Uses the Collections.shuffle() method to shuffle the list. 
	 * 
	 * @param size The size of the arrayList and the maximum value for the integers in the list
	 * @return The randomized ArrayList of integers from 1 to size
	 */
	public static ArrayList<Integer> generatePermuted(int size) {
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++) {
			intList.add(i);
		}
		Collections.shuffle(intList);
		return intList;
	}
	
	
	/**
	* This method creates an ArrayList of integers in decending order from the size specified to 1
	*
	* @param size The size of the arrayLIst and the maximum value for the integers in the list
	* @return The ArrayList in descending order from size to 1
	**/
	public static ArrayList<Integer> generateDescending(int size) {
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for (int i = size; i >= 1; i --) {
			intList.add(i);
		}
		return intList;
	}
	
	
	/**
	 * A Comparator class that handles multiple types of comparison by calling the specified 
	 * type's compareTo method. This works for this class because all types that are being used in this class
	 * already extend Comparable, so that means they have a compareTo method implemented. This just allows the creation 
	 * of one 'generic' comparator for use in this class to allow methods to remain generic in nature. (This comparator isn't 
	 * actually 'generic' but acts as if it is due to the specification that all types will already extend Comparable)
	 *
	 * @param <T> The type of the objects being compared
	 */
	protected static class TComparator<T extends Comparable<T>> implements Comparator<T> {

		public int compare(T object1, T object2) {
			return object1.compareTo(object2) > 0 ? 1 : object1.compareTo(object2) < 0 ? -1 : 0;
		}
	}
	
}
