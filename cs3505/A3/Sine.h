
#ifndef SINE_H
#define SINE_H

#include <iostream> 

///Author: Kira Halls u1250109
///Class: CS 3505
///Project: Assignment 2 - Making a Class
/// @brief This class represents a Sine wave that has an amplitude, a wavelength, an angle, and a value 
/// called angleIncrement that is how much the angle should be increased. It contains declarations for 
/// the constructor, a getter for the angle called currentAngle and a getter for the height called 
/// currentHeight that calculates the current height of the sine wave using a formula. It also overloads 
/// the ++ operator to increment the sine wave angle by the angleIncrement value, and overloads the << 
/// operator to allow the program to insert a sine object after the << and it will output the current angle
/// followed by the current height. This header file declares the class and its members, but leaves the 
/// implementation to the Sine.cpp file.
class Sine {

private:
    double amplitude, wavelength, angleIncrement, angle; 
    
public: 

    /// @brief A constructor for a sine object. Takes in three parameters and creates a sine object using those values.
    /// @param amplitude A value representing the sine wave's amplitude, which is the height from the x axis to 
    ///                  the top of the wave.
    /// @param waveLength A value representing the sine wave's wavelength, which is the distance from the top of 
    ///                   one wave to the top of the next wave.
    /// @param increment The value with which to increment the wave's angle 
    Sine(double amplitude, double waveLength, double increment);

    /// @brief This is a getter function that returns the sine wave's current inner angle
    /// @return The double value that represents the sine wave's current inner angle
    double currentAngle();

    /// @brief This is a getter function for the sine wave's current height. It calculates the height 
    ///        using the equation height = (amplitude)(sin((2pi(currentAngle))/wavelength)
    /// @return The calculated value for the sine wave's height.
    double currentHeight();

    //Operators - This section declares the functions to overload the ++ and << operators
    
    /// @brief This function overloads the ++ operator to allow a sine object to call the ++ operator 
    ///        with the form ++sineObject. The sine wave's inner angle is incremented with the value contained
    ///        in the angleIncrement variable.
    /// @return The reference to the sine object that was incremented
    Sine& operator++ ();

    /// @brief This function overloads the ++ operator with a dummy int parameter to distinguish it from the 
    ///        previous function. This function allows users to use ++ as a postfix operator in the form 
    ///        sineObject++. This copies the current angle of the wave, increments it by adding the angleIncrement to the current angle,
    ///        then returns the copied value to simulate incrementing the value after using it in the expression.     
    /// @param  placeholder A dummy parameter to distinguish this function from the previous one. It is never used.
    /// @return A copy of the sine wave object before it was incremented.
    Sine operator++ (int);

    /// @brief This function overloads the << operator to allow the program to simply input a Sine object and get an 
    ///        output stream that contains the sine wave's current angle followed by the sine wave's current height.
    /// @param sineWave - the sine wave that we want to output to a stream using the overloaded << operator
    /// @return - the reference to the ostream that is outputting our data
    friend std::ostream& operator<< (std::ostream& output, Sine sineWave);

};

#endif