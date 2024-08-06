package assign07;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * This class creates a Binary Search Tree using Binary Nodes to contain the values inserted. It has a root and 
 * contains methods to add new values, remove values, check for existence of values, and methods to clear the tree. 
 * Each inserted node must follow the proper order and organization of a binary search tree. 
 * 
 * @author Kira Halls and Nandini Goel
 *
 * @param <Type> Ensures the generic nature of the class
 */
public class BinarySearchTree<Type extends Comparable<? super Type>> implements SortedSet<Type> {
	private BinaryNode<Type> root;
	private int elementCount;
	
	/**
	 * This is a constructor for the BinarySearchTree. It creates a new tree with a root Node set to null and it has a elementCount of zero.
	 */
	public BinarySearchTree() {
		root = null;
		elementCount = 0;
	}

	/**
	 * Ensures that this set contains the specified item.
	 * 
	 * @param item - the item whose presence is ensured in this set
	 * @return true if this set changed as a result of this method call (that is, if
	 *         the input item was actually inserted); otherwise, returns false
	 */
	@Override
	public boolean add(Type item) {
		boolean added = false;
		BinaryNode<Type> tempNode = root;
		if (tempNode == null) {
			root = new BinaryNode<Type>(item);
			tempNode = root;
			elementCount++;
			added = true;
		} else if (!(this.contains(item))) {
				added = tempNode.add(item);
				elementCount++;
		}
		return added;
	}

	/**
	 * Ensures that this set contains all items in the specified collection.
	 * 
	 * @param items - the collection of items whose presence is ensured in this set
	 * @return true if this set changed as a result of this method call (that is, if
	 *         any item in the input collection was actually inserted); otherwise,
	 *         returns false
	 */
	@Override
	public boolean addAll(Collection<? extends Type> items) {
		boolean added = false;
		for(Type item : items) {
			if(!this.contains(item)) {
				added = this.add(item);
			}
		}
		return added;
	}

	/**
	 * Removes all items from this set. The set will be empty after this method
	 * call.
	 */
	@Override
	public void clear() {
		root = null;
		elementCount = 0;
	}

	/**
	 * Determines if there is an item in this set that is equal to the specified
	 * item.
	 * 
	 * @param item - the item sought in this set
	 * @return true if there is an item in this set that is equal to the input item;
	 *         otherwise, returns false
	 */
	@Override
	public boolean contains(Type item) {
		if(this.root == null) {
			return false;
		}
		return this.root.contains(item);
	}

	/**
	 * Determines if for each item in the specified collection, there is an item in
	 * this set that is equal to it.
	 * 
	 * @param items - the collection of items sought in this set
	 * @return true if for each item in the specified collection, there is an item
	 *         in this set that is equal to it; otherwise, returns false
	 */
	@Override
	public boolean containsAll(Collection<? extends Type> items) {
		for(Type item : items) {
			if(!this.contains(item)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the first (i.e., smallest) item in this set.
	 * 
	 * @throws NoSuchElementException if the set is empty
	 */
	@Override
	public Type first() throws NoSuchElementException {
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return root.getLeftmostNode().getData();
	}

	/**
	 * Returns true if this set contains no items.
	 */
	@Override
	public boolean isEmpty() {
		if (elementCount == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the last (i.e., largest) item in this set.
	 * 
	 * @throws NoSuchElementException if the set is empty
	 */
	@Override
	public Type last() throws NoSuchElementException {
		if (this.root == null) {
			throw new NoSuchElementException();
		}
		return root.getRightmostNode().getData();
	}

	/**
	 * Ensures that this set does not contain the specified item.
	 * 
	 * @param item - the item whose absence is ensured in this set
	 * @return true if this set changed as a result of this method call (that is, if
	 *         the input item was actually removed); otherwise, returns false
	 */
	@Override
	public boolean remove(Type item) {
		if (this.contains(item)) {
			BinaryNode<Type> removedNode = findNode(item, root);
			BinaryNode<Type> successorNode = successor(removedNode);
			
			if(elementCount == 1) {
				this.root = null;
				elementCount--;
			} else if(removedNode.equals(root)) {
				root = successorNode;
				root.setParentNode(null);
				if (!(successorNode.equals(removedNode.getRightChild()))) {
					successorNode.setRightChild(removedNode.getRightChild());
				} 
				if (!(successorNode.equals(removedNode.getLeftChild()))) {
					successorNode.setLeftChild(removedNode.getLeftChild());
				}
				elementCount--;
			} else if(removedNode.getRightChild() == null && removedNode.getLeftChild() == null) {
				BinaryNode<Type> previousNode = removedNode.getParentNode();
				if (item.compareTo(previousNode.getData()) < 0) {
					previousNode.setLeftChild(null);
				} else {
					previousNode.setRightChild(null);
				}
				elementCount--;
			} else {
				BinaryNode<Type> previousNode = removedNode.getParentNode();
				if(item.compareTo(previousNode.getData()) < 0) {
					previousNode.setLeftChild(successorNode);
					successorNode.setParentNode(previousNode);
					if (!(successorNode.equals(removedNode.getRightChild()))) {
						successorNode.setRightChild(removedNode.getRightChild());
					} 
					if (!(successorNode.equals(removedNode.getLeftChild()))) {
						successorNode.setLeftChild(removedNode.getLeftChild());
					}
				} else {
					previousNode.setRightChild(successorNode);
					successorNode.setParentNode(previousNode);
					if (!(successorNode.equals(removedNode.getRightChild()))) {
						successorNode.setRightChild(removedNode.getRightChild());
					} 
					if (!(successorNode.equals(removedNode.getLeftChild()))) {
						successorNode.setLeftChild(removedNode.getLeftChild());
					} 
				}
				elementCount--;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * This is a helper method for the remove() method. This traverses the tree to find the node we want to remove
	 *  and returns it for use in the remove() method. 
	 *  
	 * @param item The item we want to remove
	 * @param currentNode The node we start our search on
	 * @return The node that contains the item we want to remove
	 */
	public BinaryNode<Type> findNode(Type item, BinaryNode<Type> currentNode) {
		if (currentNode.getData().equals(item)) {
			return currentNode;
		} else {
			if (currentNode.getData().compareTo(item) < 0) {
				currentNode = currentNode.getRightChild();
			} else {
				currentNode = currentNode.getLeftChild();
			}
			return findNode(item, currentNode);
		}
	}
	
	/**
	 * This is a helper method for the remove() method. This traverses the tree to find the node that will replace the node 
	 * we are removing. The successor node will either be the largest node in the leftmost tree or the smallest node in the 
	 * rightmost tree
	 * 
	 * @param node The node we will be removing, the node to be replaced
	 * @return The node that will replace our removed Node
	 */
	public BinaryNode<Type> successor(BinaryNode<Type> node){
		if (node.getRightChild() != null) {
			return node.getRightChild().getLeftmostNode();
		} else if (node.getLeftChild() != null) {
			return node.getLeftChild().getRightmostNode();
		}
		return null;
	}

	/**
	 * Ensures that this set does not contain any of the items in the specified
	 * collection.
	 * 
	 * @param items - the collection of items whose absence is ensured in this set
	 * @return true if this set changed as a result of this method call (that is, if
	 *         any item in the input collection was actually removed); otherwise,
	 *         returns false
	 */
	@Override
	public boolean removeAll(Collection<? extends Type> items) {
		int previousSize = this.size();
		for (Type item : items) {
			this.remove(item);
		}
		return previousSize != this.size();
	}

	/**
	 * Returns the number of items in this set.
	 */
	@Override
	public int size() {
		return elementCount;
	}

	/**
	 * Returns an ArrayList containing all of the items in this set, in sorted
	 * order.
	 */
	@Override
	public ArrayList<Type> toArrayList() {
		ArrayList<Type> sortedArrayList = new ArrayList<Type>();
		if (root != null) {
			root.inOrderSorted(sortedArrayList);
		}
		return sortedArrayList;
	}
}
