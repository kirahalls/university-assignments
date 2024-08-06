package assign10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class contains generic static methods for finding the k largest items in a list.
 * 
 * @author Erin Parker & Nandini Goel and Kira Halls
 * @version Nov 27, 2022
 */
public class FindKLargest {
	
	/**
	 * Determines the k largest items in the given list, using a binary max heap and the 
	 * natural ordering of the items.
	 * 
	 * @param items - the given list
	 * @param k - the number of largest items
	 * @return a list of the k largest items, in descending order
	 * @throws IllegalArgumentException if k is negative or larger than the size of the given list
	 */
	public static <E extends Comparable<? super E>> List<E> findKLargestHeap(List<E> items, int k) throws IllegalArgumentException {
		BinaryMaxHeap<E> heap = new BinaryMaxHeap<>(items);
		List<E> kthLargestItems = new ArrayList<E>();
		if(k < 0 || k > heap.size()){
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < k; i++) {
			kthLargestItems.add(heap.extractMax());
		}
		return kthLargestItems;
	}

	/**
	 * Determines the k largest items in the given list, using a binary max heap.
	 * 
	 * @param items - the given list
	 * @param k - the number of largest items
	 * @param cmp - the comparator defining how to compare items
	 * @return a list of the k largest items, in descending order
	 * @throws IllegalArgumentException if k is negative or larger than the size of the given list
	 */
	public static <E> List<E> findKLargestHeap(List<E> items, int k, Comparator<? super E> cmp) throws IllegalArgumentException {
		BinaryMaxHeap<E> heap = new BinaryMaxHeap<>(items, cmp);
		List<E> kthLargestItemsWithCmp = new ArrayList<E>();
		if(k < 0 || k > heap.size()){
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < k; i++) {
			kthLargestItemsWithCmp.add(heap.extractMax());
		}
		return kthLargestItemsWithCmp;
	}

	/**
	 * Determines the k largest items in the given list, using Java's sort routine and the 
	 * natural ordering of the items.
	 * 
	 * @param items - the given list
	 * @param k - the number of largest items
	 * @return a list of the k largest items, in descending order
	 * @throws IllegalArgumentException if k is negative or larger than the size of the given list
	 */
	public static <E extends Comparable<? super E>> List<E> findKLargestSort(List<E> items, int k) throws IllegalArgumentException {
		items.sort(null);
		List<E> kthLargestSortedItems = new ArrayList<E>();
		for (int i = items.size() - 1; i > items.size() - 1 - k; i--) {
			kthLargestSortedItems.add(items.get(i));
		}
		return kthLargestSortedItems;
	}

	/**
	 * Determines the k largest items in the given list, using Java's sort routine.
	 * 
	 * @param items - the given list
	 * @param k - the number of largest items
	 * @param cmp - the comparator defining how to compare items
	 * @return a list of the k largest items, in descending order
	 * @throws IllegalArgumentException if k is negative or larger than the size of the given list
	 */
	public static <E> List<E> findKLargestSort(List<E> items, int k, Comparator<? super E> cmp) throws IllegalArgumentException {
		items.sort(cmp);
		List<E> kthLargestSortedItems = new ArrayList<E>();
		for (int i = items.size() - 1; i > items.size() - 1 - k; i--) {
			kthLargestSortedItems.add(items.get(i));
		}
		return kthLargestSortedItems;
	}
}