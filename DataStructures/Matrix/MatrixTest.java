package assignment1;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * 
 * @author CS 2420 staff and Kira Halls
 * @version August 18, 2021
 *
 */
class MatrixTest {

	/* ******equals tests****** */
	@Test
	void testModerateMatricesEqual() {
		Matrix m1 = new Matrix(new int[][] {
			{1, 2, 3},
			{4, 5, 6}
		});

		Matrix m2 = new Matrix(new int[][] {
			{1, 2, 3},
			{4, 5, 6}
		});

		assertTrue(m1.equals(m2));
	}
	
	
	@Test
	void testLargeMatricesEqual() {
		Matrix j1 = new Matrix(new int[][] {
			{1, 2, 3, 7, 1},
			{4, 5, 6, 90, 8},
			{34, 7, 90, 87, 2}
		});

		Matrix j2 = new Matrix(new int[][] {
			{1, 2, 3, 7, 1},
			{4, 5, 6, 90, 8},
			{34, 7, 90, 87, 2}
		});
		
		assertTrue(j1.equals(j2));
	}
	
	@Test 
	void testSmallMatricesEqual() {
		Matrix k1 = new Matrix(new int[][] {
			{1},
		});
		
		Matrix k2 = new Matrix(new int[][] {
			{1},
		});
		
		assertTrue(k1.equals(k2));
	}
	
	
	/**
	 * Test that ensures order of the matrices doesn't impact the answer
	 * Reversed order of the first Equals test
	 */
	@Test 
	void testEqualButReversedOrder() {
		Matrix m1 = new Matrix(new int[][] {
			{1, 2, 3},
			{4, 5, 6}
		});

		Matrix m2 = new Matrix(new int[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
		
		assertTrue(m2.equals(m1));
	}
	
	
	/**
	 * Testing to matrices with the same dimensions but are not equal
	 * Must return false to pass
	 */
	@Test 
	void testCorrectDimensionsButInequal() {
		Matrix a1 = new Matrix(new int[][] {
			{12, 8, 4, 7},
			{3, 17, 14, 8},
			{9, 8, 10, 12},
		});
		
		Matrix a2 = new Matrix(new int[][] {
			{126, 9, 0, 18},
			{32, 7, 14, 9},
			{6, 213, 10, 4},
		});
		
		assertFalse(a1.equals(a2));
	}
	
	
	/**
	 * Testing the Equals function on two matrices with different dimensions
	 * Must return false to pass the test
	 */
	@Test 
	void testIncorrectDimensionEquals() {
		Matrix b1 = new Matrix(new int[][] {
			{12, 8, 4},
			{3, 17, 14},
			{9, 8, 10},
		});
		
		Matrix b2 = new Matrix(new int[][] {
			{12, 8},
			{3, 17},
			{9, 8},
		});
		
		assertFalse(b1.equals(b2)); 
	}
	
	
	/**
	 * Testing the equals function against a non matrix object
	 * Must return false to pass
	 */
	@Test 
	void testNonMatrixEquals() {
		int nonMatrixInt = 6;
		
		Matrix c1 = new Matrix(new int[][] {
			{6},
		});
		
		assertFalse(c1.equals(nonMatrixInt));
	}
	
	/* ******end equals tests****** */


	/* ******toString tests****** */
	@Test
	void testModerateToString() {
		Matrix m = new Matrix(new int[][] {
			{1, 2},
			{3, 4}
		});

		assertEquals("1 2\n3 4\n", m.toString());
	}
	
	
	@Test
	void testLargeToString() {
		Matrix n1 = new Matrix(new int[][] {
			{1, 2, 9, 27},
			{3, 4, 209, 69},
			{421, 87, 3, 21},
			{5, 99, 33, 11},
		});
		
		assertEquals("1 2 9 27\n3 4 209 69\n421 87 3 21\n5 99 33 11\n", n1.toString());
	}
	
	
	@Test 
	void testSmallToString() {
		Matrix o1 = new Matrix(new int[][] {
			{1},
			{2},
		});
		
		assertEquals("1\n2\n", o1.toString());
	}
	
	
	@Test 
	void testSingleColumnToString() {
		Matrix d1 = new Matrix(new int[][] {
			{8},
			{129},
			{0},
		});
		
		assertEquals("8\n129\n0\n", d1.toString());
	}
	
	
	@Test 
	void testSingleRowToString() {
		Matrix e1 = new Matrix(new int[][] {
			{86, 67, 4, 29, 1029},
		});
		
		assertEquals("86 67 4 29 1029\n", e1.toString());
	}
	
	
	@Test 
	void testSingleIntMatrixToString() {
		Matrix f1 = new Matrix(new int[][] {
			{78},
		});
		
		assertEquals("78\n", f1.toString());
	}
	
	/* ******end toString tests****** */



	/* ******times tests****** */
	@Test
	void testCompatibleTimes() {
		Matrix m1 = new Matrix(new int[][]
				{{1, 2, 3},
				{2, 5, 6}});

		Matrix m2 = new Matrix(new int[][]
				{{4, 5},
				{3, 2},
				{1, 1}});

		// the known correct result of multiplying m1 by m2
		int[][] expected = new int[][]
				{{13, 12},
				{29, 26}};
		
		// the result produced by the times method
		Matrix mulResult = m1.times(m2);
		int[][] resultArray = mulResult.getData();
		
		
		// compare the raw arrays
		assertEquals(expected.length, resultArray.length);
		for(int i = 0; i < expected.length; i++)
			assertArrayEquals(expected[i], resultArray[i]);
	}
	
	
	@Test
	void testModerateTimes() {
		Matrix p1 = new Matrix(new int[][]
				{{1, 2},
				{3, 4},
		});

		Matrix p2 = new Matrix(new int[][]
				{{3, 2},
				{1, 4},
		});
		
		int[][] expected = new int[][]
				{{5, 10},
				{13, 22}};
				
		Matrix actualResult = p1.times(p2);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expected.length, actualResultArray.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actualResultArray[i]);
		}
	}
	
	
	@Test
	void testLargeTimes() {
		Matrix q1 = new Matrix(new int[][] {
			{12, 8, 4},
			{3, 17, 14},
			{9, 8, 10},
		});
		
		Matrix q2 = new Matrix(new int[][] {
			{5, 19, 3},
			{6, 15, 9},
			{7, 8, 16}
		});
		
		int[][] expectedArray = new int[][] 
				{{136, 380, 172}, 
				{215, 424, 386},
				{163, 371, 259}};
				
		Matrix actualResult = q1.times(q2);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	@Test
	void testSmallTimes() {
		Matrix r1 = new Matrix(new int[][] {
			{1},
		});
		
		Matrix r2 = new Matrix(new int[][] {
			{5},
		});
		
		int[][] expectedArray = new int[][] {
			{5}};
			
		Matrix actualResult = r1.times(r2);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Test a uniform number matrix to ensure it throws with incompatible dimensions
	 */
	@Test 
	void testIncompatibleMultiplicationDimensions() {
		Matrix h1 = new Matrix(new int[][] {
			{1, 1, 1, 1},
			{1, 1, 1, 1}
		});

		Matrix h2 = new Matrix(new int[][] {
			{1, 1},
			{1, 1}
		});
		
		assertThrows(IllegalArgumentException.class, () -> { h1.times(h2); });  
	}
	
	
	/**
	 * Test a uniform number matrix to check if it 
	 * results in the proper dimensions
	 */
	@Test
	void testProperDimensionsTimes() {
		Matrix s1 = new Matrix(new int[][]
				{{1, 1},
				{1, 1},
		});

		Matrix s2 = new Matrix(new int[][]
				{{1, 1},
				{1, 1},
		});
		
		int[][] expected = new int[][]
				{{2, 2},
				{2, 2}};
				
		Matrix actualResult = s1.times(s2);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expected.length, actualResultArray.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Test to make sure reversing the order does affect the answer
	 * Tested reverse of the ModerateTimesTest, changed expected 
	 * answer to reflect how the reversal of the matrices change it
	 */
	@Test
	void testReverseOrder() {
		Matrix p1 = new Matrix(new int[][]
				{{1, 2},
				{3, 4},
		});

		Matrix p2 = new Matrix(new int[][]
				{{3, 2},
				{1, 4},
		});
		
		int[][] expected = new int[][]
				{{9, 14},
				{13, 18}};
				
		Matrix actualResult = p2.times(p1);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expected.length, actualResultArray.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], actualResultArray[i]);
		}
	}

	/* ******end times tests****** */
	

	/* ******plus tests****** */
	@Test
	public void testIncompatiblePlus() {	
		Matrix m1 = new Matrix(new int[][] {
			{1, 1, 1},
			{1, 1, 1}
		});

		Matrix m2 = new Matrix(new int[][] {
			{2, 2},
			{2, 2}
		});
		
		// This is an example of how to test that an exception is thrown when needed.
		// The odd syntax below is an example of a lambda expression.
		// See Lab 1 for more info.
		assertThrows(IllegalArgumentException.class, () -> { m1.plus(m2); });  
	}
	
	
	@Test
	void testSmallPlus() {
		Matrix t1 = new Matrix(new int[][]
				{{2},
				});
		
		Matrix t2 = new Matrix(new int[][]
				{{4},
				});
		
		int[][] expectedArray = new int[][]
				{{6}, 
				};
				
		Matrix actualMatrix = t1.plus(t2);
		int[][] actualMatrixArray = actualMatrix.getData();
		assertEquals(expectedArray.length, actualMatrixArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualMatrixArray[i]);
		}
	}
	
	
	@Test
	void testModeratePlus() {
		Matrix u1 = new Matrix(new int[][]
				{{1, 2},
				{3, 4},
		});

		Matrix u2 = new Matrix(new int[][]
				{{3, 2},
				{1, 4},
		});
		
		int[][] expectedArray = new int[][]
				{{4, 4},
				{4, 8}};
				
		Matrix actualMatrix = u1.plus(u2);
		int[][] actualMatrixArray = actualMatrix.getData();
		
		assertEquals(expectedArray.length, actualMatrixArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualMatrixArray[i]);
		}
	}
	
	
	@Test
	void testLargePlus() {
		Matrix v1 = new Matrix(new int[][] {
			{12, 8, 4},
			{3, 17, 14},
			{9, 8, 10},
		});
		
		Matrix v2 = new Matrix(new int[][] {
			{5, 19, 3},
			{6, 15, 9},
			{7, 8, 16}
		});
		
		int[][] expectedArray = new int[][] 
				{{17, 27, 7}, 
				{9, 32, 23},
				{16, 16, 26}};
				
		Matrix actualResult = v1.plus(v2);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Test uniform matrix to ensure proper dimensions
	 * in the resulting matrix
	 */
	@Test
	void testProperDimensionsPlus() {
		Matrix s1 = new Matrix(new int[][]
				{{1, 1},
				{1, 1},
		});

		Matrix s2 = new Matrix(new int[][]
				{{1, 1},
				{1, 1},
		});
		
		Matrix actualResult = s1.plus(s2);
		int[][] actualResultArray = actualResult.getData();
		int[][] expectedArray = new int[][] 
				{{2, 2},
				{2, 2}};
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Test to ensure changing the order of the matrices
	 * doesn't affect the answer. Reverse of testLargePlus()
	 */
	@Test 
	void testReverseOrderPlus() {
		Matrix v1 = new Matrix(new int[][] {
			{12, 8, 4},
			{3, 17, 14},
			{9, 8, 10},
		});
		
		Matrix v2 = new Matrix(new int[][] {
			{5, 19, 3},
			{6, 15, 9},
			{7, 8, 16}
		});
		
		int[][] expectedArray = new int[][] 
				{{17, 27, 7}, 
				{9, 32, 23},
				{16, 16, 26}};
				
		Matrix actualResult = v2.plus(v1);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Test a negative matrix and the opposite positive matrix
	 * to ensure it handles negative numbers
	 */
	@Test
	 void testNegativePlus() {
		Matrix w1 = new Matrix(new int[][] {
			{12, 8, 4},
			{3, 17, 14},
			{9, 8, 10},
		});
		
		Matrix w2 = new Matrix(new int[][] {
			{-12, -8, -4},
			{-3, -17, -14},
			{-9, -8, -10}
		});
		
		int[][] expectedArray = new int[][] 
				{{0, 0, 0}, 
				{0, 0, 0},
				{0, 0, 0}};
				
		Matrix actualResult = w2.plus(w1);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Test to make sure it properly handles all negative matrix addition
	 */
	@Test 
	void testAllNegative() {
		Matrix x1 = new Matrix(new int[][] {
			{-12, -8, -4},
			{-3, -17, -14},
			{-9, -8, -10},
		});
		
		Matrix x2 = new Matrix(new int[][] {
			{-12, -8, -4},
			{-3, -17, -14},
			{-9, -8, -10}
		});
		
		int[][] expectedArray = new int[][] 
				{{-24, -16, -8}, 
				{-6, -34, -28},
				{-18, -16, -20}};
				
		Matrix actualResult = x2.plus(x1);
		int[][] actualResultArray = actualResult.getData();
		
		assertEquals(expectedArray.length, actualResultArray.length);
		for (int i = 0; i < expectedArray.length; i++) {
			assertArrayEquals(expectedArray[i], actualResultArray[i]);
		}
	}
	
	
	/**
	 * Make sure that the method does return false 
	 * if it does the addition incorrectly
	 */
	@Test
	void testReturnsFalse() {
		Matrix y1 = new Matrix(new int[][] {
			{-12, -8, -4},
			{-3, -17, -14},
			{-9, -8, -10},
		});
		
		Matrix y2 = new Matrix(new int[][] {
			{-12, -8, -4},
			{-3, -17, -14},
			{-9, -8, -10}
		});
		
		int[][] expectedArray = new int[][] 
				{{-24, -16, -8, 0}, 
				{-6, -34, -28, 0},
				{-18, -16, -20, 0},
				{21, -60, -4, -27}};
				
		Matrix actualResult = y2.plus(y1);
		int[][] actualResultArray = actualResult.getData();
				
		assertNotEquals(expectedArray.length, actualResultArray.length);
	}
	
	/* ******end plus tests****** */
	
	
	
	/* ******transpose tests****** */
	@Test
	void testSmallTranspose() {
		Matrix z1 = new Matrix(new int[][] {
			{1, 2, 3}
		});
		
		int expected[][] = {
				{1},
				{2},
				{3},
		};
		
		Matrix z2 = z1.transpose();
		int resultArray[][] = z2.getData();
		
		assertEquals(expected.length, resultArray.length);
		for (int i = 0; i < expected.length; i++)
			assertArrayEquals(expected[i], resultArray[i]);
	}
	
	
	@Test
	void testModerateTranspose() {
		Matrix m1 = new Matrix(new int[][] {
			{1, 2, 3},
			{4, 5, 6}
		});
	
		int expected[][] = {
			{1, 4},
			{2, 5},
			{3, 6}
		};
	
		Matrix result = m1.transpose();
		int resultArray[][] = result.getData();
	
		assertEquals(expected.length, resultArray.length);
		for(int i = 0; i < expected.length; i++)
			assertArrayEquals(expected[i], resultArray[i]);
	}
	
	
	@Test
	void testLargeTranspose() {
		Matrix a1 = new Matrix(new int[][] {
			{2, 3, 7},
			{3, 4, 5},
			{6, 8, 9}
		});
		
		int expected[][] = {
				{2, 3, 6},
				{3, 4, 8},
				{7, 5, 9}
		};
		
		Matrix result = a1.transpose();
		int resultArray[][] = result.getData();
		
		assertEquals(expected.length, resultArray.length);
		for (int i = 0; i < expected.length; i++) {
			assertArrayEquals(expected[i], resultArray[i]);
		}
	}
	
	
	/**
	 * Test only that the method can transpose a large matrix with correct dimensions
	 * Kept all values as one to only check that it creates the right 
	 * dimensions on the transposed matrix
	 */
	@Test 
	void testTransposeDimensions() {
		Matrix g1 = new Matrix(new int[][] {
			{1, 1, 1, 1},
			{1, 1, 1, 1}, 
			{1, 1, 1, 1}, 
			{1, 1, 1, 1}, 
			{1, 1, 1, 1},
		});
		
		Matrix expectedTranspositionG1 = new Matrix(new int[][] {
			{1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1},
		});
		
		Matrix transposedMatrixG1 = g1.transpose();
		int transposedArray[][] = transposedMatrixG1.getData();
		int expectedTransposedArray[][] = expectedTranspositionG1.getData();
		assertEquals(transposedArray.length, expectedTransposedArray.length);
		for (int i = 0; i < expectedTransposedArray.length; i++) {
			assertArrayEquals(transposedArray[i], expectedTransposedArray[i]);
		}
	}
	
	
	/**
	 * Testing that when we transpose a matrix that was created through transposing,
	 * we get the original matrix. Essentially, this test shows that doing a double transposition 
	 * reverses the process
	 * 
	 */
	@Test
	void testDoubleTransposedMatrix() {
		Matrix originalMatrix = new Matrix(new int[][] {
			{11, 76, 9, 47},
			{90, 9, 4, 7},
			{102, 8, 43, 43},
		});
		
		int expectedTransposed[][] = {
				{11, 90, 102},
				{76, 9, 8},
				{9, 4, 43},
				{47, 7, 43},
		};
		
		Matrix actualInitialTransposition = originalMatrix.transpose();
		int actualInitialTranspositionArray[][] = actualInitialTransposition.getData();
		
		assertEquals(expectedTransposed.length, actualInitialTranspositionArray.length); //test initial transposition
		for(int i = 0; i < actualInitialTranspositionArray.length; i++) {
			assertArrayEquals(expectedTransposed[i], actualInitialTranspositionArray[i]);
		}
		
		Matrix actualReversedTransposed = actualInitialTransposition.transpose();
		int actualReversedTransposedArray[][] = actualReversedTransposed.getData();
		int originalMatrixArray[][] = originalMatrix.getData();
		
		assertEquals(originalMatrixArray.length, actualReversedTransposedArray.length); //test that reversing the tranposition gives us the original matrix
		for (int i = 0; i < actualReversedTransposedArray.length; i++) {
			assertArrayEquals(originalMatrixArray[i], actualReversedTransposedArray[i]);
		}
	}
	
	/* ******end transpose tests****** */


}
