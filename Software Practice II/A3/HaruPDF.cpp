#include <iostream>
#include <string.h>
#include <math.h>
#include "HaruPDF.h" 

/// Author: Kira Halls
/// Class: CS3505
/// Project: Assignment 3 - Facades and Makefiles

HaruPDF::HaruPDF() 
{
    pdf = HPDF_New (NULL, NULL); //create a new pdf
    page = HPDF_AddPage (pdf); //add a new page object

    HPDF_Page_SetSize (page, HPDF_PAGE_SIZE_A5, HPDF_PAGE_PORTRAIT);

    /* Set font details */
    HPDF_Font font = HPDF_GetFont (pdf, "Courier-Bold", NULL);
    HPDF_Page_SetTextLeading (page, 20);
    HPDF_Page_SetGrayStroke (page, 0);
    HPDF_Page_SetFontAndSize (page, font, 30);
} 

void HaruPDF::placeCharacterOnPage(char character, float xPosition, float yPosition) 
{
    HPDF_Page_BeginText (page);
    
    char buf[2];

    /* Determines where any following text will be placed on the page. The cos and sin expressions ensure the text is not rotated when placed*/
    HPDF_Page_SetTextMatrix (page,
                                cos(0), sin(0), -sin(0), cos(0),
                                xPosition, yPosition);

    buf[0] = character; // The character to display
    buf[1] = 0;

    HPDF_Page_ShowText (page, buf);

    HPDF_Page_EndText (page);
}

void HaruPDF::savePDF(char* filename) 
{
    /* save the document to a file */
    HPDF_SaveToFile (pdf, filename);

    /* clean up */
    HPDF_Free (pdf);
}