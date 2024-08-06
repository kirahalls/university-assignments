package assign06;

import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class for the WebBrowser class. Tests all methods to ensure quality and correctness,
 * including edge cases. 
 * 
 * @author Kira Halls
 *
 */
class WebBrowserTest {
	private WebBrowser emptyBrowser, historyBrowser;
	
	@BeforeEach
	void setUp() throws MalformedURLException {
		emptyBrowser = new WebBrowser();
		
		SinglyLinkedList<URL> urlHistory = new SinglyLinkedList<URL>();
		urlHistory.insertFirst(new URL("http://google.com"));
		urlHistory.insertFirst(new URL("http://utah.instructure.com"));
		urlHistory.insertFirst(new URL("http://stackoverflow.com"));
		urlHistory.insertFirst(new URL("http://docs.oracle.com"));
		urlHistory.insertFirst(new URL("http://slcolibrary.org"));
		historyBrowser = new WebBrowser(urlHistory);
	}
	
	/**
	 * This tests the visit() method of WebBrowser. It must clear the forward stack when called, add the old website you were visiting to 
	 * the back stack, and set the currentwebpage to be the new webpage you are visiting. Tested both an empty and a browser with a previous history
	 * to ensure it works with both kinds of browsers. Also tests the size and contents of the back stack to ensure the right website and number of websites were added
	 * 
	 * @throws MalformedURLException throws this exception if the given URL is malformed
	 */
	@Test
	void testVisit() throws MalformedURLException {
		emptyBrowser.visit(new URL("http://google.com"));
		URL currentWebpage = emptyBrowser.getCurrentWebpage();
		assertEquals("http://google.com", currentWebpage.toString());
		assertThrows(NoSuchElementException.class, ()-> emptyBrowser.getForward());
		assertThrows(NoSuchElementException.class, ()-> emptyBrowser.getBack());
		
		historyBrowser.visit(new URL("http://sephora.com"));
		URL currentWebpage1 = historyBrowser.getCurrentWebpage();
		assertEquals("http://sephora.com", currentWebpage1.toString());
		assertEquals(5, historyBrowser.getBack().size());
		assertThrows(NoSuchElementException.class, ()-> historyBrowser.getForward().toString());
		assertEquals("{http://slcolibrary.org, http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", historyBrowser.getBack().toString());
	}
	
	/**
	 * This tests the visit() method with values contained in the forward stack. This must clear the forward stack and complete all other tasks as outlined in the 
	 * above test. 
	 * 
	 * @throws MalformedURLException throws this exception if the given URL is malformed
	 */
	@Test
	void testVisitClearForward() throws MalformedURLException {
		historyBrowser.visit(new URL("http://sephora.com"));
		historyBrowser.back();
		historyBrowser.visit(new URL("http://twitter.com"));
		URL currentWebpage1 = historyBrowser.getCurrentWebpage();
		assertEquals("http://twitter.com", currentWebpage1.toString());
		assertEquals(5, historyBrowser.getBack().size());
		assertThrows(NoSuchElementException.class, ()-> historyBrowser.getForward().toString());
		assertEquals("{http://slcolibrary.org, http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", historyBrowser.getBack().toString());
	}
	
	/**
	 * This tests the back() function. It must add the current website to the forward stack, and set the new current website to be the URL at the top of the back stack. 
	 * Tests the content and size of the forward and back stacks to ensure the proper websites have been added and removed, as well as checking the contents of the current
	 * webpage variable to ensure the proper website was visited. 
	 * 
	 * @throws MalformedURLException throws this exception if the given URL is malformed
	 */
	@Test
	void testBack() throws MalformedURLException {
		emptyBrowser.visit(new URL("http://utah.instructure.com"));
		assertThrows(NoSuchElementException.class, () -> {emptyBrowser.back();});
		
		historyBrowser.visit(new URL("http://youtube.com"));
		historyBrowser.visit(new URL("http://twitter.com"));
		historyBrowser.back();
		assertEquals("http://youtube.com", historyBrowser.getCurrentWebpage().toString());
		assertEquals(5, historyBrowser.getBack().size());
		assertEquals("{http://slcolibrary.org, http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", historyBrowser.getBack().toString());
		assertEquals(1, historyBrowser.getForward().size());
		assertEquals("{http://twitter.com, }", historyBrowser.getForward().toString());
	}
	
	/**
	 * This test the forward() function. It must add the current website to the back stack, and set teh new current website to be the URL at the top of the forward stack. 
	 * Tests the content and size of the forward and back stacks to ensure the proper websites have been added and removed, as well as checking the contents of the current
	 * webpage variable to ensure the proper website was visited 
	 * 
	 * @throws MalformedURLException throws this exception if the given URL is malformed
	 */
	@Test
	void testForward() throws MalformedURLException {
		emptyBrowser.visit(new URL("http://slcolibrary.org"));
		assertThrows(NoSuchElementException.class, () -> {emptyBrowser.forward();});
		
		historyBrowser.visit(new URL("http://youtube.com"));
		historyBrowser.visit(new URL("http://twitter.com"));
		historyBrowser.back();
		historyBrowser.forward();
		assertEquals("http://twitter.com", historyBrowser.getCurrentWebpage().toString());
		assertThrows(NoSuchElementException.class, ()-> historyBrowser.getForward());
		assertEquals("{http://youtube.com, http://slcolibrary.org, http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", historyBrowser.getBack().toString());
		assertEquals(6, historyBrowser.getBack().size());
	}
	
	/**
	 * This tests the history() function. It must return a list of every website visited from most recently visited to least recently visited including the current webpage
	 * Websites on the forward stack are not included. Tests the size and contents of the returned history Linked List to ensure the correct number, size, and order of 
	 * websites were returned 
	 * 
	 * @throws MalformedURLException throws this exception if the given URL is malformed
	 */
	@Test
	void testHistory() throws MalformedURLException {
		assertThrows(NoSuchElementException.class, ()-> emptyBrowser.getForward());
		assertThrows(NoSuchElementException.class, ()-> emptyBrowser.getBack());
		
		SinglyLinkedList<URL> history = historyBrowser.history();
		assertEquals("{http://slcolibrary.org, http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", history.toString());
		assertEquals(5, historyBrowser.history().size());
		assertEquals("{http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", historyBrowser.getBack().toString());
		assertThrows(NoSuchElementException.class, ()-> historyBrowser.getForward());
	}
	
	/**
	 * This tests the history() function. It must return a list of every website visited from the most recently visited to least recently visited including the current
	 * webpage. This test specifically tests a forward stack that has actual contents to ensure they are not added. Also tests teh size and contents of the returned history
	 * linked list to ensure the correct number, size, and order of the websites were returned. Also checks the forward stack to ensure it did not alter it in any way. 
	 * 
	 * @throws MalformedURLException throws this exception if the given URL is malformed
	 */
	@Test
	void testHistoryWithForward() throws MalformedURLException {
		historyBrowser.visit(new URL("http://youtube.com"));
		historyBrowser.visit(new URL("http://twitter.com"));
		historyBrowser.back();
		historyBrowser.back();
		SinglyLinkedList<URL> history = historyBrowser.history();
		assertEquals("{http://slcolibrary.org, http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", history.toString());
		assertEquals(5, historyBrowser.history().size());
		assertEquals("{http://docs.oracle.com, http://stackoverflow.com, http://utah.instructure.com, http://google.com, }", historyBrowser.getBack().toString());
		assertEquals(2, historyBrowser.getForward().size());
		assertEquals("{http://youtube.com, http://twitter.com, }", historyBrowser.getForward().toString());
	}
}
