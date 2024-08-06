#include <iostream>
#include <string.h>
#include "Sine.h"
#include "HaruPDF.h"
/// Author: Kira Halls
/// Class: CS 3505
/// Project: Assignment 3 - Facades and Makefiles
/// @brief This method links the HaruPDF and Sine classes to create a new pdf that places the characters of an input text 
///        on a pdf in the shape of a sine wave. This method specifically follows a sine curve that has an amplitude 
///        of 50, a wavelength of 275, and increments the angle by 18 degrees after each character is placed. It determines 
///        the x and y positions of the character by the sine wave's current angle and current height, respectively. After 
///        placing the text on the page, it saves the pdf to "sinePDF.pdf". 
/// @param argc The number of arguments
/// @param argv An array of character arrays
/// @return 0 to indicate a successful execution of the method
int main(int argc, char **argv) 
{
    /* Get the text the user inputs on the command line*/
    const char* pdfText = argv[1]; 

    // argv[0] is the name of the executable program
    // This makes an output pdf named after the program's name
    char fileName[256];
    strcpy (fileName, argv[0]);
    strcat (fileName, ".pdf");

    HaruPDF pdf = HaruPDF();

    Sine sineWave = Sine(50, 275, 18);

    float xPosition; 
    float yPosition;

    //Loop through the string and place each character on pdf with the x and y positions calculated by the sine wave
    for (unsigned int i = 0; i < strlen (pdfText); i++) 
    { 
        /* Calculate x and y positions */
        xPosition = sineWave.currentAngle();
        yPosition = sineWave.currentHeight() + 200; //Bring the y position up 200 to center it on the pdf page

        pdf.placeCharacterOnPage(pdfText[i], xPosition, yPosition);
        sineWave++;
    }

    pdf.savePDF(fileName);
    
    return 0;
}