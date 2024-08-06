package assign03;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assign03.ArrayCollection.IntegerComparator;
import assign03.ArrayCollection.StringComparator;

/**
 * Test cases for SearchUtil class
 * 
 * @author Kira Halls and Braden Campbell
 *
 */
class SearchUtilTest {
	private ArrayCollection<String> mediumStringAC;
	private ArrayCollection<Integer> mediumIntAC;
	
	@BeforeEach
	void setUp() throws Exception {
		mediumStringAC = new ArrayCollection<String>();
		ArrayList<String> itemsToAdd = new ArrayList<String>();
		String[] strings = {"hi", "no", "cookie", "tea", "water", "dog", "sun", "car", "eat", "run"};
		for (String item : strings) {
			itemsToAdd.add(item); 
		}
		mediumStringAC.addAll(itemsToAdd);
		
		mediumIntAC = new ArrayCollection<Integer>();
		ArrayList<Integer> intsToAdd = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 90, 2, 5, 36, 1, 11, 21};
		for (Integer item : ints) {
			intsToAdd.add(item);
		}
		mediumIntAC.addAll(intsToAdd);
	}
	

	/**
	 * Test a binary search where expected object is in right half of sorted list, but not 
	 * the first checked. Testing using Integers.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testIntBinarySearch() {
		IntegerComparator comparator = mediumIntAC.new IntegerComparator();
		ArrayList<Integer> sortedList = mediumIntAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, 43, comparator));
	}
	
	/**
	 * Test a binary search where expected object is in the left half of the sorted list, but not 
	 * the first checked. Testing that it reaches and checks the lowest index. Testing using Integers.
	 */
	@Test 
	void testIntBinarySearchLeft() {
		IntegerComparator comparator = mediumIntAC.new IntegerComparator();
		ArrayList<Integer> sortedList = mediumIntAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, 1, comparator));
	}
	
	/**
	 * Test a binary search where expected object is the last element of the sorted list. Testing that it reaches and 
	 * checks the highest index. Testing using Integers.
	 */
	@Test 
	void testIntBinarySearchLast() {
		IntegerComparator comparator = mediumIntAC.new IntegerComparator();
		ArrayList<Integer> sortedList = mediumIntAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, 90, comparator));
	}
	
	/**
	 * Test a binary search where expected object is the first object checked. Testing using Integers.
	 */
	@Test 
	void testIntBinarySearchFirst() {
		IntegerComparator comparator = mediumIntAC.new IntegerComparator();
		ArrayList<Integer> sortedList = mediumIntAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, 13, comparator));
	}
	
	/**
	 * Testing binary search where the object is not in the list and should return false. Testing 
	 * using Integers.
	 */
	@Test
	void testIntBinarySearchFalse() {
		IntegerComparator comparator = mediumIntAC.new IntegerComparator();
		ArrayList<Integer> sortedList = mediumIntAC.toSortedList(comparator);
		assertFalse(SearchUtil.binarySearch(sortedList, 1234, comparator));
	}
	
	/**
	 * Test a binary search where expected object is in right half of sorted list, but not 
	 * the first checked. Testing using Strings.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testStringBinarySearch() {
		StringComparator comparator = mediumStringAC.new StringComparator();
		ArrayList<String> sortedList = mediumStringAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, "tea", comparator));
	}
	
	/**
	 * Test a binary search where expected object is in the left half of the sorted list, but not 
	 * the first checked. Testing that it reaches and checks the lowest index. Testing using Strings.
	 */
	@Test 
	void testStringBinarySearchLeft() {
		StringComparator comparator = mediumStringAC.new StringComparator();
		ArrayList<String> sortedList = mediumStringAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, "car", comparator));
	}
	
	/**
	 * Test a binary search where expected object is the last element of the sorted list. Testing that it reaches and 
	 * checks the highest index. Testing using Strings.
	 */
	@Test 
	void testStringBinarySearchLast() {
		StringComparator comparator = mediumStringAC.new StringComparator();
		ArrayList<String> sortedList = mediumStringAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, "water", comparator));
	}
	
	/**
	 * Test a binary search where expected object is the first object checked. Testing using Strings.
	 */
	@Test 
	void testStringBinarySearchFirst() {
		StringComparator comparator = mediumStringAC.new StringComparator();
		ArrayList<String> sortedList = mediumStringAC.toSortedList(comparator);
		assertTrue(SearchUtil.binarySearch(sortedList, "no", comparator));
	}
	
	/**
	 * Testing binary search where the object is not in the list and should return false. Testing using Strings.
	 */
	@Test
	void testStringBinarySearchFalse() {
		StringComparator comparator = mediumStringAC.new StringComparator();
		ArrayList<String> sortedList = mediumStringAC.toSortedList(comparator);
		assertFalse(SearchUtil.binarySearch(sortedList, "examination", comparator));
	}

}
