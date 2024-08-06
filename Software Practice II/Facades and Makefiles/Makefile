#Add and set variables 
LIBHARU = /root/cs3505/libharu
INCLUDE = ../libharu/include
SRC = ../libharu/src

sinePDF : sinePDF.o Sine.o HaruPDF.o 
	g++ -Wall -I$(LIBHARU)/include -o sinePDF sinePDF.o HaruPDF.o Sine.o -L$(LIBHARU)/src -lhpdfs

HaruPDF.o : HaruPDF.cpp HaruPDF.h
	g++ -Wall -c -I$(LIBHARU)/include HaruPDF.cpp

Sine.o : Sine.cpp Sine.h
	g++ -Wall -c Sine.cpp

sinePDF.o : sinePDF.cpp Sine.h HaruPDF.h
	g++ -Wall -c -I$(LIBHARU)/include sinePDF.cpp

clean : 
	rm sinePDF *.o
