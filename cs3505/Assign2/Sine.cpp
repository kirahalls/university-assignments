///Author: Kira Halls u1250109
///Class: CS3505
///Project: Assignment 2 - Making a Class

#define _USE_MATH_DEFINES

#include "Sine.h"
#include <iostream>
#include <cmath>


Sine::Sine(double curveAmplitude, double curveWavelength, double increment) 
{
    amplitude = curveAmplitude; 
    wavelength = curveWavelength;
    angleIncrement = increment;
    angle = 0; //the inner angle always starts at 0
}

double Sine::currentAngle() 
{
    return angle;
}

double Sine::currentHeight() 
{
    double sineInput = (2 * M_PI * angle) / wavelength; //calculate the angle to input into sine in the equation
    return (amplitude * sin(sineInput));
}

//General code structure for this method taken from stack overflow provided in assignment description
//https://stackoverflow.com/questions/3846296/how-to-overload-the-operator-in-two-different-ways-for-postfix-a-and-prefix
Sine& Sine::operator++ () 
{
    angle += angleIncrement;
    return *this;
}

//General code structure for this method taken from stack overflow provided in assignment description
//https://stackoverflow.com/questions/3846296/how-to-overload-the-operator-in-two-different-ways-for-postfix-a-and-prefix
Sine Sine::operator++(int placeholder)
{
    Sine result(*this); //make a copy so it returns old value while still incrementing it
    ++(*this);
    return result; 
}

 std::ostream& operator<< (std::ostream& output, Sine sineWave) 
{
    output << sineWave.currentAngle() << ", " << sineWave.currentHeight();
    return output;
}

