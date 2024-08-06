package assign03;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 
 * @author Daniel Kopta and Kira Halls and Braden Campbell
 * A utility class for searching, containing just one method (see below).
 *
 */
public class SearchUtil {

	/**
	 * 
	 * @param <T>	The type of elements contained in the list
	 * @param list	An ArrayList to search through. 
	 * 				This ArrayList is assumed to be sorted according 
	 * 				to the supplied comparator. This method has
	 * 				undefined behavior if the list is not sorted. 
	 * @param item	The item to search for
	 * @param cmp	Comparator for comparing Ts or a super class of T
	 * @return		true if the item exists in the list, false otherwise
	 */
	public static <T> boolean binarySearch(ArrayList<T> list, T item, Comparator<? super T> cmp) {
		int currentIndex = list.size() / 2;
		int maxIndex = list.size();
		int lowestIndex = 0;
		for (int i = 0; i < java.lang.Math.log(list.size()) + 1; i++) {
			if (cmp.compare(list.get(currentIndex), item) < 0) {
				lowestIndex = currentIndex;
				currentIndex = (maxIndex + lowestIndex)/2;
			}
			else if (cmp.compare(list.get(currentIndex), item) > 0) {
				maxIndex = currentIndex;
				currentIndex = (maxIndex)/2;
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	protected class StringComparator implements Comparator<String> {

		/**
		 *Uses the string compareTo() method to sort two String objects
		 *
		 */
		@Override
		public int compare(String o1, String o2) {
			if (o1 == null || o2 == null) {
				throw new NullPointerException();
			}
			return o1.compareTo(o2) > 0 ? 1 : o1.compareTo(o2) < 0 ? -1 : 0;
		}
	}
	
	protected class IntegerComparator implements Comparator<Integer> {
		
		/**
		 *Uses the compareTo() method to sort to Integer objects
		 */
		@Override 
		public int compare(Integer o1, Integer o2) {
			return o1.compareTo(o2) > 0 ? 1 : o1.compareTo(o2) < 0 ? -1 : 0;
		}
	}
}
