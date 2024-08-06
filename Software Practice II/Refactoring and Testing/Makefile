GOOGLETEST = ./googletest

test : TrieTests
	./TrieTests

TrieTests : TrieTests.cpp Trie.o
	g++ -Wall -o TrieTests -std=c++2a Trie.o TrieTests.cpp -L$(GOOGLETEST)/lib -I$(GOOGLETEST)/googletest/include -lgtest_main -lpthread -lgtest

Trie.o : Trie.cpp Trie.h 
	g++ -Wall -std=c++2a -c Trie.cpp

clean : 
	rm TrieTests *.o


