package assign06;

import java.util.NoSuchElementException;

/**
 * This generic class mimics a Stack using a singly linked list. The constructor creates a new singly linked list and assigns it to an instance variable,
 * and the class methods use the already implemented LinkedListStack methods to complete their function. 
 * 
 * @author Kira Halls
 *
 * @param <T> This ensures the generic nature of the class
 */
public class LinkedListStack<T> implements Stack<T> {
	private SinglyLinkedList<T> linkedList = new SinglyLinkedList<T>();
	
	/**
	 * This is the LinkedListStack constructor. It creates a new linked list and assigns it to the linkedList instance variable
	 */
	public LinkedListStack() {}

	/**
	*This method removes all of the elements from the stack, or the linked list
	*/
	@Override
	public void clear() {
		linkedList.clear();
	}

	/**
	*This method checks if the stack is empty. 
	*
	*@return false if there are elements in the stack (linked list), true if not
	*/
	@Override
	public boolean isEmpty() {
		if (linkedList.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	*This method returns (but doesn't remove) the element at the top of the stack, aka the last element in the linked list
	*
	*@return the value stored in the last node of the linked list, or the value on top of the stack
	*/
	@Override
	public T peek() throws NoSuchElementException {
		return linkedList.getFirst();
	}

	/**
	*This method retunrs and removes the element at the top of the stack, aka the last element in the linked list
	*
	*@return the element that was removed from the top of the stack, aka the last element in the linked list
	*/
	@Override
	public T pop() throws NoSuchElementException {
		T deletedValue = linkedList.deleteFirst();
		return deletedValue;
	}

    /**
	* This method adds an element to the top of the stack.
	*
	* @param element the object that will be added to the top of the stack
	*/
	@Override
	public void push(T element) {
		linkedList.insertFirst(element);
	}

	/**
	* This method returns the size of the linked list, aka the number of elements in the stack
	*
	* @return the size of the linked list, aka the size of the stack
	*/
	@Override
	public int size() {
		return linkedList.size();
	}
	
	/**
	 *This overridden method takes the linkedList instance variable and converts it to a string to aid in testing
	 */
	@Override
	public String toString() {
		String listString = "{";
		Object[] objectArray = linkedList.toArray();
		for (Object value : objectArray) {
			listString += value + ", ";
		}
		listString += "}";
		return listString;
	}
}
