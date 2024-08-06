package assign04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @author Kira Halls and Braden Campbell
 * 
 * This class provides methods to check if words are anagrams. Also provides methods to find the largest 
 * group of anagrams in a given file or list. The file/list is guaranteed to not have any duplicates.
 * This class performs these methods by sorting given strings or list of strings in alphabetical order, then 
 * compares the sorted strings to check for equality.
 *
 */
public class AnagramChecker {

	/**
	 * Sort the letters in the input string in alphabetical order. 
	 * Used to make comparing words to find anagrams easier.
	 *
	 * @param string - the string to be resorted
	 * @return - the resorted string 
	 */
	public static String sort(String string) {
		StringBuilder sortedString = new StringBuilder(string);
		String finalSortedString;
		char currentCharI;
		char currentCharJ;
		for (int i = 1; i < string.length(); i++) {
			currentCharI = string.charAt(i);
			for (int j = i-1; j >= 0; j--) {
				currentCharJ = sortedString.charAt(j);
				if (currentCharJ <= currentCharI) {
					sortedString.setCharAt(j, currentCharJ);
					sortedString.setCharAt((j+1), currentCharI);
					break;
				}
				else {
					sortedString.setCharAt(j, currentCharI);
					sortedString.setCharAt((j+ 1),  currentCharJ);
					currentCharI = sortedString.charAt(j);
				}
			}
		}
		finalSortedString = sortedString.toString();
		return finalSortedString;
	}
	
	/**
	 * Uses an insertion sort to organize a given array by using the given comparator object.
	 * 
	 * @param <T> - The type of elements contained in the list
	 * @param arrayToSort - The array that needs to be sorted
	 * @param comp - the comparator that assists in sorting
	 */
	public static <T> void insertionSort(T[] arrayToSort, Comparator<? super T> comp) {
		T currentI;
		T currentJ;
		for (int i = 1; i < arrayToSort.length; i++) {
			currentI = arrayToSort[i];
			for (int j = i-1; j >= 0; j--) {
				currentJ = arrayToSort[j];
				if (comp.compare(currentJ, currentI) <= 0) {
					break;
				}
				else {
					arrayToSort[j] = currentI;
					arrayToSort[j+1] = currentJ;
				}
			}
		}
	}
	
	/**
	 * Checks if two words are anagrams i.e. if they contain the same letters
	 * in the same frequency. Calls the sort method to aid in checking if they 
	 * are anagrams
	 * 
	 * @param string1 - the first string to be compared
	 * @param string2 - the second string to be compared
	 * @return - true if they are anagrams, false if they are not.
	 */
	public static boolean areAnagrams(String string1, String string2) {
		if(sort(string1.toLowerCase()).equals(sort(string2.toLowerCase()))) {
			return true;
		}
		return false;
	}
	
	/**
	 * Given an array of strings, check the whole list and return 
	 * the largest group of anagrams. Returns the list of words, not the 
	 * number of words in the group. Calls insertion sort to make this easier 
	 * to compare.
	 * 
	 * @param words - the list of words to search through
	 * @return - the array containing the largest group of anagrams
	 */
	public static String[] getLargestAnagramGroup(String[] words) {
		int currentGroupSize = 1; //Set at one to account for the for loop starting at i = 1
		int largestGroupSize = 0;
		ArrayList<String> largestGroup = new ArrayList<String>(); //ensuring there is enough storage for the groups
		String[] finalLargestGroup = new String[largestGroupSize];
		if (words.length == 0) {
			finalLargestGroup = largestGroup.toArray(finalLargestGroup);
			return finalLargestGroup;
		}
		ArrayList<String> currentGroup = new ArrayList<String>(); //ensuring there is enough storage for the groups
		AnagramChecker checker = new AnagramChecker();
		AnagramComparator comparator = checker.new AnagramComparator();
		insertionSort(words, comparator);
		currentGroup.add(words[0]); //initialize currentGroup to make sure the first value is included
		for (int i = 1; i < words.length; i ++) {
			if(areAnagrams(words[i], words[i-1])) {
				currentGroup.add(words[i]);
				currentGroupSize ++;
				if (i == words.length - 1) {
					if (currentGroupSize > largestGroupSize) {
						largestGroup.removeAll(largestGroup);
						largestGroup.addAll(currentGroup);
						largestGroupSize = currentGroupSize;
					}
				}
			}
			else {
				if(currentGroupSize > largestGroupSize) {
					largestGroup.removeAll(largestGroup);
					largestGroup.addAll(currentGroup);
					largestGroupSize = currentGroupSize;
				}
				currentGroup.removeAll(currentGroup);
				currentGroup.add(words[i]); //initialize currentGroup to make sure the first value is included
				currentGroupSize = 1;
			}
		}
		if(largestGroupSize == 1) { // Checking to ensure an empty array is returned if there are no anagrams
			//if largest group size = 1, then there were no anagrams in the list
			largestGroup.removeAll(largestGroup); 
			largestGroupSize = 0;
		}
		finalLargestGroup = new String[largestGroupSize];
		finalLargestGroup = largestGroup.toArray(finalLargestGroup);
		return finalLargestGroup;
	}
	
	/**
	 * A method to get the largest anagram group from a list of words. 
	 * The words comes from a file that is passed in as a parameter, must use 
	 * a scanner to loop over the words and put them into an array to pass into 
	 * the above getLargestAnagramGroup method. 
	 * 
	 * @param filename - the filename of the file to retrieve the words from
	 * @return - the array that contains the words of the largest anagram group in the file
	 */
	public static String[] getLargestAnagramGroup(String filename) {
		String[] wordListArray = {}; 
		File file = new File(filename);
		Scanner fs;
		try {
			fs = new Scanner(file);
		} catch (FileNotFoundException e) {
			return wordListArray;
		}
		ArrayList<String> wordList = new ArrayList<String>();
		while(fs.hasNextLine()) {
			wordList.add(fs.next());
		}
		wordListArray = wordList.toArray(wordListArray);
		fs.close();
		String[] finalLargestGroup = getLargestAnagramGroup(wordListArray);
		return finalLargestGroup;
	}
	
	
	protected class StringComparator implements Comparator<String> {

		/**
		 *Uses the string compareTo() method to sort two String objects
		 *
		 */
		@Override
		public int compare(String o1, String o2) {
			o1 = o1.toLowerCase();
			o2 = o2.toLowerCase();
			return o1.compareTo(o2) > 0 ? 1 : o1.compareTo(o2) < 0 ? -1 : 0;
		}
	}
	
	
	protected class AnagramComparator implements Comparator<String> {

		/**
		 *Uses the string compareTo() method to sort the anagram of two string objects
		 *
		 */
		@Override
		public int compare(String o1, String o2) {
			o1 = sort(o1.toLowerCase());
			o2 = sort(o2.toLowerCase());
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
