#ifndef TRIE_H
#define TRIE_H

#include <iostream>
#include <vector>
#include <map>

using namespace std;

/// Author: Kira Halls
/// Class: CS 3505
/// Assignment: A4 - Trie and Rule of Three
///
/// @brief This header file defines the Trie class. It creates a trie that stores 
///        a fixed length array of pointers to other Trie objects, each of which 
///        represent a letter of the alphabet. It follows the rule of three implementation
///        and explicitly declares a copy constructor, copy assignment operator, and destructor
///        It has a method to add a word to the trie, check if a certain word is in the trie,
///        and a method to return a list of every word beginning with a specific prefix.
class Trie {

private: 
    map<char, Trie> branches;
    bool endOfWord;

    /// @brief This method is a helper method for the method allWordsStartingWithPrefix. 
    ///        It is called once the main prefix method reaches the trie associated with the end 
    ///        of the prefix. This method loops through every child trie and array to 
    ///        check what words are associated with that specific prefix. It adds each found 
    ///        word to a vector of strings and returns it to the main method to be added.
    ///        This method uses recursion to accomplish its task.
    /// @param  The prefix to use
    /// @return A vector of strings containing all the words beginning with the prefix contained in the trie
    vector<string> prefixHelper(Trie rootNode, string prefix);

public:

    /// @brief The default constructor for a trie object
    Trie();

    /// @brief Add an inputted word to the trie object. Each character should indicate 
    ///        that there is another trie object in associated index of the current trie's
    ///        branches array. If there is no pointer already in the array at that index, it creates 
    ///        a new trie for that index and continues adding the word. This method uses recursion
    ///        to accomplish this task.
    /// @param The word to be added 
    void addWord(string word);

    /// @brief This method recurses through a trie to see if a word has been placed 
    ///        in the trie by either returning false when it reaches a null pointer before 
    ///        the word is fully completed, or by checking the wordFlag at the trie associated
    ///        with the last letter. 
    /// @param  The word to check 
    /// @return true if the word has been added to the trie, false if not
    bool isWord(string word);


    /// @brief This method checks the trie and returns every word that begins with the 
    ///         associated prefix. It checks the prefix for invalid characters, then it 
    ///         traverses the tree until it reaches the trie associated with the last letter 
    ///         in the prefix. It checks if that is a word, adds it to the vector if it is, and 
    ///         then  calls the helper method to check all the child tries for the rest of the words.
    /// @param  The prefix to use
    /// @return A vector of strings containing all the words beginning with the prefix contained in the trie
    vector<string> allWordsStartingWithPrefix(string prefix);

};

#endif