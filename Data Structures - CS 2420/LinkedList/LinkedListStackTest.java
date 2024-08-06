package assign06;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the test class for the LinkedListStack class. Tests all functions for quality and correctness,
 * including edge cases.
 * 
 * @author Kira Halls
 *
 */
class LinkedListStackTest {
	private LinkedListStack<Integer> emptyIntStack, intStack;
	private LinkedListStack<String> emptyStringStack, stringStack;
	
	@BeforeEach
	void setUp() {
		emptyIntStack = new LinkedListStack<Integer>();
		intStack = new LinkedListStack<Integer>();
		for (int i = 0; i < 5; i++) {
			intStack.push(i);
		}
		
		emptyStringStack = new LinkedListStack<String>();
		stringStack = new LinkedListStack<String>();
		String[] stringArray = {"car", "dog", "snack", "movie", "river"};
		for (String value: stringArray) {
			stringStack.push(value);
		}
	}
	
	/**
	 * This tests the clear() method to ensure it correctly removes all items from the stack.
	 */
	@Test
	void testClear() {
		emptyIntStack.clear();
		intStack.clear();
		assertEquals("{}", emptyIntStack.toString());
		assertEquals("{}", intStack.toString());
		
		emptyStringStack.clear();
		stringStack.clear();
		assertEquals("{}", emptyStringStack.toString());
		assertEquals("{}", stringStack.toString());
	}
	
	/**
	 * Tests the isEmpty() method on a stack that is already empty. Should return true for the empty stacks and false 
	 * for the other stacks.
	 */
	@Test
	void testIsEmptyAlready() {
		assertTrue(emptyIntStack.isEmpty());
		assertTrue(emptyStringStack.isEmpty());
		
		assertFalse(intStack.isEmpty());
		assertFalse(stringStack.isEmpty());
	}
	
	/**
	 * Tests the isEmpty() method on a stack that calls the clear() method before calling isEmpty(). Should return true as the 
	 * clear() function must empty the stack.
	 */
	@Test
	void testIsEmptyClear() {
		intStack.clear();
		stringStack.clear();
		assertTrue(intStack.isEmpty());
		assertTrue(stringStack.isEmpty());
	}
	
	/**
	 * This tests the peek() method to ensure it returns but does not remove the item at the top of the specified stack.
	 */
	@Test
	void testPeek() {
		assertEquals(4, intStack.peek());
		assertEquals("{4, 3, 2, 1, 0, }", intStack.toString());
		
		assertEquals("river", stringStack.peek());
		assertEquals("{river, movie, snack, dog, car, }", stringStack.toString());
	}
	
	/**
	 * Tests the pop() function to ensure it removes and returns the item at the top of the specified stack.
	 */
	@Test
	void testPop() {
		Object poppedInt = intStack.pop();
		assertEquals(4, poppedInt);
		String intString = "{3, 2, 1, 0, }";
		assertEquals(intString, intStack.toString());
		
		Object poppedString = stringStack.pop();
		assertEquals("river", poppedString);
		String expectedString = "{movie, snack, dog, car, }";
		assertEquals(expectedString, stringStack.toString());
	}
	
	/**
	 * This tests the push() function to ensure it adds the item to the top of the specified stack
	 */
	@Test
	void testPush() {
		intStack.push(6);
		String intString = "{6, 4, 3, 2, 1, 0, }";
		assertEquals(intString, intStack.toString());
		
		stringStack.push("water");
		String string = "{water, river, movie, snack, dog, car, }";
		assertEquals(string, stringStack.toString());
	}
	
	/**
	 * This tests the size() function to ensure it correctly gets the size of an unaltered stack
	 */
	@Test
	void testOriginalSize() {
		assertEquals(5, intStack.size());
		
		assertEquals(5, stringStack.size());
	}
	
	/**
	 * This tests the size() function after calling clear() to ensure it returns a size of 0
	 */
	@Test
	void testSizeClear() {
		intStack.clear();
		stringStack.clear();
		assertEquals(0, intStack.size());
		assertEquals(0, stringStack.size());
	}
	
	/**
	 * This tests the size() function to ensure the peek() method does not alter the stack
	 */
	@Test
	void testSizePeek() {
		intStack.peek();
		stringStack.peek();
		assertEquals(5, intStack.size());
		assertEquals(5, stringStack.size());
	}
	
	/**
	 * This tests the size() function to ensure the pop() method returns and removes the item on top of the stack
	 */
	@Test
	void testSizePop() {
		intStack.pop();
		stringStack.pop();
		assertEquals(4, intStack.size());
		assertEquals(4, stringStack.size());
	}
	
	/**
	 * This tests the size() function to ensure the push() method adds the item to the top of the stack
	 */
	@Test
	void testSizePush() {
		intStack.push(43);
		stringStack.push("call");
		assertEquals(6, intStack.size());
		assertEquals(6, stringStack.size());
	}
}
