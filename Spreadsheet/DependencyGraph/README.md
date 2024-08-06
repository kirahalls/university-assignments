```
Author:     Kira Halls
Partner:    None
Date:       24-Jan-2023
Course:     CS 3500, University of Utah, School of Computing
GitHub ID:  kirahalls
Repo:       https://github.com/uofu-cs3500-spring23/spreadsheet-kirahalls
Date:       26-Jan-2023
Solution:   Spreadsheet
Copyright:  CS 3500 and Kira Halls - This work may not be copied for use in Academic Coursework.
```

# Description
 
This class represents a dependency graph using two dictionaries with string keys and List<string> values. One dictionary
contains node keys and their dependees (the nodes that it depends on) and the second contains node keys and their dependents
(the nodes that depend on it). There are provided methods for getting the number of ordered pairs in the graph, a node's dependents
and dependees, setting new dependencies, removing dependencies, and replacing dependencies with a list. This class will be used
in the Spreadsheet solution. This class does have the limitation of ignoring possible cycles, but this will be addressed 
in later assignments.

# Comments to Evaluators:

My work for this assignment stands on its own.

# Assignment Specific Topics

Estimated Time: 15 hours    Actual Time: 6:15

This assignment did not specify any further writeup.

# Consulted Peers:

None, other than reading through class posts on Piazza. I found that all my questions were answered either on Piazza 
or I was able to solve the issue myself through research (cited below) or spending more time with the code. 

# References:

    1. Difference Between Hashtable and Dictionary in C# - Used to help decide implementation - https://www.geeksforgeeks.org/difference-between-hashtable-and-dictionary-in-c-sharp/
    2. C# Dictionary.Count Property - Used for Count syntax - https://www.geeksforgeeks.org/c-sharp-dictionary-count-property/
    3. Dictionary <TKey, TValue> Class - Used to see method options while using dictionary - https://learn.microsoft.com/en-us/dotnet/api/system.collections.generic.dictionary-2?view=net-7.0
    4. Get Dictionary Value By Key - Used for syntax to get a value from a key - https://stackoverflow.com/questions/12169443/get-dictionary-value-by-key
    5. IEnumerable - Used to get syntax for the IEnumerable foreach loop - https://www.dotnetperls.com/ienumerable
    6. Piazza Post ArrayList to IEnumerable - Used to learn syntax to convert an ArrayList to an IEnumerable - https://piazza.com/class/lcnzpmpokoq4zi/post/396
    7. Resolve nullable warnings - Used to learn about specific warning when returning ArrayList as IEnumerable - https://learn.microsoft.com/en-us/dotnet/csharp/language-reference/compiler-messages/nullable-warnings#possible-null-assigned-to-a-nonnullable-reference
    8. arraylist and IEnumerable<T> - Used to understand why I was having issues converting from an ArrayList to an IEnumerable - https://social.msdn.microsoft.com/Forums/vstudio/en-US/a377866f-2901-45ae-be78-565090e4b44d/arraylist-and-ienumerablelttgt?forum=csharpgeneral
