package assign04;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The testing class for AnagramChecker class. Tests all methods in the AnagramChecker class. Does not set up a BeforeEach 
 * method because initializing and creating the different arrays and variables in each test helps describe the tests and guide the 
 * user to understand the purpose of each variable/test.
 * 
 * @author Kira Halls and Braden Campbell
 *
 */
class AnagramCheckerTest {
	
	/** Begin Sort() Tests **/
	
	/**
	 * Test the sort method on an already sorted string to ensure it doesn't undo the sorting of the string
	 */
	@Test
	void testSortAlreadySorted() {
		assertEquals("abs", AnagramChecker.sort("abs"));
	}
	
	/**
	 * Test the sort method on a string that is in reverse order to ensure it correctly sorts the string and reaches all characters in the string
	 */
	@Test 
	void testSortReverseSorted() {
		assertEquals("eert", AnagramChecker.sort("tree"));
	}
	
	/**
	 * Test the sort method on a word that is averagely unsorted to ensure it switches characters when necessary and doesn't switch when it shouldn't
	 */
	@Test 
	void testSortAveragelyUnsorted() {
		assertEquals("aelpps", AnagramChecker.sort("apples"));
	}
	
	/**
	 * Test the sort method on an empty string (not null) to ensure it does not return null pointer exception and just returns the empty string
	 */
	@Test
	void testSortEmptyString() {
		assertEquals("", AnagramChecker.sort(""));
	}
	
	/**
	 * Test the sort method on a single character string to ensure it doesn't cause any errors with such a short string
	 */
	@Test 
	void testSortSingleLetterCase() {
		assertEquals("a", AnagramChecker.sort("a"));
	}
	
	/**
	 * Test the sort method with a null string to ensure it throws the null pointer exception
	 */
	@Test
	void testSortNullString() {
		String nullString = null;
		assertThrows(NullPointerException.class, ()-> AnagramChecker.sort(nullString));
	}
	
	/** End Sort() Tests **/
	
	
	/** Begin InsertionSort() Tests **/
	
	/**
	 * Testing the insertionSort() method using an empty array. Should return an empty array.
	 */
	@SuppressWarnings("unchecked")
	@Test 
	void testInsertionSortEmptyList() {
		String[] emptyStringArray = {};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new StringComparator();
		AnagramChecker.insertionSort(emptyStringArray, comparator);
		assertEquals("[]", Arrays.toString(emptyStringArray));
	}
	
	/**
	 * Test the insertionSort() method using a single string array. Should return exact same array.
	 */
	@SuppressWarnings("unchecked")
	@Test 
	void testInsertionSortSingleItemListString() {
		String[] singleStringArray = {"dog"};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new StringComparator();
		AnagramChecker.insertionSort(singleStringArray, comparator);
		assertEquals("[dog]", Arrays.toString(singleStringArray));
	}
	
	/**
	 * Testing the insertionSort() method using a single integer array. Should return exact same array.
	 * Testing the generic nature of the insertionSort() method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInsertionSortSingleItemListInteger() {
		Integer[] singleIntArray = {43};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new IntegerComparator();
		AnagramChecker.insertionSort(singleIntArray, comparator);
		assertEquals("[43]", Arrays.toString(singleIntArray));
	}
	
	/**
	 * Testing the insertionSort() method using an already sorted list of strings. Should return exact same array. 
	 * Should not be case sensitive, so it should ignore uppercase vs. lowercase strings. Additionally, testing both 
	 * custom AnagramComparator and StringComparator, so the first set should be in anagram order, and the second set should 
	 * be in alphabetical order.
	 */
	@SuppressWarnings("unchecked")
	@Test 
	void testInsertionSortAlreadySortedListString() {
		String[] anagramAlreadySortedArray = {"Bats", "tabs", "Bast", "Stab", "scar", "arcs", "cars", "Fear", "fare"};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new AnagramComparator();
		AnagramChecker.insertionSort(anagramAlreadySortedArray, comparator);
		assertEquals("[Bats, tabs, Bast, Stab, scar, arcs, cars, Fear, fare]", Arrays.toString(anagramAlreadySortedArray));
		
		String[] alphabeticalAlreadySorted = {"arcs", "Bast", "Bats", "cars", "fare", "Fear", "scar", "Stab", "tabs"};
		@SuppressWarnings("rawtypes")
		Comparator comparator1 = checker.new StringComparator();
		AnagramChecker.insertionSort(alphabeticalAlreadySorted, comparator1);
		assertEquals("[arcs, Bast, Bats, cars, fare, Fear, scar, Stab, tabs]", Arrays.toString(alphabeticalAlreadySorted));
	}
	
	/**
	 * Testing the insertionSort() method using an already sorted list of integers. Should return the exact same array. 
	 * Testing the generic nature of the insertionSort() method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInsertionSortAlreadySortedListInteger() {
		Integer[] integerAlreadySorted = {1, 2, 3, 4, 5};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new IntegerComparator();
		AnagramChecker.insertionSort(integerAlreadySorted, comparator);
		assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(integerAlreadySorted));
	}
	
	/**
	 * Testing the insertionSort() method using a reverse sorted list of strings. Should return the opposite of that array.
	 * Additionally, testing the custom AnagramComparator and the StringComparator, so the first set should be in anagram 
	 * order and the second set should be in alphabetical order.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInsertionSortReverseSortedListString() {
		String[] anagramReverseSorted = {"fare", "Fear", "cars", "arcs", "scar", "Stab", "Bast", "tabs", "Bats"};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new AnagramComparator();
		AnagramChecker.insertionSort(anagramReverseSorted, comparator);
		assertEquals("[Stab, Bast, tabs, Bats, cars, arcs, scar, fare, Fear]", Arrays.toString(anagramReverseSorted));
		
		String[] alphabeticalReverseSorted = {"tabs", "Stab", "scar", "Fear", "fare", "cars", "Bats", "Bast", "arcs"};
		@SuppressWarnings("rawtypes")
		Comparator comparator1 = checker.new StringComparator();
		AnagramChecker.insertionSort(alphabeticalReverseSorted, comparator1);
		assertEquals("[arcs, Bast, Bats, cars, fare, Fear, scar, Stab, tabs]", Arrays.toString(alphabeticalReverseSorted));
	}
	
	/**
	 * Testing the insertionSort() method using a reverse sorted list of integers. Should return the opposite of that input array. 
	 * Testing the generic nature of the insertionSort() method.
	 */
	@SuppressWarnings("unchecked")
	@Test 
	void testInsertionSortReverseSortedListInteger() {
		Integer[] integerReverseSorted = {5, 4, 3, 2, 1};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new IntegerComparator();
		AnagramChecker.insertionSort(integerReverseSorted, comparator);
		assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(integerReverseSorted));
	}
	
	/**
	 * Testing the insertionSort() method for an averagely unsorted list of strings. Should return the sorted version of that 
	 * list. Also testing the custom AnagramComparator and StringComparator, so the first set should be in anagram order and the 
	 * second set should be in alphabetical order.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInsertionSortAveragelyUnsortedListSring() {
		String[] anagramAveragelyUnsorted = {"tabs", "fare", "cars", "Bats", "Stab", "arcs", "Fear", "scar", "Bast"};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new AnagramComparator();
		AnagramChecker.insertionSort(anagramAveragelyUnsorted, comparator);
		assertEquals("[tabs, Bats, Stab, Bast, cars, arcs, scar, fare, Fear]", Arrays.toString(anagramAveragelyUnsorted));
		
		String[] alphabeticalAveragelyUnsorted = {"tabs", "fare", "cars", "Bats", "Stab", "arcs", "Fear", "scar", "Bast"};
		@SuppressWarnings("rawtypes")
		Comparator comparator1 = checker.new StringComparator();
		AnagramChecker.insertionSort(alphabeticalAveragelyUnsorted, comparator1);
		assertEquals("[arcs, Bast, Bats, cars, fare, Fear, scar, Stab, tabs]", Arrays.toString(alphabeticalAveragelyUnsorted));
	}
	
	/**
	 * Testing the insertionSort() method for an averagely unsorted list of integers. Should return the sorted version of that list. 
	 * Testing the generic nature of the insertionSort() method.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInsertionSortAveragelyUnsortedListInteger() {
		Integer[] integerAveragelyUnsorted = {4, 1, 3, 2, 5};
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new IntegerComparator();
		AnagramChecker.insertionSort(integerAveragelyUnsorted, comparator);
		assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(integerAveragelyUnsorted));
	}
	
	/**
	 * Testing the insertionSort() method using a null list. Should throw the null pointer exception.
	 */
	@SuppressWarnings("unchecked")
	@Test
	void testInsertionSortNullList() {
		String[] nullStringArray = null;
		AnagramChecker checker = new AnagramChecker();
		@SuppressWarnings("rawtypes")
		Comparator comparator = checker.new StringComparator();
		assertThrows(NullPointerException.class, ()-> AnagramChecker.insertionSort(nullStringArray, comparator));
	}
	
	/** End InsertionSort() Tests **/
	
	
	/** Begin AreAnagrams() Tests **/
	
	/**
	 * Testing the areAnagrams() method with two empty strings. Should return true, not throw a null pointer exception.
	 */
	@Test
	void testAreAnagramsTwoEmpty() {
		assertTrue(AnagramChecker.areAnagrams("",""));
	}
	
	/**
	 * Testing the areAnagrams() method with one empty string and one non-empty string. Should return false.
	 */
	@Test
	void testAreAnagramsOneEmpty() {
		assertFalse(AnagramChecker.areAnagrams("", "apple"));
	}
	
	/**
	 * Testing the areAnagrams() method with two lowercase strings that are anagrams. Should return true.
	 */
	@Test
	void testAreAnagramsReturnsTrue() {
		assertTrue(AnagramChecker.areAnagrams("tabs", "bats"));
	}
	
	/**
	 * Testing the areAnagrams() method with one lowercase and one uppercase string that are anagrams. Should return true. 
	 * Testing that this method ignores cases when checking for equality.
	 */
	@Test
	void testAreAnagramsWithCaseSensitivity() {
		assertTrue(AnagramChecker.areAnagrams("Bats", "tabs"));
	}
	
	/**
	 * Testing the areAnagrams() method with two strings that are not anagrams. Should return false.
	 */
	@Test
	void testAreAnagramsReturnsFalse() {
		assertFalse(AnagramChecker.areAnagrams("calendar", "tab"));
	}
	
	/**
	 * Testing the areAnagrams() method with one null string and one non-null string. Should throw the null pointer exception.
	 */
	@Test
	void testAreAnagramsNullString() {
		assertThrows(NullPointerException.class, ()-> AnagramChecker.areAnagrams(null, "car"));
	}
	
	/** End AreAnagrams() Tests **/
	
	
	/** Begin getLargestAnagramGroup(String[] words) Tests **/
	
	/**
	 * Testing the getLargestAnagramGroup() method with an empty string array. Should return an empty array.
	 */
	@Test 
	void testGetLargestAnagramGroupEmpty() {
		String[] emptyStringArray = {};
		assertEquals("[]", Arrays.toString(AnagramChecker.getLargestAnagramGroup(emptyStringArray)));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() method with an array containing no anagrams. Should return an empty array.
	 */
	@Test
	void testGetLargestAnagramGroupNoAnagrams() {
		String[] noAnagramStringsArray = {"apples", "scream", "tabs", "eat", "salamander", "cookie"};
		assertEquals("[]", Arrays.toString(AnagramChecker.getLargestAnagramGroup(noAnagramStringsArray)));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() method with an array containing some anagrams. Should return an array 
	 * with all anagrams from the largest group of anagrams in the list.
	 */
	@Test
	void testGetLargestAnagramGroupSomeAnagrams() {
		String[] someAnagramStringsArray = {"tabs", "fare", "cars", "Bats", "Stab", "arcs", "Fear", "scar", "Bast"};
		assertEquals("[tabs, Bats, Stab, Bast]", Arrays.toString(AnagramChecker.getLargestAnagramGroup(someAnagramStringsArray)));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() method with an array containing multiple anagram groups that have equal numbers of anagram groups.
	 * Should return any one of those groups, but only one. Should not have all equal anagram groups in the return array.
	 */
	@Test
	void testGetLargestAnagramGroupEqualAnagramsGroupSize() {
		String[] equalAnagramGroupSizeArray = {"fare", "cars", "Bats", "Stab", "arcs", "Fear", "scar", "Bast"};
		assertEquals("[Bats, Stab, Bast]", Arrays.toString(AnagramChecker.getLargestAnagramGroup(equalAnagramGroupSizeArray)));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() method with an array parameter that is already sorted. This tests to ensure the insertionSort() method
	 * call doesn't unsort the parameter list. Should return the array containing the anagrams of the largest group of anagrams contained in the list.
	 */
	@Test
	void testGetLargestAnagramGroupSortedListParameter() {
		String[] anagramAlreadySortedArray = {"Bats", "tabs", "Bast", "Stab", "scar", "arcs", "cars", "Fear", "fare"};
		assertEquals("[Bats, tabs, Bast, Stab]", Arrays.toString(AnagramChecker.getLargestAnagramGroup(anagramAlreadySortedArray)));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() method with an unsorted array parameter. This tests to ensure the insertionSort() method call 
	 * is actually called and sorts the list as needed. Should return the array containing the anagrams of the largest group of anagrams contained in the list.
	 */
	@Test
	void testGetLargestAnagramGroupUnsortedListParameter() {
		String[] anagramReverseSorted = {"fare", "Fear", "cars", "arcs", "scar", "Stab", "Bast", "tabs", "Bats"};
		assertEquals("[Stab, Bast, tabs, Bats]", Arrays.toString(AnagramChecker.getLargestAnagramGroup(anagramReverseSorted)));
	}
	
	/**
	 * Testing the getLargestAnagramGroup method with a null array parameter. Should throw a null pointer exception.
	 */
	@Test
	void testGetLargestAnagramGroupNullException() {
		String[] nullStringArray = null;
		assertThrows(NullPointerException.class, ()-> AnagramChecker.getLargestAnagramGroup(nullStringArray));
	}
	
	/** End getLargestAnagramGroup(String[] words) Tests **/
	
	
	/** Begin getLargestAnagramGroup(String filename) Tests **/
	//These tests focus only on the different file cases, as this method calls the getLargestAnagramGroup() method from above and 
	//all edge cases have been tested in that testing section.

	/**
	 * Testing the getLargestAnagramGroup() method using an invalid file name to ensure it just returns 
	 * an empty array instead of throwing a File Not Found exception.
	 */
	@Test 
	void testGetLargestAnagramGroupFileNotFound() {
		assertEquals("[]", Arrays.toString(AnagramChecker.getLargestAnagramGroup("bananas.txt")));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() with a valid file containing some anagrams and some not. 
	 * Testing to make sure the file can be opened and read properly as well as gathering the largest anagram 
	 * group
	 */
	@Test
	void testGetLargestAnagramGroupValidFileFound() {
		assertEquals("[carets, Caters, caster, crates, Reacts, recast, traces]", Arrays.toString(AnagramChecker.getLargestAnagramGroup("C:\\Users\\kr697\\eclipse-workspace2022\\CS 2420 Assignments\\src\\assign04\\sample_word_list.txt\\")));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() using an empty file. Should return an empty array.
	 */
	@Test 
	void testGetLargestAnagramGroupEmptyFile() {
		assertEquals("[]", Arrays.toString(AnagramChecker.getLargestAnagramGroup("C:\\Users\\kr697\\eclipse-workspace2022\\CS 2420 Assignments\\src\\assign04\\BlankFile.txt\\")));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() using a file with all strings that are anagrams. Tests to make sure the file is opened 
	 * and specifically tests to make sure that the word array is being created properly from the file. The scanner must read the lines
	 * of the file to do so.
	 */
	@Test
	void testGetLargestAnagramGroupArrayCreation() {
		assertEquals("[Bats, Stab, tabs, Bast]", Arrays.toString(AnagramChecker.getLargestAnagramGroup("C:\\Users\\kr697\\eclipse-workspace2022\\CS 2420 Assignments\\src\\assign04\\StringFile.txt\\")));
	}
	
	/**
	 * Testing the getLargestAnagramGroup() using an integer only file. Should return an empty array because 
	 * the lines of the file aren't strings.
	 */
	@Test
	void testGetLargestAnagramGroupIntFile() {
		assertEquals("[]", Arrays.toString(AnagramChecker.getLargestAnagramGroup("C:\\Users\\kr697\\eclipse-workspace2022\\CS 2420 Assignments\\src\\assign04\\IntFile.txt\\")));
	}
	
	/** End getLargestAnagramGroup(String filename) Tests **/
}
