using NuGet.Frameworks;
using SpreadsheetUtilities;
using SS;
namespace SpreadsheetTests
{
    /// <summary>
    /// Author:    Kira Halls
    /// Partner:   None
    /// Date:      February 19, 2023
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
    ///This class contains the testing for the Spreadsheet class. It tests cell creation, altering cell contents with all three content types (string, double, formula), getting a cell's direct 
    /// dependents, getting cell contents and names of all nonempty cells, errors associated with invalid cell naming, errors associated with invalid cell contents, and cells that have circular dependencies. It must also 
    /// test saving, reading, and writing files to contain a spreadsheet object.
    /// 
    /// <summary>

    [TestClass]
    public class SpreadsheetTests
    {
 
        /// <summary>
        /// This test method tests cell creation with valid names and contents. It tests all three SetCellContent methods, one for each a double, string, and a formula.
        /// Tests that the spreadsheet contains accurate information about cell contents, and that the cell contents are altered correctly.
        /// </summary>
        [TestMethod]
        public void ValidCellCreation()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A2", "test");
            Assert.AreEqual("test", spreadsheet.GetCellContents("A2"));
            spreadsheet.SetContentsOfCell("A2", "2.0");
            Assert.AreEqual(2.0, spreadsheet.GetCellContents("A2"));
            spreadsheet.SetContentsOfCell("A2", "=2+2");
            Formula formula = new Formula("2+2");
            Assert.AreEqual(formula, spreadsheet.GetCellContents("A2"));
        }

        /// <summary>
        /// This method tests invalid cell creation specifically focusing on errors with cell names. It tests invalid names with  all three content types (double, string, formula)
        /// and should catch an invalid name exception every time. It also tests GetCellContents with the same invalid name and/or null name and should also catch an invalid name exception.
        /// </summary>
        [TestMethod]
        public void InvalidNameCellCreation()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            try
            {
                spreadsheet.SetContentsOfCell("2", "2.0");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
            try
            {
                spreadsheet.GetCellContents("2");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
            try
            {
                spreadsheet.SetContentsOfCell("2", "test");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
            try
            {
                spreadsheet.GetCellContents("2");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
            try
            {
                spreadsheet.SetContentsOfCell("2", "=2+2");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
            try
            {
                spreadsheet.GetCellContents("2");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
        }

        /// <summary>
        /// This method tests that altering a cell's content actually sets the content to the new content, including setting contents to be a different object type. It tests this by 
        /// checking what is returned after setting the new contents and calling GetCellContents.
        /// </summary>
        [TestMethod]
        public void AlterCellContent()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Assert.AreEqual("", spreadsheet.GetCellContents("A2"));
            spreadsheet.SetContentsOfCell("A2", "test");
            Assert.AreEqual("test", spreadsheet.GetCellContents("A2"));
            spreadsheet.SetContentsOfCell("A2", "2.0");
            Assert.AreEqual(2.0, spreadsheet.GetCellContents("A2"));
            spreadsheet.SetContentsOfCell("A2", "=2+2");
            Formula formula1 = new Formula("2+2");
            Assert.AreEqual(formula1, spreadsheet.GetCellContents("A2"));
            Assert.AreNotEqual("=2+6", spreadsheet.GetCellContents("A2"));
            spreadsheet.SetContentsOfCell("A2", "=2+6");
            Formula formula2 = new Formula("2+6");
            Assert.AreEqual(formula2, spreadsheet.GetCellContents("A2"));
        }

        /// <summary>
        /// This method tests that errors are thrown when circular dependencies are detected. It tests for direct circular dependencies and indirect circular dependencies.
        /// </summary>
        [TestMethod]
        public void CircularDependencies()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            //direct circular dependency
            spreadsheet.SetContentsOfCell("A2", "=C2 + 2");
            try
            {
                spreadsheet.SetContentsOfCell("C2",  "=A2 + 2");
                Assert.Fail();
            }
            catch(CircularException) { }

            //indirect circular dependency
            spreadsheet.SetContentsOfCell("C2", "=B2 + 2");
            try
            {
                spreadsheet.SetContentsOfCell("B2", "=A2 + 2");
                Assert.Fail();
            }
            catch (CircularException) { }
        }

        /// <summary>
        /// This method tests the GetNamesOfAllNonEmptyCells method. It tests that an empty spreadsheet returns an empty list, and that setting cell contents with all three different content 
        /// types will be counted as nonempty cells. It also tests to ensure that if a cell's contents are set to "" then they are considered empty and are removed from the list.
        /// </summary>
        [TestMethod]
        public void GetNamesOfNonEmptyCells() 
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            List<string> cells = new List<string>();
            List<string> actual = (List<string>) spreadsheet.GetNamesOfAllNonemptyCells(); //Empty to begin 
            Assert.AreEqual(actual.ToString(), cells.ToString());
            spreadsheet.SetContentsOfCell("C2", "test`");
            spreadsheet.SetContentsOfCell("A2", "2.0");
            spreadsheet.SetContentsOfCell("CC9", "=2+2");
            cells.Add("C2");
            cells.Add("A2");
            cells.Add("CC9");
            actual = (List<string>) spreadsheet.GetNamesOfAllNonemptyCells();
            Assert.AreEqual(actual.ToString(), cells.ToString());
            spreadsheet.SetContentsOfCell("A2", ""); //Should not register "" as a non empty cell
            cells.Remove("A2");
            actual = (List<string>)spreadsheet.GetNamesOfAllNonemptyCells();
            Assert.AreEqual(actual.ToString(), cells.ToString());
        }

        /// <summary>
        /// This method tests the SetCellConents method that takes in a double as the content. It tests that the method returns the correct list of dependents of the cell being altered, and that 
        /// the list includes the name of the cell itself. It adds dependents on that cell to test that those dependents are returned as well.
        /// </summary>
        [TestMethod]
        public void SetCellContentsDouble()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Assert.AreEqual("", spreadsheet.GetCellContents("A2"));
            List<string> dependents = new List<string>();
            dependents.Add("A2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "2.0").ToString());
            spreadsheet.SetContentsOfCell("B2", "=A2+ 2");
            dependents.Add("B2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "2.0").ToString());
            spreadsheet.SetContentsOfCell("B2", "=C2+ 2");
            dependents.Add("C2");
            spreadsheet.SetContentsOfCell("C2", "=A2+ 2");
            dependents.Add("B2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "2.0").ToString());
        }

        /// <summary>
        /// This method tests the SetCellConents method that takes in a string as the content. It tests that the method returns the correct list of dependents of the cell being altered, and that 
        /// the list includes the name of the cell itself. It adds dependents on that cell to test that those dependents are returned as well.
        /// </summary>
        [TestMethod]
        public void SetCellContentsString()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Assert.AreEqual("", spreadsheet.GetCellContents("A2"));
            List<string> dependents = new List<string>();
            dependents.Add("A2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "test").ToString());
            spreadsheet.SetContentsOfCell("B2", "=A2+ 2");
            dependents.Add("B2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "test").ToString());
            spreadsheet.SetContentsOfCell("B2", "=C2+ 2");
            dependents.Add("C2");
            spreadsheet.SetContentsOfCell("C2", "=A2+ 2");
            dependents.Add("B2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "test").ToString());
        }

        /// <summary>
        /// This method tests the SetCellConents method that takes in a Formula as the content. It tests that the method returns the correct list of dependents of the cell being altered, and that 
        /// the list includes the name of the cell itself. It adds dependents on that cell to test that those dependents are returned as well.
        /// </summary>
        [TestMethod]
        public void SetCellContentsFormula()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Assert.AreEqual("", spreadsheet.GetCellContents("A2"));
            List<string> dependents = new List<string>();
            dependents.Add("A2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "=2+2").ToString());
            spreadsheet.SetContentsOfCell("B2", "=A2+ 2");
            dependents.Add("B2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "=2+2").ToString());
            spreadsheet.SetContentsOfCell("B2", "=C2+ 2");
            dependents.Add("C2");
            spreadsheet.SetContentsOfCell("C2", "=A2+ 2");
            dependents.Add("B2");
            Assert.AreEqual(dependents.ToString(), spreadsheet.SetContentsOfCell("A2", "=2+2").ToString());
            try
            {
                spreadsheet.SetContentsOfCell("A2", "=2 2");
                Assert.Fail();
            }
            catch (FormulaFormatException) { }
        }

        /// <summary>
        /// This method tests the GetCellValue method. It should return the value, not the contents of a cell. Must throw an InvalidNameException 
        /// for an invalid name, and return an empty string if the cell is empty. Tests for all three possible return values: string, double, and 
        /// FormulaError
        /// </summary>
        [TestMethod]
        public void GetCellValue()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Spreadsheet spreadsheet2 = new Spreadsheet(s => false, s => s, "default");
            try
            {
                spreadsheet.GetCellValue("___");
                spreadsheet2.GetCellValue("A2");
                Assert.Fail();
            }
            catch (InvalidNameException) { }
            Assert.AreEqual("", spreadsheet.GetCellValue("A2"));
            spreadsheet.SetContentsOfCell("A2", "test");
            Assert.AreEqual("test", spreadsheet.GetCellValue("A2"));
            spreadsheet.SetContentsOfCell("A2", "2.0");
            Assert.AreEqual(2.0, spreadsheet.GetCellValue("A2"));
            spreadsheet.SetContentsOfCell("A2", "=2+2");
            Assert.AreEqual(4.0, spreadsheet.GetCellValue("A2"));
            spreadsheet.SetContentsOfCell("A2", "=2/0");
            Assert.IsTrue(spreadsheet.GetCellValue("A2").GetType() == typeof(FormulaError));
            spreadsheet.SetContentsOfCell("B2", "1.0");
            spreadsheet.SetContentsOfCell("A2", "=B2 + 1");
            Assert.AreEqual(2.0, spreadsheet.GetCellValue("A2"));
        }

        /// <summary>
        /// This method tests the GetCellValueLambda method. It should return the value, not the contents of a cell. Must throw an InvalidNameException 
        /// for an invalid name, and return an empty string if the cell is empty. Tests for all three possible return values: string, double, and 
        /// FormulaError
        /// </summary>
        [TestMethod]
        public void GetCellValueLambda()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Spreadsheet spreadsheet2 = new Spreadsheet(s => false, s => s, "default");
            try
            {
                spreadsheet.GetCellValueLambda("___");
                spreadsheet2.GetCellValueLambda("A2");
                Assert.Fail();
            }
            catch (InvalidNameException) { }

            try
            {
                spreadsheet.SetContentsOfCell("A2", "");
                spreadsheet.GetCellValueLambda("A2");
                spreadsheet.GetCellValueLambda("B2");
                spreadsheet.SetContentsOfCell("A2", "=2/0");
                spreadsheet.GetCellValueLambda("A2");
                Assert.Fail();
            }
            catch (ArgumentException) { }
            spreadsheet.SetContentsOfCell("A2", "2.0");
            Assert.AreEqual(2.0, spreadsheet.GetCellValueLambda("A2"));
            spreadsheet.SetContentsOfCell("A2", "=2+2");
            Assert.AreEqual(4.0, spreadsheet.GetCellValueLambda("A2"));
            spreadsheet.SetContentsOfCell("B2", "1.0");
            spreadsheet.SetContentsOfCell("A2", "=B2 + 1");
            Assert.AreEqual(2.0, spreadsheet.GetCellValueLambda("A2"));
        }

        /// <summary>
        /// This method tests the return value of setting a cell's contents.
        /// </summary>
        [TestMethod]
        public void ReturnSetContents()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            List<string> expected = new List<string>();
            expected.Add("A2");
            Assert.AreEqual(expected.ToString(), spreadsheet.SetContentsOfCell("A2", "2.0").ToString());
            expected.Add("B2");
            spreadsheet.SetContentsOfCell("B2", "=A2");
            Assert.AreEqual(expected.ToString(), spreadsheet.SetContentsOfCell("A2", "2.0").ToString());
            spreadsheet.SetContentsOfCell("B2", "2.0");
            expected.Remove("B2");
            Assert.AreEqual(expected.ToString(), spreadsheet.SetContentsOfCell("A2", "6").ToString());

            Spreadsheet spreadsheet1 = new Spreadsheet();
            List<string> expected1 = new List<string>();
            expected1.Add("A2");
            Assert.AreEqual(expected1.ToString(), spreadsheet1.SetContentsOfCell("A2", "test").ToString());
            expected1.Add("B2");
            spreadsheet1.SetContentsOfCell("B2", "=A2");
            Assert.AreEqual(expected1.ToString(), spreadsheet1.SetContentsOfCell("A2", "testing").ToString());
            spreadsheet1.SetContentsOfCell("B2", "2.0");
            expected1.Remove("B2");
            Assert.AreEqual(expected1.ToString(), spreadsheet1.SetContentsOfCell("A2", "hello").ToString());

            Spreadsheet spreadsheet2 = new Spreadsheet();
            List<string> expected2 = new List<string>();
            expected2.Add("A2");
            Assert.AreEqual(expected2.ToString(), spreadsheet2.SetContentsOfCell("A2", "=2+2").ToString());
            expected2.Add("B2");
            spreadsheet2.SetContentsOfCell("B2", "=A2");
            Assert.AreEqual(expected2.ToString(), spreadsheet2.SetContentsOfCell("A2", "=4+6").ToString());
            spreadsheet2.SetContentsOfCell("B2", "2.0");
            expected2.Remove("B2");
            Assert.AreEqual(expected2.ToString(), spreadsheet2.SetContentsOfCell("A2", "=C3").ToString());
        }

        /// <summary>
        /// Testing the GetSavedVersion method to ensure that the proper version is returned. Empty constructor should return 
        /// "default", the other two constructors should return the version that was inputted by the user. 
        /// </summary>
        [TestMethod]
        public void GetSavedVersion()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            Assert.AreEqual(true, spreadsheet.Changed);
            spreadsheet.Save("text.txt");
            Assert.AreEqual(false, spreadsheet.Changed);
            Assert.AreEqual("default", spreadsheet.GetSavedVersion("text.txt"));

            Spreadsheet spreadsheet2 = new Spreadsheet(s => true, s => s, "1.1");
            Assert.AreEqual(true, spreadsheet2.Changed);
            spreadsheet2.Save("text2.txt");
            Assert.AreEqual(false, spreadsheet2.Changed);
            Assert.AreEqual("1.1", spreadsheet2.GetSavedVersion("text2.txt"));

            Spreadsheet spreadsheet3 = new Spreadsheet("text.txt", s => true, s => s, "1.1");
            Assert.AreEqual(true, spreadsheet3.Changed);
            spreadsheet3.Save("text.txt");
            Assert.AreEqual(false, spreadsheet3.Changed);
            Assert.AreEqual("1.1", spreadsheet3.GetSavedVersion("text.txt"));
        }

        /// <summary>
        /// This method tests the Save method of the spreadsheet class. It should save each cell in the spreadsheet using the 
        /// XML Writer. 
        /// </summary>
        [TestMethod]
        public void Save()
        {
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A2", "2.0");
            spreadsheet.SetContentsOfCell("A1", "text");
            spreadsheet.SetContentsOfCell("A3", "=2+2");
            spreadsheet.Save("text.txt");
            Assert.AreEqual("default", spreadsheet.GetSavedVersion("text.txt"));

            Spreadsheet spreadsheet2 = new Spreadsheet(s => true, s => s, "1.1");
            spreadsheet2.SetContentsOfCell("A2", "2.0");
            spreadsheet2.SetContentsOfCell("A1", "text");
            spreadsheet2.SetContentsOfCell("A3", "=2+2");
            spreadsheet2.Save("text.txt");
            Assert.AreEqual("1.1", spreadsheet2.GetSavedVersion("text.txt"));

            Spreadsheet spreadsheet3 = new Spreadsheet("text.txt", s => true, s => s, "1.1");
            spreadsheet3.SetContentsOfCell("A2", "2.0");
            spreadsheet3.SetContentsOfCell("A1", "text");
            spreadsheet3.SetContentsOfCell("A3", "=2+2");
            spreadsheet3.Save("text.txt");
            Assert.AreEqual("1.1", spreadsheet3.GetSavedVersion("text.txt"));
        }
    }
}