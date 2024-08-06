package assign02;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class contains tests for LibraryGeneric.
 * 
 * @author Erin Parker and Braden Campbell and Kira Halls
 * @version September 2, 2020
 */
public class LibraryGenericTester {
	
	private LibraryGeneric<String> nameLib;  // library that uses names to identify patrons (holders)
	private LibraryGeneric<PhoneNumber> phoneLib;  // library that uses phone numbers to identify patrons
	private LibraryGeneric<String> medLibName;
	private LibraryGeneric<PhoneNumber> medLibPhone;
	
	@BeforeEach
	void setUp() throws Exception {
		nameLib = new LibraryGeneric<String>();
		nameLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		nameLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		nameLib.add(9780446580342L, "David Baldacci", "Simple Genius");

		phoneLib = new LibraryGeneric<PhoneNumber>();
		phoneLib.add(9780374292799L, "Thomas L. Friedman", "The World is Flat");
		phoneLib.add(9780330351690L, "Jon Krakauer", "Into the Wild");
		phoneLib.add(9780446580342L, "David Baldacci", "Simple Genius");	
		
		medLibName = new LibraryGeneric<String>();
		medLibName.addAll("src/assign02/Mushroom_Publishing.txt");
		
		medLibPhone = new LibraryGeneric<PhoneNumber>();
		medLibPhone.addAll("src/assign02/Mushroom_Publishing.txt");
	}
	
	/**
	 * Testing checkout via holder name
	 */
	@Test
	public void testNameLibCheckout() {
		String patron = "Jane Doe";
		assertTrue(nameLib.checkout(9780330351690L, patron, 1, 1, 2008));
		assertTrue(nameLib.checkout(9780374292799L, patron, 1, 1, 2008));
	}

	/**
	 * Testing lookup via holder name
	 */
	@Test
	public void testNameLibLookup() {
		String patron = "Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<String>> booksCheckedOut = nameLib.lookup(patron);
		
		assertNotNull(booksCheckedOut);
		assertEquals(2, booksCheckedOut.size());
		assertTrue(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
		assertEquals(patron, booksCheckedOut.get(1).getHolder());
	}
	
	/**
	 * Testing Check in via holder Name w/ books checked out
	 */
	@Test
	public void testNameLibCheckin() {
		String patron = "Jane Doe";
		nameLib.checkout(9780330351690L, patron, 1, 1, 2008);
		nameLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertTrue(nameLib.checkin(patron));
	}
	
	/**
	 * Testing Check in via holder name w/ no books checked out
	 */
	@Test
	public void testNameCheckinHolder() {
		assertFalse(nameLib.checkin("Jane Doe"));
	}
	
	/**
	 * Testing lookup via holder name w/ no books checked out
	 */
	@Test
	public void testNameLookupHolder() {
		ArrayList<LibraryBookGeneric <String>> booksCheckedOut = nameLib.lookup("Jane Doe");
		assertNotNull(booksCheckedOut);
		assertEquals(0, booksCheckedOut.size());
	}
	
	/**
	 * Testing checkout w/ holder Name w/ book not in library
	 */
	@Test
	public void testNameCheckout() {
		assertFalse(nameLib.checkout(9781843190004L, "Jane Doe", 1, 1, 2008));
	}

	/**
	 * Testing checkout w/ holder Name w/ book in library
	 */
	@Test
	public void testNameCheckoutInLibrary() {
		assertTrue(nameLib.checkout(9780330351690L, "Jane Doe", 1, 1, 2008));
	}
	
	/**
	 * Testing checkout w/ holder Name w/ book already checked out
	 */
	@Test
	public void testNameCheckoutInLibraryCheckedOut() {
		nameLib.checkout(9781843190004L, "John Doe", 12, 31, 2022);
		assertFalse(nameLib.checkout(9781843190004L, "Jane Doe", 1, 1, 2008));
	}
	
	/**
	 * Testing checkout via phone num
	 */
	@Test
	public void testPhoneLibCheckout() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		assertTrue(phoneLib.checkout(9780330351690L, patron, 1, 1, 2008));
		assertTrue(phoneLib.checkout(9780374292799L, patron, 1, 1, 2008));
	}

	/**
	 * Testing Lookup via phone num
	 */
	@Test
	public void testPhoneLibLookup() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		phoneLib.checkout(9780330351690L, patron, 1, 1, 2008);
		phoneLib.checkout(9780374292799L, patron, 1, 1, 2008);
		ArrayList<LibraryBookGeneric<PhoneNumber>> booksCheckedOut = phoneLib.lookup(patron);
		assertNotNull(booksCheckedOut);
		assertEquals(2, booksCheckedOut.size());
		assertTrue(booksCheckedOut.contains(new Book(9780330351690L, "Jon Krakauer", "Into the Wild")));
		assertTrue(booksCheckedOut.contains(new Book(9780374292799L, "Thomas L. Friedman", "The World is Flat")));
		assertEquals(patron, booksCheckedOut.get(0).getHolder());
		assertEquals(patron, booksCheckedOut.get(1).getHolder());
	}

	/**
	 * Testing checkin via phone num w/ books checked out
	 */
	@Test
	public void testPhoneLibCheckin() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		phoneLib.checkout(9780330351690L, patron, 1, 1, 2008);
		phoneLib.checkout(9780374292799L, patron, 1, 1, 2008);
		assertTrue(phoneLib.checkin(patron));
	}
	
	/**
	 * Testing checkin via phone num w/ no books checked out
	 */
	@Test
	public void testPhoneCheckinHolder() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		assertFalse(phoneLib.checkin(patron));
	}
	
	/**
	 * Testing Look up via phone num w/ no books checked out
	 */
	@Test
	public void testPhoneLookupHolder() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		ArrayList<LibraryBookGeneric <PhoneNumber>> booksCheckedOut = phoneLib.lookup(patron);
		assertNotNull(booksCheckedOut);
		assertEquals(0, booksCheckedOut.size());
	}
	
	/**
	 * Testing checkout via phone num w/ book not in library
	 */
	@Test
	public void testPhoneCheckout() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		assertFalse(phoneLib.checkout(9781843190004L, patron, 1, 1, 2008));
	}

	/**
	 * Testing checkout via phone num w/ book in library
	 */
	@Test
	public void testPhoneCheckoutInLibrary() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		assertTrue(phoneLib.checkout(9780330351690L, patron, 1, 1, 2008));
	}
	
	/**
	 * Testing checkout via phone num w/ book already checked out
	 */
	@Test
	public void testPhoneCheckoutInLibraryCheckedOut() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		PhoneNumber patron1 = new PhoneNumber("801.555.1235");
		phoneLib.checkout(9780330351690L, patron, 12, 31, 2022);
		assertFalse(phoneLib.checkout(9780330351690L, patron1, 1, 1, 2008));
	}
	
	/**
	 * Testing dueDate sort with multiple books but only one overdue - name
	 */
	@Test
	public void testGetOverDueListName() {
		String patron = "Jane Doe";
		String patron1 = "John Doe";
		medLibName.checkout(9781843190004L, patron, 4, 28, 3333);
		medLibName.checkout(9781843190011L, patron, 1, 1, 2008);
		medLibName.checkout(9781843190028L, patron1, 1, 1, 2008);
		medLibName.checkout(9781843190042L, patron1, 12, 5, 2001);
		assertEquals("[9781843190042, Martyn Folkes, \"Bath City Centre Street Map and Guide\"]", medLibName.getOverdueList(1, 1, 2008).toString());
	}
	
	/**
	 * Testing dueDate sort with multiple books w/ multiple over due and sorts oldest first
	 * for overDue books (Does not include books due on specified due date) - name
	 */
	@Test
	public void testGetOverDueListMultipleBooksName() {
		String patron = "Jane Doe";
		String patron1 = "John Doe";
		medLibName.checkout(9781843190004L, patron, 4, 28, 3333);
		medLibName.checkout(9781843190011L, patron, 1, 1, 2007);
		medLibName.checkout(9781843190110L, patron, 1, 1, 2007);
		medLibName.checkout(9781843190028L, patron1, 1, 1, 2008);
		medLibName.checkout(9781843190042L, patron1, 12, 5, 2001);
		assertEquals("[9781843190042, Martyn Folkes, \"Bath City Centre Street Map and Guide\", 9781843190011, Moyra Caldecott, \"The Eye of Callanish\", 9781843190110, David Meade Betts, \"Breaking the Gaze\"]", 
				medLibName.getOverdueList(1, 1, 2008).toString());
	}
	
	/**
	 * Testing dueDate sort with multiple books but only one overdue - phone numbers
	 */
	@Test
	public void testGetOverDueListPhone() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		PhoneNumber patron1 = new PhoneNumber("801.555.1235");
		medLibPhone.checkout(9781843190004L, patron, 4, 28, 3333);
		medLibPhone.checkout(9781843190011L, patron, 1, 1, 2008);
		medLibPhone.checkout(9781843190028L, patron1, 1, 1, 2008);
		medLibPhone.checkout(9781843190042L, patron1, 12, 5, 2001);
		assertEquals("[9781843190042, Martyn Folkes, \"Bath City Centre Street Map and Guide\"]", medLibPhone.getOverdueList(1, 1, 2008).toString());
	}
	
	/**
	 * Testing dueDate sort with multiple books w/ multiple over due and sorts oldest first
	 * for overDue books (Does not include books due on specified due date) - Phone numbers
	 */
	@Test
	public void testGetOverDueListMultipleBooksPhone() {
		PhoneNumber patron = new PhoneNumber("801.555.1234");
		PhoneNumber patron1 = new PhoneNumber("801.555.1235");
		medLibPhone.checkout(9781843190004L, patron, 4, 28, 3333);
		medLibPhone.checkout(9781843190011L, patron, 1, 1, 2007);
		medLibPhone.checkout(9781843190110L, patron, 1, 1, 2007);
		medLibPhone.checkout(9781843190028L, patron1, 1, 1, 2008);
		medLibPhone.checkout(9781843190042L, patron1, 12, 5, 2001);
		assertEquals("[9781843190042, Martyn Folkes, \"Bath City Centre Street Map and Guide\", 9781843190011, Moyra Caldecott, "
				+ "\"The Eye of Callanish\", 9781843190110, David Meade Betts, \"Breaking the Gaze\"]", 
				medLibPhone.getOverdueList(1, 1, 2008).toString());
	}
	
	/**
	 * Testing order by title for medium list
	 */
	@Test
	public void testOrderByTitle() {
		assertEquals("[9781843191230, Mary Lancaster, \"An Endless Exile\", 9781843190042, Martyn Folkes, "
				+ "\"Bath City Centre Street Map and Guide\", 9781843190110, David Meade Betts, \"Breaking the Gaze\", "
				+ "9781843190363, Emma Lorant, \"Cloner\", 9781843190028, Moyra Caldecott, \"Crystal Legends\", "
				+ "9781843190479, Anthony J D Burns, \"Demogorgon Rising\", 9781843190677, Cheryl Jones, \"Herbs for Healthy Skin\", "
				+ "9781843190875, Renee Angers, \"Ice and a Curious Man\", 9781843190998, Helen K Barker, \"Iceni\", "
				+ "9781843190936, Carol E. Meacham, \"Machina Obscura\", 9781843192039, William Fitzmaurice, \"Operation: Sergeant York\", "
				+ "9781843190349, Esme Ellis, \"Pathway Into Sunrise\", 9781843192954, Dennis Radha-Rose, \"The Anxiety Relief Program\", "
				+ "9781843190769, Roger Taylor, \"The Call of the Sword\", 9781843190073, Jen Alexander, \"The Coming of the Third\", "
				+ "9781843190011, Moyra Caldecott, \"The Eye of Callanish\", 9781843190516, Daniel Wyatt, \"The Fuehrermaster\", "
				+ "9781843192701, Moyra Caldecott, \"The Lily and the Bull\", 9781843190394, Kate Clarke, \"The Royal United Hospital\", "
				+ "9781843190400, Jean Fanelli, \"The War Comes to Witham Street\", 9781843193319, Alan Burt Akers, \"Transit to Scorpio\", "
				+ "9781843190004, Moyra Caldecott, \"Weapons of the Wolfhound\", 9781843192022, Roger Taylor, \"Whistler\"]", 
				medLibName.getOrderedByTitle().toString());
	}
	
}
