package assign05;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import assign05.ArrayListSorter;

class ArrayListSorterTest {
	
	private ArrayList<String> emptyStringAL, singleStringAL, mediumStringAL, expectedMediumSortedStringAL, mediumReverseSortedStringAL, duplicateStringAL, expectedDuplicateSortedStringAL;
	private ArrayList<Integer> emptyIntAL, singleIntAL, mediumIntAL, expectedMediumSortedIntAL, mediumReverseSortedIntAL, duplicateIntAL, expectedDuplicateIntAL;
	
	@BeforeEach
	void setUp() throws Exception {
		emptyStringAL = new ArrayList<String>();
		singleStringAL = new ArrayList<String>();
		singleStringAL.add("hi");
		duplicateStringAL = new ArrayList<String>();
		mediumStringAL = new ArrayList<String>();
		expectedMediumSortedStringAL = new ArrayList<String>();
		mediumReverseSortedStringAL = new ArrayList<String>();
		String[] reverseSortedStrings = {"water", "tea", "sun", "run", "no", "hi", "eat", "dog", "cookie", "car"};
		for (String item: reverseSortedStrings) {
			mediumReverseSortedStringAL.add(item);
		}
		expectedDuplicateSortedStringAL = new ArrayList<String>();
		expectedDuplicateSortedStringAL.add("car");
		String[] sortedStrings = {"car", "cookie", "dog", "eat", "hi", "no", "run", "sun", "tea", "water"};
		for (String item : sortedStrings) {
			expectedMediumSortedStringAL.add(item);
			duplicateStringAL.add(item);
			expectedDuplicateSortedStringAL.add(item);
		}
		duplicateStringAL.add("car");
		
		String[] strings = {"hi", "no", "cookie", "tea", "water", "dog", "sun", "car", "eat", "run"};
		for (String item : strings) {
			mediumStringAL.add(item);
		}
		
		
		emptyIntAL = new ArrayList<Integer>();
		singleIntAL = new ArrayList<Integer>();
		singleIntAL.add(43);
		mediumIntAL = new ArrayList<Integer>();
		mediumReverseSortedIntAL = new ArrayList<Integer>();
		expectedMediumSortedIntAL = new ArrayList<Integer>();
		duplicateIntAL = new ArrayList<Integer>();
		expectedDuplicateIntAL = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 199, 453, 2, -54, 45};
		for (Integer item : ints) {
			mediumIntAL.add(item);
			duplicateIntAL.add(item);
		}
		duplicateIntAL.add(453);
		Integer[] sortedInts = {-54, 2, 13, 43, 45, 199, 453};
		for (Integer item: sortedInts) {
			expectedMediumSortedIntAL.add(item);
			expectedDuplicateIntAL.add(item);
		}
		expectedDuplicateIntAL.add(453);
		
		Integer[] reverse = {453, 199, 45, 43, 13, 2, -54};
		for (Integer item : reverse) {
			mediumReverseSortedIntAL.add(item);
		}
	}
	
	
	/** Begin Mergesort Tests **/
	
	/**
	* This test is making sure that an empty list of strings does not  throw an error
	**/
	@Test
	void testMergeSortEmptyStringList() {
		ArrayListSorter.mergesort(emptyStringAL);
		assertEquals("[]", emptyStringAL.toString());
	}
	
	/**
	* This test is checking to make sure that an already sorted String list stays sorted
	**/
	@Test
	void testMergeSortSortedIntList() {
		String sortedList = expectedMediumSortedStringAL.toString();
		ArrayListSorter.mergesort(expectedMediumSortedStringAL);
		assertEquals(sortedList, expectedMediumSortedStringAL.toString());
	}
	
	/**
	* This test is making sure that a string list with only a single entry does not throw an error
	**/
	void testMergeSortSingleStringList() {
		ArrayListSorter.mergesort(singleStringAL);
		assertEquals("[hi]", singleStringAL.toString());
	}
	
	/**
	* This test is making sure that an unorganized string list to an organized string list
	**/
	@Test
	void testMergeSortMediumStringList() {
		ArrayListSorter.mergesort(mediumStringAL);
		assertEquals(expectedMediumSortedStringAL.toString(), mediumStringAL.toString());
	}
	/**
	* This test is making sure that string lists with duplicate values are sorted correctly
	**/
	@Test
	void testMergeSortDuplicateStringList() {
		ArrayListSorter.mergesort(duplicateStringAL);
		assertEquals(expectedDuplicateSortedStringAL.toString(), duplicateStringAL.toString());
	}
	
	/**
	* This test is to make sure a list of strings in reverse sorted order can be sorted
	**/
	@Test
	void testMergeSortReverseSortedStrings() {
		ArrayListSorter.mergesort(mediumReverseSortedStringAL);
		assertEquals(mediumReverseSortedStringAL.toString(), mediumReverseSortedStringAL.toString());
	}

	/**
	* This test is making sure that an empty list of ints does not throw an error
	**/
	@Test
	void testMergeSortEmptyIntList() {
		ArrayListSorter.mergesort(emptyIntAL);
		assertEquals("[]", emptyIntAL.toString());
	}
	
	/**
	* This test is checking to make sure that an already sorted int list stays sorted
	**/
	@Test
	void testMergeSortSortedStringList() {
		String sortedList = expectedMediumSortedIntAL.toString();
		ArrayListSorter.mergesort(expectedMediumSortedIntAL);
		assertEquals(sortedList, expectedMediumSortedIntAL.toString());
	}
	
	/**
	* This test is making sure that an int list with a single entry does not throw an error
	**/
	@Test 
	void testMergeSortSingleIntList() {
		ArrayListSorter.mergesort(singleIntAL);
		assertEquals("[43]", singleIntAL.toString());
	}
	
	/**
	* This test is making sure that an unorganized int list to an organized string list
	**/
	@Test
	void testMergeSortMediumIntList() {
		ArrayListSorter.mergesort(mediumIntAL);
		assertEquals(expectedMediumSortedIntAL.toString(), mediumIntAL.toString());
	}
	
	/**
	* This test is making sure that int lists with duplicate values are sorted correctly
	**/
	@Test
	void testMergeSortDuplicateIntList() {
		ArrayListSorter.mergesort(duplicateIntAL);
		assertEquals(expectedDuplicateIntAL.toString(), duplicateIntAL.toString());
	}
	
	/**
	* This test is to make sure a list of ints in reverse sorted order can be sorted
	**/
	@Test
	void testMergeSortReverseSortedInts() {
		ArrayListSorter.mergesort(mediumReverseSortedIntAL);
		assertEquals(expectedMediumSortedIntAL.toString(), mediumReverseSortedIntAL.toString());
	}
	
	
	/** Begin QuickSort Tests **/
	
	/**
	 * Testing edge case of an empty String list with quicksort. Should return empty list.
	 */
	@Test 
	void testQuickSortEmptyStringList() {
		ArrayListSorter.quicksort(emptyStringAL);
		assertEquals("[]", emptyStringAL.toString());
	}
	
	/**
	 * Testing quicksort with a single string list.
	 */
	@Test
	void testQuickSortSingleStringList() {
		ArrayListSorter.quicksort(singleStringAL);
		assertEquals("[hi]", singleStringAL.toString());
	}
	
	/**
	 * Test quicksort on a medium sized string list
	 */
	@Test 
	void testQuickSortMediumStringList() {
		ArrayListSorter.quicksort(mediumStringAL);
		assertEquals(expectedMediumSortedStringAL.toString(), mediumStringAL.toString());
	}
	
	/**
	 * Testing that quicksort handles a duplicate value correctly
	 */
	@Test
	void testQuickSortDuplicateString() {
		ArrayListSorter.quicksort(duplicateStringAL);
		assertEquals("[car, car, cookie, dog, eat, hi, no, run, sun, tea, water]", duplicateStringAL.toString());
	}
	
	/**
	 * Testing quicksort on an already sorted array. Should not change sort of array.
	 */
	@Test
	void testQuickSortAlreadySortedString() {
		String originalExpectedString = expectedMediumSortedStringAL.toString();
		ArrayListSorter.quicksort(expectedMediumSortedStringAL);
		assertEquals(originalExpectedString, expectedMediumSortedStringAL.toString());
	}
	
	/**
	 * Testing quicksort on reverse sorted array. Should reverse the order of the array.
	 */
	@Test
	void testQuickSortReverseSortedString() {
		ArrayListSorter.quicksort(mediumReverseSortedStringAL);
		assertEquals(expectedMediumSortedStringAL.toString(), mediumReverseSortedStringAL.toString());
	}
	
	/**
	 * Testing that quicksort correctly handles an empty int list. Should return an empty list.
	 */
	@Test 
	void testQuickSortEmptyIntList() {
		ArrayListSorter.quicksort(emptyIntAL);
		assertEquals("[]", emptyIntAL.toString());
	}
	
	/**
	 * Testing quicksort with a single integer list.
	 */
	@Test
	void testQuickSortSingleIntList() {
		ArrayListSorter.quicksort(singleIntAL);
		assertEquals("[43]", singleIntAL.toString());
	}
	
	/**
	 * Testing quicksort on a medium sized integer list.
	 */
	@Test 
	void testQuickSortMediumIntList() {
		ArrayListSorter.quicksort(mediumIntAL);
		assertEquals(expectedMediumSortedIntAL.toString(), mediumIntAL.toString());
	}
	
	/**
	 * Testing that quicksort handles a duplicate integer correctly
	 */
	@Test
	void testQuickSortDuplicateInt() {
		ArrayListSorter.quicksort(duplicateIntAL);
		assertEquals(expectedDuplicateIntAL.toString(), duplicateIntAL.toString());
	}
	
	/**
	 * Testing quicksort on an already sorted array. Should not change sort of array.
	 */
	@Test
	void testQuickSortAlreadySortedInt() {
		String originalExpectedInt = expectedMediumSortedIntAL.toString();
		ArrayListSorter.quicksort(expectedMediumSortedIntAL);
		assertEquals(originalExpectedInt, expectedMediumSortedIntAL.toString());
	}
	
	/**
	 * Testing quicksort on reverse sorted array. Should reverse the order of the array.
	 */
	@Test
	void testQuickSortReverseSortedInt() {
		ArrayListSorter.quicksort(mediumReverseSortedIntAL);
		assertEquals(expectedMediumSortedIntAL.toString(), mediumReverseSortedIntAL.toString());
	}
}
