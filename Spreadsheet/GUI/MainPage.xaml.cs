



using SpreadsheetUtilities;
using SS;
using System.Collections;
using System.Diagnostics;
using System.Reflection.Metadata.Ecma335;
using System.Runtime.CompilerServices;

namespace GUI
{
    /// <summary>
    /// Author:    Kira Halls and Nandini Goel
    /// Partner:   Nandini Goel, Kira Halls (Both worked equally)
    /// Date:      March 3, 2023
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
    /// This class is responsible for implementing the actual code behind our Spreadsheet GUI. It handles the creation of the spreadsheet grid and the row and column labels, creates the entries for the cells, and all other issues associated with
    /// initializing the Main Page of the GUI. It also handles the tasks expected when clicking on a cell, hitting enter after typing in a cell, changing the cell text when a formula is evaluated, resetting the cell text if the cell contents are changed through 
    /// an upper widget, etc. This class has an instance variable containing the spreadsheet being used, the currently selected entry, as well as two dictionaries that contain a column name with a list of its associated cell/entry values, and a dictionary
    /// that contains cell names as the key and their contents as the value. This class must accomplish all spreadsheet functionality as listed in the spreadsheet class (i.e. storing a cells contents and value as separate variables, evaluating a formula, 
    /// alerting the user if a formula is incorrectly formatted, allowing other spreadsheet cells to be used as variables in a formula, etc.). It uses a custom MyEntry class to represent the cells, allowing the user to change the text, select and highlight 
    /// a cell, etc. This class is, in general, just a visual representation of what the spreadsheet class can do. 
    /// One special feature of this class is the ability to change the color scheme from default to a red, orange, yellow, green, blue, or purple color scheme. This is accomplished through setting 
    /// the background colors of the page elements based on the color scheme the user selects. 
    ///
    /// 
    /// </summary>
    public partial class MainPage : ContentPage
    {
        /// <summary>
        ///    Definition of what information (method signature) must be sent
        ///    by the Entry when it is modified.
        /// </summary>
        /// <param name="col"> col (char) in grid, e.g., A5 </param>
        /// <param name="row"> row (int) in grid,  e.g., A5 </param>
        public delegate void ActionOnCompleted(string col, int row);

        private Dictionary<string, List<MyEntry>> Entries = new Dictionary<string, List<MyEntry>>(); //A dictionary containing column names ("A", "B", etc) as keys and a List of MyEntry objects that are contained in that column
        private Dictionary<string, string> singularEntries = new Dictionary<string, string>(); //A dictionary containing cell names as keys and their associated contents as values
        private Spreadsheet spreadsheet = new Spreadsheet(s => true, s => s.ToUpper(), "six");
        private MyEntry selectedEntry; //The current highlighted entry
        private string filepath = Environment.GetFolderPath(Environment.SpecialFolder.Desktop); //Default filepath to the user's desktop
        private Color backgroundColor = Colors.Gainsboro; 
        private Color highlightColor = Colors.DarkSeaGreen;

        /// <summary>
        /// This method handles the initialization and creation of the content window. It initializes all necessary visual components, the column and row headers with labels, as well as 
        /// the grid containing the cells themselves. 
        /// </summary>
        public MainPage()
        {
            InitializeComponent();
            InitializeColumnHeaders();
            InitializeRowHeaders();
            InitializeGrid();
        }

        /// <summary>
        /// This method is responsible for creating a new spreadsheet when the user selects 'File'> 'New'. It must first check that the current spreadsheet was saved, and display a Loss of Data warning message if it has not. 
        /// After it ensures that, it creates a new spreadsheet and initializes a new MainPage with  a blank spreadsheet. 
        /// 
        /// </summary>
        /// <param name="sender"></param> Part of required parameter for method header of this type
        /// <param name="e"></param> Part of required parameter for method header of this type
        private async void FileMenuNew(object sender, EventArgs e)
        {
            if (spreadsheet.Changed == true)
            {
                bool answer = await DisplayAlert("Warning", "You have not saved your file. Any unsaved data will be lost.", "Continue", "Cancel");
            }
            foreach (string cell in spreadsheet.GetNamesOfAllNonemptyCells()) //Clear all nonempty cells
            {
               List<MyEntry> rowEntries = Entries.GetValueOrDefault(cell[0].ToString(), new List<MyEntry>());
                if (rowEntries.Count != 0)
                {
                    int row = System.Convert.ToInt32(cell[1].ToString());
                    Debug.WriteLine("row " + row);
                    MyEntry entry = rowEntries[row - 1];
                    entry.ClearAndUnfocus();
                }
            }
            this.spreadsheet = new Spreadsheet(s => true, s => s.ToUpper(), "six");
            this.Entries = new Dictionary<string, List<MyEntry>>();
            this.singularEntries = new Dictionary<string, string>();
        }

        /// <summary>
        /// This method is responsible for opening a previously saved spreadsheet when the user selects 'File' > 'Open'. It must first check that the current spreadsheet was saved, and display a Loss of Data warning message if it has not. 
        /// After that is ensured, it will ask for a filepath to the spreadsheet the user wants to open. It then creates a new spreadsheet using that saved spreadsheet version and initializes a new MainPage with the opened spreadsheet. 
        /// 
        /// </summary>
        /// <param name="sender"></param> Part of required parameter for method header of this type
        /// <param name="e"></param> Part of required parameter for method header of this type
        private async void FileMenuOpenAsync(object sender, EventArgs e)
        {
            if (spreadsheet.Changed == true)
            {
                bool answer = await DisplayAlert("Warning", "You have not saved your file. Any unsaved data will be lost.", "Continue", "Cancel");
            }
            string result = await DisplayPromptAsync("Please provide a file path", "File Path:");
            foreach (string cell in spreadsheet.GetNamesOfAllNonemptyCells()) //Clear current cells
            {
                List<MyEntry> rowEntries = Entries.GetValueOrDefault(cell[0].ToString(), new List<MyEntry>());
                if (rowEntries.Count != 0)
                {
                    int row = System.Convert.ToInt32(cell[1].ToString());
                    MyEntry entry = rowEntries[row - 1];
                    entry.ClearAndUnfocus();
                }
            }
            this.spreadsheet = new Spreadsheet(filepath + result + ".sprd", s => true, s => s.ToUpper(), "six");
            foreach (string cell in spreadsheet.GetNamesOfAllNonemptyCells()) //Set contents of gui cells to be the contents of opened spreadsheet
            {
                List<MyEntry> rowEntries = Entries.GetValueOrDefault(cell[0].ToString(), new List<MyEntry>());
                if (rowEntries.Count != 0)
                {
                    int row = System.Convert.ToInt32(cell[1].ToString());
                    MyEntry entry = rowEntries[row - 1];
                    entry.SetContents(spreadsheet.GetCellContents(cell).ToString());
                }
            }
        }

        /// <summary>
        /// This method handles saving the current spreadsheet. It automatically saves it to the user's desktop using the .sprd extension. 
        /// 
        /// </summary>
        /// <param name="sender"></param> Part of required parameter for method header of this type
        /// <param name="e"></param> Part of required parameter for method header of this type
        private async void FileMenuSave(object sender, EventArgs e)
        {
            string result = await DisplayPromptAsync("Please provide a file path", "File Path:");
            spreadsheet.Save(filepath + result+ ".sprd");
        }

        /// <summary>
        /// This method handles displaying help messages when the user selects 'File' > 'Help'. It discusses navigating the spreadsheet, expected cell contents, and the special feature of the spreadsheet.
        /// </summary>
        /// <param name="sender"></param> Part of the required parameters for method headers of this type
        /// <param name="e"></param> Part of the required parameters for method headers of this type
        private async void FileMenuHelp(object sender, EventArgs e)
        {
            await DisplayAlert("Help", "Overview and Navigation: Click on desired cell to select it. Selected cells are highlighted and will display its contents (i.e. a string, a number, or a formula) when highlighted. " +
                "To change the content of a cell, simply highlight the cell and begin typing. Once the cell is clicked out of, or the user hits enter, the cell will be unselected and the cell value (i.e. a string, a number, or the evaluated formula)" +
                "will then be displayed. To see both the contents and the value of a cell at the same time, select the cell and the widget located at the top of the spreadsheet will display the cell name, the cell value, and the cell contents. " +
                "Cell contents can be edited from this widget, as well. To go to the next cell down, simply hit enter and that next cell will be selected.", "Okay");
            await DisplayAlert("Help", "Cell Contents: Cell contents can be some text, a number, or a formula. To indicate that the cell contents are a formula, begin with '='. If your cell does not immediately evaluate" +
                ", that means there is an issue with your formula. Click on the cell and an error message will be displayed indicating the problem.", "Okay");
            await DisplayAlert("Help", "Special Feature: This spreadsheet allows users to select different color schemes from a list located in the File menu. The options are Default, Red, Orange, Yellow, Green," +
                " Blue, and Purple. Selecting the desired color will change the spreadsheet color scheme and can be altered at any time. To do so, select the 'File' button in the top left of the spreadsheet, select " +
                "'Color Scheme', and choose your desired color scheme from the given menu.", "Okay");
        }

        /// <summary>
        /// This method contains the execution of our special feature: changing the spreadsheet color scheme. It allows a user to select from seven color schemes by adding a 'Color Scheme' option to the file dropdown menu
        /// and displaying a list of Default, Red, Orange, Yellow, Green, Blue, and Purple color schemes for the user to choose from. It changes the entry colors, the highlighted cell colors, and the row and column header colors.
        ///
        /// </summary>
        /// <param name="sender"></param> Part of the required parameters for method headers of this type
        /// <param name="e"></param> Part of the required parameters for method headers of this type
        private async void FileMenuColorScheme(object sender, EventArgs e)
        {
            string result = await DisplayActionSheet("Please select a color scheme", null, null, "Default", "Red", "Orange", "Yellow", "Green", "Blue", "Purple");
            if (result == "Default")
            {
                this.TopLabels.BackgroundColor = Colors.DarkGreen;
                this.LeftLabels.BackgroundColor = Colors.DarkGreen;
                this.Grid.BackgroundColor = Colors.DarkGray;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.DarkSeaGreen;
            }
            if (result == "Red")
            {
                this.TopLabels.BackgroundColor = Colors.IndianRed;
                this.LeftLabels.BackgroundColor = Colors.IndianRed;
                this.Grid.BackgroundColor = Colors.Pink;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.LightCoral;
            }
            if (result == "Orange")
            {
                this.TopLabels.BackgroundColor = Colors.SandyBrown;
                this.LeftLabels.BackgroundColor = Colors.SandyBrown;
                this.Grid.BackgroundColor = Colors.PeachPuff;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.PapayaWhip;
            }
            if (result == "Yellow")
            {
                this.TopLabels.BackgroundColor = Colors.Goldenrod;
                this.LeftLabels.BackgroundColor = Colors.Goldenrod;
                this.Grid.BackgroundColor = Colors.PaleGoldenrod;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.LightYellow;
            }
            if (result == "Green")
            {
                this.TopLabels.BackgroundColor = Colors.OliveDrab;
                this.LeftLabels.BackgroundColor = Colors.OliveDrab;
                this.Grid.BackgroundColor = Colors.DarkKhaki;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.Olive;
            }
            if (result == "Blue")
            {
                this.TopLabels.BackgroundColor = Colors.DarkCyan;
                this.LeftLabels.BackgroundColor = Colors.DarkCyan;
                this.Grid.BackgroundColor = Colors.PowderBlue;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.PaleTurquoise;
            }
            if (result == "Purple")
            {
                this.TopLabels.BackgroundColor = Colors.DarkMagenta;
                this.LeftLabels.BackgroundColor = Colors.DarkMagenta;
                this.Grid.BackgroundColor = Colors.Thistle;
                this.BackgroundColor = Colors.Black;
                this.highlightColor = Colors.Plum;
            }

        }

        /// <summary>
        /// This method handles the creation of the column headers (i.e. "A", "B", etc.). It creates a new border for each letter of the alphabet, adds a label to that border 
        /// containing the column name, and adds a blank border as the very first cell so that all row headers are not listed incorrectly under the "A" column.
        /// </summary>
        private void InitializeColumnHeaders()
        {
            //Add a blank cell to ensure row labels aren't all listed under column header 'A'
            TopLabels.Add(
                new Border
                {
                    Stroke = Color.FromRgb(250, 250, 250),
                    StrokeThickness = 1,
                    HeightRequest = 20,
                    WidthRequest = 75,
                    HorizontalOptions = LayoutOptions.Center,
                }
                );

            //Add actual column headers
            for (int i = 0; i < 26; i++)
            {
                String label = (char)('A' + i % 26) + "";
                TopLabels.Add(
               new Border
               {
                   Stroke = Color.FromRgb(250, 250, 250),
                   StrokeThickness = 1,
                   HeightRequest = 20,
                   WidthRequest = 75,
                   HorizontalOptions = LayoutOptions.Center,
                   BackgroundColor = Colors.Transparent,
                   Content =
                       new Label
                       {
                           Text = $"{label}",
                           BackgroundColor = Colors.Transparent,
                           HorizontalTextAlignment = TextAlignment.Center
                       }
               }
               ) ;
            }
            this.TopLabels.BackgroundColor = Colors.DarkGreen;
        }

        /// <summary>
        /// This method initializes the row headers for the spreadsheet. It adds ten new borders containing labels titling the numbers 1-10 to the left labels of the grid. 
        /// Originally, the spreadsheet was meant to contain 99 rows, but due to issues with slow computer processing, it has been reduced to 10 per the assignment requirements.
        /// </summary>
        private void InitializeRowHeaders()
        {
            for (int i = 1; i <= 10; i++)
            {
                string label = i.ToString();
                LeftLabels.Add(
               new Border
               {
                   Stroke = Color.FromRgb(250, 250, 250),
                   StrokeThickness = 1,
                   HeightRequest = 20,
                   WidthRequest = 75,
                   HorizontalOptions = LayoutOptions.Center,
                   BackgroundColor = Colors.Transparent,
                   Content =
                       new Label
                       {
                           Text = $"{label}",
                           BackgroundColor = Colors.Transparent,
                           HorizontalTextAlignment = TextAlignment.Center
                       }
               }
               );
            }
            this.LeftLabels.BackgroundColor = Colors.DarkGreen;
        }

        /// <summary>
        /// This method initializes the actual cell grid of the spreadsheet. It creates ten horizontal stack layout objects that represent each 'row' of the grid, and adds twenty six 
        /// entries to each row to represent the individual cells. The entries are objects of the MyEntry class as provided by the professors and altered by Nandini and Kira. Additionally, this 
        /// method adds each new cell to the Entries dictionary that holds all entries of the spreadsheet for later use.
        /// </summary>
        private void InitializeGrid()
        {
            for (int i = 0; i < 10; i++)
            {
                HorizontalStackLayout row = new HorizontalStackLayout();
                Grid.Add(row);
                for (int j = 0; j < 26; j++)
                {
                    string columnName = ((char) ('A' + j % 26)) + "";
                    MyEntry entry = new MyEntry(i + 1, columnName, handleCellChanged, this, this.spreadsheet)
                    {
                        BackgroundColor = Colors.Transparent,
                        HorizontalTextAlignment = TextAlignment.Center,
                        VerticalTextAlignment = TextAlignment.Center,
                        HeightRequest = 30,
                        FontSize = 12.5,
                        
                    };

                    row.Add(
                     new Border
                     {
                         Stroke = Color.FromRgb(250, 250, 250),
                         StrokeThickness = 1,
                         HeightRequest = 20,
                         WidthRequest = 75,
                         HorizontalOptions = LayoutOptions.Center,
                         VerticalOptions = LayoutOptions.Center,
                         BackgroundColor = Colors.Transparent,
                         Content = entry
                     }
               );
                    if (Entries.ContainsKey(columnName))
                    {
                        List<MyEntry> entries = Entries.GetValueOrDefault(columnName, new List<MyEntry>());
                        entries.Add(entry);

                    }
                    else
                    {
                        List<MyEntry> entries = new List<MyEntry>();
                        entries.Add(entry);
                        Entries.Add(columnName, entries);
                    }    
                }
            }
            this.Grid.BackgroundColor = Colors.DarkGray;
        }

        /// <summary>
        /// This method handles auto-focusing on cell A1 when the spreadsheet is first opened. 
        /// </summary>
        /// <param name="sender"></param> Part of the required parameters for method headers of this type
        /// <param name="e"></param> Part of the required parameters for method headers of this type
        private void ActionOnLoaded(object sender, EventArgs e)
        {
            this.Entries["A"][0].Focus();
        }

        /// <summary>
        ///   This method will be called by the individual Entry elements when Enter
        ///   is pressed in them.
        ///   
        ///   The idea is to move to the next cell in the list.
        /// </summary>
        /// <param name="col"> e.g., The 'A' in A5 </param>
        /// <param name="row"> e.g., The  5  in A5 </param>
        void handleCellChanged(string col, int row)
        {
            Debug.WriteLine($"changed: {col}{row}");
            List<MyEntry> entry = Entries.GetValueOrDefault(col, new List<MyEntry>());
        }

        /// <summary>
        /// This method is called when an entry, or 'cell', is clicked. It highlights the cell by changing the background color, sets the instance variable 
        /// 'selectedEntry' to the entry calling the method, and updates the widget at the top of the spreadsheet to display the entry name followed by the entry value, 
        /// and shows the entry's contents in an editable widget just after that. It also sets the text of the cell itself to the contents of the cell as opposed to the value of the cell.
        /// </summary>
        /// <param name="entry"></param> The entry that was clicked
        private void CellClicked(MyEntry entry)
        {
            this.selectedEntry = entry;
            entry.BackgroundColor = highlightColor;
            selectedCell.Text = entry.GetName() + ": " + entry.GetValue();
            selectedCellContents.Text = entry.GetContents();    
        }

        /// <summary>
        /// This method is called when a cell becomes unfocused or 'completed' (i.e. when the user presses enter in the cell). It changes the cell to no longer be highlighted and sets the background color to the unhighlighted color, 
        /// resets the contents of the cell if the contents are a formula and resets the text of the cell to be the evaluated value of the formula. If the contents are not a formula, it sets the contents to be the text of the cell.
        /// </summary>
        /// <param name="sender"></param> Part of the required parameters for method headers of this type
        /// <param name="e"></param> Part of the required parameters for method headers of this type
        private void ContentTextChanged(object sender, EventArgs e)
        {          
            if (selectedCellContents.Text != "" && selectedCellContents.Text[0] == '=') //If contents of cell is a formula
            {
                this.selectedEntry.SetContents(selectedCellContents.Text);
                selectedCell.Text = selectedEntry.GetName() + ": " + selectedEntry.GetValue();
            }
            else
            {
                this.selectedEntry.SetContents(selectedCellContents.Text);
                selectedEntry.Text = selectedCellContents.Text;
                selectedCell.Text = selectedEntry.GetName() + ": " + selectedEntry.GetValue();
            }
        }

        /// <summary>
        ///   Author: H. James de St. Germain, altered by Kira Halls and Nandini Goel
        ///   Date:   Spring 2023
        ///   
        ///   This code shows:
        ///   1) How to augment a Maui Widget (i.e., Entry) to add more information
        ///       a) using StyleId
        ///       b) using fields
        ///   2) How to provide a method that matches an event handler (used to clear all cells)
        ///   3) How to attach a method to an event handler (i.e., Completed)
        /// </summary>
        public class MyEntry : Entry
        {
            private int row;
            private string col;
            private string name;
            private string contents = "";
            private MainPage page;
            private string value;
            private Spreadsheet spreadsheet;

            /// <summary>
            ///   Function provided by "outside world" to be called whenever
            ///   this entry is modified
            /// </summary>
            private ActionOnCompleted onChange;

            /// <summary>
            ///   build an Entry element with the row "remembered"
            /// </summary>
            /// <param name="row"> unique identifier for this item </param>
            /// <param name="changeAction"> outside action that should be invoked after this cell is modified </param>
            public MyEntry(int row, string col, ActionOnCompleted changeAction, MainPage page, Spreadsheet spreadsheet) : base()
            {
                this.StyleId = $"{col + row.ToString()}";
                this.row = row;
                this.col = col;
                this.name = col + row.ToString();
                this.spreadsheet = spreadsheet;
                this.page = page;
                this.Text = "";
                page.singularEntries.Add(name, ""); //add the newly created entry to the dictionary containing the list of all entries in the spreadsheet
                this.BackgroundColor = page.backgroundColor;

                spreadsheet.SetContentsOfCell(name, "");

                // Action to take when the user presses enter on this cell
                this.Completed += CellChangedValue;

                //Action to take when the cell becomes focused or unfocused
                this.Focused += CellFocused;

                this.Unfocused += CellUnfocused;

                // "remember" outside worlds request about what to do when we change.
                onChange = changeAction;
            }

            /// <summary>
            ///   Remove focus and text from this widget
            /// </summary>
            public void ClearAndUnfocus()
            {
                this.contents = "";
                this.value = "";
                this.Text = "";
            }

            /// <summary>
            ///   Action to take when the value of this entry widget is changed
            ///   and the Enter Key pressed.
            /// </summary>
            /// <param name="sender"> ignored, but should == this </param>
            /// <param name="e"> ignored </param>
            private void CellChangedValue(object sender, EventArgs e)
            {              
                Unfocus();
                if (this.row < 10)
                {
                    page.Entries[this.col][row].Focus(); //Do not need to add +1 as each entry is contained at the index row - 1 in the associated List<MyEntry> in the Entries dictionary
                }
                CellUnfocused(sender, e);

                // Inform the outside world that we have changed
                onChange("A", row);
            }

            /// <summary>
            /// This method is called when the cell is 'focused'. It calls the 'CellClicked' method that handles updating the text, the entry contents, etc.
            /// </summary>
            /// <param name="sender"></param> Part of the required parameters for method headers of this type
            /// <param name="e"></param> Part of the required parameters for method headers of this type
            private void CellFocused(object sender, EventArgs e)
            {
                page.CellClicked(this);
            }

            /// <summary>
            /// This method is called when the cell becomes 'unfocused'. If the cell's contents are currently set to the default (i.e. "") then the cell's contents are set to the 
            /// text of the cell. If the cell content is not a formula, then the contents are also set to the text of the cell. Otherwise, the cell contents are a formula and the cell text is set to the evaluated 
            /// formula value.
            /// </summary>
            /// <param name="sender"></param> Part of the required parameters for method headers of this type
            /// <param name="e"></param> Part of the required parameters for method headers of this type
            private void CellUnfocused(object sender, EventArgs e)
            {
                if (this.contents == "")
                {
                    this.contents = this.Text;        
                }
                if ( spreadsheet.GetCellContents(name).GetType() != typeof(Formula))
                {  
                    this.contents = this.Text;     
                }
                try
                {
                    spreadsheet.SetContentsOfCell(name, contents);
                }
                catch (FormulaFormatException ex)
                {
                    this.value = "Invalid Formula";
                    this.Text = contents;  
                }
                catch (CircularException)
                {
                    this.value = "Circular Dependency detected";
                    this.Text = contents;
                }
                if (this.value != "Invalid Formula")
                {
                    this.contents = (string)spreadsheet.GetCellContents(name).ToString();
                    this.value = spreadsheet.GetCellValue(this.name).ToString();
                    this.Text = value;
                }
                BackgroundColor = Colors.Transparent;
                page.singularEntries[name] = this.contents;            
            }

            /// <summary>
            /// This method returns the cell/entry name
            /// </summary>
            /// <returns></returns> The string that is the cell name ex "A1"
            public string GetName()
            {
                return name;
            }

            /// <summary>
            /// This method returns the contents of the cell
            /// </summary>
            /// <returns></returns> The string that is the cells contents
            public string GetContents()
            {
                return contents;
            }
             
            /// <summary>
            /// This method returns the value (as opposed to the contents) of the cell
            /// </summary>
            /// <returns></returns> The string that is the value of the cell
            public string GetValue()
            {
                return value;
            }

            /// <summary>
            /// This method handles setting the contents of the cell. It sets the contents, gets all cells that depend on the current cell and recalculates their value and resets their text, 
            /// resets the value of the current cell, and resets the text to be the value of the cell.
            /// </summary>
            /// <param name="contents"></param> The new contents of the cell
            public void SetContents(string contents)
            {
                this.contents = contents;
                List<string> cellsToRecalculate = (List<string>) spreadsheet.SetContentsOfCell(name, contents);
                this.value = (string)spreadsheet.GetCellValue(this.name).ToString();
                this.Text = value;
                 foreach (string cell in cellsToRecalculate)
                {
                    string row = cell[1].ToString();
                    int tempVal;
                    Int32.TryParse(row, out tempVal);
                    MyEntry entry = page.Entries[cell[0].ToString()][tempVal];
                    spreadsheet.SetContentsOfCell(cell, page.singularEntries.GetValueOrDefault(cell, ""));
                    entry.Text = entry.GetValue().ToString();
                }
            }
        }
    }
}