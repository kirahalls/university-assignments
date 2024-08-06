#include <iostream>
#include <fstream>
#include "Trie.h"
using namespace std;
/// Author: Kira Halls
/// Class: CS 3505
/// Assignment: A4 - Trie and Rule of Three
///
/// @brief The class tests the Trie class by reading words from a file inputted by the user,
///        and adding them to a new trie. Another file is used to query the trie to test 
///        the method isWord. The rule of three implementation is also tested by creating 
///        a new trie, then calling a copy constructor and the copy assignment operator on it
///        and performing basic tests to show that they are all truly independent copies. 
/// @param argc The number of arguments
/// @param argv The file names inputted by the user
/// @return 0 to indicate successful execution of the method.
int main(int argc, char **argv) 
{
    Trie* trie = new Trie();
    string line;
    const string filename = argv[1]; 
    ifstream wordFile (filename);
    if (wordFile.is_open())
    {
        while ( getline (wordFile,line) )
        {
            (*trie).addWord(line);
        }
        wordFile.close();
    }

    
    const string queries = argv[2]; 
    ifstream queryFile (queries);
    if (queryFile.is_open())
    {
        while ( getline (queryFile,line) )
        {
            cout << "Checking " + line + ": ";
            if ((*trie).isWord(line) == true)
            {
                cout << "Word found" << endl;
            }

            else 
            {
                cout << "Word not found" << endl;
            }
        }
        queryFile.close();
    }
    
    vector<string> textWords = (*trie).allWordsStartingWithPrefix("text");
    for (string words : textWords) 
    {
        cout << words + " ";
    }

    cout << "" << endl;

    delete trie;

    //Testing Rule-Of-Three
    Trie ruleTrie;

    ruleTrie.addWord("your");
    ruleTrie.addWord("mind");
    ruleTrie.addWord("is");
    ruleTrie.addWord("a");
    ruleTrie.addWord("stream");
    ruleTrie.addWord("of");
    ruleTrie.addWord("colors");

    Trie* copyConstructorTrie(&ruleTrie);

    Trie* assignmentTrie;
    assignmentTrie = &ruleTrie;

    //Testing to ensure adding word to one tree doesn't add to others
    //i.e. copies are truly independent
    (*copyConstructorTrie).addWord("light");

    //Should print out 0
    cout << ruleTrie.isWord("light") << endl;
    cout << (*assignmentTrie).isWord("light") << endl;

    (*assignmentTrie).addWord("love");

    //Should print out 0
    cout << ruleTrie.isWord("love") << endl;
    cout << (*copyConstructorTrie).isWord("love") << endl;

    ruleTrie.addWord("sky");
    
    //Should print out 0
    cout << (*copyConstructorTrie).isWord("sky") << endl;
    cout << (*assignmentTrie).isWord("sky") << endl;
}