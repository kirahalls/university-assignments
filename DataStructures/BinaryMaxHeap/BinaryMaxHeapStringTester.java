package assign10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains JUnit tests for a String type of BinaryMaxHeap.java.
 *
 * @author Nandini Goel and Kira Halls
 * @version Nov 27, 2022
 */
class BinaryMaxHeapStringTester {

    private BinaryMaxHeap<String> strMaxHeap = new BinaryMaxHeap<>();
    private BinaryMaxHeap<String> emptyMaxHeap = new BinaryMaxHeap<>();

    private BinaryMaxHeap<String> strMaxHeapList;
    private ArrayList<String> list = new ArrayList<>();
    private BinaryMaxHeap<String> emptyMaxHeapList;

    @BeforeEach
    void setUp() {
        emptyMaxHeapList = new BinaryMaxHeap<>(list);
        for (int i = 0; i < 26; i++) {
            String letter = (char)('A' + i % 26) + "";
            strMaxHeap.add(letter);
            list.add(letter);
        }
        strMaxHeapList = new BinaryMaxHeap<>(list);
    }

    @Test
    void addGreaterOnStrMaxHeap() {
        strMaxHeap.add("ZZ");
        String expected = "[ZZ, V, Z, Q, U, Y, M, J, P, R, T, N, X, E, L, A, G, D, O, C, I, H, S, B, K, F, W]";
        assertEquals(expected, Arrays.toString(strMaxHeap.toArray()));
        assertEquals(27, strMaxHeap.size());
    }

    @Test
    void addMiddleOnStrMaxHeap() {
        strMaxHeap.add("Mm");
        String expected = "[Z, V, Y, Q, U, X, M, J, P, R, T, N, W, E, L, A, G, D, O, C, I, H, S, B, K, F, Mm]";
        assertEquals(expected, Arrays.toString(strMaxHeap.toArray()));
        assertEquals(27, strMaxHeap.size());
    }

    @Test
    void addMinLowercaseOnStrMaxHeap() {
        strMaxHeap.add("AA");
        String expected = "[Z, V, Y, Q, U, X, M, J, P, R, T, N, W, E, L, A, G, D, O, C, I, H, S, B, K, F, AA]";
        assertEquals(expected, Arrays.toString(strMaxHeap.toArray()));
        assertEquals(27, strMaxHeap.size());
    }

    @Test
    void peekOnStrMaxHeap() {
        assertEquals("Z", strMaxHeap.peek());
        assertEquals(26, strMaxHeap.size());
        assertEquals("Z", strMaxHeap.peek());
    }

    @Test
    void peekOnEmptyMaxHeap() {
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeap.peek());
    }

    @Test
    void peekAfterExtractMaxOnStrMaxHeap() {
        strMaxHeap.extractMax();
        assertEquals("Y", strMaxHeap.peek());
        assertEquals(25, strMaxHeap.size());
    }

    @Test
    void peekAfterAddingMaxOnStrMaxHeap() {
        strMaxHeap.add("ZZZ");
        assertEquals("ZZZ", strMaxHeap.peek());
        assertEquals(27, strMaxHeap.size());
    }

    @Test
    void peekAfterAddingLessThanMaxOnStrMaxHeap() {
        strMaxHeap.add("EE");
        assertEquals("Z", strMaxHeap.peek());
        assertEquals(27, strMaxHeap.size());
    }

    @Test
    void extractMaxOnStrMaxHeap() {
        assertEquals("Z", strMaxHeap.extractMax());
        assertEquals(25, strMaxHeap.size());
        assertEquals("Y", strMaxHeap.extractMax());
    }

    @Test
    void extractMaxOnEmptyMaxHeap() {
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeap.peek());
    }

    @Test
    void extractMaxTwiceOnStrMaxHeap() {
        assertEquals("Z", strMaxHeap.extractMax());
        assertEquals(25, strMaxHeap.size());
        assertEquals("Y", strMaxHeap.extractMax());
        assertEquals(24, strMaxHeap.size());
    }

    @Test
    void extractMaxAfterAddingMaxOnStrMaxHeap() {
        strMaxHeap.add("ZZ");
        assertEquals("ZZ", strMaxHeap.extractMax());
        assertEquals(26, strMaxHeap.size());
    }

    @Test
    void extractMaxAfterAddingLessThanMaxOnStrMaxHeap() {
        strMaxHeap.add("BB");
        assertEquals("Z", strMaxHeap.extractMax());
        assertEquals(26, strMaxHeap.size());
    }

    @Test
    void testSizeOriginalOfStrMaxHeap() {
        assertEquals(26, strMaxHeap.size());
    }

    @Test
    void testSizeAfterAddOnStrMaxHeap() {
        strMaxHeap.add("AA");
        assertEquals(27, strMaxHeap.size());
    }

    @Test
    void testSizeAfterRemoveOnStrMaxHeap() {
        for (int i = 1; i <= 5; i++) {
            strMaxHeap.extractMax();
        }
        assertEquals(21, strMaxHeap.size());
    }

    @Test
    void testSizeAfterPeekOnStrMaxHeap() {
        strMaxHeap.peek();
        assertEquals(26, strMaxHeap.size());
    }

    @Test
    void testSizeOnEmptyOnStrMaxHeap() {
        assertEquals(0, emptyMaxHeap.size());
    }

    @Test
    void testIsEmptyOnEmptyMaxHeapOnStrMaxHeap() {
        assertTrue(emptyMaxHeap.isEmpty());
        emptyMaxHeap.add("a");
        assertFalse(emptyMaxHeap.isEmpty());
    }

    @Test
    void testIsEmptyOnStrMaxHeap() {
        assertFalse(strMaxHeap.isEmpty());
        strMaxHeap.extractMax();
        assertFalse(strMaxHeap.isEmpty());
        for (int i = 0; i < 25; i++) {
            strMaxHeap.extractMax();
        }
        assertTrue(strMaxHeap.isEmpty());
    }

    @Test
    void testClearOnEmptyMaxHeap() {
        emptyMaxHeap.clear();
        assertEquals(0, emptyMaxHeap.size());
    }

    @Test
    void testClearOnStrMaxHeap() {
        strMaxHeap.clear();
        assertEquals(0, strMaxHeap.size());
    }

    @Test
    void testToArrayOnEmptyMaxHeap() {
        assertEquals("[]", Arrays.toString(emptyMaxHeap.toArray()));
    }

    @Test
    void testToArrayOnStrMaxHeap() {
        Object[] actual = strMaxHeap.toArray();
        assertEquals("[Z, V, Y, Q, U, X, M, J, P, R, T, N, W, E, L, A, G, D, O, C, I, H, S, B, K, F]", Arrays.toString(actual));
    }

    @Test
    void testToArrayAfterAddOnStrMaxHeap() {
        strMaxHeap.add("ZZ");
        assertEquals("[ZZ, V, Z, Q, U, Y, M, J, P, R, T, N, X, E, L, A, G, D, O, C, I, H, S, B, K, F, W]", Arrays.toString(strMaxHeap.toArray()));
    }

    @Test
    void testToArrayAfterExtractMaxOnStrMaxHeap() {
        strMaxHeap.extractMax();
        assertEquals("[Y, V, X, Q, U, W, M, J, P, R, T, N, F, E, L, A, G, D, O, C, I, H, S, B, K]", Arrays.toString(strMaxHeap.toArray()));
    }

    // ---------------------------------------------------

    @Test
    void addGreaterOnStrMaxHeapList() {
        strMaxHeapList.add("ZZ");
        String expected = "[ZZ, V, Z, Q, U, Y, M, J, P, R, T, N, X, E, L, A, G, D, O, C, I, H, S, B, K, F, W]";
        assertEquals(expected, Arrays.toString(strMaxHeapList.toArray()));
        assertEquals(27, strMaxHeapList.size());
    }

    @Test
    void addMiddleOnStrMaxHeapList() {
        strMaxHeapList.add("Mm");
        String expected = "[Z, V, Y, Q, U, X, M, J, P, R, T, N, W, E, L, A, G, D, O, C, I, H, S, B, K, F, Mm]";
        assertEquals(expected, Arrays.toString(strMaxHeapList.toArray()));
        assertEquals(27, strMaxHeapList.size());
    }

    @Test
    void addMinLowercaseOnStrMaxHeapList() {
        strMaxHeapList.add("AA");
        String expected = "[Z, V, Y, Q, U, X, M, J, P, R, T, N, W, E, L, A, G, D, O, C, I, H, S, B, K, F, AA]";
        assertEquals(expected, Arrays.toString(strMaxHeapList.toArray()));
        assertEquals(27, strMaxHeapList.size());
    }

    @Test
    void peekOnStrMaxHeapList() {
        assertEquals("Z", strMaxHeapList.peek());
        assertEquals(26, strMaxHeapList.size());
        assertEquals("Z", strMaxHeapList.peek());
    }

    @Test
    void peekOnEmptyMaxHeapList() {
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeapList.peek());
    }

    @Test
    void peekAfterExtractMaxOnStrMaxHeapList() {
        strMaxHeapList.extractMax();
        assertEquals("Y", strMaxHeapList.peek());
        assertEquals(25, strMaxHeapList.size());
    }

    @Test
    void peekAfterAddingMaxOnStrMaxHeapList() {
        strMaxHeapList.add("ZZZ");
        assertEquals("ZZZ", strMaxHeapList.peek());
        assertEquals(27, strMaxHeapList.size());
    }

    @Test
    void peekAfterAddingLessThanMaxOnStrMaxHeapList() {
        strMaxHeapList.add("EE");
        assertEquals("Z", strMaxHeapList.peek());
        assertEquals(27, strMaxHeapList.size());
    }

    @Test
    void extractMaxOnStrMaxHeapList() {
        assertEquals("Z", strMaxHeapList.extractMax());
        assertEquals(25, strMaxHeapList.size());
        assertEquals("Y", strMaxHeapList.extractMax());
    }

    @Test
    void extractMaxOnEmptyMaxHeapList() {
        assertThrows(NoSuchElementException.class, () -> emptyMaxHeapList.peek());
    }

    @Test
    void extractMaxTwiceOnStrMaxHeapList() {
        assertEquals("Z", strMaxHeapList.extractMax());
        assertEquals(25, strMaxHeapList.size());
        assertEquals("Y", strMaxHeapList.extractMax());
        assertEquals(24, strMaxHeapList.size());
    }

    @Test
    void extractMaxAfterAddingMaxOnStrMaxHeapList() {
        strMaxHeapList.add("ZZ");
        assertEquals("ZZ", strMaxHeapList.extractMax());
        assertEquals(26, strMaxHeapList.size());
    }

    @Test
    void extractMaxAfterAddingLessThanMaxOnStrMaxHeapList() {
        strMaxHeapList.add("BB");
        assertEquals("Z", strMaxHeapList.extractMax());
        assertEquals(26, strMaxHeapList.size());
    }

    @Test
    void testSizeOriginalOfStrMaxHeapList() {
        assertEquals(26, strMaxHeapList.size());
    }

    @Test
    void testSizeAfterAddOnStrMaxHeapList() {
        strMaxHeapList.add("AA");
        assertEquals(27, strMaxHeapList.size());
    }

    @Test
    void testSizeAfterRemoveOnStrMaxHeapList() {
        for (int i = 1; i <= 5; i++) {
            strMaxHeapList.extractMax();
        }
        assertEquals(21, strMaxHeapList.size());
    }

    @Test
    void testSizeAfterPeekOnStrMaxHeapList() {
        strMaxHeapList.peek();
        assertEquals(26, strMaxHeapList.size());
    }

    @Test
    void testSizeOnEmptyOnStrMaxHeapList() {
        assertEquals(0, emptyMaxHeapList.size());
    }

    @Test
    void testIsEmptyOnEmptyMaxHeapOnStrMaxHeapList() {
        assertTrue(emptyMaxHeapList.isEmpty());
        emptyMaxHeapList.add("a");
        assertFalse(emptyMaxHeapList.isEmpty());
    }

    @Test
    void testIsEmptyOnStrMaxHeapList() {
        assertFalse(strMaxHeapList.isEmpty());
        strMaxHeapList.extractMax();
        assertFalse(strMaxHeapList.isEmpty());
        for (int i = 0; i < 25; i++) {
            strMaxHeapList.extractMax();
        }
        assertTrue(strMaxHeapList.isEmpty());
    }

    @Test
    void testClearOnEmptyMaxHeapList() {
        emptyMaxHeapList.clear();
        assertEquals(0, emptyMaxHeapList.size());
    }

    @Test
    void testClearOnStrMaxHeapList() {
        strMaxHeapList.clear();
        assertEquals(0, strMaxHeapList.size());
    }

    @Test
    void testToArrayOnEmptyMaxHeapList() {
        assertEquals("[]", Arrays.toString(emptyMaxHeapList.toArray()));
    }

    @Test
    void testToArrayOnStrMaxHeapList() {
        Object[] actual = strMaxHeapList.toArray();
        assertEquals("[Z, V, Y, Q, U, X, M, J, P, R, T, N, W, E, L, A, G, D, O, C, I, H, S, B, K, F]", Arrays.toString(actual));
    }

    @Test
    void testToArrayAfterAddOnStrMaxHeapList() {
        strMaxHeapList.add("ZZ");
        assertEquals("[ZZ, V, Z, Q, U, Y, M, J, P, R, T, N, X, E, L, A, G, D, O, C, I, H, S, B, K, F, W]", Arrays.toString(strMaxHeapList.toArray()));
    }

    @Test
    void testToArrayAfterExtractMaxOnStrMaxHeapList() {
        strMaxHeapList.extractMax();
        assertEquals("[Y, V, X, Q, U, W, M, J, P, R, T, N, F, E, L, A, G, D, O, C, I, H, S, B, K]", Arrays.toString(strMaxHeapList.toArray()));
    }
}