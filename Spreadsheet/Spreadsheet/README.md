```
Author:     Kira Halls
Partner:    None
Course:     CS 3500, University of Utah, School of Computing
GitHub ID:  kirahalls
Repo:       https://github.com/uofu-cs3500-spring23/spreadsheet-kirahalls 
Date:       February 19, 2023 
Project:    Spreadsheet
Copyright:  CS 3500 and Kira Halls - This work may not be copied for use in Academic Coursework.
```

# Description

 
This project creates a spreadsheet object by implementing the AbstractSpreadsheet class written by the instructors of this course. The spreadsheet object contains a dictionary of cell names as keys and cells as values,
as well as a dependency graph instance variable that keeps track of dependencies among cells. Each cell is represented by a cell object created from the Cell helper class that I wrote, which contains a cell's contents, 
name, and value. This cell class is nested within the Spreadsheet class. The spreadsheet class contains methods to create a new spreadsheet object, set cell contents, get all nonempty cells, get direct dependents of a cell,
as well as two helper methods to check for name validity and circular dependencies. Exceptions should be thrown for invalid names and circular dependencies. This spreadsheet class also handles saving and reading versions of a 
spreadsheet object. It contains three constructors: one that is empty and will create a spreadsheet under a "default" version, one that allows users to input 
their own normalizers and IsValid functions as well as the version, and finally, a constructor that allows users to open a spreadsheet from a given filepath and allowing them to pass in 
their own normalizers and IsValid functions and the version of the saved spreadsheet they want to get. The class uses XML writers and readers to handle saving and reading the file.

# Comments to Evaluators:

No additional comments to evaluators, my work for this assignment stands on its own.

# Assignment Specific Topics

No additional writeup was specified for this assignment.

# Consulted Peers:

I did not consult any other peers for this assignment, as I found that all my questions were answered through lecture,
Piazza, or through simple research (for syntax related questions).

# References:
1. C# Inheritance - Used to get syntax for inheritance - https://www.w3schools.com/cs/cs_inheritance.php
2. Dictionary <TKey, TValue> class - used to double check dictionary methods - https://learn.microsoft.com/en-us/dotnet/api/system.collections.generic.dictionary-2.add?view=net-7. 
3. Nested Types - Used to understand nested class access modifiers - https://learn.microsoft.com/en-us/dotnet/csharp/programming-guide/classes-and-structs/nested-types
4. protected internal - Used to understand nested class access modifiers - https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/protected-internal
5. private - Used to understand nested class access modifiers - https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/private
6. internal - Used to understand nested class access modifiers - https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/keywords/internal
7. Access Modifiers - Used to understand nested class access modifiers - https://learn.microsoft.com/en-us/dotnet/csharp/programming-guide/classes-and-structs/access-modifiers
8. HashSet<T> Class - Used to determine better set to use between dictionary and hashset - https://learn.microsoft.com/en-us/dotnet/api/system.collections.generic.hashset-1.add?view=net-7.0
9. How do I get the list of keys in a Dictionary? - Used to get syntax for getting list of keys from dictionary - https://stackoverflow.com/questions/1276763/how-do-i-get-the-list-of-keys-in-a-dictionary

Assignment 5 References: 
1. Ilist Example - Used to decide what IList to return from methods - https://www.dotnetperls.com/ilist
2. Data Structures - Used to find overlapping data structure between IEnumerable and IList - https://dev.to/scorpio69/data-structures-enumerable-enumerator-collection-list-dictionary-arrays-arraylist-hashtable-stack-queue-2dbe
3. Piazza Post - Used in creating my lookup function - https://piazza.com/class/lcnzpmpokoq4zi/post/1034
4. In Visual Studio 2019... - Used to understand why tests were not running - https://stackoverflow.com/questions/61188972/in-visual-studio-2019-what-is-the-difference-between-a-green-and-white-check-ic