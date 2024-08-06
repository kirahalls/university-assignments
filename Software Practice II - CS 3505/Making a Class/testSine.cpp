///Author: Kira Halls u1250109
///Class: CS3505
///Project: Assignment 2 - Making a Class

#include "Sine.h"
#include <iostream>

int main() 
{
    Sine sineWave(3.0, 400.0, 5.0); //make a sine object with random values
    for (int i = 0; i < 361; i++) //loop 361 times, print out the sine wave, and increment it
    {
        std::cout << sineWave << std::endl;
        sineWave++;
    }
}