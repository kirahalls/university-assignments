package assign06;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import assign06.SinglyLinkedList.LinkedListIterator;

/**
 * This class simulates a web browser. It keeps track of every website the user visits and stores them in two LinkedListStacks to track 
 * if it was a previously visited page or a page they visited in the 'future' aka if they click the back button and want to return to the page 
 * they had previously been at. 
 * 
 * @author Kira Halls
 *
 */
public class WebBrowser {
	private LinkedListStack<URL> back = new LinkedListStack<URL>();
	private LinkedListStack<URL> forward = new LinkedListStack<URL>();
	private URL currentWebpage;

	/**
	 * This is the constructor for the WebBrowser with no previous website history. It sets the back and forward stacks to null and creates an iterator 
	 * to traverse through the back stack for use in the history() method.
	 */
	public WebBrowser() {}

	/**
	 * This constructor creates a new WebBrowser with a previous website history. It adds each website from history to the back stack in order 
	 * from most recently visited to least recently visited. It also creates an iterator for the back stack to use in the history() method.
	 * 
	 * @param history a singlyLinkedList of URLs containing the user's previous history.
	 */
	public WebBrowser(SinglyLinkedList<URL> history) {
		for (int i = history.size() - 1; i >= 0; i--) {
			back.push(history.get(i));
		}
		currentWebpage = back.pop();
	}
	
	/**
	 * This method simulates a new visit to a webpage by setting the currentWebpage to the given URL and clearing the forward stack
	 * 
	 * @param webpage the webpage the user is visiting
	 */
	public void visit(URL webpage) {
		if (currentWebpage != null) {
			back.push(currentWebpage);
		}
		forward.clear();
		currentWebpage = webpage;
	}
	
	/**
	 * This method simulates using the back button and returns the URL most recently visited. Returns a NoSuchElementException if back is empty
	 * 
	 * @return the most recently visited URL
	 * @throws NoSuchElementException throws this exception if back is empty
	 */
	public URL back() throws NoSuchElementException {
		if (back.isEmpty()) {
			throw new NoSuchElementException();
		}
		URL previousPage = back.pop();
		forward.push(currentWebpage);
		currentWebpage = previousPage;
		return currentWebpage;
	}
	
	/**
	 * This method simulates using the forward button and returns the URL previously visited. Returns a NoSuchElementException if forward is empty
	 * 
	 * @return the URL previously visited
	 * @throws NoSuchElementException throws this exception if forward is empty
	 */
	public URL forward() throws NoSuchElementException {
		if (forward.isEmpty()) {
			throw new NoSuchElementException();
		}
		URL newWebPage = forward.pop();
		back.push(currentWebpage);
		currentWebpage = newWebPage;
		return currentWebpage;
	}
	
	/**
	 * This method generates a history of URLs visited and stores them in the linked list, with the linked list returned when the method is called.
	 * "Forward" links are not included. 
	 *
	 * @return a singly linked list with the most recently visited websites, with the first meaning most recently visited, second on the list being the 
	 *		   second most recently visited, and so on
	 */
	public SinglyLinkedList<URL> history() {
		SinglyLinkedList<URL> history = new SinglyLinkedList<URL>();
		LinkedListStack<URL> temp = new LinkedListStack<URL>();
		int timesToLoop = back.size();
		for (int i = 0; i < timesToLoop; i++) {
			URL currentSite = back.pop();
			history.insert(i, currentSite);
			temp.push(currentSite);
		}
		history.insertFirst(currentWebpage);
		while (!(temp.isEmpty())) {
			back.push(temp.pop());
		}
		return history;
	}
	
	/**
	 * This getter method returns the current webpage to the user. It aids in testing.
	 * 
	 * @return the current webpage
	 */
	public URL getCurrentWebpage() {
		return currentWebpage;
	}
	
	/**
	 * This getter method returns the back stack. It aids in testing.
	 * 
	 * @return the back stack
	 */
	public LinkedListStack<URL> getBack() throws NoSuchElementException {
		if (back.isEmpty()) {
			throw new NoSuchElementException();
		}
		return back;
	}
	
	/**
	 * This getter method returns the forward stack. It aids in testing. 
	 * 
	 * @return the forward stack
	 */
	public LinkedListStack<URL> getForward() throws NoSuchElementException {
		if (forward.isEmpty()) {
			throw new NoSuchElementException();
		}
		return forward;
	}
}
