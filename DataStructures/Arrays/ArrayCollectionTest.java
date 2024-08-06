package assign03;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assign03.ArrayCollection.IntegerComparator;
import assign03.ArrayCollection.StringComparator;

/**
 * Test cases for ArrayCollection class
 * @author Kira Halls and Braden Campbell
 *
 */
class ArrayCollectionTest {
	
	private ArrayCollection<String> emptyStringAC, mediumStringAC;
	private ArrayCollection<Integer> emptyIntAC, mediumIntAC;
	
	@BeforeEach
	void setUp() throws Exception {
		emptyStringAC = new ArrayCollection<String>();
		mediumStringAC = new ArrayCollection<String>();
		ArrayList<String> itemsToAdd = new ArrayList<String>();
		String[] strings = {"hi", "no", "cookie", "tea", "water", "dog", "sun", "car", "eat", "run"};
		for (String item : strings) {
			itemsToAdd.add(item); 
		}
		mediumStringAC.addAll(itemsToAdd);
		
		emptyIntAC = new ArrayCollection<Integer>();
		mediumIntAC = new ArrayCollection<Integer>();
		ArrayList<Integer> intsToAdd = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 90, 2, 5, 36, 1, 11, 21};
		for (Integer item : ints) {
			intsToAdd.add(item);
		}
		mediumIntAC.addAll(intsToAdd);
	}
	
	
	/** Begin String Tests **/
	
	/**
	 * Testing the grow function to make sure it does not grow when ten items are added
	 * and makes a full list
	 */
	@Test
	void testFullCollectionGrow() {
		assertEquals(10, mediumStringAC.getSize());
		assertEquals(10, mediumStringAC.getDataLength());
	}
	
	/**
	 * Testing grow function to ensure it grows when necessary and doubles the length of data[]
	 */
	@Test 
	void testOverflowCollectionGrow() {
		mediumStringAC.add("billy");
		assertEquals(11, mediumStringAC.getSize());
		assertEquals(20, mediumStringAC.getDataLength());
	}
	
	/**
	 * Test to make sure it returns true when it adds a value and returns 
	 * false if it didn't add it
	 */
	@Test
	void addSingleStringBoolean() {
		assertTrue(emptyStringAC.add("Hi")); //add a value that isn't already contained in the collection, will return True
		assertFalse(emptyStringAC.add("Hi")); //add that same value again, it will return false because it is a duplicate
	}
	
	/**
	 * Checks the actual result of the add method to ensure 
	 * the proper word was added to the ArrayCollection
	 */
	@Test 
	void addSingleStringResult() {
		emptyStringAC.add("Hi");
		String expectedArray = "{Hi, null, null, null, null, null, null, null, null, null}";
		assertEquals(expectedArray, emptyStringAC.collectionToString());
	}
	
	/**
	 * Testing to make sure the addAll method returns true if it at least one value
	 * or returns false if it doesn't add any of the values
	 */
	@Test
	void addAllMediumEmptyStringBoolean() {
		ArrayList<String> localItemsToAdd = new ArrayList<String>();
		String[] strings = {"hi", "no", "cookie", "tea", "water", "dog", "sun", "car", "eat", "run"};
		for (String item : strings) {
			localItemsToAdd.add(item);
		}
		assertTrue(emptyStringAC.addAll(localItemsToAdd)); //add at least one value from a set of values that isn't already contained in the collection, will return True
		assertFalse(emptyStringAC.addAll(localItemsToAdd)); //add that same set of values again, it will return false because they are all a duplicate
	}
	
	/**
	 *Checks the result of addAll to ensure it added the necessary values
	 */
	@Test
	void addAllMediumEmptyStringResultAllNew() {
		ArrayList<String> localItemsToAdd = new ArrayList<String>();
		String[] strings = {"hi", "no", "cookie", "tea", "water", "dog", "sun", "car", "eat", "run"};
		for (String item : strings) {
			localItemsToAdd.add(item);
		}
		emptyStringAC.addAll(localItemsToAdd);
		String expectedArray = "{hi, no, cookie, tea, water, dog, sun, car, eat, run}";
		assertEquals(expectedArray, emptyStringAC.collectionToString()); //add at least one value from a set of values that isn't already contained in the collection, will return True
	}
	
	/**
	 * Test to make sure addAll doesn't add any duplicate values already 
	 * contained in ArrayCollection, returns false if not added, returns true 
	 * if at least one value was added. This test should return true.
	 */
	@Test 
	void addAllMediumStringSomeDuplicatesTrue() {
		ArrayList<String> itemsToAdd2 = new ArrayList<String>();
		String[] strings2 = {"salamander", "cookie", "test", "dog"};
		for (String item : strings2) {
			itemsToAdd2.add(item);
		}
		assertTrue(mediumStringAC.addAll(itemsToAdd2));
	}
	
	/**
	 * Test to make sure addAll doesn't add any duplicate values already 
	 * contained in ArrayCollection, returns false if not added, returns true 
	 * if at least one value was added. This test should return false.
	 */
	@Test 
	void addAllMediumStringSomeDuplicatesFalse() {
		ArrayList<String> itemsToAdd2 = new ArrayList<String>();
		String[] strings2 = {"cookie", "dog"};
		for (String item : strings2) {
			itemsToAdd2.add(item);
		}
		assertFalse(mediumStringAC.addAll(itemsToAdd2));
	}
	
	/**
	 * Test to make sure addAll doesn't add any duplicate values already 
	 * contained in ArrayCollection
	 */
	@Test 
	void addAllMediumStringSomeDuplicates() {
		ArrayList<String> itemsToAdd2 = new ArrayList<String>();
		String[] strings2 = {"salamander", "cookie", "test", "dog"};
		for (String item : strings2) {
			itemsToAdd2.add(item);
		}
		mediumStringAC.addAll(itemsToAdd2);
		String expectedArray = "{hi, no, cookie, tea, water, dog, sun, car, eat, run, salamander, test, null, null, null, null, null, null, null, null}";
		assertEquals(expectedArray, mediumStringAC.collectionToString());
	}
	
	/**
	 * Test the clear method on an ArrayCollection that hasn't called the grow method
	 * to make sure it resets size to 0 and the data array length stays the same. 
	 * Also compares the result to an expectedArray variable to make sure the data array
	 * contains only null values
	 */
	@Test
	void testClearNoGrowth() {
		mediumStringAC.clear();
		String expectedArray = "{null, null, null, null, null, null, null, null, null, null}";
		assertEquals(0, mediumStringAC.getSize());
		assertEquals(10, mediumStringAC.getDataLength());
		assertEquals(expectedArray, mediumStringAC.collectionToString());
		
	}
	
	/**
	 * Test the clear method on an ArrayCollection that hasn't called the grow method
	 * to make sure it resets size to 0 and the data array length stays the same
	 */
	@Test
	void testClearSomeGrowth() {
		ArrayList<String> itemsToAdd = new ArrayList<String>();
		String[] strings = {"salamander", "teeth", "test"};
		for (String item : strings) {
			itemsToAdd.add(item);
		}
		mediumStringAC.addAll(itemsToAdd);
		mediumStringAC.clear();
		assertEquals(0, mediumStringAC.getSize());
		assertEquals(20, mediumStringAC.getDataLength());
	}
	
	/**
	 * Testing contains method. Returns true if collection contains specified item
	 * returns false if collection does not contain specified item
	 */
	@Test
	void testContains() {
		assertTrue(mediumStringAC.contains("no"));
		assertFalse(mediumStringAC.contains("yes"));
	}
	
	/**
	 * Testing contains all method. Returns true if collection contains all items specified
	 */
	@Test
	void testContainsAll() {
		ArrayList<String> itemsToCheck = new ArrayList<String>();
		String[] strings = {"hi", "no", "run"};
		for (String item : strings) {
			itemsToCheck.add(item);
		}
		assertTrue(mediumStringAC.containsAll(itemsToCheck));
	}
	
	/**
	 * Testing contains all method. Returns false if not all values are in collection
	 */
	@Test
	void testContainsAllSomeContained() {
		ArrayList<String> itemsToCheck = new ArrayList<String>();
		String[] strings = {"hi", "no", "run", "fall", "jump"};
		for (String item : strings) {
			itemsToCheck.add(item);
		}
		assertFalse(mediumStringAC.containsAll(itemsToCheck));
	}
	
	/**
	 * Testing contains all method. Returns false if no items are in collection
	 */
	@Test
	void testContainsAllNoneContained() {
		ArrayList<String> itemsToCheck = new ArrayList<String>();
		String[] strings = {"fall", "sport", "sunshine"};
		for (String item : strings) {
			itemsToCheck.add(item);
		}
		assertFalse(mediumStringAC.containsAll(itemsToCheck));
	}
	
	/**
	 * Testing isEmpty method. Returns true if size is 0. 
	 * Returns false otherwise
	 */
	@Test
	void testIsEmpty() {
		assertTrue(emptyStringAC.isEmpty());
		assertFalse(mediumStringAC.isEmpty());
	}
	
	/**
	 * Test to make sure the remove() function returns true when it has removed a value,
	 * false if it can't remove the value. This test specifically tries removing "car" twice 
	 * to make sure that "car" was actually removed when it says it did, and didn't just return 
	 * true without removing it.
	 */
	@Test 
	void testRemoveMediumStringBoolean() {
		assertTrue(mediumStringAC.remove("car"));
		assertFalse(mediumStringAC.remove("car"));
		assertFalse(mediumStringAC.remove("sunshine"));
	}
	
	/**
	 * Test the remove() method by comparing it to what the new array should look like
	 * as well as confirming that the size and data variables were adjusted accordingly. 
	 * Size decreases by one and data's length does not change.
	 */
	@Test
	void testRemoveMediumStringResult() {
		mediumStringAC.remove("dog");
		String expectedResult = "{hi, no, cookie, tea, water, sun, car, eat, run, null}";
		assertEquals(expectedResult, mediumStringAC.collectionToString());
		assertEquals(9, mediumStringAC.getSize());
		assertEquals(10, mediumStringAC.getDataLength());
	}
	
	/**
	 * Test removing an item from an ArrayCollection that has called the grow() method.
	 * This test makes sure the length of data doesn't change, only the size variable changes.
	 */
	@Test
	void testRemoveMediumStringResultHasGrown() {
		ArrayList<String> itemsToRemove = new ArrayList<String>();
		String[] strings = {"salamander", "teeth", "test"};
		for (String item : strings) {
			itemsToRemove.add(item);
		}
		mediumStringAC.addAll(itemsToRemove);
		mediumStringAC.remove("dog");
		String expectedResult = "{hi, no, cookie, tea, water, sun, car, eat, run, salamander, teeth, test, null, null, "
				+ "null, null, null, null, null, null}";
		assertEquals(expectedResult, mediumStringAC.collectionToString());
		assertEquals(12, mediumStringAC.getSize());
		assertEquals(20, mediumStringAC.getDataLength());
	}
	
	/**
	 * Test the removeAll() function by removing every item in the Collection
	 * Test to make sure it returns true because at least one value was removed, 
	 * that it adjusts size to be 0, and that the resulting array is correct
	 */
	@Test
	void removeAllMediumStringAllRemoved() {
		ArrayList<String> localItemsToRemove = new ArrayList<String>();
		String[] strings = {"hi", "no", "cookie", "tea", "water", "dog", "sun", "car", "eat", "run"};
		for (String item : strings) {
			localItemsToRemove.add(item);
		}
		String expectedArray = "{null, null, null, null, null, null, null, null, null, null}";
		assertTrue(mediumStringAC.removeAll(localItemsToRemove));
		assertEquals(expectedArray, mediumStringAC.collectionToString());
		assertEquals(0, mediumStringAC.getSize());
	}
	
	/**
	 * Test the removeAll() function that only removes some, the other values to be removed
	 * aren't in that collection. Returns true as at least one value was removed, adjusts the size accordingly, 
	 * and compares that the actual result is as expected
	 */
	@Test
	void removeAllMediumStringSomeRemoved() {
		ArrayList<String> localItemsToRemove = new ArrayList<String>();
		String[] strings = {"hi", "tour", "why", "tea", "water", "dog", "example"};
		for (String item : strings) {
			localItemsToRemove.add(item);
		}
		String expectedArray = "{no, cookie, sun, car, eat, run, null, null, null, null}";
		assertTrue(mediumStringAC.removeAll(localItemsToRemove));
		assertEquals(expectedArray, mediumStringAC.collectionToString());
		assertEquals(6, mediumStringAC.getSize());
	}
	
	/**
	 * Tests the removeAll() function that does not remove any values. 
	 * Returns false as nothing was removed, and the size and array are not changed.
	 */
	@Test
	void removeAllMediumStringNoneRemoved() {
		ArrayList<String> localItemsToRemove = new ArrayList<String>();
		String[] strings = {"yes", "tour", "why", "sad", "string", "cow", "example"};
		for (String item : strings) {
			localItemsToRemove.add(item);
		}
		String expectedArray = "{hi, no, cookie, tea, water, dog, sun, car, eat, run}";
		assertFalse(mediumStringAC.removeAll(localItemsToRemove));
		assertEquals(expectedArray, mediumStringAC.collectionToString());
		assertEquals(10, mediumStringAC.getSize());
	}
	
	/**
	 * Testing the retainAll() function. Test that retainAll() returns true because some values
	 * were removed, test that the size and data array were affected accordingly.
	 */
	@Test 
	void testRetainAll() {
		ArrayList<String> localItemsToRetain = new ArrayList<String>();
		String[] strings = {"hi", "tour", "why", "tea", "water", "dog", "example"};
		for (String item : strings) {
			localItemsToRetain.add(item);
		}
		String expectedArray = "{hi, tea, water, dog, null, null, null, null, null, null}";
		assertTrue(mediumStringAC.retainAll(localItemsToRetain));
		assertEquals(4, mediumStringAC.getSize());
		assertEquals(expectedArray, mediumStringAC.collectionToString());
	}
	
	/**
	 * Testing retainAll() function. Test that this returns True because all items were removed.
	 * Test that the size and data array were affected accordingly.
	 */
	@Test 
	void testRetainAllRetainsNone() {
		ArrayList<String> localItemsToRetain = new ArrayList<String>();
		String[] strings = {"cow", "tour", "why", "sure", "light", "sour", "example"};
		for (String item : strings) {
			localItemsToRetain.add(item);
		}
		String expectedArray = "{null, null, null, null, null, null, null, null, null, null}";
		assertTrue(mediumStringAC.retainAll(localItemsToRetain));
		assertEquals(0, mediumStringAC.getSize());
		assertEquals(expectedArray, mediumStringAC.collectionToString());
	}
	
	/**
	 * Testing retainAll() function. Test that this returns false because no items were removed.
	 * Test that the size and data array were affected accordingly.
	 */
	@Test 
	void testRetainAllRetainsAll() {
		String expectedArray = "{hi, no, cookie, tea, water, dog, sun, car, eat, run}";
		assertFalse(mediumStringAC.retainAll(mediumStringAC));
		assertEquals(10, mediumStringAC.getSize());
		assertEquals(expectedArray, mediumStringAC.collectionToString());
	}
	
	/**
	 * Testing the toArray() function. Should return an array of data without any extra 
	 * storage cells. An array representation of the data actually held in data[]
	 */
	@Test
	void testToArray() {
		mediumStringAC.add("salamander");
		mediumStringAC.add("teeth");
		String expectedArray = "hi no cookie tea water dog sun car eat run salamander teeth ";
		assertEquals(expectedArray, mediumStringAC.toString(mediumStringAC.toArray()));
	}
	
	/**
	 * Testing toSortedList method. Puts the array collection in alphabetical order (A - Z) and includes null values at the end if applicable
	 */
	@Test
	void testToSortedList() {
		mediumStringAC.add("salamander");
		mediumStringAC.add("teeth");
		String sortedArray = "[car, cookie, dog, eat, hi, no, run, salamander, sun, tea, teeth, water]";
		StringComparator comparator = mediumStringAC.new StringComparator();
		ArrayList<String> sortedList = mediumStringAC.toSortedList(comparator);
		assertEquals(sortedArray, sortedList.toString());
	}
	
	
	/** begin Integer Tests to check for Generic applicability of ArrayCollection class**/
	
	/**
	 * Testing the grow function to make sure it does not grow when ten items are added
	 * and makes a full list
	 */
	@Test
	void testIntFullCollectionGrow() {
		assertEquals(10, mediumIntAC.getSize());
		assertEquals(10, mediumIntAC.getDataLength());
	}
	
	/**
	 * Testing grow function to ensure it grows when necessary and doubles the length of data[]
	 */
	@Test 
	void testIntOverflowCollectionGrow() {
		mediumIntAC.add(56);
		assertEquals(11, mediumIntAC.getSize());
		assertEquals(20, mediumIntAC.getDataLength());
	}
	
	/**
	 * Test to make sure it returns true when it adds a value and returns false if it didn't add it
	 */
	@Test
	void addIntSingleStringBoolean() {
		assertTrue(emptyIntAC.add(999)); //add a value that isn't already contained in the collection, will return true
		assertFalse(emptyIntAC.add(999)); //add that same value again, it will return false because it is a duplicate
	}
	
	/**
	 * Checks the actual result of the add method to ensure 
	 * the proper word was added to the ArrayCollection
	 */
	@Test 
	void addIntSingleStringResult() {
		emptyIntAC.add(999);
		String expectedArray = "{999, null, null, null, null, null, null, null, null, null}";
		assertEquals(expectedArray, emptyIntAC.collectionToString());
	}
	
	/**
	 * Testing to make sure the addAll method returns true if it at least one value
	 * or returns false if it doesn't add any of the values
	 */
	@Test
	void addIntAllMediumEmptyStringBoolean() {
		ArrayList<Integer> localItemsToAdd = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 90, 2, 5, 36, 1, 11, 21};
		for (Integer item : ints) {
			localItemsToAdd.add(item);
		}
		assertTrue(emptyIntAC.addAll(localItemsToAdd)); //add at least one value from a set of values that isn't already contained in the collection, will return true
		assertFalse(emptyIntAC.addAll(localItemsToAdd)); //add that same set of values again, it will return false because they are all a duplicate
	}
	
	/**
	 *Checks the result of addAll to ensure it added the necessary values
	 */
	@Test
	void addIntAllMediumEmptyStringResultAllNew() {
		ArrayList<Integer> localItemsToAdd = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 90, 2, 5, 36, 1, 11, 21};
		for (Integer item : ints) {
			localItemsToAdd.add(item);
		}
		emptyIntAC.addAll(localItemsToAdd);
		String expectedArray = "{13, 43, 6, 90, 2, 5, 36, 1, 11, 21}";
		assertEquals(expectedArray, emptyIntAC.collectionToString()); //add at least one value from a set of values that isn't already contained in the collection, will return True
	}
	
	/**
	 * Test to make sure addAll doesn't add any duplicate values already 
	 * contained in ArrayCollection, returns false if not added, returns true 
	 * if at least one value was added. This test should return true.
	 */
	@Test 
	void addIntAllMediumStringSomeDuplicatesTrue() {
		ArrayList<Integer> itemsToAdd2 = new ArrayList<Integer>();
		Integer[] int2 = {63, 597, 61, 94};
		for (Integer item : int2) {
			itemsToAdd2.add(item);
		}
		assertTrue(mediumIntAC.addAll(itemsToAdd2));
	}
	
	/**
	 * Test to make sure addAll doesn't add any duplicate values already 
	 * contained in ArrayCollection, returns false if not added, returns true 
	 * if at least one value was added. This test should return false.
	 */
	@Test 
	void addIntAllMediumStringSomeDuplicatesFalse() {
		ArrayList<Integer> itemsToAdd2 = new ArrayList<Integer>();
		Integer[] int2 = {13, 5};
		for (Integer item : int2) {
			itemsToAdd2.add(item);
		}
		assertFalse(mediumIntAC.addAll(itemsToAdd2));
	}
	
	/**
	 * Test to make sure addAll doesn't add any duplicate values already 
	 * contained in ArrayCollection
	 */
	@Test 
	void addIntAllMediumStringSomeDuplicates() {
		ArrayList<Integer> itemsToAdd2 = new ArrayList<Integer>();
		Integer[] int2 = {13, 5, 91, 3};
		for (Integer item : int2) {
			itemsToAdd2.add(item);
		}
		mediumIntAC.addAll(itemsToAdd2);
		String expectedArray = "{13, 43, 6, 90, 2, 5, 36, 1, 11, 21, 91, 3, null, null, null, null, null, null, null, null}";
		assertEquals(expectedArray, mediumIntAC.collectionToString());
	}
	
	/**
	 * Test the clear method on an ArrayCollection that hasn't called the grow method
	 * to make sure it resets size to 0 and the data array length stays the same. 
	 * Also compares the result to an expectedArray variable to make sure the data array
	 * contains only null values
	 */
	@Test
	void testIntClearNoGrowth() {
		mediumIntAC.clear();
		String expectedArray = "{null, null, null, null, null, null, null, null, null, null}";
		assertEquals(0, mediumIntAC.getSize());
		assertEquals(10, mediumIntAC.getDataLength());
		assertEquals(expectedArray, mediumIntAC.collectionToString());
		
	}
	
	/**
	 * Test the clear method on an ArrayCollection that hasn't called the grow method
	 * to make sure it resets size to 0 and the data array length stays the same
	 */
	@Test
	void testIntClearSomeGrowth() {
		ArrayList<Integer> itemsToAdd = new ArrayList<Integer>();
		Integer[] ints = {91, 5, 43};
		for (Integer item : ints) {
			itemsToAdd.add(item);
		}
		mediumIntAC.addAll(itemsToAdd);
		mediumIntAC.clear();
		assertEquals(0, mediumIntAC.getSize());
		assertEquals(20, mediumIntAC.getDataLength());
	}
	
	/**
	 * Testing contains method. Returns true if collection contains specified item
	 * returns false if collection does not contain specified item
	 */
	@Test
	void testIntContains() {
		assertTrue(mediumIntAC.contains(43));
		assertFalse(mediumIntAC.contains(1000));
	}
	
	/**
	 * Testing contains all method. Returns true if collection contains all items specified
	 */
	@Test
	void testIntContainsAll() {
		ArrayList<Integer> itemsToCheck = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 5};
		for (Integer item : ints) {
			itemsToCheck.add(item);
		}
		assertTrue(mediumIntAC.containsAll(itemsToCheck));
	}
	
	/**
	 * Testing contains all method. Returns false if not all values are in collection
	 */
	@Test
	void testIntContainsAllSomeContained() {
		ArrayList<Integer> itemsToCheck = new ArrayList<Integer>();
		Integer[] ints = {13, 5, 43, 1234, 9789};
		for (Integer item : ints) {
			itemsToCheck.add(item);
		}
		assertFalse(mediumIntAC.containsAll(itemsToCheck));
	}
	
	/**
	 * Testing contains all method. Returns false if no items are in collection
	 */
	@Test
	void testIntContainsAllNoneContained() {
		ArrayList<Integer> itemsToCheck = new ArrayList<Integer>();
		Integer[] ints = {100000, 200000, 31568};
		for (Integer item : ints) {
			itemsToCheck.add(item);
		}
		assertFalse(mediumIntAC.containsAll(itemsToCheck));
	}
	
	/**
	 * Testing isEmpty method. Returns true if size is 0. 
	 * Returns false otherwise
	 */
	@Test
	void testIntIsEmpty() {
		assertTrue(emptyIntAC.isEmpty());
		assertFalse(mediumIntAC.isEmpty());
	}
	
	/**
	 * Test to make sure the remove() function returns true when it has removed a value,
	 * false if it can't remove the value. This test specifically tries removing 43 twice 
	 * to make sure that 43 was actually removed when it says it did, and didn't just return 
	 * true without removing it.
	 */
	@Test 
	void testIntRemoveMediumStringBoolean() {
		assertTrue(mediumIntAC.remove(43));
		assertFalse(mediumIntAC.remove(43));
		assertFalse(mediumIntAC.remove(789456));
	}
	
	/**
	 * Test the remove() method by comparing it to what the new array should look like
	 * as well as confirming that the size and data variables were adjusted accordingly. 
	 * Size decreases by one and data's length does not change.
	 */
	@Test
	void testIntRemoveMediumStringResult() {
		mediumIntAC.remove(43);
		String expectedResult = "{13, 6, 90, 2, 5, 36, 1, 11, 21, null}";
		assertEquals(expectedResult, mediumIntAC.collectionToString());
		assertEquals(9, mediumIntAC.getSize());
		assertEquals(10, mediumIntAC.getDataLength());
	}
	
	/**
	 * Test removing an item from an ArrayCollection that has called the grow() method.
	 * This test makes sure the length of data doesn't change, only the size variable changes.
	 */
	@Test
	void testIntRemoveMediumStringResultHasGrown() {
		ArrayList<Integer> itemsToRemove = new ArrayList<Integer>();
		Integer[] ints = {999, 111, 222};
		for (Integer item : ints) {
			itemsToRemove.add(item);
		}
		mediumIntAC.addAll(itemsToRemove);
		mediumIntAC.remove(43);
		String expectedResult = "{13, 6, 90, 2, 5, 36, 1, 11, 21, 999, 111, 222, null, null, "
				+ "null, null, null, null, null, null}";
		assertEquals(expectedResult, mediumIntAC.collectionToString());
		assertEquals(12, mediumIntAC.getSize());
		assertEquals(20, mediumIntAC.getDataLength());
	}
	
	/**
	 * Test the removeAll() function by removing every item in the Collection
	 * Test to make sure it returns true because at least one value was removed, 
	 * that it adjusts size to be 0, and that the resulting array is correct
	 */
	@Test
	void removeIntAllMediumStringAllRemoved() {
		ArrayList<Integer> localItemsToRemove = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 90, 2, 5, 36, 1, 11, 21};
		for (Integer item : ints) {
			localItemsToRemove.add(item);
		}
		String expectedArray = "{null, null, null, null, null, null, null, null, null, null}";
		assertTrue(mediumIntAC.removeAll(localItemsToRemove));
		assertEquals(expectedArray, mediumIntAC.collectionToString());
		assertEquals(0, mediumIntAC.getSize());
	}
	
	/**
	 * Test the removeAll() function that only removes some, the other values to be removed
	 * aren't in that collection. Returns true as at least one value was removed, adjusts the size accordingly, 
	 * and compares that the actual result is as expected
	 */
	@Test
	void removeIntAllMediumStringSomeRemoved() {
		ArrayList<Integer> localItemsToRemove = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 90, 123456, 789, 987, 29};
		for (Integer item : ints) {
			localItemsToRemove.add(item);
		}
		String expectedArray = "{2, 5, 36, 1, 11, 21, null, null, null, null}";
		assertTrue(mediumIntAC.removeAll(localItemsToRemove));
		assertEquals(expectedArray, mediumIntAC.collectionToString());
		assertEquals(6, mediumIntAC.getSize());
	}
	
	/**
	 * Tests the removeAll() function that does not remove any values. 
	 * Returns false as nothing was removed, and the size and array are not changed.
	 */
	@Test
	void removeIntAllMediumStringNoneRemoved() {
		ArrayList<Integer> localItemsToRemove = new ArrayList<Integer>();
		Integer[] ints = {456, 555, 278, 642, -1};
		for (Integer item : ints) {
			localItemsToRemove.add(item);
		}
		String expectedArray = "{13, 43, 6, 90, 2, 5, 36, 1, 11, 21}";
		assertFalse(mediumIntAC.removeAll(localItemsToRemove));
		assertEquals(expectedArray, mediumIntAC.collectionToString());
		assertEquals(10, mediumIntAC.getSize());
	}
	
	/**
	 * Testing the retainAll() function. Test that retainAll() returns true because some values
	 * were removed, test that the size and data array were affected accordingly.
	 */
	@Test 
	void testIntRetainAll() {
		ArrayList<Integer> localItemsToRetain = new ArrayList<Integer>();
		Integer[] ints = {13, 43, 6, 0, 58, 11, 29};
		for (Integer item : ints) {
			localItemsToRetain.add(item);
		}
		String expectedArray = "{13, 43, 6, 11, null, null, null, null, null, null}";
		assertTrue(mediumIntAC.retainAll(localItemsToRetain));
		assertEquals(4, mediumIntAC.getSize());
		assertEquals(expectedArray, mediumIntAC.collectionToString());
	}
	
	/**
	 * Testing retainAll() function. Test that this returns True because all items were removed.
	 * Test that the size and data array were affected accordingly.
	 */
	@Test 
	void testIntRetainAllRetainsNone() {
		ArrayList<Integer> localItemsToRetain = new ArrayList<Integer>();
		Integer[] ints = {111, 2987, 555555, 0, 456, 99999};
		for (Integer item : ints) {
			localItemsToRetain.add(item);
		}
		String expectedArray = "{null, null, null, null, null, null, null, null, null, null}";
		assertTrue(mediumIntAC.retainAll(localItemsToRetain));
		assertEquals(0, mediumIntAC.getSize());
		assertEquals(expectedArray, mediumIntAC.collectionToString());
	}
	
	/**
	 * Testing retainAll() function. Test that this returns false because no items were removed.
	 * Test that the size and data array were affected accordingly.
	 */
	@Test 
	void testIntRetainAllRetainsAll() {
		String expectedArray = "{13, 43, 6, 90, 2, 5, 36, 1, 11, 21}";
		assertFalse(mediumIntAC.retainAll(mediumIntAC));
		assertEquals(10, mediumIntAC.getSize());
		assertEquals(expectedArray, mediumIntAC.collectionToString());
	}
	
	/**
	 * Testing the toArray() function. Should return an array of data without any extra 
	 * storage cells. An array representation of the data actually held in data[]
	 */
	@Test
	void testIntToArray() {
		mediumIntAC.add(111);
		mediumIntAC.add(222);
		String expectedArray = "13 43 6 90 2 5 36 1 11 21 111 222 ";
		assertEquals(expectedArray, mediumIntAC.toString(mediumIntAC.toArray()));
	}
	
	/**
	 * Testing toSortedList method. Puts the array collection in numerical order and includes null values at the end if applicable
	 */
	@Test
	void testIntToSortedList() {
		mediumIntAC.add(111);
		mediumIntAC.add(222);
		String sortedArray = "[1, 2, 5, 6, 11, 13, 21, 36, 43, 90, 111, 222]";
		IntegerComparator comparator = mediumIntAC.new IntegerComparator();
		ArrayList<Integer> sortedList = mediumIntAC.toSortedList(comparator);
		assertEquals(sortedArray, sortedList.toString());
	}

}
