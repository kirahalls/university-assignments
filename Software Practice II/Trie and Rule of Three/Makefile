INCLUDE = ../cs3505

TrieTest : TrieTest.o Trie.o
	g++ -Wall -g -o TrieTest -fsanitize=address -fsanitize=undefined TrieTest.o Trie.o

Trie.o : Trie.cpp Trie.h 
	g++ -Wall -g -c -fsanitize=address -fsanitize=undefined Trie.cpp

TrieTest.o : TrieTest.cpp Trie.h 
	g++ -Wall -g -c -fsanitize=address -fsanitize=undefined TrieTest.cpp

clean : 
	rm TrieTest *.o


