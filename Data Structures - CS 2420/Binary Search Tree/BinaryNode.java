package assign07;

import java.util.ArrayList;

/**
 * Represents a generically-typed binary tree node. Each binary node contains
 * data, a reference to the left child, and a reference to the right child.
 * 
 * @author Erin Parker and Kira Halls and Nandini Goel
 * @version October 23, 2022
 */
public class BinaryNode<Type extends Comparable<? super Type>>{

	private Type data;

	private BinaryNode<Type> leftChild;

	private BinaryNode<Type> rightChild;
	
	private BinaryNode<Type> parentNode;

	public BinaryNode(Type data, BinaryNode<Type> leftChild, BinaryNode<Type> rightChild) {
		this.data = data;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.parentNode = null;
	}

	/**
	 * A constructor for a new binary Node. It contains the data passed into this constructor as a parameter.
	 * Calls the three parameter constructor that also assigns the left and right child Nodes
	 * 
	 * @param data The data this node holds.
	 */
	public BinaryNode(Type data) {
		this(data, null, null);
	}
	
	/**
	 * This method attaches a new node containing the item parameter to the current set of Binary Nodes. It assigns parent and children nodes
	 * according to where it belongs on the tree, and ensures it does not add the node if the item is a duplicate. Returns true if it was added
	 * and false if it could not be added
	 * 
	 * @param item The item to be contained in the node added to the set
	 * @return true if added to the set, false if not
	 */
	public boolean add(Type item) {
		boolean added = false;
		if (this.contains(item)) {
			return added;
		}
		if (this.data == null) { //this is used if we are trying to add to an empty list, will only reach this statement if the root is null
			this.data = item;
			added = true;
		} else {
			if (item.compareTo(this.data) < 0) {
				if (this.leftChild != null) {
					added = this.leftChild.add(item);
				} else if (this.leftChild == null) {
					this.setLeftChild(new BinaryNode<Type>(item));
					this.leftChild.setParentNode(this);
					added = true;
				}
			} else if (item.compareTo(this.data) > 0 )	{
				if (this.rightChild != null) {
					added = this.rightChild.add(item);
				} else if (this.rightChild == null) {
					this.setRightChild(new BinaryNode<Type>(item));
					this.rightChild.setParentNode(this);
					added = true;
				}
			}
		}
		return added;
	}
	
	/**
	 * This checks to see if the current set of Binary nodes contains the item passed in as a parameter. 
	 * 
	 * @param item The item to check for
	 * @return true if the item is contained in a node in the set, false if not
	 */
	public boolean contains(Type item) {
		if (this.data == null) {
			return false;
		}
		else if (this.data.equals(item)) {
			return true;
		} else {
			if (item.compareTo(this.data) < 0) {
				if (this.leftChild != null) {
					return (this.leftChild.contains(item));
				} else {
					return false;
				}
			} else if (item.compareTo(this.getData()) > 0) {
				if (this.rightChild != null) {
					return (this.rightChild.contains(item));
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * The getter for the data contained in the node 
	 * 
	 * @return the node data
	 */
	public Type getData() {
		return data;
	}

	/**
	 * The setter for the data contained in the node 
	 * 
	 * @param data - the node data to be set
	 */
	public void setData(Type data) {
		this.data = data;
	}

	/**
	 * The getter for getting the left child of the current node 
	 * 
	 * @return reference to the left child node
	 */
	public BinaryNode<Type> getLeftChild() {
		return leftChild;
	}

	/**
	 * The setter for setting the left child of the current node. Sets the left child to the new node passed in
	 * as a parameter.
	 * 
	 * @param leftChild - reference of the left child node to be set
	 */
	public void setLeftChild(BinaryNode<Type> leftChild) {
		this.leftChild = leftChild;
	}

	/**
	 * The getter for the right child of the current Node 
	 * 
	 * @return reference to the right child node
	 */
	public BinaryNode<Type> getRightChild() {
		return rightChild;
	}

	/**
	 * The setter for the right child of the current Node. Sets the right child to be the new node passed in as 
	 * as parameter. 
	 * 
	 * @param rightChild - reference of the right child node to be set
	 */
	public void setRightChild(BinaryNode<Type> rightChild) {
		this.rightChild = rightChild;
	}

	/**
	 * The getter for the leftmost node in the set, or the node containing the data with the smallest/least value
	 * 
	 * @return reference to the leftmost node in the binary tree rooted at this node
	 */
	public BinaryNode<Type> getLeftmostNode() {
		if (this.leftChild == null) {
			return this;
		}
		return leftChild.getLeftmostNode();
	}

	/**
	 * The getter for the rightmost node in the set, or the node containing the data with the largest value
	 * 
	 * @return reference to the rightmost node in the binary tree rooted at this node
	 */
	public BinaryNode<Type> getRightmostNode() {
		if (this.rightChild == null) {
			return this;
		}
		return rightChild.getRightmostNode();
	}
	
	/**
	 * The setter method to set the parent node of the current node to be the node passed in as a parameter
	 * 
	 * @param parent The reference to the new parent node of the node calling the method
	 */
	public void setParentNode(BinaryNode<Type> parent) {
		this.parentNode = parent;
	}
	
	/**
	 * The getter method to return the parent node of the node calling the method. 
	 * 
	 * @return The reference to the parent node of the current node
	 */
	public BinaryNode<Type> getParentNode() {
		return this.parentNode;
	}
	
	/**
	 * This method creates and returns an ordered array of the values of the nodes in the set 
	 * 
	 * @param array The array containing the values of the nodes in order
	 */
	public void inOrderSorted(ArrayList<Type> array) {
		if (!(this.getLeftChild() == null)) {
			this.getLeftChild().inOrderSorted(array);
		} 
		array.add(data);
		if (this.getRightChild() != null) {
			this.getRightChild().inOrderSorted(array);
		}
	}
}
