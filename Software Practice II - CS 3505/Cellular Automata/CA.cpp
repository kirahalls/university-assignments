#include <iostream>

using std::cout;
using std::cin;
using std::endl;

/*
Author: Kira Halls
Assignment: 1D Cellular Automata
Class: CS 3505

Program Description: 
This program calculates and prints out fifty generations of 1D cellular automatas. It asks the user for a number from 0 to 255, which will be used as the ruleset to determine what values the next generation will contain. 
*/

int convertNeighborhoodToIndex(int left, int current, int right); 

/*
This function implements an algorithm to convert an integer (inputted by the user) to its binary 
equivalent. The algorithm takes in the number, subtracts a one if it is odd and puts a 1 in the next index in the 
ruleSet array (starting with index 0), then divides the number by two. If the number is even, it places a 
0 in the next index in the ruleSet array and divides the number by two. This is repeated until the number is 0. 
If the binary number is less than eight bits long, the remaining array spaces are filled with zeroes. 
*/
void convertRuleSetNumberToRuleSetArray(int ruleSetNumber, int ruleSetArray[]) 
{
    int currentIndex = 0;
    while (ruleSetNumber > 0) 
    {
        if (ruleSetNumber % 2 == 1) //if number is odd, insert 1 in array, subtract number by one and divide by two
        {
            ruleSetArray[currentIndex] = 1;
            ruleSetNumber--;
            ruleSetNumber = ruleSetNumber / 2;
        }
        else //if number is even, insert 0 in array, and divide number by two
        {
            ruleSetArray[currentIndex] = 0;
            ruleSetNumber = ruleSetNumber / 2;
        }
        currentIndex++;
    }
    while (currentIndex < 8) //if array is not filled after converting number to binary, fill rest of array with zeroes
    {
        ruleSetArray[currentIndex] = 0;
        currentIndex++;
    }
}

/*
This function takes in the current generation and prints it out. It loops 
over the array and prints out a space if the number is a 0, and a # if the 
number is a one. It adds a single end line once all the elements in the array 
have been printed out so that each generation is on its own line.
*/
void displayCurrentGeneration(int currentGen[], int genArrayLength) 
{
    for (int i = 0; i < genArrayLength; i++)
    {
        if (currentGen[i] == 1) 
        {
            cout << "#";
        }
        else 
        {
            cout << " ";
        }
    }
    cout << "" << endl; //insert end line after generation has been printed out
}

/*
This function is responsible for computing the next generation. It copies the first and last 
elements from the current generation and inserts them into the first and last places of the next 
generation. From there, it loops through the current generation array and calls the function
convertNeighborhoodToIndex using the current index, the previous index, and the next index. 
The function returns an integer. The integer will be used as an index for the ruleSet array, and the 
number contained at that index will be placed in the new generation array at the current index. This will 
be repeated until the new generation array is completely filled up.
*/
void computeNextGeneration(int currentGen[], int nextGen[], int generationLength, int ruleSet[]) 
{
    //copy first and last element from the current generation to the next generation
    nextGen[0] = currentGen[0];
    nextGen[generationLength - 1] = currentGen[generationLength - 1];

    int ruleSetIndex; //used to hold the index calculated by convertNeighborhoodToIndex

    //loop over array starting at index 1 and ending at the second to last index
    for (int i = 1; i < generationLength - 1; i++) 
    {
        ruleSetIndex = convertNeighborhoodToIndex(currentGen[i - 1], currentGen[i], currentGen[i + 1]);
        nextGen[i] = ruleSet[ruleSetIndex];
    }
}

/*
This function takes in three int parameters containing either a zero or a one, the combination of which 
represents a binary number. This function converts the binary number to a base ten integer and returns it.
*/
int convertNeighborhoodToIndex(int left, int current, int right) 
{
    return ((left * 4) + (current * 2) + (right * 1));
}

/*
The main function that executes the program. It asks the user for a number between 0 and 255, checks to ensure it is a valid input (displaying an error message and exiting the program if not),
then calls the proper functions to convert the rule into a binary number and store it in an array. The function then creates the first generation and loops forty nine times 
to compute and display the next forty nine generations. It ends the program by returning zero once all fifty generations are printed out.
*/
int main() 
{
    int rule;

    //Ask for user input to determine rule number
    cout << "Please input a number from 0 to 255" << endl;
    cin >> rule;
    if ((cin.fail())) //If cin does not parse a number, give explanatory message and end program
    {
        cout << "Sorry! That is not a valid input" << endl;
        return 0;
    } 
    else {}
    
    //Ensure number is within accepted range. If not, display explanatory message and end the program.
    if (rule < 0 || rule > 255) 
    {
        cout << "Sorry! That number is not within the accepted range" << endl;
        return 0;
    }

    //If number is in accepted range, execute the program
    int ruleSetArray[8] = {};
    convertRuleSetNumberToRuleSetArray(rule, ruleSetArray);

    int currentGen[64] = {};
    int previousGen[64] = {}; //will be used to temporarily store the current generation while calculating the next generation

    //generate first generation (all zeroes except for a 1 at index 32)
    for (int i = 0; i < 64; i++) 
    {
        currentGen[i] = 0;
    }
    currentGen[32] = 1;
    displayCurrentGeneration(currentGen, 64); //display first generation

    for (int i = 2; i <= 50; i++) //start at 2 to represent generation 2 and ending at generation 50
    {
        //copy current generation over to previous generation to allow the current generation to be overwritten by next generation during calculation without error
        for (int i = 0; i < 64; i++) 
        {
            previousGen[i] = currentGen[i];
        }
        computeNextGeneration(previousGen, currentGen, 64, ruleSetArray);
        displayCurrentGeneration(currentGen, 64);
    }
    return 0;
}