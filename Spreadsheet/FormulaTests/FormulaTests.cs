using SpreadsheetUtilities;
using System.Data;

namespace FormulaTests
{
    /// <summary>
    /// Author:    Kira Halls
    /// Partner:   None
    /// Date:      February 2, 2023
    /// Course:    CS 3500, University of Utah, School of Computing
    /// Copyright: CS 3500 and [Your Name(s)] - This work may not 
    ///            be copied for use in Academic Coursework.
    ///
    /// I, Kira Halls, certify that I wrote this code from scratch and
    /// did not copy it in part or whole from another source.  All 
    /// references used in the completion of the assignments are cited 
    /// in my README file.
    ///
    /// File Contents
    ///
    /// This test class tests all cases and edge cases for the Formula class.It also tests all possible exceptions.
    /// Each test method tests a different subset of functionality--for example, simple addition, precedence rules, syntax errors, etc. 
    /// Many of these tests are adapted from my assignment 1 test class, as a lot of the testing needed for this class is 
    /// the same testing that was needed for assignment 1. Comments are written above each test method to explain what is being tested.
    /// </summary>
    [TestClass]
    public class FormulaTests
    {
        //// <summary>
        /// This method tests simple addition equations. It ensures the program handles addition properly, 
        /// follows the rules of the commutative property, and handles whitespace correctly. It contains two sections; the first section is 
        /// simple addition equations with no variables, the second contains the same equations but with variables. Both formula constructors 
        /// are tested within this method.
        /// All variables used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void simpleAddition()
        {
            Assert.AreEqual(3.0, (new Formula("1+2").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("2+1").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("1+ 2").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("1+2", N => N.ToUpper(), V => true).Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("2+1", N => N.ToUpper(), V => true).Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("1+ 2", N => N.ToUpper(), V => true).Evaluate(s => 1)));

            Assert.AreEqual(3.0, (new Formula("1+a1").Evaluate(a1 => 2)));
            Assert.AreEqual(3.0, (new Formula("b1+1").Evaluate(b1 => 2)));
            Assert.AreEqual(3.0, (new Formula("1+ c_6").Evaluate(c_6 => 2)));
            Assert.AreEqual(3.0, (new Formula("1+a1", N => N.ToUpper(), V => true).Evaluate(a1 => 2)));
            Assert.AreEqual(3.0, (new Formula("b2+1", N => N.ToUpper(), V => true).Evaluate(b2 => 2)));
            Assert.AreEqual(3.0, (new Formula("1+ c_6", N => N.ToUpper(), V => true).Evaluate(c_6 => 2)));
        }

        /// <summary>
        /// This method tests simple subtraction equations. It ensures the program handles subtraction properly, 
        /// the answer changes when the order of the equation is changed, can give negative answers, and handles whitespace correctly.
        /// It contains two sections; the first section is simple subtraction equations with no variables, the second contains the same equations but with variables.
        /// All variables used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void simpleSubtraction()
        {
            Assert.AreEqual(3.0, (new Formula("4-1").Evaluate(s => 1)));
            Assert.AreNotEqual(3.0, (new Formula("1-4").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("4 -1").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("4-1", N => N.ToUpper(), V => true).Evaluate(s => 1)));
            Assert.AreNotEqual(3.0, (new Formula("1-4", N => N.ToUpper(), V => true).Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("4 -1", N => N.ToUpper(), V => true).Evaluate(s => 1)));

            Assert.AreEqual(3.0, (new Formula("b1-1").Evaluate(B1 => 4)));
            Assert.AreNotEqual(3.0, (new Formula("1-S9").Evaluate(S9 => 4)));
            Assert.AreEqual(3.0, (new Formula("4 -YX1").Evaluate(YX1 => 1)));
            Assert.AreEqual(3.0, (new Formula("_b-1", N => N.ToUpper(), V => true).Evaluate(_b => 4)));
            Assert.AreNotEqual(3.0, (new Formula("c101-4", N => N.ToUpper(), V => true).Evaluate(C101 => 1)));
            Assert.AreEqual(3.0, (new Formula("4 -C6", N => N.ToUpper(), V => true).Evaluate(C6 => 1)));
        }

        /// <summary>
        /// This method tests simple multiplication equations. It ensures the program handles multiplication properly, 
        /// follows the rules of the commutative property, and handles whitespace correctly. It contains two sections; the first section is 
        /// simple multiplication equations with no variables, the second contains the same equations but with variables. All variables 
        /// used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void simpleMultiplication()
        {
            Assert.AreEqual(6.0, (new Formula("2*3").Evaluate(s => 1)));
            Assert.AreEqual(6.0, (new Formula("3*2").Evaluate(s => 1)));
            Assert.AreEqual(6.0, (new Formula("2 * 3").Evaluate(s => 1)));
            Assert.AreEqual(6.0, (new Formula("2*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(6.0, (new Formula("3*2", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(6.0, (new Formula("2 * 3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(6.0, (new Formula("b*3").Evaluate(b => 2)));
            Assert.AreEqual(6.0, (new Formula("3*ten1").Evaluate(TEN1 => 2)));
            Assert.AreEqual(6.0, (new Formula("2 * c4").Evaluate(C4 => 3)));
            Assert.AreEqual(6.0, (new Formula("2*D9", N => N.ToUpper(), S => true).Evaluate(D9 => 3)));
            Assert.AreEqual(6.0, (new Formula("3*_9", N => N.ToUpper(), S => true).Evaluate(_9 => 2)));
            Assert.AreEqual(6.0, (new Formula("2 * b3", N => N.ToUpper(), S => true).Evaluate(B3 => 3)));
        }

        /// <summary>
        /// This method tests simple division equations. It ensures the program handles division properly, 
        /// the answer changes when the order of the equation is changed, and handles whitespace correctly.
        /// It contains two sections; the first section is simple division equations with no variables, the second contains the same equations but with variables.
        /// All variables used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void simpleDivision()
        {
            Assert.AreEqual(3.0, (new Formula("6/2").Evaluate(s => 1)));
            Assert.AreNotEqual(3.0, (new Formula("2/6").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("6 / 2").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("6/2", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreNotEqual(3.0, (new Formula("2/6", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("6 / 2", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(3.0, (new Formula("B2/2").Evaluate(B2 => 6)));
            Assert.AreNotEqual(3.0, (new Formula("A12/6").Evaluate(A12 => 2)));
            Assert.AreEqual(3.0, (new Formula("6 / E_").Evaluate(E_ => 2)));
            Assert.AreEqual(3.0, (new Formula("6/b", N => N.ToUpper(), S => true).Evaluate(B => 2)));
            Assert.AreNotEqual(3.0, (new Formula("2/_a", N => N.ToUpper(), S => true).Evaluate(_a => 6)));
            Assert.AreEqual(3.0, (new Formula("6 / e99", N => N.ToUpper(), S => true).Evaluate(E99 => 2)));
        }

        /// <summary>
        /// This method tests equations that have addition combined with each of the other operators. It ensures the program can follow order of operations, specifically in regards to 
        /// addition. The method contains two sections; there is a section without variables for each of the operators (-, *, /) in an equation with an addition section, and each of those equations 
        /// is repeated with variables contained in the equation. Whitespace handling testing is interspersed throughout to ensure it is handled properly. All variables 
        /// used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void additionCombinations()
        {
            Assert.AreEqual(0.0, (new Formula("1+2-3").Evaluate(s => 1)));
            Assert.AreEqual(7.0, (new Formula("1+2*3").Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("1+6/3").Evaluate(s => 1)));
            Assert.AreEqual(0.0, (new Formula("1+2-3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(7.0, (new Formula("1+2*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(3.0, (new Formula("1+6/3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(0.0, (new Formula("1 +s- 3").Evaluate(S => 2)));
            Assert.AreEqual(7.0, (new Formula("1+2 * ee").Evaluate(EE => 3)));
            Assert.AreEqual(3.0, (new Formula("1+ 6/s9 ").Evaluate(S9 => 3)));
            Assert.AreEqual(0.0, (new Formula("c +2- 3", N => N.ToUpper(), S => true).Evaluate(C => 1)));
            Assert.AreEqual(7.0, (new Formula("1+math * 3", N => N.ToUpper(), S => true).Evaluate(MATH => 2)));
            Assert.AreEqual(3.0, (new Formula("1+ 6/k9 ", N => N.ToUpper(), S => true).Evaluate(K9 => 3)));
        }

        /// <summary>
        /// This method tests equations that have subtraction combined with each of the other operators. It ensures the program can follow order of operations, specifically in regards to 
        /// subtraction. The method contains six sections; there is a section without variables for each of the operators (+, *, /) in an equation with a subtraction section, and each of those three sections 
        /// is repeated with variables contained in the equation. Whitespace handling testing is interspersed throughout to ensure it is handled properly. All variables 
        /// used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void subtractionCombinations()
        {
            Assert.AreEqual(7.0, (new Formula("6-2+3").Evaluate(s => 1)));
            Assert.AreEqual(0.0, (new Formula("6-2*3").Evaluate(s => 1)));
            Assert.AreEqual(4.0, (new Formula("6-6/3").Evaluate(s => 1)));
            Assert.AreEqual(7.0, (new Formula("6-2+3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(0.0, (new Formula("6-2*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(4.0, (new Formula("6-6/3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(7.0, (new Formula("6 -f2+ 3").Evaluate(F2 => 2)));
            Assert.AreEqual(0.0, (new Formula(" 6 -2*ex ").Evaluate(EX => 3)));
            Assert.AreEqual(4.0, (new Formula(" 6- s11/ 3").Evaluate(s11 => 6)));
            Assert.AreEqual(7.0, (new Formula("x_-2+3", N => N.ToUpper(), S => true).Evaluate(x_ => 6)));
            Assert.AreEqual(0.0, (new Formula("6-r*3", N => N.ToUpper(), S => true).Evaluate(R => 2)));
            Assert.AreEqual(4.0, (new Formula("6-6/l2", N => N.ToUpper(), S => true).Evaluate(L2 => 3)));
        }

        /// <summary>
        ///  This method tests equations that have multiplication combined with each of the other operators. It ensures the program can follow order of operations, specifically in regards to 
        /// multiplication. The method contains six sections; there is a section without variables for each of the operators (-, +, /) in an equation with a multiplication section, and each of those three sections 
        /// is repeated with variables contained in the equation. Whitespace handling testing is interspersed throughout to ensure it is handled properly. All variables 
        /// used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void multiplicationCombinations()
        {
            Assert.AreEqual(13.0, (new Formula("5*2+3").Evaluate(s => 1)));
            Assert.AreEqual(7.0, (new Formula("5*2-3").Evaluate(s => 1)));
            Assert.AreEqual(4.0, (new Formula("2*6/3").Evaluate(s => 1)));
            Assert.AreEqual(13.0, (new Formula("5*2+3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(7.0, (new Formula("5*2-3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(4.0, (new Formula("2*6/3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(13.0, (new Formula("5* _+ 3").Evaluate(_ => 2)));
            Assert.AreEqual(7.0, (new Formula("5 *2-a ").Evaluate(a => 3)));
            Assert.AreEqual(4.0, (new Formula("e9t* 6 /3").Evaluate(e9t => 2)));
            Assert.AreEqual(13.0, (new Formula("5*2+a8", N => N.ToUpper(), S => true).Evaluate(A8 => 3)));
            Assert.AreEqual(7.0, (new Formula("5*2-p_p", N => N.ToUpper(), S => true).Evaluate(p_p => 3)));
            Assert.AreEqual(4.0, (new Formula("b0*6/3", N => N.ToUpper(), S => true).Evaluate(b0 => 2)));
        }

        /// <summary>
        ///  This method tests equations that have division combined with each of the other operators. It ensures the program can follow order of operations, specifically in regards to 
        /// division. The method contains six sections; there is a section without variables for each of the operators (-, *, +) in an equation with a division section, and each of those three sections 
        /// is repeated with variables contained in the equation. Whitespace handling testing is interspersed throughout to ensure it is handled properly. All variables 
        /// used are valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void divisionCombinations()
        {
            Assert.AreEqual(8.0, (new Formula("10/2+3").Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("10/2-3").Evaluate(s => 1)));
            Assert.AreEqual(15.0, (new Formula("10/2*3").Evaluate(s => 1)));
            Assert.AreEqual(8.0, (new Formula("10/2+3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("10/2-3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(15.0, (new Formula("10/2*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(8.0, (new Formula(" 10/w0w +3").Evaluate(w0w => 2)));
            Assert.AreEqual(2.0, (new Formula("e9/ 2 - 3").Evaluate(e9 => 10)));
            Assert.AreEqual(15.0, (new Formula("10 / n2*3").Evaluate(n2 => 2)));
            Assert.AreEqual(8.0, (new Formula("10/2+eee", N => N.ToUpper(), S => true).Evaluate(EEE => 3)));
            Assert.AreEqual(2.0, (new Formula("10/2-n_", N => N.ToUpper(), S => true).Evaluate(N_ => 3)));
            Assert.AreEqual(15.0, (new Formula("10/c_9*3", N => N.ToUpper(), S => true).Evaluate(C_9 => 2)));
        }

        /// <summary>
        ///  This method tests equations that have parenthesis combined with each of the other operators. It ensures the program can follow order of operations, specifically in regards to 
        /// parenthesis. The method contains eight sections; there is a section without variables for each of the operators (-, *, /, +) contained in the parenthesis, and each of those four sections 
        /// is repeated with variables contained in the equation. The operators outside of the parenthesis are either multiplication or division symbols, to ensure the parenthesis
        /// are actually first, as required by order of operations. Whitespace handling testing is interspersed throughout to ensure it is handled properly. All variables are 
        /// valid as all variable exceptions will be tested in another method.
        /// </summary>
        [TestMethod]
        public void parenthesisCombinations()
        {
            Assert.AreEqual(30.0, (new Formula("(2+3)*6").Evaluate(s => 1)));
            Assert.AreEqual(4.0, (new Formula("(5+3)/2").Evaluate(s => 1)));
            Assert.AreEqual(30.0, (new Formula("(2+3)*6", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(4.0, (new Formula("(5+3)/2", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(12.0, (new Formula("(10-6)*3").Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("(12-6)/3").Evaluate(s => 1)));
            Assert.AreEqual(12.0, (new Formula("(10-6)*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("(12-6)/3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(12.0, (new Formula("(2*2)*3").Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("(2*3)/3").Evaluate(s => 1)));
            Assert.AreEqual(12.0, (new Formula("(2*2)*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("(2*3)/3", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(6.0, (new Formula("(10/5)*3").Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("(12/3)/2").Evaluate(s => 1)));
            Assert.AreEqual(6.0, (new Formula("(10/5)*3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(2.0, (new Formula("(12/3)/2", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(30.0, (new Formula("(2 +n8 )*6").Evaluate(n8 => 3)));
            Assert.AreEqual(4.0, (new Formula("(5+uuu )/ 2").Evaluate(uuu => 3)));
            Assert.AreEqual(30.0, (new Formula("(2 +uNt )*6", N => N.ToUpper(), S => true).Evaluate(UNT => 3)));
            Assert.AreEqual(4.0, (new Formula("(s7+3 )/ 2", N => N.ToUpper(), S => true).Evaluate(S7 => 5)));

            Assert.AreEqual(12.0, (new Formula(" (k8-6 )*3").Evaluate(k8 => 10)));
            Assert.AreEqual(2.0, (new Formula("(12-u6_ )/3").Evaluate(u6_ => 6)));
            Assert.AreEqual(12.0, (new Formula(" (10-6 )*ou4", N => N.ToUpper(), S => true).Evaluate(OU4 => 3)));
            Assert.AreEqual(2.0, (new Formula("(e12-6 )/3", N => N.ToUpper(), S => true).Evaluate(E12 => 12)));

            Assert.AreEqual(12.0, (new Formula(" (2*pi)* 3").Evaluate(pi => 2)));
            Assert.AreEqual(2.0, (new Formula("(2 *ut_ )/3").Evaluate(ut_ => 3)));
            Assert.AreEqual(12.0, (new Formula(" (2*2)* a0a", N => N.ToUpper(), S => true).Evaluate(A0A => 3)));
            Assert.AreEqual(2.0, (new Formula("(star *3 )/3", N => N.ToUpper(), S => true).Evaluate(STAR => 2)));

            Assert.AreEqual(6.0, (new Formula("(t99 /5)*3 ").Evaluate(t99 => 10)));
            Assert.AreEqual(2.0, (new Formula("(12/ s__) /2").Evaluate(s__ => 3)));
            Assert.AreEqual(6.0, (new Formula("(10 /5)*i1 ", N => N.ToUpper(), S => true).Evaluate(I1 => 3)));
            Assert.AreEqual(2.0, (new Formula("(12/ 3) /am", N => N.ToUpper(), S => true).Evaluate(AM => 2)));
        }

        /// <summary>
        /// This method tests random combinations of the operators to test the overall function of the program. It must follow the rules of precedence as 
        /// well as typical arithmetic rules. This method contains two sections, one without variables, and one with. Whitespace handling
        /// testing is interspersed throughout to ensure it is handled properly. All variables used are valid as 
        /// all variable exceptions will be tested in a different method.
        /// </summary>
        [TestMethod]
        public void generalCombinations()
        {
            Assert.AreEqual(8.0, (new Formula("10/2+6-3").Evaluate(s => 1)));
            Assert.AreEqual(39.0, (new Formula("(16+2) * 2 + (21/7)").Evaluate(s => 1)));
            Assert.AreEqual(16.0, (new Formula("2*(2*2)*2").Evaluate(s => 1)));
            Assert.AreEqual(0.0, (new Formula("16+2*4-24").Evaluate(s => 1)));
            Assert.AreEqual(120.0, (new Formula("((2+2 )+6) * 12").Evaluate(s => 1)));
            Assert.AreEqual(8.0, (new Formula("10/2+6-3", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(39.0, (new Formula("(16+2) * 2 + (21/7)", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(16.0, (new Formula("2*(2*2)*2", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(0.0, (new Formula("16+2*4-24", N => N.ToUpper(), S => true).Evaluate(s => 1)));
            Assert.AreEqual(120.0, (new Formula("((2+2 )+6) * 12", N => N.ToUpper(), S => true).Evaluate(s => 1)));

            Assert.AreEqual(8.0, (new Formula("ab3/2+6-3").Evaluate(ab3 => 10)));
            Assert.AreEqual(39.0, (new Formula("(16+2) * k8 + (21/7)").Evaluate(k8 => 2)));
            Assert.AreEqual(16.0, (new Formula("2*(2*2)*u76").Evaluate(u76 => 2)));
            Assert.AreEqual(0.0, (new Formula("arm4+2*4-24").Evaluate(arm4 => 16)));
            Assert.AreEqual(120.0, (new Formula("((2+2 )+uk) * 12").Evaluate(uk => 6)));
            Assert.AreEqual(8.0, (new Formula("i9/2+6-3", N => N.ToUpper(), S => true).Evaluate(I9 => 10)));
            Assert.AreEqual(39.0, (new Formula("(16+2) * m0 + (21/7)", N => N.ToUpper(), S => true).Evaluate(M0 => 2)));
            Assert.AreEqual(16.0, (new Formula("cm*(2*2)*2", N => N.ToUpper(), S => true).Evaluate(CM => 2)));
            Assert.AreEqual(0.0, (new Formula("16+2*4-or7", N => N.ToUpper(), S => true).Evaluate(OR7 => 24)));
            Assert.AreEqual(120.0, (new Formula("((_kk+2 )+6) * 12", N => N.ToUpper(), S => true).Evaluate(_KK => 2)));
        }

        /// <summary>
        /// This method tests exceptions that are associated with integers and variables. The program must return a FormulaError if 
        /// the expression attempts to divide by 0 or does not contain two values per operator. Each integer test is repeated with a variable 
        /// to ensure similarity between behaviors associated with integers and variables. Whitespace handling
        /// testing is interspersed throughout to ensure it is handled properly. 
        /// </summary>
        [TestMethod]
        public void integerVariableExceptions()
        {
            Assert.IsInstanceOfType((new Formula("2/0").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2/0", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2+").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2+", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2-").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2-", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2*").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2*", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2/").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2/", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("c/0").Evaluate(C => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2/_9c", N => N.ToUpper(), S => true).Evaluate(_9C => 0)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("blue9+").Evaluate(blue9 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("s11+", N => N.ToUpper(), S => true).Evaluate(S11 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("st0-").Evaluate(st0 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("um4-", N => N.ToUpper(), S => true).Evaluate(UM4 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("m8*").Evaluate(m8 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("oUt*", N => N.ToUpper(), S => true).Evaluate(OUT => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("u9/").Evaluate(u9 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("p0/", N => N.ToUpper(), S => true).Evaluate(P0 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("p9/2")).Evaluate(s => 1), typeof(FormulaError));
        }

        [TestMethod]
        public void addSubtractExceptions()
        {
            Assert.IsInstanceOfType((new Formula("+3").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("+3", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("-3").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("-3", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("+b00").Evaluate(b00 => 3)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("+u81", N => N.ToUpper(), S => true).Evaluate(U81 => 3)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("-t0").Evaluate(t0 => 3)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("-p_1", N => N.ToUpper(), S => true).Evaluate(P_1 => 3)), typeof(FormulaError));
        }

        /// <summary>
        /// This method tests exceptions associated with addition and subtraction. A FormulaError should be returned if 
        /// there are not two integers per operator or a negative operator comes before a number (and does not function as a 
        /// subtraction symbol). Each test is repeated with variables to ensure the functionality between variables and integers
        /// is the same. Whitespace handling testing is interspersed throughout to ensure it is handled properly.
        /// </summary>
        [TestMethod]
        public void parenthesisExceptions()
        {
            Assert.IsInstanceOfType((new Formula("(2+)").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("(2+)", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2+2)").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2+2)", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("() *2").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("() *2", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("()/2").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("()/2", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2* ()").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2* ()", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2/ (3-3)").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2/ (3-3)", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("(t+)").Evaluate(t => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("(_t+)", N => N.ToUpper(), S => true).Evaluate(_T => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2+o90)").Evaluate(o90 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("c1l+2)", N => N.ToUpper(), S => true).Evaluate(C1L => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("() *er3").Evaluate(er3 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("() *R2D2", N => N.ToUpper(), S => true).Evaluate(R2D2 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("()/s0s").Evaluate(s0s => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("()/k7n", N => N.ToUpper(), S => true).Evaluate(K7N => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("b12* ()").Evaluate(b12 => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("c9* ()", N => N.ToUpper(), S => true).Evaluate(C9 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("b_/ (3-3)").Evaluate(b_ => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("_9/ (3-3)", N => N.ToUpper(), S => true).Evaluate(_9 => 2)), typeof(FormulaError));
        }

        /// <summary>
        /// This method tests all exceptions associated with parenthesis. A FormulaError must be returned if there is not two integers
        /// per operator within the parenthesis, the parenthesis are incomplete (either to the left or right side), or the parenthesis 
        /// are empty. The tests are repeated with variables to ensure integers and variables have the same functionality. Whitespace handling
        /// testing is interspersed throughout to ensure it is handled properly.
        /// </summary>
        [TestMethod]
        public void lastTokenExceptions()
        {
            Assert.IsInstanceOfType((new Formula("2 2").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2 2", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2+2 2").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2+2 2", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2 + 2 -").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("2 + 2 -", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("(2 + 2").Evaluate(s => 1)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("(2 + 2", N => N.ToUpper(), S => true).Evaluate(s => 1)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("r 2").Evaluate(r => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("k 2", N => N.ToUpper(), S => true).Evaluate(K => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2+2 ku").Evaluate(ku => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("s34+2 2", N => N.ToUpper(), S => true).Evaluate(S34 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("2 + ut -").Evaluate(ut => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("p98 + 2 -", N => N.ToUpper(), S => true).Evaluate(P98 => 2)), typeof(FormulaError));

            Assert.IsInstanceOfType((new Formula("(2 + _o").Evaluate(_o => 2)), typeof(FormulaError));
            Assert.IsInstanceOfType((new Formula("(_9_ + 2", N => N.ToUpper(), S => true).Evaluate(_9_ => 2)), typeof(FormulaError));
        }

        /// <summary>
        /// This method tests exceptions associated with normalizing the variables. A FormulaFormatException
        /// should be thrown if a variable does not meet the legal format as outlined in instructions. 
        /// Tested with both constructors.
        /// </summary>
        [TestMethod]
        public void normalizerExceptions()
        {
            try
            {
                new Formula("2x + 3").Evaluate(x2 => 2);
                Assert.Fail();
            }
            catch (FormulaFormatException) { }

            try
            {
                new Formula("2x + 3", N => N.ToUpper(), S => true).Evaluate(x2 => 2);
                Assert.Fail();
            }
            catch (FormulaFormatException) { }
        }

        /// <summary>
        /// This method tests all exceptions associated with the isValid function. isValid() should
        /// throw an exception if the parameter isValid() method determines that the variable is invalid.
        /// </summary>
        [TestMethod]
        public void isValidExceptions()
        {
            try
            {
                new Formula("2+b", N => N.ToUpper(), S => false).Evaluate(B => 2);
                Assert.Fail();
            }
            catch (FormulaFormatException) { }
        }

        /// <summary>
        /// This method tests all errors associated with invalid variables. A FormulaFormatException should 
        /// be thrown if the variable is not in a valid format, similar to the normalizer exceptions. Tested with both 
        /// constructors.
        /// </summary>
        [TestMethod]
        public void invalidVariable()
        {
            try
            {
                new Formula("2x + 3").Evaluate(x2 => 2);
                Assert.Fail();
            }
            catch (FormulaFormatException) { }

            try
            {
                new Formula("2x + 3", N => N.ToUpper(), S => true).Evaluate(x2 => 2);
                Assert.Fail();
            }
            catch (FormulaFormatException) { }
        }

        /// <summary>
        /// This method tests the getVariables() method to ensure it returns variables in their normalized
        /// form. It must return all variables and ignore any numbers or operators. Tested with both constructors
        /// and with an equation that contains numbers.
        /// </summary>
        [TestMethod]
        public void getVariables()
        {
            List<string> variables = new List<string>();
            variables.Add("x");
            variables.Add("y");
            Assert.AreEqual(variables.ToString(), new Formula("x + y").GetVariables().ToString());

            List<string> variables2 = new List<string>();
            variables2.Add("X");
            variables2.Add("Y");
            Assert.AreEqual(variables.ToString(), new Formula("x + y", N => N.ToUpper(), S => true).GetVariables().ToString());

            List<string> variables3 = new List<string>();
            variables3.Add("x");
            variables3.Add("y");
            Assert.AreEqual(variables.ToString(), new Formula("x + y + 3").GetVariables().ToString());
        }

        /// <summary>
        /// This method tests the ToString() override method to ensure it returns the proper final formula. 
        /// All variables in the returned string should be normalized. Tested with both constructors.
        /// </summary>
        [TestMethod]
        public void testToString()
        {
            Assert.AreEqual("X + 2", new Formula("X + 2").ToString());
            Assert.AreEqual("X + 2", new Formula("x + 2", N => N.ToUpper(), S => true).ToString());
            Assert.AreEqual("X1 +2", new Formula("X1 +2").ToString());
            Assert.AreEqual("X1+2", new Formula("x1+2", N => N.ToUpper(), S => true).ToString());
        }

        /// <summary>
        /// This method tests the Equals() method to ensure that two formula objects with the exact 
        /// same formula return true when tested with Equals. If whitespaces are added, and one formula does not 
        /// contain whitespaces, they are not equal. With variables, cases impact equality of formulas. Tested 
        /// with both constructors
        /// </summary>
        [TestMethod]
        public void testEquals()
        {
            Assert.IsTrue(new Formula("2+2").Equals(new Formula("2+2")));
            Assert.IsTrue(new Formula("X + 2").Equals(new Formula("x + 2", N => N.ToUpper(), S => true)));

            Assert.IsFalse(new Formula("2 + 2").Equals(new Formula("2+2")));
            Assert.IsFalse(new Formula("x*y").Equals(new Formula("x*y", N => N.ToUpper(), S => true)));
        }

        /// <summary>
        /// This method tests the overridden == operator. This method must return true if two formula
        /// objects run through the Equals() method returns true, false otherwise. Tested with both constructors.
        /// </summary>
        [TestMethod]
        public void testEqualSign()
        {
            Assert.IsTrue(new Formula("2+2") == (new Formula("2+2")));
            Assert.IsTrue(new Formula("X + 2") == (new Formula("x + 2", N => N.ToUpper(), S => true)));

            Assert.IsFalse(new Formula("2 + 2") == (new Formula("2+2")));
            Assert.IsFalse(new Formula("x*y") == (new Formula("x*y", N => N.ToUpper(), S => true)));
        }

        /// <summary>
        /// This method tests the overridden != operator. This method must return false if two formula objects 
        /// run through the Equals() method returns true, true otherwise. Tested with both constructors.
        /// </summary>
        [TestMethod]
        public void testNotEqualSign()
        {
            Assert.IsFalse(new Formula("2+2") != (new Formula("2+2")));
            Assert.IsFalse(new Formula("X + 2") != (new Formula("x + 2", N => N.ToUpper(), S => true)));

            Assert.IsTrue(new Formula("2 + 2") != (new Formula("2+2")));
            Assert.IsTrue(new Formula("x*y") != (new Formula("x*y", N => N.ToUpper(), S => true)));
        }

        /// <summary>
        /// This method tests the getHashCode() function. Two formulas that return true after 
        /// running through the Equals() method should have equal hashcodes, and vice versa. Tested with both 
        /// constructors.
        /// </summary>
        [TestMethod]
        public void getHashCode()
        {
            Assert.AreEqual(new Formula("2+2").GetHashCode(), new Formula("2+2").GetHashCode());
            Assert.AreEqual(new Formula("2+2", N => N.ToUpper(), S => true).GetHashCode(), new Formula("2+2").GetHashCode());

            Assert.AreEqual(new Formula("x*y").GetHashCode(), new Formula("x*y").GetHashCode());
            Assert.AreEqual(new Formula("X*Y").GetHashCode(), new Formula("x*y", N => N.ToUpper(), s => true).GetHashCode());

            Assert.AreNotEqual(new Formula("2 + 2").GetHashCode(), new Formula("2+2").GetHashCode());
            Assert.AreNotEqual(new Formula("2 + 2", N => N.ToUpper(), S => true).GetHashCode(), new Formula("2+2").GetHashCode());

            Assert.AreNotEqual(new Formula("x*y").GetHashCode(), new Formula("x*y", N => N.ToUpper(), s => true).GetHashCode());
            Assert.AreNotEqual(new Formula("x *y").GetHashCode(), new Formula("x * y ").GetHashCode());
        }
    }
}