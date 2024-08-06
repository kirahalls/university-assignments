package assign10;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a binary max heap using a backing array, with four different constructors,
 * two with a comparator and/or a list. Provides methods to add, extractMax, peek,
 * convert to array, check if empty, and to clear.
 *
 * @author Nandini Goel and Kira Halls
 * @version Nov 27, 2022
 */
public class BinaryMaxHeap<E> implements PriorityQueue<E> {

    // INSTANCE VARIABLES
    private E[] heap;
    private int size;
    private Comparator<? super E> cmp;

    /**
     * Constructor for a blank Binary Max Heap.
     */
    @SuppressWarnings("unchecked")
    public BinaryMaxHeap() {
        heap = (E[]) new Object[10];
        size = 0;
    }

    /**
     * Constructor where elements are ordered using the provided Comparator object.
     *
     * @param cmp provided Comparator object.
     */
    @SuppressWarnings("unchecked")
    public BinaryMaxHeap(Comparator<? super E> cmp) {
        heap = (E[]) new Object[10];
        size = 0;
        this.cmp = cmp;
    }

    /**
     * Constructor where elements are added to a Binary Max Heap from a list.
     *
     * @param list list of items to add to a Binary Max Heap
     */
    @SuppressWarnings("unchecked")
    public BinaryMaxHeap(List<? extends E> list) {
        if (list.size() <= 0) {
            heap = (E[]) new Object[10];
        } else {
            heap = (E[]) new Object[list.size()];
        }
        size = 0;
        for (int i = 0; i < list.size(); i++) {
            heap[i] = list.get(i);
            percolateUp(i);
            size++;
        }
    }

    /**
     * Constructor where elements are added to a Binary Max Heap from a list and using a comparator.
     *
     * @param list list of items to add to a Binary Max Heap
     * @param cmp  Comparator object to compare generic elements
     */
    @SuppressWarnings("unchecked")
    public BinaryMaxHeap(List<? extends E> list, Comparator<? super E> cmp) {
        if (list.size() == 0) {
            heap = (E[]) new Object[10];
        } else {
            heap = (E[]) new Object[list.size()];
        }
        size = 0;
        this.cmp = cmp;
        for (int i = 0; i < list.size(); i++) {
            size++;
            heap[i] = list.get(i);
            percolateUp(i);
        }
    }

    /**
     * Adds the given item to this priority queue.
     * O(1) in the average case, O(log N) in the worst case
     *
     * @param item E element we want to add to heap
     */
    @Override
    public void add(E item) {
        if (size < heap.length) {
            heap[size] = item;
            percolateUp(size);
            size++;
        } else {
            E[] temp = heap;
            heap = (E[]) new Object[size * 2];
            for (int i = 0; i < temp.length; i++) {
                heap[i] = temp[i];
            }
            heap[size] = item;
            percolateUp(size);
            size++;
        }
    }

    /**
     * This helper method aids in ensuring proper order of BMH.
     * If a child node is more than the parent node, then they switch. Continues
     * switching until the current node is less than the parent node.
     *
     * @param currIndex index of current node in BMH
     */
    private void percolateUp(int currIndex) {
        int parentIndex = getParentIndex(currIndex);
        if (cmp != null) {
            while (cmp.compare(heap[currIndex], heap[parentIndex]) > 0) {
                E temp = heap[currIndex];
                heap[currIndex] = heap[parentIndex];
                heap[parentIndex] = temp;
                currIndex = parentIndex;
                parentIndex = getParentIndex(currIndex);
            }
        } else {
            while (((Comparable<? super E>) heap[currIndex]).compareTo(heap[parentIndex]) > 0) {
                E temp = heap[currIndex];
                heap[currIndex] = heap[parentIndex];
                heap[parentIndex] = temp;
                currIndex = parentIndex;
                parentIndex = getParentIndex(currIndex);
            }
        }
    }

    /**
     * This helper method provides the index of the largest child for a given parent index.
     * It compares between the left and right children and returns the index of whichever is larger.
     * Returns -1 if the parent has no children.
     *
     * @param parentIndex The parent index of the two children being compared
     * @return the index of the larger child, left or right, or -1 if the parent has no children
     */
    private int getLargestChildIndex(int parentIndex) {
        int leftChildIndex = getLeftChildIndex(parentIndex);
        int rightChildIndex = getRightChildIndex(parentIndex);
        int largestChildIndex;
        if (cmp != null) {
            if (leftChildIndex < size && rightChildIndex < size) {
                if (cmp.compare(heap[leftChildIndex], heap[rightChildIndex]) < 0) {
                    largestChildIndex = rightChildIndex;
                } else {
                    largestChildIndex = leftChildIndex;
                }
            } else if (leftChildIndex < size && rightChildIndex >= size) {
                largestChildIndex = leftChildIndex;
            } else {
                return -1;
            }
        } else {
            if (leftChildIndex < size && rightChildIndex < size) {
                if (((Comparable<? super E>) heap[leftChildIndex]).compareTo(heap[rightChildIndex]) < 0) {
                    largestChildIndex = rightChildIndex;

                } else {
                    largestChildIndex = leftChildIndex;
                }
            } else if (leftChildIndex < size && rightChildIndex >= size) {
                largestChildIndex = leftChildIndex;
            } else {
                return -1;
            }
        }
        return largestChildIndex;
    }

    /**
     * This helper method ensures proper order of the binary max heap. If the parent node is less than
     * one or both of its children, it swaps with the larger of the two children until it is no longer
     * less than either of its child nodes.
     *
     * @param currIndex The index of the item currently being percolated
     */
    private void percolateDown(int currIndex) {
        int largestChildIndex = getLargestChildIndex(currIndex);
        if (largestChildIndex >= 0) {
            if (cmp != null) {
                while (largestChildIndex >= 0 && cmp.compare(heap[currIndex], heap[largestChildIndex]) < 0) {
                    E temp = heap[currIndex];
                    heap[currIndex] = heap[largestChildIndex];
                    heap[largestChildIndex] = temp;
                    currIndex = largestChildIndex;
                    largestChildIndex = getLargestChildIndex(currIndex);
                }
            } else {
                while (largestChildIndex >= 0 && ((Comparable<? super E>) heap[currIndex]).compareTo(heap[largestChildIndex]) < 0) {
                    E temp = heap[currIndex];
                    heap[currIndex] = heap[largestChildIndex];
                    heap[largestChildIndex] = temp;
                    currIndex = largestChildIndex;
                    largestChildIndex = getLargestChildIndex(currIndex);
                }
            }
        }
    }

    /**
     * This helper method returns the index of the left child of the given node
     *
     * @param i The index of the parent node
     * @return The index of the left child
     */
    private int getLeftChildIndex(int i) {
        return (i * 2) + 1;
    }

    /**
     * This helper method returns the index of the right child of the given node
     *
     * @param i The index of the parent node
     * @return The index of the right child
     */
    private int getRightChildIndex(int i) {
        return (i * 2) + 2;
    }

    /**
     * This helper method returns the index of the parent of the given node
     *
     * @param i The index of the child node
     * @return The index of the parent
     */
    private int getParentIndex(int i) {
        return (i - 1) / 2;
    }

    /**
     * Returns, but does not remove, the maximum item this priority queue.
     * O(1)
     *
     * @return the maximum item
     * @throws NoSuchElementException if this priority queue is empty
     */
    @Override
    public E peek() throws NoSuchElementException {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        return heap[0];
    }

    /**
     * Returns and removes the maximum item this priority queue.
     * O(log N)
     *
     * @return the maximum item
     * @throws NoSuchElementException if this priority queue is empty
     */
    @Override
    public E extractMax() throws NoSuchElementException {
        if (this.isEmpty()) {
            throw new NoSuchElementException();
        }
        E max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        percolateDown(0);
        return max;
    }

    /**
     * Returns the number of items in this priority queue.
     * O(1)
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this priority queue is empty, false otherwise.
     * O(1)
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Empties this priority queue of items.
     * O(1)
     */
    @Override
    public void clear() {
        heap = (E[]) new Object[10];
        size = 0;
    }

    /**
     * Creates and returns an array of the items in this priority queue,
     * in the same order they appear in the backing array.
     * O(N)
     * <p>
     * (NOTE: This method is needed for grading purposes. The root item
     * must be stored at index 0 in the returned array, regardless of
     * whether it is in stored there in the backing array.)
     */
    @Override
    public Object[] toArray() {
        Object[] heapCopy = new Object[size];
        for (int i = 0; i < size; i++) {
            heapCopy[i] = heap[i];
        }
        return heapCopy;
    }
}