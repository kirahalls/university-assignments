```
Author:     Kira Halls and Nandini Goel
Partner:    None/NA
Course:     CS 3500, University of Utah, School of Computing
GitHub ID:  kirahalls, itsnandinigoel
Repo:       https://github.com/uofu-cs3500-spring23/assignment-six---gui---functioning-spreadsheet-cs3500_khng 
Date:		March 3, 2023
Project:    Spreadsheet
Copyright:  CS 3500 and Kira Halls, Nandini Goel - This work may not be copied for use in Academic Coursework.
```

# Description

This class creates a window that displays the spreadsheet and allows the user all functionality as coded in the spreadsheet class (allows formulas to be evaluated, cells contain contents and values, cells can use other cells as variables, etc.) 
It initializes a window showing the spreadsheet with an empty grid showing the cells, column headers containing letter labels A through Z, row labels containing numbers 1 through 10, and has an alterable color scheme. It has an editable widget that 
shows the cell name, cell value, and an editable box that displays and alters the selected cell contents.It also has a file menu that contains options for saving the spreadsheet, opening a new spreadsheet, opening a previously saved spreadsheet, 
changing the color scheme, and a help menu that guides the user through cell navigation, expected cell contents, and the special feature of the spreadsheet that allows them to change the color scheme. The color scheme special feature alters the grid color, 
the row and column header color, and the highlighted cell color. Users can choose from the following color schemes: Default, Red, Orange, Yellow, Green, Blue, and Purple. 

# Comments to Evaluators:

No additional comments to evaluators, our work for this assignment stands on its own.

# Assignment Specific Topics

No additional writeup was specified for this assignment.

# Consulted Peers:

We did not consult any other peers for this assignment, as I found that all my questions were answered through lecture,
Piazza, or through simple research (for syntax related questions).

# References:
1. ScrollView - Used to make column headers scroll alongside grid - https://learn.microsoft.com/en-us/dotnet/maui/user-interface/controls/scrollview?view=net-maui-7.0
2. Grid - Used to understand grid functionality while implementing our spreadsheet - https://learn.microsoft.com/en-us/dotnet/maui/user-interface/layouts/grid?view=net-maui-7.0
3. Layouts - Used to understand different potential layouts and understand our chosen layout - https://learn.microsoft.com/en-us/dotnet/maui/user-interface/layouts/?view=net-maui-7.0
4. Can I access... - Used to pass the current page into each entry object as an instance variable for later use - https://stackoverflow.com/questions/2957900/can-i-access-outer-class-objects-in-inner-class
5. Focus Cell... - Used to understand autofocusing cell and how to implement - https://piazza.com/class/lcnzpmpokoq4zi/post/1196
6. Solid Color Brushes - Used while creating our color scheme special feature - https://learn.microsoft.com/en-us/dotnet/maui/user-interface/brushes/solidcolor?view=net-maui-7.0
7. Display Pop Ups - Used to create pop ups for saving, opening, etc. - https://learn.microsoft.com/en-us/dotnet/maui/user-interface/pop-ups?view=net-maui-7.0
8. Convert Class - Used to get syntax for converting a string to an int - https://learn.microsoft.com/en-us/dotnet/api/system.convert?view=net-7.0
9. Confused on new... - Used to understand possible implementation for creating new spreadsheet - https://piazza.com/class/lcnzpmpokoq4zi/post/1253