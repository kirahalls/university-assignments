package assign10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains JUnit tests for an Integer type of BinaryMaxHeap.java.
 *
 * @author Nandini Goel and Kira Halls
 * @version Nov 27, 2022
 */
class BinaryMaxHeapIntegerTester {

    private BinaryMaxHeap<Integer> intMaxHeap = new BinaryMaxHeap<>();
    private BinaryMaxHeap<Integer> emptyMaxHeap = new BinaryMaxHeap<>();

    private BinaryMaxHeap<Integer> intMaxHeapList;
    private ArrayList<Integer> list = new ArrayList<>();
    private BinaryMaxHeap<Integer> emptyMaxHeapList;

    @BeforeEach
    void setUp() {
        emptyMaxHeapList = new BinaryMaxHeap<>(list);
        for(int i = 0; i <= 9; i++){
            intMaxHeap.add(i + 1);
            list.add(i + 1);
        }
        intMaxHeapList = new BinaryMaxHeap<>(list);
    }

    @Test
    void addGreaterOnIntMaxHeap() {
        intMaxHeap.add(11);
        assertEquals("[11, 10, 6, 7, 9, 2, 5, 1, 4, 3, 8]", Arrays.toString(intMaxHeap.toArray()));
        assertEquals(11, intMaxHeap.size());
    }

    @Test
    void addMiddleOnIntMaxHeap(){
        intMaxHeap.add(5);
        assertEquals("[10, 9, 6, 7, 8, 2, 5, 1, 4, 3, 5]", Arrays.toString(intMaxHeap.toArray()));
        assertEquals(11, intMaxHeap.size());
    }

    @Test
    void addMinZeroOnIntMaxHeap(){
        intMaxHeap.add(0);
        assertEquals("[10, 9, 6, 7, 8, 2, 5, 1, 4, 3, 0]", Arrays.toString(intMaxHeap.toArray()));
        assertEquals(11, intMaxHeap.size());
    }

    @Test
    void peekOnIntMaxHeap() {
        assertEquals(10, intMaxHeap.peek());
        assertEquals(10, intMaxHeap.size());
        assertEquals(10, intMaxHeap.peek());
    }

    @Test
    void peekOnEmptyMaxHeap(){
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeap.peek());
    }

    @Test
    void peekAfterExtractMaxOnIntMaxHeap(){
        intMaxHeap.extractMax();
        assertEquals(9, intMaxHeap.peek());
        assertEquals(9, intMaxHeap.size());
    }

    @Test
    void peekAfterAddingMaxOnIntMaxHeap(){
        intMaxHeap.add(11);
        assertEquals(11, intMaxHeap.peek());
        assertEquals(11, intMaxHeap.size());
    }

    @Test
    void peekAfterAddingLessThanMaxOnIntMaxHeap(){
        intMaxHeap.add(2);
        assertEquals(10, intMaxHeap.peek());
        assertEquals(11, intMaxHeap.size());
    }

    @Test
    void extractMaxOnIntMaxHeap() {
        assertEquals(10, intMaxHeap.extractMax());
        assertEquals(9, intMaxHeap.size());
        assertEquals(9, intMaxHeap.extractMax());
    }

    @Test
    void extractMaxOnEmptyMaxHeap(){
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeap.peek());
    }

    @Test
    void extractMaxTwiceOnIntMaxHeap(){
        assertEquals(10, intMaxHeap.extractMax());
        assertEquals(9, intMaxHeap.size());
        assertEquals(9, intMaxHeap.extractMax());
        assertEquals(8, intMaxHeap.size());
    }

    @Test
    void extractMaxAfterAddingMaxOnIntMaxHeap(){
        intMaxHeap.add(11);
        assertEquals(11, intMaxHeap.extractMax());
        assertEquals(10, intMaxHeap.size());
    }

    @Test
    void extractMaxAfterAddingLessThanMaxOnIntMaxHeap(){
        intMaxHeap.add(2);
        assertEquals(10, intMaxHeap.extractMax());
        assertEquals(10, intMaxHeap.size());
    }

    @Test
    void testSizeOriginalOfIntMaxHeap() {
        assertEquals(10, intMaxHeap.size());
    }

    @Test
    void testSizeAfterAddOnIntMaxHeap(){
        for (int i = 11; i <= 20; i++) {
            intMaxHeap.add(i);
        }
        assertEquals(20, intMaxHeap.size());
    }

    @Test
    void testSizeAfterRemoveOnIntMaxHeap(){
        for (int i = 1; i <= 5; i++) {
            intMaxHeap.extractMax();
        }
        assertEquals(5, intMaxHeap.size());
    }

    @Test
    void testSizeAfterPeekOnIntMaxHeap(){
        intMaxHeap.peek();
        assertEquals(10, intMaxHeap.size());
    }

    @Test
    void testSizeOnEmptyOnIntMaxHeap(){
        assertEquals(0, emptyMaxHeap.size());
    }

    @Test
    void testIsEmptyOnEmptyMaxHeapOnIntMaxHeap() {
        assertTrue(emptyMaxHeap.isEmpty());
        emptyMaxHeap.add(1);
        assertFalse(emptyMaxHeap.isEmpty());
    }

    @Test
    void testIsEmptyOnIntMaxHeap(){
        assertFalse(intMaxHeap.isEmpty());
        intMaxHeap.extractMax();
        assertFalse(intMaxHeap.isEmpty());
        for (int i = 0; i < 9; i++) {
            intMaxHeap.extractMax();
        }
        assertTrue(intMaxHeap.isEmpty());
    }

    @Test
    void testClearOnEmptyMaxHeap() {
        emptyMaxHeap.clear();
        assertEquals(0, emptyMaxHeap.size());
    }

    @Test
    void testClearOnIntMaxHeap() {
        intMaxHeap.clear();
        assertEquals(0, intMaxHeap.size());
    }

    @Test
    void testToArrayOnEmptyMaxHeap(){
        assertEquals("[]", Arrays.toString(emptyMaxHeap.toArray()));
    }

    @Test
    void testToArrayOnIntMaxHeap() {
        Object[] actual = intMaxHeap.toArray();
        assertEquals("[10, 9, 6, 7, 8, 2, 5, 1, 4, 3]", Arrays.toString(actual));
    }

    @Test
    void testToArrayAfterAddOnIntMaxHeap(){
        intMaxHeap.add(11);
        assertEquals("[11, 10, 6, 7, 9, 2, 5, 1, 4, 3, 8]", Arrays.toString(intMaxHeap.toArray()));
    }

    @Test
    void testToArrayAfterExtractMaxOnIntMaxHeap(){
        intMaxHeap.extractMax();
        assertEquals("[9, 8, 6, 7, 3, 2, 5, 1, 4]", Arrays.toString(intMaxHeap.toArray()));
    }

    // ---------------------------------------------------

    @Test
    void addGreaterOnIntMaxHeapList() {
        intMaxHeapList.add(11);
        assertEquals("[11, 10, 6, 7, 9, 2, 5, 1, 4, 3, 8]", Arrays.toString(intMaxHeapList.toArray()));
        assertEquals(11, intMaxHeapList.size());
    }

    @Test
    void addMiddleOnIntMaxHeapList(){
        intMaxHeapList.add(5);
        assertEquals("[10, 9, 6, 7, 8, 2, 5, 1, 4, 3, 5]", Arrays.toString(intMaxHeapList.toArray()));
        assertEquals(11, intMaxHeapList.size());
    }

    @Test
    void addMinZeroOnIntMaxHeapList(){
        intMaxHeapList.add(0);
        assertEquals("[10, 9, 6, 7, 8, 2, 5, 1, 4, 3, 0]", Arrays.toString(intMaxHeapList.toArray()));
        assertEquals(11, intMaxHeapList.size());
    }

    @Test
    void peekOnIntMaxHeapList() {
        assertEquals(10, intMaxHeapList.peek());
        assertEquals(10, intMaxHeapList.size());
        assertEquals(10, intMaxHeapList.peek());
    }

    @Test
    void peekOnEmptyMaxHeapList(){
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeapList.peek());
    }

    @Test
    void peekAfterExtractMaxOnIntMaxHeapList(){
        intMaxHeapList.extractMax();
        assertEquals(9, intMaxHeapList.peek());
        assertEquals(9, intMaxHeapList.size());
    }

    @Test
    void peekAfterAddingMaxOnIntMaxHeapList(){
        intMaxHeapList.add(11);
        assertEquals(11, intMaxHeapList.peek());
        assertEquals(11, intMaxHeapList.size());
    }

    @Test
    void peekAfterAddingLessThanMaxOnIntMaxHeapList(){
        intMaxHeapList.add(2);
        assertEquals(10, intMaxHeapList.peek());
        assertEquals(11, intMaxHeapList.size());
    }

    @Test
    void extractMaxOnIntMaxHeapList() {
        assertEquals(10, intMaxHeapList.extractMax());
        assertEquals(9, intMaxHeapList.size());
        assertEquals(9, intMaxHeapList.extractMax());
    }

    @Test
    void extractMaxOnEmptyMaxHeapList(){
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeapList.peek());
    }

    @Test
    void extractMaxTwiceOnIntMaxHeapList(){
        assertEquals(10, intMaxHeapList.extractMax());
        assertEquals(9, intMaxHeapList.size());
        assertEquals(9, intMaxHeapList.extractMax());
        assertEquals(8, intMaxHeapList.size());
    }

    @Test
    void extractMaxAfterAddingMaxOnIntMaxHeapList(){
        intMaxHeapList.add(11);
        assertEquals(11, intMaxHeapList.extractMax());
        assertEquals(10, intMaxHeapList.size());
    }

    @Test
    void extractMaxAfterAddingLessThanMaxOnIntMaxHeapList(){
        intMaxHeapList.add(2);
        assertEquals(10, intMaxHeapList.extractMax());
        assertEquals(10, intMaxHeapList.size());
    }

    @Test
    void testSizeOriginalOfIntMaxHeapList() {
        assertEquals(10, intMaxHeapList.size());
    }

    @Test
    void testSizeAfterAddOnIntMaxHeapList(){
        for (int i = 11; i <= 20; i++) {
            intMaxHeapList.add(i);
        }
        assertEquals(20, intMaxHeapList.size());
    }

    @Test
    void testSizeAfterRemoveOnIntMaxHeapList(){
        for (int i = 1; i <= 5; i++) {
            intMaxHeapList.extractMax();
        }
        assertEquals(5, intMaxHeapList.size());
    }

    @Test
    void testSizeAfterPeekOnIntMaxHeapList(){
        intMaxHeapList.peek();
        assertEquals(10, intMaxHeapList.size());
    }

    @Test
    void testSizeOnEmptyOnIntMaxHeapList(){
        assertEquals(0, emptyMaxHeapList.size());
    }

    @Test
    void testIsEmptyOnEmptyMaxHeapOnIntMaxHeapList() {
        assertTrue(emptyMaxHeapList.isEmpty());
        emptyMaxHeapList.add(1);
        assertFalse(emptyMaxHeapList.isEmpty());
    }

    @Test
    void testIsEmptyOnIntMaxHeapList(){
        assertFalse(intMaxHeapList.isEmpty());
        intMaxHeapList.extractMax();
        assertFalse(intMaxHeapList.isEmpty());
        for (int i = 0; i < 9; i++) {
            intMaxHeapList.extractMax();
        }
        assertTrue(intMaxHeapList.isEmpty());
    }

    @Test
    void testClearOnEmptyMaxHeapList() {
        emptyMaxHeapList.clear();
        assertEquals(0, emptyMaxHeapList.size());
    }

    @Test
    void testClearOnIntMaxHeapList() {
        intMaxHeapList.clear();
        assertEquals(0, intMaxHeapList.size());
    }

    @Test
    void testToArrayOnEmptyMaxHeapList(){
        assertEquals("[]", Arrays.toString(emptyMaxHeapList.toArray()));
    }

    @Test
    void testToArrayOnIntMaxHeapList() {
        Object[] actual = intMaxHeapList.toArray();
        assertEquals("[10, 9, 6, 7, 8, 2, 5, 1, 4, 3]", Arrays.toString(actual));
    }

    @Test
    void testToArrayAfterAddOnIntMaxHeapList(){
        intMaxHeapList.add(11);
        assertEquals("[11, 10, 6, 7, 9, 2, 5, 1, 4, 3, 8]", Arrays.toString(intMaxHeapList.toArray()));
    }

    @Test
    void testToArrayAfterExtractMaxOnIntMaxHeapList(){
        intMaxHeapList.extractMax();
        assertEquals("[9, 8, 6, 7, 3, 2, 5, 1, 4]", Arrays.toString(intMaxHeapList.toArray()));
    }
}