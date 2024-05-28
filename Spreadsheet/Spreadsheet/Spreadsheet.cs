using SpreadsheetUtilities;
using SS;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net.Mime;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Channels;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Linq;
using static System.Net.Mime.MediaTypeNames;

namespace SS
{
    /// <summary>
    /// Author:    Kira Halls
    /// Partner:   Nandini Goel
    /// Date:      February 25, 2023
    /// Course:    CS 3500, University of Utah, School of Computing
    /// Copyright: CS 3500 and Kira Halls, Nandini Goel - This work may not 
    ///            be copied for use in Academic Coursework.
    ///
    /// We, Kira Halls and Nandini Goel, certify that I wrote this code from scratch and
    /// did not copy it in part or whole from another source.  All 
    /// references used in the completion of the assignments are cited 
    /// in my README file.
    ///
    /// File Contents
    ///
    /// This file implements the AbstractSpreadsheet file to create a Spreadsheet object that contains a dictionary containing cell name keys and cell values, and a dependency graph to track dependencies among cells. It contains methods to get the contents (string, double, or formula)
    /// of a cell, set the contents of a cell (string, double, or formula), get the direct dependents of a cell, get the names of all nonempty cells, as well as two helper methods to check for name validity and circular dependencies. 
    /// This class references and uses three other classes: the DependencyGraph class, the Cell class, and the Formula class. The nested cell class is a helper class created by me, Kira Halls, to aid in this assignment and future assignments 
    /// building on the spreadsheet. It allows us to compartmentalize a lot of cell functions (getting and setting contents, values, etc) in a different class to keep code organized. 
    /// 
    /// Nandini and I worked together to alter and fix any bugs in this code after the A5 due date, once the assignment five grading tests were released. These fixes were intended to make our Assignment 6 project fully functional, and thus we worked together 
    /// on the fixes. 
    ///
    /// 
    /// </summary>
    public class Spreadsheet : AbstractSpreadsheet
    {
        Dictionary <string, Cell> cells = new Dictionary <string, Cell> ();
        DependencyGraph dependencies = new DependencyGraph();

        /// <summary>
        /// True if this spreadsheet has been modified since it was created or saved                  
        /// (whichever happened most recently); false otherwise.
        /// </summary>
        public override bool Changed { get; protected set; }

        /// <summary>
        /// A zero parameter constructor for a new Spreadsheet object. The IsValid function always returns true, 
        /// the Normalizer does not change the variable, and the version is "default". This constructor uses those 
        /// constraints and passes them to the three parameter constructor.
        /// </summary>
        public Spreadsheet() : this(s => true, s => s, "default"){ }

        /// <summary>
        /// A three parameter constructor for a new Spreadsheet object. This constructor allows the user to input their own IsValid 
        /// function, their own normalizer, and the version of the file. 
        /// </summary>
        /// <param name="isValid"></param> The function that the user inputs to determine if variable/cell names are valid
        /// <param name="normalize"></param> The function that the user inputs to set all cell/variable names to a standard format
        /// <param name="version"></param> The version of the spreadsheet they want to load, if loading from a previous file
        public Spreadsheet(Func<string, bool> isValid, Func<string, string> normalize, string version) : base(isValid, normalize, version)
        {
            this.IsValid = isValid;
            this.Normalize = normalize;
            this.Version = version;
            this.Changed = false;
        }

        /// <summary>
        /// A four parameter constructor for a new Spreadsheet object. This constructor allows a user to input the filepath to the 
        /// previously constructed Spreadsheet file they want to load, allows the user to input their own isValid function, their own 
        /// normalize function, and the version of the file. 
        /// </summary>
        /// <param name="filePath"></param> The string containing the filepath to the previously created Spreadsheet object the user wants to load
        /// <param name="isValid"></param> The function that the user inputs to determine if variable/cell names are valid
        /// <param name="normalize"></param> The function that the user inputs to set all cell/variable names to a valid format
        /// <param name="version"></param> The version of the spreadsheet they want to load, if loading from a previous file
        public Spreadsheet(string filePath, Func<string, bool> isValid, Func<string, string> normalize, string version) : base(isValid, normalize, version)
        {
            cells = new Dictionary<string, Cell>();
            dependencies = new DependencyGraph();
            this.Changed = false;
            

            bool fileIsOpen = true;

            XmlReaderSettings readerSettings = new XmlReaderSettings();
            readerSettings.IgnoreWhitespace = true;
            try
            {
                using (XmlReader reader = XmlReader.Create(filePath, readerSettings))
                {
                    while (fileIsOpen)
                    {
                        if (reader.Read())
                        {
                            if (reader.Name == "spreadsheet")
                            {
                                if (reader.IsStartElement())
                                {
                                    this.Version = reader.GetAttribute("version")!;
                                }
                            }
                            else if (reader.Name == "cell")
                            {
                                string cellName = "", cellContents = "";
                                reader.Read();

                                if (reader.IsStartElement())
                                {
                                    if (reader.Name == "name")
                                    {
                                        reader.Read();
                                        cellName = reader.Value;
                                        reader.Read();
                                        reader.ReadEndElement();
                                    }
                                    else
                                    {
                                        throw new SpreadsheetReadWriteException("Cell is incorrectly written!");
                                    }
                                }

                                if (reader.IsStartElement())
                                {
                                    if (reader.Name == "contents")
                                    {
                                        reader.Read();
                                        cellContents = reader.Value;
                                        reader.Read();
                                        reader.ReadEndElement();
                                    }
                                    else
                                    {
                                        throw new SpreadsheetReadWriteException("ERROR: Cell is incorrectly ordered!");
                                    }
                                }
                                 
                                SetContentsOfCell(cellName, cellContents);
                            }
                            else if (reader.Name == "xml")
                            {
                                
                            }

                            else
                            {
                                throw new SpreadsheetReadWriteException("ERROR: Invalid Element Property!!");
                            }
                        }
                        else
                        {
                            fileIsOpen = false;
                        }
                    }
                    if (! Version.Equals(version))
                    {
                        throw new SpreadsheetReadWriteException("ERROR: Invalid Version Number");
                    }
                }
            }
            catch (CircularException cE)
            {
                throw cE;
            }
            catch (InvalidNameException iE)
            {
                throw iE;
            }
            catch (SpreadsheetReadWriteException ssException)
            {
                throw ssException;
            }
            catch (DirectoryNotFoundException)
            {
                throw new SpreadsheetReadWriteException("ERROR: File directory not found");
            }
            catch (Exception)
            {
                throw new SpreadsheetReadWriteException("ERROR: Cannot read XML file or write to spreadsheet properly!");
            }
            
        }

        /// <summary>
        /// This private helper method checks if a given name is valid according to the set parameters of a valid cell name
        /// Valid names must start with a letter or underscore and are followed by 0 or more numbers, letters, or underscores.
        /// </summary>
        /// <param name="name"></param> The name being checked for validity
        /// <returns></returns> true if the name is valid, false otherwise
        public Boolean nameValidity(string name)
        {
            name = Normalize(name);
            Regex format = new Regex(@"^[a-zA-Z]([a-zA-Z0-9])*$");
            if (!(format.IsMatch(name)))
            {
                return false;
            }
            return true;
        }

        /// <summary>
        ///   Returns the contents (as opposed to the value) of the named cell.
        /// </summary>
        /// 
        /// <exception cref="InvalidNameException"> 
        ///   Thrown if the name is invalid: blank/empty/""
        /// </exception>
        /// 
        /// <param name="name">The name of the spreadsheet cell to query</param>
        /// 
        /// <returns>
        ///   The return value should be either a string, a double, or a Formula.
        ///   See the class header summary 
        /// </returns>
        public override object GetCellContents(string name)
        {
            name = Normalize(name);
            if (!nameValidity(name) || name.Equals("") || IsValid(name) != true)
            {
                throw new InvalidNameException();
            }
            if (cells.ContainsKey(name))
            {
                return cells[name].getContents();
            } 
            return ""; //return empty string if the cell is empty
        }

        /// <summary>
        ///   Returns the names of all non-empty cells.
        /// </summary>
        /// 
        /// <returns>
        ///     Returns an Enumerable that can be used to enumerate
        ///     the names of all the non-empty cells in the spreadsheet.  If 
        ///     all cells are empty then an IEnumerable with zero values will be returned.
        /// </returns>
        public override IEnumerable<string> GetNamesOfAllNonemptyCells()
        {
            List<string> keys = new List<string>(cells.Keys);
            return keys;
        }

        /// <summary>
        ///   <para>Sets the contents of the named cell to the appropriate value. </para>
        ///   <para>
        ///       First, if the content parses as a double, the contents of the named
        ///       cell becomes that double.
        ///   </para>
        ///
        ///   <para>
        ///       Otherwise, if content begins with the character '=', an attempt is made
        ///       to parse the remainder of content into a Formula.  
        ///       There are then three possible outcomes:
        ///   </para>
        ///
        ///   <list type="number">
        ///       <item>
        ///           If the remainder of content cannot be parsed into a Formula, a 
        ///           SpreadsheetUtilities.FormulaFormatException is thrown.
        ///       </item>
        /// 
        ///       <item>
        ///           If changing the contents of the named cell to be f
        ///           would cause a circular dependency, a CircularException is thrown,
        ///           and no change is made to the spreadsheet.
        ///       </item>
        ///
        ///       <item>
        ///           Otherwise, the contents of the named cell becomes f.
        ///       </item>
        ///   </list>
        ///
        ///   <para>
        ///       Finally, if the content is a string that is not a double and does not
        ///       begin with an "=" (equal sign), save the content as a string.
        ///   </para>
        /// </summary>
        ///
        /// <exception cref="InvalidNameException"> 
        ///   If the name parameter is null or invalid, throw an InvalidNameException
        /// </exception>
        /// 
        /// <exception cref="SpreadsheetUtilities.FormulaFormatException"> 
        ///   If the content is "=XYZ" where XYZ is an invalid formula, throw a FormulaFormatException.
        /// </exception>
        /// 
        /// <exception cref="CircularException"> 
        ///   If changing the contents of the named cell to be the formula would 
        ///   cause a circular dependency, throw a CircularException.  
        ///   (NOTE: No change is made to the spreadsheet.)
        /// </exception>
        /// 
        /// <param name="name"> The cell name that is being changed</param>
        /// <param name="content"> The new content of the cell</param>
        /// 
        /// <returns>
        ///       <para>
        ///           This method returns a list consisting of the passed in cell name,
        ///           followed by the names of all other cells whose value depends, directly
        ///           or indirectly, on the named cell. The order of the list MUST BE any
        ///           order such that if cells are re-evaluated in that order, their dependencies 
        ///           are satisfied by the time they are evaluated.
        ///       </para>
        ///
        ///       <para>
        ///           For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        ///           list {A1, B1, C1} is returned.  If the cells are then evaluate din the order:
        ///           A1, then B1, then C1, the integrity of the Spreadsheet is maintained.
        ///       </para>
        /// </returns>
        public override IList<string> SetContentsOfCell(string name, string content)
        {
            name = Normalize(name);
            this.Changed = true;
            //Check name validity
            if ((!(nameValidity(name))) || IsValid(name) != true)
            {
                throw new InvalidNameException();
            }

            //First check: Content is a double
            if (Double.TryParse(content, out double tempNum))
            {
                List<string> doubleList = (List<string>)SetCellContents(name, tempNum);
                foreach (string cell in doubleList)
                {
                    if (cells.ContainsKey(cell) && cells[cell].getContents().GetType() == typeof(Formula))
                    {
                        Formula formula = (Formula)cells[cell].getContents();
                        cells[cell].setValue(formula.Evaluate(s => GetCellValueLambda(s)));
                    }
                }
                return doubleList;
            }

            //Second check: Content is formula
            else if (content.Length != 0 && content[0].Equals('='))
            {
                //Check formula format 
                try
                {
                    Formula tryFormula = new  Formula(content, this.Normalize, this.IsValid);
                }
                catch (FormulaFormatException) { throw new FormulaFormatException("Please enter a valid formula."); }
                
                //Check for circular dependencies
                Formula formula = new Formula(content, this.Normalize, this.IsValid); //For use if we can create a valid formula
                if (circularDependency(formula, name))
                {
                    throw new CircularException();
                }
                
                //Format is valid and no circular dependencies detected, contents can be set as a Formula
                else
                {
                    List<string> formulaList = (List<string>)SetCellContents(name, formula);
                    foreach (string cell in formulaList)
                    {
                        if (cells.ContainsKey(cell) && cells[cell].getContents().GetType() == typeof(Formula))
                        {
                            Formula formula2 = (Formula)cells[cell].getContents();
                            cells[cell].setValue(formula2.Evaluate(s => GetCellValueLambda(s)));
                        }
                    }
                    return formulaList;
                }
            }

            //Third Check: Assume content is string
            else
            {
                List<string> stringList = (List<string>)SetCellContents(name, content);
                foreach (string cell in stringList)
                {
                    if (cells.ContainsKey(cell) && cells[cell].getContents().GetType() == typeof(Formula))
                    {
                        Formula formula = (Formula) cells[cell].getContents();
                        cells[cell].setValue(formula.Evaluate(s => GetCellValueLambda(s)));
                    }
                }
                return stringList;
            }
            
        }

        /// <summary>
        ///  Set the contents of the named cell to the given number.  
        /// </summary>
        /// 
        /// <requires> 
        ///   The name parameter must be valid: non-empty/not ""
        /// </requires>
        /// 
        /// <exception cref="InvalidNameException"> 
        ///   If the name is invalid, throw an InvalidNameException
        /// </exception>
        /// 
        /// <param name="name"> The name of the cell </param>
        /// <param name="number"> The new contents/value </param>
        /// 
        /// <returns>
        ///   <para>
        ///       This method returns a LIST consisting of the passed in name followed by the names of all 
        ///       other cells whose value depends, directly or indirectly, on the named cell.
        ///   </para>
        ///
        ///   <para>
        ///       The order must correspond to a valid dependency ordering for recomputing
        ///       all of the cells, i.e., if you re-evaluate each cell in the order of the list,
        ///       the overall spreadsheet will be consistently updated.
        ///   </para>
        ///
        ///   <para>
        ///     For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        ///     set {A1, B1, C1} is returned, i.e., A1 was changed, so then A1 must be 
        ///     evaluated, followed by B1 re-evaluated, followed by C1 re-evaluated.
        ///   </para>
        /// </returns>
        protected override IList<string> SetCellContents(string name, double number)
        {
            if (!(cells.ContainsKey(name))) 
            {
                cells.Add(name, new Cell(name, number)); 
            }
            cells[name].setContents(number);
            cells[name].setValue(number);
            return (IList<string>) GetCellsToRecalculate(name).ToList();
        }

        /// <summary>
        /// The contents of the named cell becomes the text.  
        /// </summary>
        /// 
        /// <requires> 
        ///   The name parameter must be valid/non-empty ""
        /// </requires>
        /// 
        /// <exception cref="InvalidNameException"> 
        ///   If the name is invalid, throw an InvalidNameException
        /// </exception>       
        /// 
        /// <param name="name"> The name of the cell </param>
        /// <param name="text"> The new content/value of the cell</param>
        /// 
        /// <returns>
        ///   <para>
        ///       This method returns a LIST consisting of the passed in name followed by the names of all 
        ///       other cells whose value depends, directly or indirectly, on the named cell.
        ///   </para>
        ///
        ///   <para>
        ///       The order must correspond to a valid dependency ordering for recomputing
        ///       all of the cells, i.e., if you re-evaluate each cell in the order of the list,
        ///       the overall spreadsheet will be consistently updated.
        ///   </para>
        ///
        ///   <para>
        ///     For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        ///     set {A1, B1, C1} is returned, i.e., A1 was changed, so then A1 must be 
        ///     evaluated, followed by B1 re-evaluated, followed by C1 re-evaluated.
        ///   </para>
        /// </returns>
        protected override IList<string> SetCellContents(string name, string text)
        {
            if (!(cells.ContainsKey(name)))
            {
                cells.Add(name, new Cell(name, text));
            }
            cells[name].setContents(text);
            cells[name].setValue(text);
            if (text == "")
            {
                cells.Remove(name); //remove the cell from the cells dictionary if it is 'empty' (the string is "").
            }
            return (IList<string>)GetCellsToRecalculate(name).ToList();

        }

        /// <summary>
        /// Set the contents of the named cell to the formula.  
        /// </summary>
        /// 
        /// <requires> 
        ///   The name parameter must be valid/non empty
        /// </requires>
        /// 
        /// <exception cref="InvalidNameException"> 
        ///   If the name is invalid, throw an InvalidNameException
        /// </exception>
        /// 
        /// <exception cref="CircularException"> 
        ///   If changing the contents of the named cell to be the formula would 
        ///   cause a circular dependency, throw a CircularException.  
        ///   (NOTE: No change is made to the spreadsheet.)
        /// </exception>
        /// 
        /// <param name="name"> The cell name</param>
        /// <param name="formula"> The content of the cell</param>
        /// 
        /// <returns>
        ///   <para>
        ///       This method returns a LIST consisting of the passed in name followed by the names of all 
        ///       other cells whose value depends, directly or indirectly, on the named cell.
        ///   </para>
        ///
        ///   <para>
        ///       The order must correspond to a valid dependency ordering for recomputing
        ///       all of the cells, i.e., if you re-evaluate each cell in the order of the list,
        ///       the overall spreadsheet will be consistently updated.
        ///   </para>
        ///
        ///   <para>
        ///     For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        ///     set {A1, B1, C1} is returned, i.e., A1 was changed, so then A1 must be 
        ///     evaluated, followed by B1 re-evaluated, followed by C1 re-evaluated.
        ///   </para>
        /// </returns>
        protected override IList<string> SetCellContents(string name, Formula formula)
        {
            if (!(cells.ContainsKey(name)))
            {
                cells.Add(name, new Cell(name, formula));
            }
            cells[name].setContents(formula);
            cells[name].setValue(formula.Evaluate(s => GetCellValueLambda(s)));   
            List<string> variables = (List<string>) formula.GetVariables();
            foreach (string variable in variables)
            {
                dependencies.AddDependency(variable, name);
            }
            return (IList<string>)GetCellsToRecalculate(name).ToList();
        }
        /// <summary>
        /// This private helper method checks for circular dependencies in cells. It is used when setting a cells contents to a formula.  
        /// It checks each dependent for the given named cell and checks to ensure the new formula being proposed does not contain that dependent. It also
        /// uses recursion to check if that dependent has other dependents that may cause an indirect circular dependency. 
        /// </summary>
        /// <param name="formula"></param> The new formula being proposed, contains variables
        /// <param name="name"></param> The name of the cell that is being altered
        /// <returns></returns> true if it contains a circular dependency, false otherwise.
        private Boolean circularDependency(Formula formula, string name)
        {
            List<string> variables = (List<string>)formula.GetVariables();
            if (dependencies.HasDependents(name))
            {
                HashSet<string> dependents = (HashSet<string>)dependencies.GetDependents(name);
                foreach (string dependent in dependents)
                {
                    if (variables.Contains(dependent))
                    {
                        return true;
                    }
                    if (dependencies.HasDependents(dependent))
                    {
                        return circularDependency(formula, dependent);
                    }
                }
            }
            return false;
        }

        /// <summary>
        /// Returns an enumeration, without duplicates, of the names of all cells whose
        /// values depend directly on the value of the named cell. 
        /// </summary>
        /// 
        /// <exception cref="InvalidNameException"> 
        ///   If the name is invalid, throw an InvalidNameException
        /// </exception>
        /// 
        /// <param name="name"></param>
        /// <returns>
        ///   Returns an enumeration, without duplicates, of the names of all cells that contain
        ///   formulas containing name.
        /// 
        ///   <para>For example, suppose that: </para>
        ///   <list type="bullet">
        ///      <item>A1 contains 3</item>
        ///      <item>B1 contains the formula A1 * A1</item>
        ///      <item>C1 contains the formula B1 + A1</item>
        ///      <item>D1 contains the formula B1 - C1</item>
        ///   </list>
        /// 
        ///   <para>The direct dependents of A1 are B1 and C1</para>
        /// 
        /// </returns>
        protected override IEnumerable<string> GetDirectDependents(string name)
        {
            if (!(nameValidity(name) || IsValid(name)))
            {
                throw new InvalidNameException();
            }
            return dependencies.GetDependents(name);
        }

        /// <summary>
        ///   Look up the version information in the given file. If there are any problems opening, reading, 
        ///   or closing the file, the method should throw a SpreadsheetReadWriteException with an explanatory message.
        /// </summary>
        /// 
        /// <remarks>
        ///   In an ideal world, this method would be marked static as it does not rely on an existing SpreadSheet
        ///   object to work; indeed it should simply open a file, lookup the version, and return it.  Because
        ///   C# does not support this syntax, we abused the system and simply create a "regular" method to
        ///   be implemented by the base class.
        /// </remarks>
        /// 
        /// <exception cref="SpreadsheetReadWriteException"> 
        ///   Thrown if any problem occurs while reading the file or looking up the version information.
        /// </exception>
        /// 
        /// <param name="filename"> The name of the file (including path, if necessary)</param>
        /// <returns>Returns the version information of the spreadsheet saved in the named file.</returns>
        public override string GetSavedVersion(string filename)
        {
            try
            {
                using  (XmlReader reader = XmlReader.Create(filename))
                {
                    while (reader.Read())
                    {
                        if (reader.IsStartElement())
                        {
                            switch (reader.Name)
                            {
                                case "spreadsheet":
                                    return reader["version"]!;
                            }
                        }
                    }
                }
            } 
            catch (InvalidNameException) { throw new SpreadsheetReadWriteException("File name is invalid."); }
            catch (CircularException) { throw new SpreadsheetReadWriteException("File contains circular dependencies"); }
            catch(IOException) { throw new SpreadsheetReadWriteException("An error has occurred."); }

            return ""; //Should not return this, an exception should be thrown or version information should be returned
        }

        /// <summary>
        /// Writes the contents of this spreadsheet to the named file using an XML format.
        /// The XML elements should be structured as follows:
        /// 
        /// <spreadsheet version="version information goes here">
        /// 
        /// <cell>
        /// <name>cell name goes here</name>
        /// <contents>cell contents goes here</contents>    
        /// </cell>
        /// 
        /// </spreadsheet>
        /// 
        /// There should be one cell element for each non-empty cell in the spreadsheet.  
        /// If the cell contains a string, it should be written as the contents.  
        /// If the cell contains a double d, d.ToString() should be written as the contents.  
        /// If the cell contains a Formula f, f.ToString() with "=" prepended should be written as the contents.
        /// 
        /// If there are any problems opening, writing, or closing the file, the method should throw a
        /// SpreadsheetReadWriteException with an explanatory message.
        /// </summary>
        public override void Save(string filename)
        {
            this.Changed = false;
            try
            {
                using (XmlWriter writer = XmlWriter.Create(filename))
                {
                    writer.WriteStartDocument();
                    writer.WriteStartElement("spreadsheet");
                    writer.WriteAttributeString("version", this.Version);

                    foreach (Cell cell in cells.Values)
                    {
                        cell.WriteXml(writer);
                    }
                    writer.WriteEndElement();
                    writer.WriteEndDocument();

                }
            }
            catch (CircularException cE)
            {
                throw cE;
            }
            catch (InvalidNameException iE)
            {
                throw iE;
            }
            catch (SpreadsheetReadWriteException ssException)
            {
                throw ssException;
            }
            catch (DirectoryNotFoundException)
            {
                throw new SpreadsheetReadWriteException("ERROR: File directory not found");
            }
            //catch (Exception)
            //{
            //    throw new SpreadsheetReadWriteException("ERROR: Cannot read XML file or write to spreadsheet properly!");
            //}
        }

        /// <summary>
        /// If name is invalid, throws an InvalidNameException.
        /// </summary>
        ///
        /// <exception cref="InvalidNameException"> 
        ///   If the name is invalid, throw an InvalidNameException
        /// </exception>
        /// 
        /// <param name="name"> The name of the cell that we want the value of (will be normalized)</param>
        /// 
        /// <returns>
        ///   Returns the value (as opposed to the contents) of the named cell.  The return
        ///   value should be either a string, a double, or a SpreadsheetUtilities.FormulaError.
        /// </returns>
        public override object GetCellValue(string name)
        {
            if (! (nameValidity(name) && IsValid(name))) 
            {
                throw new InvalidNameException();
            }
            if (cells.ContainsKey(name))
            {
                return cells[name].getValue();
            }
            return ""; //return empty string if cell is empty
            
        }

        /// <summary>
        /// 
        /// A helper method that is used to get the value of a variable. It is specifically for use while passing in a 
        /// formula to be evaluated, this method acts as the lookup function. It calls the GetCellValue function, and 
        /// ensures that it only returns a double as the value. If a double is not returned, an argument exception is thrown
        /// 
        /// </summary>
        /// <param name="name"></param> The name of the cell to get the value from
        /// <returns></returns> The double value of the cell passed in
        /// <exception cref="InvalidNameException"></exception> Throw an InvalidNameException if the name is invalid
        /// <exception cref="ArgumentException"></exception> Throw an ArgumentException if the cell value type is not a double
        public double GetCellValueLambda(string name)
        {
            if(! (nameValidity(name) && IsValid(name)))
            {
                throw new InvalidNameException();
            }
            if (GetCellValue(name).GetType() != typeof(double))
            {
                throw new ArgumentException();
            }
            try
            {
                double value = (double) GetCellValue(name);
            }
            catch (KeyNotFoundException) { throw new ArgumentException(); } //Should not encounter these errors, but checking as a possibility
            catch (FormatException) { throw new ArgumentException();  }
            return (double) GetCellValue(name);
        }

      

        /// <summary>
        /// /// <summary>
        /// Author:    Kira Halls
        /// Partner:   None
        /// Date:      February 10, 2023
        /// Course:    CS 3500, University of Utah, School of Computing
        /// Copyright: CS 3500 and Kira Halls - This work may not 
        ///            be copied for use in Academic Coursework.
        ///
        /// I, Kira Halls, certify that I wrote this code from scratch and
        /// did not copy it in part or whole from another source.  All 
        /// references used in the completion of the assignments are cited 
        /// in my README file.
        ///
        /// File Contents
        ///
        /// This nested class functions as a helper class for the Spreadsheet solution. It uses abstraction to help keep the solution code organized and separated. This class creates a cell object that stores the cell's 
        /// contents (either a string, a double, or a formula), the value of the cell (either the string or double that is the content, or the solved formula), and the name of the cell. It also contains a method to check for 
        /// name validity to ensure the cells are being legally named (must begin with a letter or underscore and is followed by 0 or more letters, numbers, or underscores). This class is used in the Spreadsheet class.
        ///
        /// 
        /// </summary>
        /// </summary>
        internal class Cell
        {
            object contents;
            object value;
            string name;

            /// <summary>
            /// This constructor creates a new cell given its name and contents. Contents must be a string, a double, or a formula object, and the name must be a string. 
            /// It calls the setName() function to also check for name validity. 
            /// 
            /// </summary>
            /// <param name="name"></param> The name of the cell to be created, must be a valid string following format outlined in nameValidity() function.
            /// <param name="contents"></param> The contents of the cell, either a formula, double, or string object.
            internal Cell(string name, object contents)
            {
                this.name = name;
                this.contents = contents;
                this.value = contents; //Value will be either the double or string contents, or setValue will guaranteed be called when cell contents are set to formula
            }

            /// <summary>
            /// This getter method returns the contents of the cell calling the method.
            /// </summary>
            /// <returns></returns> The contents of the cell
            internal object getContents()
            {
                return contents;
            }

            /// <summary>
            /// This getter method returns the value of the cell calling the method. The value is either the string or double contained in the cell contents, or the evaluated value of the formula
            /// contained in the contents.
            /// </summary>
            /// <returns></returns> The value of the cell
            internal object getValue()
            {
                return value;
            }

            /// <summary>
            /// This setter method sets the content of the cell to be either a string, double, or formula object
            /// </summary>
            /// <param name="newContent"></param> The new contents of the cell.
            internal void setContents(object newContent)
            {
                contents = newContent;
            }

            /// <summary>
            /// This setter method sets the value of the cell to be the given parameter value. Typically called by the setContents() method as the content of the cell dictates what the value will be.
            /// </summary>
            /// <param name="newValue"></param> The new value of the cell
            internal void setValue(object newValue) { value = newValue; }

            /// <summary>
            /// Helper method for creating/writing an xml file and adding all cell information to the file
            /// </summary>
            /// <param name="writer"></param> The xml writer creating the file
            internal void WriteXml(XmlWriter writer)
            {
                writer.WriteStartElement("cell");
                writer.WriteElementString("name", this.name);
                writer.WriteElementString("contents", this.contents.ToString());
                writer.WriteEndElement();
            }
        }
    }
}
