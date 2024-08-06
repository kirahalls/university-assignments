package assignment1;

/**
 * This class represents a 2D matrix and simple operations on them.
 * 
 * @author Daniel Kopta and ??
 * @version August 08, 2021
 *
 */

public class Matrix {
	
	// the dimensions of the matrix
	private int numRows;
	private int numColumns;
	
	// the internal storage for the matrix elements 
	private int data[][];
	
	/**
	 * DO NOT MODIFY THIS METHOD.
	 * Constructor for a new Matrix. Automatically determines dimensions.
	 * This is implemented for you.
	 * @param d - the raw 2D array containing the initial values for the Matrix.
	 */
	public Matrix(int d[][])
	{
		// d.length is the number of 1D arrays in the 2D array
		numRows = d.length; 
		if(numRows == 0)
			numColumns = 0;
		else
			numColumns = d[0].length; // d[0] is the first 1D array
		
		// create a new matrix to hold the data
		data = new int[numRows][numColumns]; 
		
		// copy the data over
		for(int i=0; i < numRows; i++) 
			for(int j=0; j < numColumns; j++)
				data[i][j] = d[i][j];
	}
	
	

	/**
	 * Determines whether this Matrix is equal to another object.
	 * @param o - the other object to compare to, which may not be a Matrix
	 */
	@Override // instruct the compiler that we intend for this method to override the superclass' (Object) version
	public boolean equals(Object o) {
		// make sure the Object we're comparing to is a Matrix
		if(!(o instanceof Matrix)) 
			return false;
		
		// if the above was not true, we know it's safe to treat 'o' as a Matrix
		Matrix m = (Matrix)o; 
		int currentRow = 0;
		int currentColumn = 0;
		if (m.numColumns != this.numColumns || m.numRows != this.numRows) { //check the dimensions of both matrices, if they are not the same then they cannot be equal
			return false;
		}
		while (currentRow < this.numRows) {
			while (currentColumn < this.numColumns) {
				if (this.data[currentRow][currentColumn] != m.data[currentRow][currentColumn]) {
					return false;
				}
				else { 
					currentColumn += 1;
				}
			}
			currentColumn = 0;
			currentRow += 1;
		}
		return true; 
	}
	
	
	
	/**
	 * Returns a String representation of this Matrix.
	 */
	@Override // instruct the compiler that we intend for this method to override the superclass' (Object) version
	public String toString() {
		int currentRow = 0;
		int currentColumn = 0;
		String matrixString = new String();
		while (currentRow < this.numRows) {
			while (currentColumn < this.numColumns) {
				if (currentColumn < this.numColumns -1) {
					matrixString += (this.data[currentRow][currentColumn] + " ");
				}
				else if (currentColumn == (this.numColumns - 1)) { //insert new line after the last number in a row
					matrixString += (this.data[currentRow][currentColumn] + "\n");
				}
				currentColumn += 1;
			}
			currentColumn = 0;
			currentRow += 1;
		}
		return matrixString; 
	}
	
	
	
	/**
	 * Returns a new Matrix that is the result of multiplying this Matrix as the left hand side
	 * by the input matrix as the right hand side.
	 * 
	 * @param m - the Matrix on the right hand side of the multiplication
	 * @return - the result of the multiplication
	 * @throws IllegalArgumentException - if the input Matrix does not have compatible dimensions
	 * for multiplication
	 */
	public Matrix times(Matrix m) throws IllegalArgumentException {
		if (this.numColumns != m.numRows) {
			throw new IllegalArgumentException();
		}
		int[][] multipliedMatrixDimensions = new int[this.numRows][m.numColumns];
		Matrix multipliedMatrix = new Matrix(multipliedMatrixDimensions);
		int currentLeftRow = 0; //the row of the left Matrix we are currently working with
		int currentRightRow = 0; //the row of the right Matrix we are currently working with
		int currentLeftIndex = 0; //the index of the number on the above row on the left matrix that we are working with
		int currentRightIndex = 0; //the index of the number on the above row on the right matrix that we are working with
		int currentSum = 0; //the sum that we are eventually inserting into the new matrix
		int currentMultMatrixRow = 0; //variable to keep track of which row our solution goes into
		int currentMultMatrixIndex = 0; //variable to keep track of what index our solution goes into
		while (currentLeftRow < this.numRows) { //run until we've gone through all the rows in the left matrix
			while (currentRightIndex < m.numColumns) {
				while (currentRightRow < m.numRows) { //run through currentLeftRow with specified index in the rows of the right matrix
					currentSum += ((this.data[currentLeftRow][currentLeftIndex]) * (m.data[currentRightRow][currentRightIndex]));
					currentRightRow++;
					currentLeftIndex++;
				}
				multipliedMatrix.data[currentMultMatrixRow][currentMultMatrixIndex] = currentSum;
				currentMultMatrixIndex++;
				currentSum = 0;
				currentLeftIndex = 0;
				currentRightIndex++;
				currentRightRow = 0;
			}
			currentRightIndex = 0;
			currentLeftRow++;
			currentMultMatrixRow++;
			currentMultMatrixIndex = 0;
		}
		return multipliedMatrix;
	}
	
	
	
	/**
	 * Returns a new Matrix that is the result of adding this Matrix as the left hand side
	 * by the input matrix as the right hand side.
	 * 
	 * @param m - the Matrix on the right hand side of the addition
	 * @return - the result of the addition
	 * @throws IllegalArgumentException - if the input Matrix does not have compatible dimensions
	 * for addition
	 */
	public Matrix plus(Matrix m) throws IllegalArgumentException {
		if (this.numRows != m.numRows || this.numColumns != m.numColumns) {
			throw new IllegalArgumentException();
		}
		int[][] additionMatrixDimensions = new int[this.numRows][this.numColumns];
		Matrix additionMatrix = new Matrix(additionMatrixDimensions);
		int currentRow = 0; 
		int currentColumn = 0;
		while (currentRow < this.numRows) {
			while (currentColumn < this.numColumns) {
				additionMatrix.data[currentRow][currentColumn] = this.data[currentRow][currentColumn] + m.data[currentRow][currentColumn];
				currentColumn += 1;
			}
			currentColumn = 0;
			currentRow += 1;
		}
		return additionMatrix; 
	} 
	
	
	
	/**
	 * Produces the transpose of this matrix.
	 * @return - the transpose
	 */
	public Matrix transpose() {
		int[][] transposedMatrixDimensions = new int[this.numColumns][this.numRows]; //create new matrix with flipped dimensions as input matrix
		Matrix transposedMatrix = new Matrix(transposedMatrixDimensions);
		int transposingRow = 0; //the row of the original matrix that we are pulling data from
		int currentRowNumber = 0; //the index of the number in the row of the original matrix currently being pulled
		int currentColumn = 0; //the column of the transposed matrix that is currently being altered
		int currentArray = 0; //the index of the array in data that is being added to/altered (ex: index 0 of data, so the first array)
		int currentIndex = 0; //the index in each array that the data is being added to/altered (ex: index 0 of all the arrays in data)
		int currentNumberAdded = this.data[transposingRow][currentRowNumber]; //the number from the original Matrix currently being added to the transposed matrix
		while (currentColumn < this.numRows) {
			while (currentArray < this.numColumns) {
				transposedMatrix.data[currentArray][currentIndex] = currentNumberAdded;
				currentArray++;
				if (currentArray != this.numColumns) {
					//only update currentNumberAdded if we still have another inner while loop to go through
					currentRowNumber++;
					currentNumberAdded = this.data[transposingRow][currentRowNumber];
				}
			}
			currentColumn++;
			currentIndex++;
			transposingRow++;
			currentArray = 0;
			currentRowNumber = 0;
			if (currentColumn != this.numRows) { //only update currentNumberAdded if we still have another outer while loop to go through
				currentNumberAdded = this.data[transposingRow][currentRowNumber]; 
			}
		}
		return transposedMatrix;
	}
	
	
	/**
	 * DO NOT MODIFY THIS METHOD.
	 * Produces a copy of the internal 2D array representing this matrix.
	 * This method is for grading and testing purposes.
	 * @return - the copy of the data
	 */
	public int[][] getData()
	{
		int[][] retVal = new int[data.length][];
		for(int i = 0; i < data.length; i++)
			retVal[i] = data[i].clone();
		return retVal;
	}
}
