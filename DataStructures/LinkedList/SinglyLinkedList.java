package assign06;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This generic class simulates a singly linked list. It implements a given List interface to include all necessary methods for managing a singly linked list, such as 
 * deleting nodes, removing nodes, adding nodes, etc. Includes nested classes of Node that handles creation of each node in the list as well as an iterator that traverses
 * through the linked list. 
 * 
 * @author Kira Halls
 *
 * @param <T> This ensures the generic nature of the class
 */
public class SinglyLinkedList<T> implements List<T> {
	private Node head;
	private int size;
	
	/**
	 * The default constructor for the singly linked list
	 */
	public SinglyLinkedList() {
		size = 0;
		head = null;
	}

	/**
	 *This method insert specified element at the beginning of the linked list and udpates the necessary node references
	 *
	 * @param element The element that will be inserted into the linked list
	 */
	@Override
	public void insertFirst(T element) {
		if (head != null) {
			Node tempNodeHolder = head;
			head = new Node(element);
			head.next = tempNodeHolder;
		}
		else {
			head = new Node(element);
		}
		size++;
	}

	/**
	*This method inserts the specified element at the specified index. 
	*
	* @param index Which index we want to insert our new element from
	* @param element The element that will be inserted into the linked list
	*/
	@Override
	public void insert(int index, T element) throws IndexOutOfBoundsException {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}
		else if (index == 0) {
			this.insertFirst(element);
		}
		else {
			Node currentNode = head;
			for (int i = 0; i < index - 1; i++) {
				currentNode = currentNode.getNextNode();
			} 
			Node tempNodeHolder = currentNode.getNextNode();
			Node insertedNode = new Node(element);
			currentNode.setNextNode(insertedNode);
			insertedNode.setNextNode(tempNodeHolder);
			size++;
		}
	}

	/**
	 * This method takes and returns the first item of the linked list
	 *
	 * @return the first item of the linked list
	 *
	 */
	@Override
	public T getFirst() throws NoSuchElementException {
		if (size <= 0) {
			throw new NoSuchElementException();
		}
		return head.getData();
	}

	/**
	 *This method gets the node at the specified index. 
	 *
	 *@return the node at teh specified index
	 */
	@Override
	public T get(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		Node currentNode = head;
		for (int i = 0; i < index; i++) {
			currentNode = currentNode.next;
		}
		return currentNode.getData();
	}

	/**
	 *This method deletes the first node in the list and sets the head variable to be the next node in the list (the new head)
	 *
	 *@return the value contained in the deleted node
	 */
	@Override
	public T deleteFirst() throws NoSuchElementException {
		if (size <= 0) {
			throw new NoSuchElementException();
		}
		T deletedValue = head.getData();
		head = head.getNextNode();
		size--;
		return deletedValue;
	}

	/**
	 *This method deletes the node at the specified index and resets the references of the previous node to accurately reference the node to 
	 *the right of the deleted node.
	 *
	 *@return the value contained in the deleted node
	 */
	@Override
	public T delete(int index) throws IndexOutOfBoundsException {
		T deletedValue;
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException();
		}
		else if (index == 0) {
			deletedValue = this.deleteFirst();
		}
		else {
			Node currentNode = head;
			for (int i = 0; i < index - 1; i++) {
				currentNode = currentNode.next;
			} 
			deletedValue = currentNode.getNextNode().getData();
			currentNode.setNextNode(currentNode.next.next);
			size--;
		}
		return deletedValue;
	}

	/**
	 *This method returns the index of the first occurrence of the specified element in the linked list. 
	 *
	 *@return the index of the first occurrence of the value or -1 if the value is not contained in the list
	 */
	@Override
	public int indexOf(Object element) {
		Node currentNode = head;
		for (int i = 0; i < size(); i++) {
			if (currentNode.getData().equals(element)) {
				return i;
			}
			currentNode = currentNode.next;
		}
		return -1;
	}

	/**
	 *This method returns the size of the list
	 *
	 *@return the size of the list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 *This method checks if the current list is empty
	 *
	 *@return true if the list contains no elements/nodes, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;
	}

	/**
	 *This method clears the current list by setting the head node to null, thereby losing all other references to nodes in the list 
	 *and allowing the user to create a new head node with a fresh list
	 */
	@Override
	public void clear() {
		head = null;
		size = 0;
	}

	/**
	 * This method takes the existing linked list and converts it into an array.
	 *
	 * @return our linked list in the form of an array
	 */
	@Override
	public Object[] toArray() {
		Object[] linkedListArray = new Object[size];
		Node currentNode = this.head;
		for (int i = 0; i < size; i++) {
			linkedListArray[i] = currentNode.getData();
			currentNode = currentNode.getNextNode();
		}
		return linkedListArray;
	}

	/**
	 * This method creates a new iterator for the linked list
	 *
	 * @return the newly created iterator
	 */
	@Override
	public Iterator<T> iterator() {
		LinkedListIterator iterator = new LinkedListIterator();
		return iterator;
	}
	
	/**
	 *This overridden method takes a list and converts it to a string. It aids in testing. 
	 *
	 *@return the string version of the list
	 */
	@Override
	public String toString() {
		String listString = "{";
		Object[] objectArray = this.toArray();
		for (Object value : objectArray) {
			listString += value + ", ";
		}
		listString += "}";
		return listString;
	}
	
	
	/**
	 * This generic nested class handles the creation of new nodes for the specified singly linked list. It creates a new node with 
	 * a given value that it holds as well as a reference to the next node in the list, if applicable. It also contains methods 
	 * to set and remove that reference to allow the user to change the singly linked list. 
	 *
	 * @param <T> This ensures the generic nature of the class
	 */
	private class Node {
		private T data;
		private Node next;
		
		/**
		 * The constructor that creates a new node. It reassigns the instance variable data
		 * to the value that is passed into the constructor. Does not automatically assign the 
		 * next Node to allow for the possibility of it being a tail node.
		 * 
		 * @param value The value of the node, the data that the node will hold.
		 */
		public Node(T value) {
			data = value;
		}
		
		/**
		 * This method allows the user to set the next node to the node passed in as a parameter
		 * This is a separate method from the constructor to allow the value of the next node to change 
		 * 
		 * @param nextNode The new node that this node will link to
		 */
		public void setNextNode(Node nextNode) {
			next = nextNode;
		}
		
		/**
		 * This method returns the value held in the specified node's data. This allows the user to actually get the value 
		 * that is stored inside this node. 
		 * 
		 * @return data, or the value contained in the node
		 */
		public T getData() {
			return data;
		}
		
		/**
		 * This method returns the next node that is referenced by the specified node. This allows the user to traverse through
		 * the linked list properly. 
		 * 
		 * @return The next node in the linked list, the node referenced by the current node
		 */
		public Node getNextNode() {
			return next;
		}
	}
	
	
	/**
	 * This generic nested class creates a new iterator to traverse the specified linked list. It keeps track of the current node, the previous node, 
	 * as well as the current index and whether or not we can remove a node (we can only remove a node if we have called next() since our last call 
	 * to remove()). It contains methods to check if we have another node in the list, a method to get the next node in the list, and a method to remove
	 * the last seen node in the list. 
	 * 
	 * @author Kira Halls
	 *
	 * @param <T> This ensures the generic nature of the class
	 */
	protected class LinkedListIterator implements Iterator<T> {
		private int currentIndex = 0; //start at 0 so the first call to next() can start with first value
		private Node currentNode = head; //the value the iterator is pointing at, the one last seen by the user
		private Node previousNode; //points to the node before the current Node. Specifically for use in remove()
		private boolean removable = false; //keeps track of whether or not next() has been called before remove()
		
		/**
		 * This is the constructor that creates a new ListIterator
		 */
		public LinkedListIterator() {}

		/**
		 *This method checks if we have another node in the list and returns true if we have another node, false if we do not
		 *
		 *@return false if there are no more nodes in the list, true if there is another node 
		 */
		public boolean hasNext() {
			if (currentIndex < size ) {
				return true;
			}
			return false;
		}

		/**
		 *This method gets the next node in the list. It also updates the variable letting us know whether or not we can remove 
		 *a node, specifically setting it to allow removal. 
		 *
		 *@return the value contained inside the new node
		 */
		public T next() throws NoSuchElementException {
			if (!(this.hasNext())) {
				throw new NoSuchElementException();
			}
			else {
				if (currentIndex == 0) {
					removable = true;
					currentIndex++;
					return currentNode.getData();
				}
				else {
					currentIndex++;
					previousNode = currentNode;
					currentNode = currentNode.getNextNode();
					removable = true;
					return currentNode.getData();
				}
			}
		}
		
		/**
		 *This method removes the last seen node in the list. Removal is only allowed if we have called the next() method since our 
		 *last call to remove(). It also updates the size of the list and resets the references of the previous node to accurately reflect the removal
		 *of said node. It also resets the variable that checks if we are allowed to remove a node to false.
		 * Throws an IllegalStateException if we have not called next() since our last call to remove().
		 */
		public void remove() {
			if (removable == true) {
				if (previousNode != null) {
					currentNode = currentNode.getNextNode();
					previousNode.setNextNode(currentNode);
					removable = false;
					size--;
				}
				else {
					currentNode = currentNode.getNextNode();
					head = currentNode;
					removable = false;
					size--;
				}
			}
			else {
				throw new IllegalStateException();
			}
		}
	}
}
