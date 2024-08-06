#include <iostream>
#include "Trie.h"

using namespace std;

/// Author: Kira Halls
/// Class: CS 3505
/// Assignment: Trie and Rule of Three 
/// @brief The implementation of the Trie_h file. 
Trie::Trie() 
{
    for (int i = 0; i < 26; i++) 
    {
        branches[i] = nullptr; 
    }
    wordFlag = false;
}

Trie::~Trie() 
{   
    for (int i = 0; i < 26; i++) 
    {
        delete branches[i];
    }
}

Trie::Trie(const Trie& that) 
{
    for (int i = 0; i < 26; i++) 
    {
        if ((that.branches[i]) != nullptr) 
        {
            branches[i] = new Trie(*(that.branches[i])); //Create a copy of each trie in the array

        }      
    }

    wordFlag = that.wordFlag;
}

Trie& Trie::operator=(Trie that)
{
    swap(branches, that.branches);
    swap(wordFlag, that.wordFlag);

    return *this;
}

void Trie::addWord(string word)
{
    //Base case. If we have reached the end of the word set the word flag to true
    if (word.length() == 0) 
    {
        (*this).wordFlag = true;  
    }

    else 
    {
        int index = word[0] - 'a';
        
        //If the index contains a null pointer, create a new trie to be held in the array at that index
        if (((*this).branches[index]) == nullptr) 
        {
            Trie* newTrie = new Trie();
            (*this).branches[index] = newTrie;
            (*newTrie).addWord(word.substr(1, word.length())); //Recursion, use the new trie to call addWord with the substring
        }

        else 
        {
            (*(*this).branches[index]).addWord(word.substr(1, word.length())); //Recursion, use the new trie to call addWord with the substring
        }
    }
}

bool Trie::isWord(string word) 
{
    for (int i = 0; i < (int) word.size(); i++) 
    {
        //Return false if it contains invalid characters
        if (!(word[i]>=97 || word[i]<=122)) 
        {
            return false;
        }
    }

    //If the word is longer than a single character then recurse
    if (word.size() != 2) 
    {
        int index = word[0] - 'a';
        if ((*this).branches[index] == nullptr) 
        {
            return false;
        }
        return (*(*this).branches[index]).isWord(word.substr(1, word.size()));
    }

    //Base case, if the word only has one character left then return the wordFlag
    else 
    {
        int index = word[0] - 'a';
        if ((*(*this).branches[index]).wordFlag == true)
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    return false;
}

vector<string> Trie::prefixHelper(string prefix) 
{
    vector<string> allWords;

    //If we have reached the end of the word, push it to the vector
    if ((*this).wordFlag == true)
    {
        allWords.push_back(prefix);
    }

    //Loop through all child tries and their arrays to find all words with prefix
    for (int i = 0; i < 26; i++) 
    {
        if ((*this).branches[i] != nullptr) 
        {
            string newPrefix = prefix + (char(i + 'a'));
            vector<string> tempVector = ((*(*this).branches[i]).prefixHelper(newPrefix));
            allWords.insert(allWords.end(), tempVector.begin(), tempVector.end());
        }
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
        if ((!(islower(prefix[i]))) || isdigit(prefix[i])) 
        {
            //return empty vector if any invalid characters are included
            return allWords;
        }
    }

    //Add prefix to the vector if the prefix itself is a word
    if (isWord(prefix))
    {
        allWords.push_back(prefix);
    }

    Trie* endPrefix = this;
    for (int i = 0; i < (int) prefix.size(); i++) 
    {
        int index = prefix[i] - 'a';
        if ((*endPrefix).branches[index] == nullptr) 
        {
            return allWords;
        }
        else 
        {
            endPrefix = (*endPrefix).branches[index];
        }
    }

    vector<string> tempVector = (*endPrefix).prefixHelper(prefix);
    allWords.insert(allWords.end(), tempVector.begin(), tempVector.end());

    return allWords;
}