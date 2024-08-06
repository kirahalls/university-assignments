package assign10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains JUnit tests for FindKLargest.java.
 *
 * @author Nandini Goel and Kira Halls
 * @version Nov 27, 2022
 */
class FindKLargestTester {

    private ArrayList<Integer> intList = new ArrayList<>();
    private class IntegerComparator implements Comparator<Integer> {
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }
    private IntegerComparator intCmp = new IntegerComparator();

    private ArrayList<String> strList = new ArrayList<>();
    private class StringComparator implements Comparator<String> {
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }
    private StringComparator stringCmp = new StringComparator();

    @BeforeEach
    void setUp() {
        for(int i = 1; i <= 10; i++){
            intList.add(i);
        }

        for (int i = 0; i < 26; i++) {
            String letter = (char)('A' + i % 26) + "";
            strList.add(letter);
        }
    }

    @Test
    void testFindKLargestHeapOnIntList() {
        ArrayList<Integer> expected = new ArrayList<>();
        for (int i = 10; i >= 5; i--) {
            expected.add(i);
        }
        assertEquals(expected, FindKLargest.findKLargestHeap(intList, 6));
    }

    @Test
    void testFindKLargestHeapOnIntListCmp() {
        ArrayList<Integer> expected = new ArrayList<>();
        for (int i = 10; i >= 5; i--) {
            expected.add(i);
        }
        assertEquals(expected, FindKLargest.findKLargestHeap(intList, 6, intCmp));
    }

    @Test
    void testFindKLargestSortOnIntList() {
        ArrayList<Integer> expected = new ArrayList<>();
        for (int i = 10; i >= 5; i--) {
            expected.add(i);
        }
        assertEquals(expected, FindKLargest.findKLargestSort(intList, 6));
    }

    @Test
    void testFindKLargestSortOnIntListCmp() {
        ArrayList<Integer> expected = new ArrayList<>();
        for (int i = 10; i >= 5; i--) {
            expected.add(i);
        }
        assertEquals(expected, FindKLargest.findKLargestHeap(intList, 6, intCmp));
    }

    // -----------------------------------------------------------

    @Test
    void testFindKLargestHeapOnStrList() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Z");
        expected.add("Y");
        expected.add("X");
        expected.add("W");
        expected.add("V");
        expected.add("U");

        assertEquals(expected, FindKLargest.findKLargestHeap(strList, 6));
    }

    @Test
    void testFindKLargestHeapOnStrListCmp() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Z");
        expected.add("Y");
        expected.add("X");
        expected.add("W");
        expected.add("V");
        expected.add("U");

        assertEquals(expected, FindKLargest.findKLargestHeap(strList, 6, stringCmp));
    }

    @Test
    void testFindKLargestSortOnStrList() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Z");
        expected.add("Y");
        expected.add("X");
        expected.add("W");
        expected.add("V");
        expected.add("U");
        assertEquals(expected, FindKLargest.findKLargestSort(strList, 6));
    }

    @Test
    void testFindKLargestSortOnStrListCmp() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Z");
        expected.add("Y");
        expected.add("X");
        expected.add("W");
        expected.add("V");
        expected.add("U");

        assertEquals(expected, FindKLargest.findKLargestHeap(strList, 6, stringCmp));
    }
}