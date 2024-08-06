#include <iostream>
#include "Trie.h"
#include <cassert>
#include <gtest/gtest.h>

TEST(TrieTests, HandlesBasicInputIsWordAddWord)
{
    Trie testTrie = *(new Trie());
    EXPECT_FALSE(testTrie.isWord("cat")) << "failed isWord with word not in trie";
    testTrie.addWord("cats");
    EXPECT_TRUE(testTrie.isWord("cats")) << "failed isWord with word cats in trie";
    EXPECT_FALSE(testTrie.isWord("cat")) << "failed isWord with subword of word in trie";

    //Add more words to same trie
    testTrie.addWord("cat");
    testTrie.addWord("apple");
    testTrie.addWord("banana");
    testTrie.addWord("miss");

    EXPECT_TRUE(testTrie.isWord("cats")) << "failed isWord with word cats in trie";
    EXPECT_TRUE(testTrie.isWord("cat")) << "failed isWord with word cat in trie";
    EXPECT_TRUE(testTrie.isWord("apple")) << "failed isWord with word apple in trie";
    EXPECT_TRUE(testTrie.isWord("banana")) << "failed isWord with word banana in trie";
    EXPECT_TRUE(testTrie.isWord("miss")) << "failed isWord with word miss in trie";
}

TEST(TrieTests, HandlesEdgeCasesInvalidWords) 
{
    Trie testTrie = *(new Trie());
    //Fill trie with random words
    testTrie.addWord("new");
    testTrie.addWord("horizons");
    testTrie.addWord("questions");

    //Test capital letters return false, even if they spell a word in the trie
    EXPECT_FALSE(testTrie.isWord("NEW")) << "failed isWord with all capital characters in trie";

    //Test capital letters return false even if they are not a word in the trie
    EXPECT_FALSE(testTrie.isWord("Right")) << "failed isWord with some capital characters in trie";
    EXPECT_FALSE(testTrie.isWord("sEiZe")) << "failed isWord with some capital characters in trie";
    EXPECT_FALSE(testTrie.isWord("yesterdaY")) << "failed isWord with capital characters at end of word in trie";

    //Test non characters return false
    EXPECT_FALSE(testTrie.isWord("&19")) << "failed isWord with all invalid characters in trie";
    EXPECT_FALSE(testTrie.isWord("r1ght")) << "failed isWord with some invalid characters in trie";

    //Test a word in the trie with invalid characters still returns false
    EXPECT_FALSE(testTrie.isWord("-new")) << "failed isWord with some invalid characters with valid word in trie";

    //Test capital letters and non characters return false
    EXPECT_FALSE(testTrie.isWord("Right1")) << "failed isWord with combination invalid and capital characters in trie";
    EXPECT_FALSE(testTrie.isWord("doOr-")) << "failed isWord with combination invalid and capital characters in trie";

    //Test capital letters and non characters included with an actual word returns false
    EXPECT_FALSE(testTrie.isWord("-NEW")) << "failed isWord with combination invalid and capital characters in valid word in trie";
}

TEST(TrieTests, HandlesBasicAllWordsWithPrefix) 
{
    //Basic prefix test
    Trie testTrie = *(new Trie());
    testTrie.addWord("prefix");
    testTrie.addWord("prenatal");
    testTrie.addWord("preternatural");
    testTrie.addWord("prejudice");


    vector<string> correctVector;
    correctVector.push_back("prefix");
    correctVector.push_back("prejudice");
    correctVector.push_back("prenatal");
    correctVector.push_back("preternatural");
    
    vector<string> resultVector = testTrie.allWordsStartingWithPrefix("pre");
  
    ASSERT_EQ(correctVector.size(), resultVector.size()) << "failed allWordsStartingWithPrefix, vectors are wrong size";

    for (int i = 0; i < (int) correctVector.size(); i++) 
    {
        EXPECT_EQ(correctVector[i], resultVector[i]) << "failed allWordsStartingWithPrefix, vectors mismatched at index " << i;
    }
}

TEST(TrieTests, HandlesInvalidAllWordsWithPrefix) 
{
    //Invalid prefix test
    Trie testTrie = *(new Trie());
    testTrie.addWord("prefix");
    testTrie.addWord("prenatal");
    testTrie.addWord("preternatural");
    testTrie.addWord("prejudice");


    vector<string> correctVector;
    
    //Testing for invalid character
    vector<string> resultVector = testTrie.allWordsStartingWithPrefix("&");
  
    ASSERT_EQ(correctVector.size(), resultVector.size()) << "failed allWordsStartingWithPrefix invalid character, vectors are wrong size";

    for (int i = 0; i < (int) correctVector.size(); i++) 
    {
        EXPECT_EQ(correctVector[i], resultVector[i]) << "failed allWordsStartingWithPrefix invalid character, vectors mismatched at index " << i;
    }

    //Testing for capital letters
    resultVector = testTrie.allWordsStartingWithPrefix("PRE");

    ASSERT_EQ(correctVector.size(), resultVector.size()) << "failed allWordsStartingWithPrefix capital letters, vectors are wrong size";

    for (int i = 0; i < (int) correctVector.size(); i++) 
    {
        EXPECT_EQ(correctVector[i], resultVector[i]) << "failed allWordsStartingWithPrefix capital letters, vectors mismatched at index " << i;
    }
}

TEST(TrieTests, RuleOfThreeTests)
{
    //Create a trie that will be copied in two different ways 
    Trie testTrie1 = *(new Trie());
    testTrie1.addWord("your");
    testTrie1.addWord("mind");
    testTrie1.addWord("is");
    testTrie1.addWord("a");
    testTrie1.addWord("stream");

    //Copy of trie one using copy assignment operator
    Trie testTrie2 = testTrie1;

    //Test that trie2 is actual copy
    EXPECT_TRUE(testTrie2.isWord("your")) << "failed to copy a trie using assignment operator - 1";
    EXPECT_TRUE(testTrie2.isWord("mind")) << "failed to copy a trie using assignment operator - 2";
    EXPECT_TRUE(testTrie2.isWord("is")) << "failed to copy a trie using assignment operator - 3";
    EXPECT_TRUE(testTrie2.isWord("a")) << "failed to copy a trie using assignment operator - 4";
    EXPECT_TRUE(testTrie2.isWord("stream")) << "failed to copy a trie using assignment operator - 5";

    testTrie2.addWord("of");
    testTrie2.addWord("colors");

    //Copy of trie one using copy constructor
    Trie testTrie3 = *(new Trie(testTrie1));

    //test that trie3 is actual copy
    EXPECT_TRUE(testTrie2.isWord("your")) << "failed to copy a trie using copy constructor - 1";
    EXPECT_TRUE(testTrie2.isWord("mind")) << "failed to copy a trie using copy constructor - 2";
    EXPECT_TRUE(testTrie2.isWord("is")) << "failed to copy a trie using copy constructor - 3";
    EXPECT_TRUE(testTrie2.isWord("a")) << "failed to copy a trie using copy constructor - 4";
    EXPECT_TRUE(testTrie2.isWord("stream")) << "failed to copy a trie using copy constructor - 5";

    testTrie3.addWord("extending");
    testTrie3.addWord("beyond");

    //Test that all three are independent copies of one another
    EXPECT_FALSE(testTrie1.isWord("of")) << "failed making Trie copies independent - copied trie and original - 1";
    EXPECT_FALSE(testTrie1.isWord("colors")) << "failed making Trie copies independent - copied trie and original - 2";
    EXPECT_FALSE(testTrie3.isWord("of")) << "failed making Trie copies independent - two copies from same trie - 1";
    EXPECT_FALSE(testTrie3.isWord("colors")) << "failed making Trie copies independent - two copies from same trie - 2";

    EXPECT_FALSE(testTrie1.isWord("extending")) << "failed making Trie copies independent - copied trie and original - 3";
    EXPECT_FALSE(testTrie1.isWord("beyond")) << "failed making Trie copies independent - copied trie and original - 4";
    EXPECT_FALSE(testTrie2.isWord("extending")) << "failed making Trie copies independent - two copies from same trie - 3";
    EXPECT_FALSE(testTrie2.isWord("beyond")) << "failed making Trie copies independent - two copies from same trie - 4";

    testTrie1.addWord("sky");
    EXPECT_FALSE(testTrie2.isWord("sky"))  << "failed making Trie copies independent - copied tries reference original";
    EXPECT_FALSE(testTrie3.isWord("sky"))  << "failed making Trie copies independent - copied tries reference original";

    
}
