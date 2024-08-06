#include <iostream>
#include "Trie.h"

using namespace std;

/// Author: Kira Halls
/// Class: CS 3505
/// Assignment: Trie and Rule of Three 
/// @brief The implementation of the Trie_h file. 
Trie::Trie() : endOfWord(false) {}

void Trie::addWord(string word)
{
    
    //Base case. If we have reached the end of the word set the word flag to true
    if (word.length() == 0) 
    {
        (*this).endOfWord = true;  
    }

    else 
    {  
        char firstLetter = word[0];

        //If the map doesn't contain a trie at the key associated with the first character in the word, add a new trie
        if (!((*this).branches.contains(firstLetter))) 
        {
            (*this).branches[firstLetter] = *(new Trie());
        }
        
        string subword = word.substr(1, word.length());
        ((*this).branches[firstLetter]).addWord(subword); //Recursion, use the new trie to call addWord with the substring
       
    }
}

bool Trie::isWord(string word) 
{
    
    for (int i = 0; i < (int) word.size(); i++) 
    {
        //Return false if it contains invalid characters
        if (!(word[i] >= 97 || word[i] <= 122)) 
        {
            return false;
        }
    }

    char firstLetter = word[0];
    //If the word is longer than a single character then recurse
    if (word.size() != 1) 
    {
        if ((*this).branches.contains(firstLetter))
        {
            return ((*this).branches[firstLetter]).isWord(word.substr(1, word.size()));
        }
        return false;
    }

    //Base case, if the word only has one character left then return the endOfWord indicator
    else 
    {
        //If the current trie contains that letter and if the letter indicates the end of a word, return true
        if ((*this).branches.contains(firstLetter)) 
        {
            if (((*this).branches[firstLetter]).endOfWord) 
            {
                return true;
            }
        }

        return false;
       
    }
    
}

vector<string> Trie::prefixHelper(Trie rootNode, string prefix) 
{
    vector<string> allWords;

    if (rootNode.isWord(prefix) == true )
    {
        allWords.push_back(prefix);
    }

    for (auto currentChar : (*this).branches ) 
    {
        string newPrefix = prefix + (currentChar.first);
        vector<string> tempVector = (currentChar.second).prefixHelper(rootNode, newPrefix);
        allWords.insert(allWords.end(), tempVector.begin(), tempVector.end());
    }

    return allWords;
}

vector<string> Trie::allWordsStartingWithPrefix(string prefix) 
{
    vector<string> allWords;

    //Return empty vector if prefix is empty
    if (prefix == "")
    {
        return allWords;
    }

    //Loop over each character to ensure no invalid characters are included
    for (int i = 0; i < (int) prefix.size(); i++) 
    {
        if ((!(prefix[i]>=97 || prefix[i]<=122)))
        {
            //return empty vector if any invalid characters are included
            return allWords;
        }
    }

    Trie* currentTrie = this;
    for (int i = 0; i < (int) prefix.size(); i++) 
    {
        if ((*currentTrie).branches.contains(prefix[i])) {
            currentTrie = &((*currentTrie).branches[prefix[i]]);
        }
    }

    vector<string> tempVector = (*currentTrie).prefixHelper((*this), prefix);
    allWords.insert(allWords.end(), tempVector.begin(), tempVector.end());

    return allWords;
}