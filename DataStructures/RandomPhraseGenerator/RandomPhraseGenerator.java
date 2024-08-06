package comprehensive;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * This class generates random phrases from an inputted grammar file. The grammar file provides the structure of the sentence through terminals and non terminals. Non-terminals are indicated
 * by <> around the non-terminal descriptive word. Any line contained outside {} is a comment and should be ignored. Each non-terminal is contained in the instance variable HashMap as a key,
 * with an ArrayList of Strings containing all potential values for the non-terminal. The phrase must randomly select a phrase structure for each non-terminal contained in the sentence structure at
 * the beginning of the file. This class must output a sentence following the given grammar structure, and must include all original punctuation and whitespaces. 
 * 
 * @author Kira Halls and Nandini Goel
 *
 */
public class RandomPhraseGenerator {
	private Scanner fileReader;
	private StringBuilder phrase;
	private HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
	
	/**
	 * The constructor for the RandomPhraseGenerator. Creates the hashmap containing all non-terminals and their 
	 * associated phrase options. Creates a Scanner to read the inputted file. 
	 * 
	 * @param filename The name of the file containing the grammar rules
	 */
	public RandomPhraseGenerator(String filename) {
		try {
			File file = new File(filename);
			fileReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.generateMap();
	}
	
	/**
	 * This method begins the phrase generation. It takes a random sentence structure from the "start" key in the 
	 * hashmap and sets it into an array split by the non-terminal symbols. Every terminal is added to the completed phrase, 
	 * and every non-terminal is sent to generateSubPhrase, a helper method to expand the subphrase until there are no more nonterminals. 
	 * Stores the phrase in the phrase instance variable. 
	 */
	public void generatePhrase() {
		phrase = new StringBuilder();
		Random random = new Random();
		ArrayList<String> startPhrase = map.get("<start>");
		String startTerminal = startPhrase.get(random.nextInt(startPhrase.size()));
		String[] phraseStructure = startTerminal.split("((?<=<)|(?=>))"); //isolate non terminals by splitting the phrase around <>
		for (int i = 0; i < phraseStructure.length; i++) {
			//checks if phrase is a non terminal. Non terminals indicated here by a '>' at the beginning of the next item.
			 if ((i < phraseStructure.length - 1 && phraseStructure[i + 1].charAt(0) == '>')) { //if the current phrase is a non-terminal, recursively call this method to get another subphrase
				 ArrayList<String> subPhrases = map.get('<' + phraseStructure[i]	+ '>');
				 String newPhrase = subPhrases.get(random.nextInt(0, subPhrases.size())); 
				phrase.append(generateSubPhrase(newPhrase));
			} else { //if the current phrase is a terminal, it may be added as is. Also removes the non-terminal bracket indicators.
				String trimmedString = ""; //must create a separate variable in case it just contains a whitespace. Ensures no extra whitespaces are added
				trimmedString += phraseStructure[i];
				trimmedString = trimmedString.replaceAll("<", "");
				trimmedString = trimmedString.replaceAll(">", "");
				if (trimmedString != " ") { //No extra spaces need to be added, so only append if there is a value contained in trimmedString
					phrase.append(trimmedString);
				}
			}
		}
	}
	
	/**
	 * This helper method aids in the phrase generation. For every non-terminal in generatePhrase(), it gets sent to generateSubPhrase(). This 
	 * method uses recursion to expand the subphrase until there are no more non-terminals contained within it, at which point, it is returned to 
	 * generatePhrase() where it is added to the final phrase. 
	 * 
	 * @param subphrase The non-terminal subphrase being generated, selected from the ArrayList contained at that non-terminal key in the map
	 * @return The subphrase, eventually expanded until it contains no more non-terminals
	 */
	public String generateSubPhrase(String subphrase) {
		String[] splitPhrase = subphrase.split("((?<=<)|(?=>))"); //isolate non-terminals by splitting the phrase around <>
		String completedPhrase = "";
		Random random = new Random();
		for (int i = 0; i < splitPhrase.length; i++) {
			//checks if phrase is a non terminal. Non terminals indicated here by a '>' at the beginning of the next item.
			if ((i < splitPhrase.length - 1 && splitPhrase[i + 1].charAt(0) == '>')) {  //if the current phrase is a non-terminal, recursively call this method to get another subphrase
				ArrayList<String> subPhrases = map.get('<' + splitPhrase[i] + '>');
				String newPhrase = subPhrases.get(random.nextInt(0, subPhrases.size()));
				completedPhrase += this.generateSubPhrase(newPhrase);
			} else { //if the current phrase is a terminal, it may be added as is. Also removes the non-terminal bracket indicators.
				completedPhrase += splitPhrase[i];
				completedPhrase = completedPhrase.replaceAll("<", "");
				completedPhrase = completedPhrase.replaceAll(">", "");
			}
		}
		return completedPhrase.substring(0, completedPhrase.length()); 
	}
	
	/**
	 * This helper method puts all non-terminals in the instance variable HashMap and stores an ArrayList of Strings containing each possible replacement for 
	 * the non-terminal key. Uses a helper method isNonTerminal() to check if a value is a non-terminal. The isNonTerminal() helper method adds the key and an empty
	 * ArrayList of Strings to the hashmap for later use.
	 */
	public void generateMap() {
		String currentLine = fileReader.nextLine();
		String nonTerminal;
		while (fileReader.hasNextLine()) {
			if (this.isNonTerminal(currentLine)) {
				nonTerminal = currentLine;
				currentLine = fileReader.nextLine();
				ArrayList<String> valueList = map.get(nonTerminal);
				while (!(currentLine.equals("}"))) { //add every line after the non-terminal header but before the ending curly brace to the ArrayList for that non-terminal object
					valueList.add(currentLine);
					currentLine = fileReader.nextLine();
				}
			} else {
				currentLine = fileReader.nextLine(); //if the line is not a non-terminal or a line contained within the non-terminal options, pass over it.
			}
		}
	}
	
	/**
	 * This helper method checks if the parameter value is a non-terminal by checkign if it has the required '<' opening bracket, 
	 * as well as if it contains any illegal whitespaces. If the value is a nontermial, it adds the value and an empty ArrayList of strings to the 
	 * HashMap to be stored for later use. It returns true if the value is a non-terminal, false if not.
	 * 
	 * @param currentLine The line that is checked for non-terminal characteristics 
	 * @return True if the parameter value is a non-terminal, false otherwise.
	 */
	private boolean isNonTerminal(String currentLine) {
		if (currentLine.length() == 0) {
			return false;
		}
		if (currentLine.charAt(0) == '<') { 
			if (currentLine.contains(" ")) { //if any whitespaces are contained within the non-terminal brackets, it is an illegal argument and cannot be used
				return false;
			}
			map.put(currentLine, new ArrayList<String>());
			return true;
		}
		return false;
	}
	
	/**
	 * This main method executes the generatePhrase() method. It takes in a filename from the user, and an int that controls the number of different phrases that are generated. 
	 * Prints out each randomly generated phrase. 
	 * 
	 * @param args The values inputted from the command line. args[0] is the filename of the grammar file, args[1] is the int for the number of phrases to be generated.
	 */
	public static void main(String[] args) {
		RandomPhraseGenerator generator = new RandomPhraseGenerator(args[0]);
		for (int i = 0; i < Integer.parseInt(args[1]); i++) {
			generator.generatePhrase();
			System.out.println(generator.phrase);
		}
	}
}
