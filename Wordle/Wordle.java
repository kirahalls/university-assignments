
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

// CS 1410 Assignment 3-Wordle created by Kira Halls

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This program recreates the popular game Wordle. The user has six chances
 * to correctly guess a randomly generated secret word, with feedback after each 
 * guess to help them. 
 * @author Kira Halls
 *
 */
public class Wordle {
	
	/**
	 * Creates an array of strings from a given file filled with strings
	 * @param givenFileName The given file name to open and create the array from
	 * @return an array that lists each string in the given file
	 * @throws FileNotFoundException
	 */
	public static String[] wordsFromFile(String givenFileName) throws FileNotFoundException {
		String fileName = givenFileName;
		File file = new File(fileName);
			Scanner fs = new Scanner(file);
			int currentArraySize = (fs.nextInt());
			fs.nextLine();
			String[]  dataArray = new String[(currentArraySize)];
			for (int count = 0; count < currentArraySize; count++) {
					String nextLine = fs.nextLine();
					dataArray[count] = nextLine;
				}
			fs.close();
			return dataArray;
			}
		
	/**
	 * Given an array of strings, chooses a random word from those choices
	 * @param stringChoices A list of strings to choose the random word from
	 * @return A randomly generated word from the given list of string choices
	 */
	public static String pickRandomWord(String[] stringChoices) {
		 Random generator = new Random();
		 int randomWordIndex = generator.nextInt(stringChoices.length - 1);
		 String randomWord = stringChoices[randomWordIndex];
		 return randomWord;
	}
	
	/**
	 * Checks if a certain string is contained within a given array of strings
	 * @param chosenString String that is being matched to a word in the array
	 * @param arrayOfStrings the array of strings being checked
	 * @return true or false based on whether or not the string is in the array
	 */
	public static boolean wordInArray(String chosenString, String[] arrayOfStrings) {
		for (String val:arrayOfStrings) {
			if (chosenString.equals(val)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the user's guess about the word
	 * @param arrayOfStrings An array containing the strings to input into
	 * above methods to generate word to guess
	 * @return the string the user inputs
	 * @throws FileNotFoundException
	 */
	public static String getUserGuess(String[] arrayOfStrings)  {
		Scanner inputScanner = new Scanner(System.in);
		String input = null;
		int count = 0;
		while (true) { 
			System.out.println("Please enter a 5 letter word");
			input = inputScanner.next();
			if (input.length() == 5) {
				if (wordInArray(input, arrayOfStrings)) {
					break;
				}
		}
			else {
				System.out.println("Invalid. Please enter a 5 letter word.");
			}
		}	
		return input;
		}
	
	/**
	 * A method that tells the user whether or not each character in the inputted
	 * word is contained in the secret word
	 * @param chosenChar The current character being checked, taken from 
	 * the inputed word
	 * @param chosenString The string to check the characters against
	 * @return Return true if the character is contained within the selected word,
	 * false if it is not 
	 */
	public static boolean letterInWord(char chosenChar, String chosenString) {
		int count = 0;
		while (count < chosenString.length()) {
			if (chosenString.charAt(count) == chosenChar) {
				return true;
			}
			else {
				count++;
			}
		}
		return false;
	}
	
	/**
	 * Creates a string following a given cipher that tells the user whether their 
	 * guess has the correct letters and their placement
	 * @param guessedString The user's guessed word
	 * @param secretWord The word the user is trying to guess
	 */
	public static void displayMatching(String guessedString, String secretWord) {
		String codedString = "";
		int count = 0;
		int correctCount = 0;
		while (count < guessedString.length()) {
			char currentChar = guessedString.charAt(count);
			if (currentChar == secretWord.charAt(count)) {
				codedString += Character.toUpperCase(currentChar);
				count++;	
			}
			else if (currentChar != secretWord.charAt(count) && letterInWord(currentChar, secretWord)) {
				codedString += currentChar;
				count++;
			}
			else if (letterInWord(currentChar, secretWord) == false) {
				codedString += '-';
				count++;
			}
		}
	System.out.print(codedString);
	System.out.println();
	
	}
	
	/**
	 * The execution of the program. Gets the user's guess until they guess
	 * correctly or they surpass six turns. Prints out feedback after each guess
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) {
		try {
			wordsFromFile("words.txt");
			String secretWord = pickRandomWord(wordsFromFile("words.txt"));
			//aSystem.out.println(secretWord);
			int count = 0;
			while (count < 6) {
				String userGuess = getUserGuess(wordsFromFile("words.txt"));
				displayMatching(userGuess, secretWord);
				count++;
				if (userGuess.equals(secretWord)) {
					System.out.print("Congratulations, you've guessed it!");
					System.exit(0);
				}
			}
			System.out.println("Sorry, the secret word was " + secretWord);
			System.exit(0);
		} catch (FileNotFoundException e) {
			System.out.println("file not found exception");
			e.printStackTrace();
		}
	}
}
