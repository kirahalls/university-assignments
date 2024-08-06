package assign06;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import assign04.AnagramChecker;
import assign06.SinglyLinkedList.LinkedListIterator;

/**
 * The JUnit test case for the SinglyLinkedList class. Each is tested on both an Integer and a String SinglyLinkedList to ensure the generic nature of the code
 * 
 * @author Kira Halls
 *
 */
class SinglyLinkedListTest {
	private SinglyLinkedList<Integer> emptyListInt, smallListInt, mediumListInt;
	private SinglyLinkedList<String> emptyListString, smallListString, mediumListString;
	private Iterator<Integer> emptyIteratorInt, smallIteratorInt, mediumIteratorInt;
	private Iterator<String> emptyIteratorString, smallIteratorString, mediumIteratorString;
	
	@BeforeEach
	void setUp() throws Exception {
		emptyListInt = new SinglyLinkedList<Integer>();
		emptyIteratorInt = emptyListInt.iterator();
		smallListInt = new SinglyLinkedList<Integer>();
		for (int i = 0; i < 6; i++) {
			smallListInt.insertFirst(i);
		}
		smallIteratorInt = smallListInt.iterator();
		mediumListInt = new SinglyLinkedList<Integer>();
		for (int i = 0; i < 11; i++) {
			mediumListInt.insertFirst(i);
		}
		mediumIteratorInt = mediumListInt.iterator();
		
		emptyListString = new SinglyLinkedList<String>();
		emptyIteratorString = emptyListString.iterator();
		smallListString = new SinglyLinkedList<String>();
		String[] smallStringList = {"cat", "dog", "alligator", "car", "snack"};
		for (String value : smallStringList) {
			smallListString.insertFirst(value);
		}
		smallIteratorString = smallListString.iterator();
		mediumListString = new SinglyLinkedList<String>();
		String[] mediumStringList = {"cat", "dog", "alligator", "car", "snack", "tea", "water", "sky", "hello", "movie"};
		for (String value : mediumStringList) {
			mediumListString.insertFirst(value);
		}
		mediumIteratorString = mediumListString.iterator();
	}

	/** Begin Outer Class Tests **/
	
	/**
	 * This tests the insertFirst() method on an empty list to ensure it properly inserts the element at the beginning of the list
	 */
	@Test
	void testInsertEmptyFirst() {
		emptyListInt.insertFirst(43);
		assertEquals("{43, }", emptyListInt.toString());
		
		emptyListString.insertFirst("salad");
		assertEquals("{salad, }", emptyListString.toString());
	}
	
	/**
	 * This tests the insertFirst() method on a small list to ensure it properly inserts the element at the beginning of the list
	 */
	@Test
	void testInsertFirstSmall() {
		smallListInt.insertFirst(43);
		assertEquals("{43, 5, 4, 3, 2, 1, 0, }", smallListInt.toString());
		
		smallListString.insertFirst("cable");
		assertEquals("{cable, snack, car, alligator, dog, cat, }", smallListString.toString());
	}
	
	/**
	 * This tests the insertFirst() method on a medium list to ensure it properly inserts the element at the beginning of the list
	 */
	@Test
	void testInsertFirstMedium() {
		mediumListInt.insertFirst(43);
		assertEquals("{43, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, }", mediumListInt.toString());
		
		mediumListString.insertFirst("cable");
		assertEquals("{cable, movie, hello, sky, water, tea, snack, car, alligator, dog, cat, }", mediumListString.toString());
	}
	
	/**
	 * This tests the insert() method on an empty list to ensure it properly inserts the correct element at the specified index
	 */
	@Test
	void testInsertEmpty() {
		emptyListInt.insert(0, 43);
		assertEquals("{43, }", emptyListInt.toString());
		
		emptyListString.insert(0, "cake");
		assertEquals("{cake, }", emptyListString.toString());
	}
	
	/**
	 * This tests the insert() method on a small list to ensure it properly inserts the correct element at the specified index
	 */
	@Test
	void testInsertSmall() {
		smallListInt.insert(4, 43);
		assertEquals("{5, 4, 3, 2, 43, 1, 0, }", smallListInt.toString());
		
		smallListString.insert(3, "apple");
		assertEquals("{snack, car, alligator, apple, dog, cat, }", smallListString.toString());
	}
	
	/**
	 * This tests the insert() method to ensure it properly inserts the correct element at the specific index
	 */
	@Test
	void testInsertMedium() {
		mediumListInt.insert(9,  43);
		assertEquals("{10, 9, 8, 7, 6, 5, 4, 3, 2, 43, 1, 0, }", mediumListInt.toString());
		
		mediumListString.insert(0, "example");
		assertEquals("{example, movie, hello, sky, water, tea, snack, car, alligator, dog, cat, }", mediumListString.toString());
	}
	
	/**
	 * This tests the insert() method to ensure it properly inserts the correct element at the end index
	 */
	@Test
	void testInsertMediumEnd() {
		mediumListInt.insert(11,  43);
		assertEquals("{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 43, }", mediumListInt.toString());
		
		mediumListString.insert(10, "example");
		assertEquals("{movie, hello, sky, water, tea, snack, car, alligator, dog, cat, example, }", mediumListString.toString());
	}
	
	/**
	 * This tests the insert() method using an index that is out of bounds to ensure it throws an index out of bounds exception
	 */
	@Test
	void testInsertIndexOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, ()-> smallListInt.insert(99, 67));
		
		assertThrows(IndexOutOfBoundsException.class, ()-> smallListString.insert(1000, "call"));
	}
	
	/**
	 * This tests the getFirst() method on an empty list to ensure it throws a no such element exception
	 */
	@Test
	void testGetFirstEmptyList() {
		assertThrows(NoSuchElementException.class, () ->{emptyListInt.getFirst();});
		
		assertThrows(NoSuchElementException.class, () ->{emptyListString.getFirst();});
	}
	
	/**
	 * This tests the getFirst() method on a small list to ensure it properly returns the first value
	 */
	@Test
	void testGetFirstSmallList() {
		Integer firstValueInt = (Integer) smallListInt.getFirst();
		assertEquals(5, firstValueInt);
		
		String firstValueString = (String) smallListString.getFirst();
		assertEquals("snack", firstValueString);
	}
	
	/**
	 * This tests the getFirst() method on a medium list to ensure it returns the proper first value
	 */
	@Test
	void testGetFirstMediumList() {
		Integer firstValueInt = (Integer) mediumListInt.getFirst();
		assertEquals(10, firstValueInt);
		
		String firstValueString = (String) mediumListString.getFirst();
		assertEquals("movie", firstValueString);
	}
	
	/**
	 * This tests the get() method on an empty list to ensure it throws an index out of bounds exception
	 */
	@Test
	void testGetEmptyList() {
		assertThrows(IndexOutOfBoundsException.class, () ->{emptyListInt.get(0);});
		
		assertThrows(IndexOutOfBoundsException.class, () ->{emptyListString.get(6);});
	}
	
	/**
	 * This tests the get() method on a small list to ensure it returns the proper value
	 */
	@Test
	void testGetSmallList() {
		Integer getValueInt = (Integer) smallListInt.get(3);
		assertEquals(2, getValueInt);
		
		String getValueString = (String) smallListString.get(3);
		assertEquals("dog", getValueString);
	}
	
	/**
	 * This tests the get() method on a medium list to ensure it returns the proper value 
	 */
	@Test
	void testGetMediumList() {
		Integer getValueInt = (Integer) mediumListInt.get(5);
		assertEquals(5, getValueInt);
		
		String getValueString = (String) mediumListString.get(5);
		assertEquals("snack", getValueString);
	}
	
	/**
	 * This tests the get() method using an index that is out of bounds to ensure it throws an index out of bounds error
	 */
	@Test
	void testGetOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, ()-> smallListInt.get(11));
		
		assertThrows(IndexOutOfBoundsException.class, ()-> smallListString.get(11));
	}
	
	/**
	 * This tests the delteFirst() method on an empty list to ensure it throws a no such element exception
	 */
	@Test
	void testDeleteFirstEmptyList() {
		assertThrows(NoSuchElementException.class, () ->{emptyListInt.deleteFirst();});
		
		assertThrows(NoSuchElementException.class, () ->{emptyListString.deleteFirst();});
	}
	
	/**
	 * This tests the deleteFirst() method on a small list to ensure it properly deletes the first value and moves all other values when necessary
	 */
	@Test
	void testDeleteFirstSmallList() {
		String expectedIntString = "{4, 3, 2, 1, 0, }";
		Object deletedValue = smallListInt.deleteFirst();
		assertEquals(5, deletedValue);
		assertEquals(expectedIntString, smallListInt.toString());
		
		String expectedString = "{car, alligator, dog, cat, }";
		Object deletedValueString = smallListString.deleteFirst();
		assertEquals("snack", deletedValueString);
		assertEquals(expectedString, smallListString.toString());	
	}
	
	/**
	 * This tests the deleteFirst() function on a medium list to ensure it properly deletes the first value and moves all other values as necessary
	 */
	@Test
	void testDeleteFirstMediumList() {
		String expectedIntString = "{9, 8, 7, 6, 5, 4, 3, 2, 1, 0, }";
		Object deletedValue = mediumListInt.deleteFirst();
		assertEquals(10, deletedValue);
		assertEquals(expectedIntString, mediumListInt.toString());

		String expectedString = "{hello, sky, water, tea, snack, car, alligator, dog, cat, }";
		Object deletedValueString = mediumListString.deleteFirst();
		assertEquals("movie", deletedValueString);
		assertEquals(expectedString, mediumListString.toString());
	}
	
	/**
	 * This tests the delete() method on an empty list to ensure it throws an index out of bounds exception 
	 */
	@Test
	void testDeleteEmptyList() {
		assertThrows(IndexOutOfBoundsException.class, ()-> emptyListInt.delete(0));
		
		assertThrows(IndexOutOfBoundsException.class, ()-> emptyListString.delete(0));
	}
	
	/**
	 * This tests the delete() method on a small list to ensure it deletes the correct element at the specified index
	 */
	@Test
	void testDeleteSmallList() {
		String expectedIntString = "{5, 4, 2, 1, 0, }";
		Object deletedValue = smallListInt.delete(2);
		assertEquals(3, deletedValue);
		assertEquals(expectedIntString, smallListInt.toString());
		
		String expectedString = "{snack, car, dog, cat, }";
		Object deletedValueString = smallListString.delete(2);
		assertEquals("alligator", deletedValueString);
		assertEquals(expectedString, smallListString.toString());
	}
	
	/**
	 * This test the delete() method on a medium list to ensure it deletes the correct element at the specified index
	 */
	@Test
	void testDeleteMediumList() {
		String expectedIntString = "{10, 9, 8, 6, 5, 4, 3, 2, 1, 0, }";
		Object deletedValue = mediumListInt.delete(3);
		assertEquals(7, deletedValue);
		assertEquals(expectedIntString, mediumListInt.toString());

		String expectedString = "{movie, hello, sky, tea, snack, car, alligator, dog, cat, }";
		Object deletedValueString = mediumListString.delete(3);
		assertEquals("water", deletedValueString);
		assertEquals(expectedString, mediumListString.toString());
	}
	
	/**
	 * This tests the delete() method using an index that is out of bounds to ensure it throws the correct error when necessary
	 */
	@Test
	void testDeleteOutOfBounds() {
		assertThrows(IndexOutOfBoundsException.class, ()-> mediumListInt.delete(20));
		
		assertThrows(IndexOutOfBoundsException.class, ()-> mediumListString.delete(20));
	}
	
	/**
	 * This test the indexOf() method on an empty list to ensure it always returns -1
	 */
	@Test
	void testIndexOfEmptyList() {
		assertEquals(-1, emptyListInt.indexOf(0));
		
		assertEquals(-1, emptyListString.indexOf("boat"));
	}
	
	/**
	 * This tests the indexOf() method on a small list to ensure it returns the correct index
	 */
	@Test
	void testIndexOfSmallList() {
		assertEquals(2, smallListInt.indexOf(3));
		
		assertEquals(1, smallListString.indexOf("car"));
	}
	
	/**
	 * This tests the indexOf() method on a medium list to ensure it returns the correct index
	 */
	@Test
	void testIndexOfMediumList() {
		assertEquals(9, mediumListInt.indexOf(1));
		
		assertEquals(4, mediumListString.indexOf("tea"));
	}
	
	/**
	 * This tests the indexOf() method to ensure it returns -1 when the list does not contain the specified element
	 */
	@Test
	void testIndexOfError() {
		assertEquals(-1, smallListInt.indexOf(99));
		assertEquals(-1, mediumListInt.indexOf(100));
		
		assertEquals(-1, smallListString.indexOf("example"));
		assertEquals(-1, mediumListString.indexOf("example2"));
	}

	/**
	*This tests the size() function on the original lists to ensure it is functioning correctly
	*/	
	@Test
	void testOriginalSize() {
		assertEquals(6, smallListInt.size());
		assertEquals(11, mediumListInt.size());
		
		assertEquals(5, smallListString.size());
		assertEquals(10, mediumListString.size());
	}
	
	/**
	*This tests the size() method after calling the delete() method to ensure the size decreases properly
	*/
	@Test
	void testDeletedSize() {
		smallListInt.delete(1);
		assertEquals(5, smallListInt.size());
		mediumListInt.delete(1);
		assertEquals(10, mediumListInt.size());
		
		smallListString.delete(1);
		assertEquals(4, smallListString.size());
		mediumListString.delete(1);
		assertEquals(9, mediumListString.size());
	}
	
	/**
	*This tests the size() function after calling the deleteFirst() function to ensure the size decreases properly
	*/
	@Test
	void testDeleteFirstSize() {
		smallListInt.deleteFirst();
		assertEquals(5, smallListInt.size());
		mediumListInt.deleteFirst();
		assertEquals(10, mediumListInt.size());
		
		smallListString.deleteFirst();
		assertEquals(4, smallListString.size());
		mediumListString.deleteFirst();
		assertEquals(9, mediumListString.size());
	}
	
	/**
	*This tests the size() function after calling the insertFirst() function to ensure the size increases properly
	*/
	@Test
	void testInsertFirstSize() {
		smallListInt.insertFirst(43);
		assertEquals(7, smallListInt.size());
		mediumListInt.insertFirst(54);
		assertEquals(12, mediumListInt.size());
		
		smallListString.insertFirst("cable");
		assertEquals(6, smallListString.size());
		mediumListString.insertFirst("brain");
		assertEquals(11, mediumListString.size());
	}
	
	/**
	*This tests the size() function after calling the insert() function to ensure the size increases properly
	*/
	@Test
	void testInsertSize() {
		smallListInt.insert(1, 43);
		assertEquals(7, smallListInt.size());
		mediumListInt.insert(1, 67);
		assertEquals(12, mediumListInt.size());
		
		smallListString.insert(3, "soar");
		assertEquals(6, smallListString.size());
		mediumListString.insert(4, "choice");
		assertEquals(11, mediumListString.size());
	}
	
	/**
	*This tests the size() function after calling the clear function to ensure the size of the cleared list is 0
	*/
	@Test
	void testClearSize() {
		smallListInt.clear();
		assertEquals(0, smallListInt.size());
		mediumListInt.clear();
		assertEquals(0, mediumListInt.size());
		
		smallListString.clear();
		assertEquals(0, smallListString.size());
		mediumListString.clear();
		assertEquals(0, mediumListString.size());
	}
	
	/**
	*This tests the clear() function to ensure the contents of the lists are actually cleared
	*/
	@Test
	void testClear() {
		smallListInt.clear();
		assertEquals("{}", smallListInt.toString());
		mediumListInt.clear();
		assertEquals("{}", mediumListInt.toString());
		
		smallListString.clear();
		assertEquals("{}", smallListString.toString());
		mediumListString.clear();
		assertEquals("{}", mediumListString.toString());
	}
	
	/**
	*This tests the isEmpty() function using values that should return true
	*/
	@Test
	void testIsEmptyTrue() {
		assertTrue(emptyListInt.isEmpty());
		
		assertTrue(emptyListString.isEmpty());
	}
	
	/**
	*This tests the isEmpty() function using values that should return false
	*/
	@Test
	void testIsEmptyFalse() {
		assertFalse(smallListInt.isEmpty());
		assertFalse(smallListString.isEmpty());
		
		assertFalse(mediumListInt.isEmpty());
		assertFalse(mediumListString.isEmpty());
	}
	
	/**
	*This tests the toArray() function to ensure it properly converts the list to an array
	*/
	@Test
	void testToArray() {
		Object[] expectedEmpty = {};
		assertArrayEquals(expectedEmpty, emptyListInt.toArray());
		assertArrayEquals(expectedEmpty, emptyListString.toArray());
		
		Object[] expectedSmallIntArray= {5, 4, 3, 2, 1, 0};
		assertArrayEquals(expectedSmallIntArray, smallListInt.toArray());
		String[] expectedSmallStringArray = {"snack", "car", "alligator", "dog", "cat"};
		assertArrayEquals(expectedSmallStringArray, smallListString.toArray());
		
		Object[] expectedMediumIntArray = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
		assertArrayEquals(expectedMediumIntArray, mediumListInt.toArray());
		Object[] expectedMediumStringArray = {"movie", "hello", "sky", "water", "tea", "snack", "car", "alligator", "dog", "cat"};
		assertArrayEquals(expectedMediumStringArray, mediumListString.toArray());
	}
	
	/**
	*This tests our custom toString() function to ensure it produces the desired string
	*/
	@Test
	void testToString() {
		
		String expectedEmptyString = "{}";
		assertEquals(expectedEmptyString, emptyListInt.toString());
		assertEquals(expectedEmptyString, emptyListString.toString());
		
		String expectedSmallIntString= "{5, 4, 3, 2, 1, 0, }";
		assertEquals(expectedSmallIntString, smallListInt.toString());
		String expectedSmallString = "{snack, car, alligator, dog, cat, }";
		assertEquals(expectedSmallString, smallListString.toString());
		
		String expectedMediumIntString = "{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, }";
		assertEquals(expectedMediumIntString, mediumListInt.toString());
		String expectedMediumString = "{movie, hello, sky, water, tea, snack, car, alligator, dog, cat, }";
		assertEquals(expectedMediumString, mediumListString.toString());
	}
	
	/** Begin Iterator Tests **/
	
	/**
	 * Tests the hasNext() function on the beginning value of a list to ensure it returns true even 
	 * if we haven't called anything else (making sure it can see the first value)
	 */
	@Test
	void testIteratorHasNextBeginning() {
		assertFalse(emptyIteratorInt.hasNext());
		assertFalse(emptyIteratorString.hasNext());
		
		assertTrue(smallIteratorInt.hasNext());
		assertTrue(smallIteratorString.hasNext());
		
		assertTrue(mediumIteratorInt.hasNext());
		assertTrue(mediumIteratorString.hasNext());
	}
	
	/**
	 * Tests the hasNext() function the last value to ensure it returns false
	 */
	@Test
	void testIteratorHasNextEnd() {
		for (int i = 0; i < smallListInt.size(); i++) {
			smallIteratorInt.next();
		}
		for (int i = 0; i < smallListString.size(); i++) {
			smallIteratorString.next();
		}
		assertFalse(smallIteratorInt.hasNext());
		assertFalse(smallIteratorInt.hasNext());
		
		for (int i = 0; i < mediumListInt.size(); i++) {
			mediumIteratorInt.next();
		}
		for (int i = 0; i < mediumListString.size(); i++) {
			mediumIteratorString.next();
		}
		assertFalse(mediumIteratorInt.hasNext());
		assertFalse(mediumIteratorString.hasNext());
	}
	
	/**
	 * Tests the hasNext() function on the second to last value to ensure it returns true
	 */
	@Test
	void testIteratorHasNextAlmostEnd() {
		for (int i = 0; i < smallListInt.size() - 1; i++) {
			smallIteratorInt.next();
		}
		for (int i = 0; i < smallListString.size() - 1; i++) {
			smallIteratorString.next();
		}
		assertTrue(smallIteratorInt.hasNext());
		assertTrue(smallIteratorString.hasNext());
		
		for (int i = 0; i < mediumListInt.size() - 1; i++) {
			mediumIteratorInt.next();
		}
		for (int i = 0; i < mediumListString.size() - 1; i++) {
			mediumIteratorString.next();
		}
		assertTrue(mediumIteratorInt.hasNext());
		assertTrue(mediumIteratorString.hasNext());
	}
	
	/**
	 * Tests the hasNext() function on an average case
	 */
	@Test
	void testIteratorHasNextAverage() {
		smallIteratorInt.next();
		smallIteratorInt.next();
		smallIteratorString.next();
		smallIteratorString.next();
		assertTrue(smallIteratorInt.hasNext());
		assertTrue(smallIteratorInt.hasNext());
		
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		assertTrue(mediumIteratorInt.hasNext());
		assertTrue(mediumIteratorString.hasNext());
	}
	
	/**
	 * Tests the next() function to ensure that one call to next gets only the first value
	 */
	@Test
	void testIteratorNext() {
		assertThrows(NoSuchElementException.class, ()-> emptyIteratorInt.next());
		assertThrows(NoSuchElementException.class, ()-> emptyIteratorString.next());
		
		assertEquals(5, smallIteratorInt.next());
		assertEquals("snack", smallIteratorString.next());
		
		assertEquals(10, mediumIteratorInt.next());
		assertEquals("movie", mediumIteratorString.next());
	}
	
	/**
	 * Tests the next() function to ensure it can access and return the last value in the string
	 */
	@Test
	void testIteratorNextEnd() {
		for (int i = 0; i < smallListInt.size(); i++) {
			smallIteratorInt.next();
		}
		for (int i = 0; i < smallListString.size(); i++) {
			smallIteratorString.next();
		}
		assertThrows(NoSuchElementException.class, ()-> smallIteratorInt.next());
		assertThrows(NoSuchElementException.class, ()-> smallIteratorString.next());
		
		for (int i = 0; i < mediumListInt.size(); i++) {
			mediumIteratorInt.next();
		}
		for (int i = 0; i < mediumListString.size(); i++) {
			mediumIteratorString.next();
		}
		assertThrows(NoSuchElementException.class, ()-> mediumIteratorInt.next());
		assertThrows(NoSuchElementException.class, ()-> mediumIteratorString.next());
	}
	
	/**
	 * Tests the next() method on an average case to ensure it returns the correct value
	 */
	@Test
	void testIteratorNextAverage() {
		smallIteratorInt.next();
		smallIteratorInt.next();
		smallIteratorString.next();
		smallIteratorString.next();
		assertEquals(3, smallIteratorInt.next());
		assertEquals("alligator", smallIteratorString.next());
		
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		assertEquals(6, mediumIteratorInt.next());
		assertEquals("tea", mediumIteratorString.next());
	}
	
	/**
	 * Tests the remove() method called twice in a row. Should throw an IllegalStateException
	 */
	@Test
	void testRemoveTwice() {
		smallIteratorInt.next();
		smallIteratorInt.remove();
		smallIteratorString.next();
		smallIteratorString.remove();
		assertThrows(IllegalStateException.class, ()-> smallIteratorInt.remove());
		assertThrows(IllegalStateException.class, ()-> smallIteratorString.remove());
		
		mediumIteratorInt.next();
		mediumIteratorInt.remove();
		mediumIteratorString.next();
		mediumIteratorString.remove();
		assertThrows(IllegalStateException.class, ()-> mediumIteratorInt.remove());
		assertThrows(IllegalStateException.class, ()-> mediumIteratorString.remove());
	}
	
	/**
	 * Tests the remove() method without a previous call to next. Should throw an IllegalStateException
	 */
	@Test
	void testRemoveWithoutNext() {
		assertThrows(IllegalStateException.class, ()-> smallIteratorInt.remove());
		assertThrows(IllegalStateException.class, ()-> smallIteratorString.remove());
		
		assertThrows(IllegalStateException.class, ()-> mediumIteratorInt.remove());
		assertThrows(IllegalStateException.class, ()-> mediumIteratorString.remove());
	}
	
	/**
	 * Tests remove on an empty list, should throw an exception. Also tests remove() on 
	 * the first value of the linked list to ensure it can access it and properly remove it
	 */
	@Test
	void testRemove() {
		assertThrows(IllegalStateException.class, ()-> emptyIteratorInt.remove());
		assertThrows(IllegalStateException.class, ()-> emptyIteratorString.remove());
		
		smallIteratorInt.next();
		smallIteratorInt.remove();
		smallIteratorString.next();
		smallIteratorString.remove();
		assertEquals("{4, 3, 2, 1, 0, }", smallListInt.toString());
		assertEquals("{car, alligator, dog, cat, }", smallListString.toString());
		
		mediumIteratorInt.next();
		mediumIteratorInt.remove();
		mediumIteratorString.next();
		mediumIteratorString.remove();
		assertEquals("{9, 8, 7, 6, 5, 4, 3, 2, 1, 0, }", mediumListInt.toString());
		assertEquals("{hello, sky, water, tea, snack, car, alligator, dog, cat, }", mediumListString.toString());
	}
	
	/**
	 * Tests the remove() method on a value at the end of the list. Ensures remove can access
	 * and properly remove the last value.
	 */
	@Test
	void testRemoveEnd() {
		for (int i = 0; i < smallListInt.size(); i++) {
			smallIteratorInt.next();
		}
		for (int i = 0; i < smallListString.size(); i++) {
			smallIteratorString.next();
		}
		smallIteratorInt.remove();
		smallIteratorString.remove();
		assertEquals("{5, 4, 3, 2, 1, }", smallListInt.toString());
		assertEquals("{snack, car, alligator, dog, }", smallListString.toString());
		
		for (int i = 0; i < mediumListInt.size(); i++) {
			mediumIteratorInt.next();
		}
		for (int i = 0; i < mediumListString.size(); i++) {
			mediumIteratorString.next();
		}
		mediumIteratorInt.remove();
		mediumIteratorString.remove();
		assertEquals("{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, }", mediumListInt.toString());
		assertEquals("{movie, hello, sky, water, tea, snack, car, alligator, dog, }", mediumListString.toString());
	}	
	
	/**
	 * Tests the iterator remove() method on an average case
	 */
	@Test
	void testRemoveAverage() {
		smallIteratorInt.next();
		smallIteratorInt.next();
		smallIteratorInt.next();
		smallIteratorInt.remove();
		smallIteratorString.next();
		smallIteratorString.next();
		smallIteratorString.next();
		smallIteratorString.remove();
		assertEquals("{5, 4, 2, 1, 0, }", smallListInt.toString());
		assertEquals("{snack, car, dog, cat, }", smallListString.toString());
		
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.next();
		mediumIteratorInt.remove();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.next();
		mediumIteratorString.remove();
		assertEquals("{10, 9, 8, 7, 6, 4, 3, 2, 1, 0, }", mediumListInt.toString());
		assertEquals("{movie, hello, sky, water, tea, car, alligator, dog, cat, }", mediumListString.toString());
	}
	
	/** End Iterator Tests **/
}
