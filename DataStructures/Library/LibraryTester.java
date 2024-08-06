package assign02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class contains tests for Library.
 * 
 * @author Erin Parker and Braden Campbell and Kira Halls
 * @version September 2, 2020
 */
public class LibraryTester {

	private Library emptyLib, smallLib, mediumLib;
	
	@BeforeEach
	void setUp() throws Exception {
		emptyLib = new Library();
		
		smallLib = new Library();
		smallLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		smallLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		smallLib.add(9780446580342L, "David Baldacci", "Simple Genius");

		mediumLib = new Library();
		mediumLib.addAll("src/assign02/Mushroom_Publishing.txt");
	}

	@Test
	public void testEmptyLookupISBN() {
		assertNull(emptyLib.lookup(978037429279L));
	}
	
	@Test
	public void testEmptyLookupHolder() {
		ArrayList<LibraryBook> booksCheckedOut = emptyLib.lookup("Jane Doe");
		assertNotNull(booksCheckedOut);
		assertEquals(0, booksCheckedOut.size());
	}
	
	@Test
	public void testEmptyCheckout() {
		assertFalse(emptyLib.checkout(978037429279L, "Jane Doe", 1, 1, 2008));
	}

	@Test
	public void testEmptyCheckinISBN() {
		assertFalse(emptyLib.checkin(978037429279L));
	}
	
	@Test
	public void testEmptyCheckinHolder() {
		assertFalse(emptyLib.checkin("Jane Doe"));
	}

	@Test
	public void testSmallLibraryLookupISBN() {
		assertNull(smallLib.lookup(9780330351690L));
	}
	
	@Test
	public void testSmallLibraryLookupHolder() {
		smallLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008);
		ArrayList<LibraryBook> booksCheckedOut = smallLib.lookup("Jane Doe");
		
		assertNotNull(booksCheckedOut);
		assertEquals(1, booksCheckedOut.size());
		assertEquals(new Book(9780330351690L, "Jon Krakauer", "Into the Wild"), booksCheckedOut.get(0));
		assertEquals("Jane Doe", booksCheckedOut.get(0).getHolder());
	}

	@Test
	public void testSmallLibraryCheckout() {
		assertTrue(smallLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008));
	}

	@Test
	public void testSmallLibraryCheckinISBN() {
		smallLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008);
		assertTrue(smallLib.checkin(9780330351690L));
	}

	@Test
	public void testSmallLibraryCheckinHolder() {
		assertFalse(smallLib.checkin("Jane Doe"));
	}
	
	/**
	 * Book not in Library returns null
	 */
	@Test
	public void testMediumLookupISBN() {
		assertNull(mediumLib.lookup(978037429279L));
	}
	
	/**
	 * Book in Library returns holder
	 */
	@Test
	public void testMediumLookupISBNInLibrary() {
		mediumLib.checkout(9781843190004L, "Bob", 12, 31, 2022);
		assertEquals("Bob", mediumLib.lookup(9781843190004L));
	}
	
	/**
	 * Book in Library not checked out returns null
	 */
	@Test
	public void testMediumLookupISBNNotCheckedOut() {
		assertNull(mediumLib.lookup(9781843190004L));
	}
	
	@Test
	public void testMediumLookupHolder() {
		ArrayList<LibraryBook> booksCheckedOut = mediumLib.lookup("Jane Doe");
		assertNotNull(booksCheckedOut);
		assertEquals(0, booksCheckedOut.size());
	}
	
	@Test
	public void testMediumLookupHolderCheckedOut() {
		mediumLib.checkout(9781843190004L, "Jane Doe", 12, 31, 2022);
		mediumLib.checkout(9781843190011L, "Jane Doe", 12, 31, 2022);
		mediumLib.checkout(9781843190028L, "Jane Doe", 12, 31, 2022);
		ArrayList<LibraryBook> booksCheckedOut = mediumLib.lookup("Jane Doe");
		assertNotNull(booksCheckedOut);
		assertEquals(3, booksCheckedOut.size());
	}
	
	/**
	 * Book not in Library return false
	 */
	@Test
	public void testMediumCheckout() {
		assertFalse(mediumLib.checkout(978037429279L, "Jane Doe", 1, 1, 2008));
	}
	
	/**
	 * Book in Library and not checked out
	 */
	@Test
	public void testMediumCheckoutInLibrary() {
		assertTrue(mediumLib.checkout(9781843190004L, "Jane Doe", 1, 1, 2008));
	}
	
	/**
	 * Book in Library but checked out
	 */
	@Test
	public void testMediumCheckoutInLibraryCheckedOut() {
		mediumLib.checkout(9781843190004L, "John Doe", 12, 31, 2022);
		assertFalse(mediumLib.checkout(9781843190004L, "Jane Doe", 1, 1, 2008));
	}
	
	/**
	 * Checking in book not in Library
	 */
	@Test
	public void testMediumCheckinISBN() {
		assertFalse(mediumLib.checkin(978037429279L));
	}
	
	/**
	 * Checking in book in Library not checked out
	 */
	@Test
	public void testMediumCheckinISBNInLibrary() {
		assertFalse(mediumLib.checkin(9781843190004L));
	}
	
	/**
	 * Checking in book that has been checked out
	 */
	@Test
	public void testMediumCheckinISBNInLibraryCheckedOut() {
		mediumLib.checkout(9781843190004L, "John Doe", 12, 31, 2022);
		assertTrue(mediumLib.checkin(9781843190004L));
	}
	
	/**
	 * Checking in but holder has no books checked out
	 */
	@Test
	public void testMediumCheckinHolder() {
		assertFalse(mediumLib.checkin("Jane Doe"));
	}
	
	/**
	 * Checking in multiple books from holder
	 */
	@Test
	public void testMediumCheckinHolderCheckedOut() {
		mediumLib.checkout(9781843190004L, "Jane Doe", 12, 31, 2022);
		mediumLib.checkout(9781843190011L, "Jane Doe", 12, 31, 2022);
		mediumLib.checkout(9781843190028L, "Jane Doe", 12, 31, 2022);
		assertTrue(mediumLib.checkin("Jane Doe"));
	}
}
