#ifndef HARUPDF_H
#define HARUPDF_H

#include <iostream>
#include "hpdf.h"

/// Author: Kira Halls
/// Class: CS3505
/// Project: Assignment 3 - Facades and Makefiles
/// @brief This class is a facade for the Haru library, which generates pdfs. This class extracts the code required
///        to place a character on a page and to save a pdf file with a given filename. It has data members that represent 
///        the HPDF_Doc representing the pdf itself and an HPDF_Page object that creates a page to add to the pdf file. 
///        Users can use this class to create a new pdf, place characters on the pdf page at a specified x and y position, and 
///        save the document under an inputted filename.
class HaruPDF 
{

private:
    HPDF_Doc pdf;
    HPDF_Page page;

public:

    /// @brief The constructor to create a new HaruPDF object. It must create a new HPDF_Doc and add to it a new HPDF_Page, as well as 
    ///        set the font details for the pdf text.
    HaruPDF();

    /// @brief This method allows a user to add a character to the HaruPDF object at a specific x and y position
    /// @param character The character to be placed on the page
    /// @param xPosition The x position that the character will be placed at
    /// @param yPosition The y position that the character will be placed at
    void placeCharacterOnPage(char character, float xPosition, float yPosition);

    /// @brief This method allows a user to save the pdf object under a specific filename
    /// @param filename The name to save the pdf as
    void savePDF(char* filename);
};

#endif