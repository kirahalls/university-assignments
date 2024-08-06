// Skeleton written by Joe Zachary for CS 3500, September 2013
// Read the entire skeleton carefully and completely before you
// do anything else!

// Version 1.1 (9/22/13 11:45 a.m.)

// Change log:
//  (Version 1.1) Repaired mistake in GetTokens
//  (Version 1.1) Changed specification of second constructor to
//                clarify description of how validation works

// (Daniel Kopta) 
// Version 1.2 (9/10/17) 

// Change log:
//  (Version 1.2) Changed the definition of equality with regards
//                to numeric tokens


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace SpreadsheetUtilities
{
    /// <summary>
    /// Represents formulas written in standard infix notation using standard precedence
    /// rules.  The allowed symbols are non-negative numbers written using double-precision 
    /// floating-point syntax (without unary preceeding '-' or '+'); 
    /// variables that consist of a letter or underscore followed by 
    /// zero or more letters, underscores, or digits; parentheses; and the four operator 
    /// symbols +, -, *, and /.  
    /// 
    /// Spaces are significant only insofar that they delimit tokens.  For example, "xy" is
    /// a single variable, "x y" consists of two variables "x" and y; "x23" is a single variable; 
    /// and "x 23" consists of a variable "x" and a number "23".
    /// 
    /// Associated with every formula are two delegates:  a normalizer and a validator.  The
    /// normalizer is used to convert variables into a canonical form, and the validator is used
    /// to add extra restrictions on the validity of a variable (beyond the standard requirement 
    /// that it consist of a letter or underscore followed by zero or more letters, underscores,
    /// or digits.)  Their use is described in detail in the constructor and method comments.
    /// </summary>
    /// <author>Nandini Goel</author>
    /// <date>February 5th 2023</date>
    public class Formula
    {

        // holds each token t in formula expression
        private List<string> tokens;
        private List<string> normalizedVariables;

        /// <summary>
        /// Creates a Formula from a string that consists of an infix expression written as
        /// described in the class comment.  If the expression is syntactically invalid,
        /// throws a FormulaFormatException with an explanatory Message.
        /// 
        /// The associated normalizer is the identity function, and the associated validator
        /// maps every string to true.  
        /// </summary>
        public Formula(String formula) : this(formula, s => s, s => true)
        {
        }

        /// <summary>
        /// Creates a Formula from a string that consists of an infix expression written as
        /// described in the class comment.  If the expression is syntactically incorrect,
        /// throws a FormulaFormatException with an explanatory Message.
        /// 
        /// The associated normalizer and validator are the second and third parameters,
        /// respectively.  
        /// 
        /// If the formula contains a variable v such that normalize(v) is not a legal variable, 
        /// throws a FormulaFormatException with an explanatory message. 
        /// 
        /// If the formula contains a variable v such that isValid(normalize(v)) is false,
        /// throws a FormulaFormatException with an explanatory message.
        /// 
        /// Suppose that N is a method that converts all the letters in a string to upper case, and
        /// that V is a method that returns true only if a string consists of one letter followed
        /// by one digit.  Then:
        /// 
        /// new Formula("x2+y3", N, V) should succeed
        /// new Formula("x+y3", N, V) should throw an exception, since V(N("x")) is false
        /// new Formula("2x+y3", N, V) should throw an exception, since "2x+y3" is syntactically incorrect.
        /// </summary>
        public Formula(String formula, Func<string, string> normalize, Func<string, bool> isValid)
        {
            formula = formula.Substring(1, formula.Length-1);
            if (String.IsNullOrEmpty(formula))
            {
                throw new FormulaFormatException("The formula provided is null or empty");
            }

            tokens = new List<string>(GetTokens(formula));
            normalizedVariables = new List<string>();

            int numOfOpenParentheses = 0;
            int numOfCloseParenthese = 0;

            double num; // stores all parsed tokens valid as numbers

            string firstToken = tokens.First<string>();
            string lastToken = tokens.Last<string>();

            // case: if first token is not an opening paren
            if ( !(firstToken.Equals("(")) )
            {
                // edge case: if the first token isn't a number
                if ( !(Double.TryParse(firstToken, out num)) )
                {
                    // edge case: check if IsVariable (private helper method), if not, we know this is not a valid first token
                    if (!(IsVariable(firstToken)))
                    {
                        throw new FormulaFormatException("first token in formula must be a number, variable, or opening parentheses");
                    }
                }
            }
            // case: if the last token is not a closing paren
            if ( !(lastToken.Equals(")")) )
            {
                // edge case: if the last token isn't a number
                if ( !(Double.TryParse(lastToken, out num)) )
                {
                    // edge case: check if IsVariable (private helper method), if not, we know this is not a valid first token
                    if (!(IsVariable(lastToken)))
                    {
                        throw new FormulaFormatException("last token in formula must be a number, variable, or closing parentheses");
                    }
                }
            }

            // using a previous token to read similar to stack - like second token popped off,
            // but before stacks are used, in the array, we consider it the previous token!
            string prevToken = "";
            for (int i = 0; i < tokens.Count; i++)
            {
                string token = tokens[i];

                // check if the token is a parentheses
                if (token.Equals("("))
                {
                    numOfOpenParentheses++;  // increment open count                                       
                }
                else if (token.Equals(")"))
                {
                    numOfCloseParenthese++; // increment close count
                    if (numOfCloseParenthese > numOfOpenParentheses)
                    {
                        throw new FormulaFormatException("ILLEGAL # OF CLOSING PAREN: # of closing cannot exceed # of opening!");
                    }
                } // case: check if token is an operator
                else if (token.Equals("+") || token.Equals("-") || token.Equals("*") || token.Equals("/"))
                { /** empty else if block implies token is operator so continue! **/ }
                else if (Double.TryParse(token, out num))
                {
                    // convert the parsed num to string to add back to tokens as long as it identifies as a number
                    string numAsString = num.ToString();
                    tokens[i] = numAsString; 

                } // otherwise check if it is a proper variable
                else if (IsVariable(token))
                { } // otherwise it must be an improper format, so throw an exception
                else throw new FormulaFormatException("At least one token is an invalid character!");

                // case: if the prev token is an open parentheses or operator, check whether current token is a number, variable, or opening parentheses
                // but if the prev token is a number, variable, or close parentheses, check whether curr token is an operator or closing parentheses 
                if (prevToken.Equals("(") || prevToken.Equals("+") || prevToken.Equals("-") || prevToken.Equals("*") || prevToken.Equals("/"))
                {
                    if (!(Double.TryParse(token, out num) || IsVariable(token) || token.Equals("(")))
                    {
                        throw new FormulaFormatException("ILLEGAL FORMAT ERROR: Token or open paren must be followed by a number, variable, or open paren");
                    }
                }
                else if (prevToken.Equals(")") || Double.TryParse(prevToken, out num) || IsVariable(prevToken))
                {
                    if (!(token.Equals(")") || token.Equals("+") || token.Equals("-") || token.Equals("*") || token.Equals("/")))
                    {
                        throw new FormulaFormatException("ILLEGAL FORMAT ERROR: The number, variable, or close parentheses must be followed by an open parentheses!");
                    }
                }

                // increment previous token as we move through the for loop!
                prevToken = token;
            } // end of for loop

            // case: if the number of open and close parentheses are inequal, throw exception!
            if (numOfCloseParenthese != numOfOpenParentheses)
            {
                throw new FormulaFormatException("ERROR: The number of opening and closing parentheses in formula are not equal!");
            }

            // If we get all the way through without an exception, it should be a valid formula
            // then we want to normalize the formula and validate it according to the delegate parameters
            for (int i = 0; i < tokens.Count; i++)
            {
                string v = tokens[i];
                if (IsVariable(v))   // if token 'v' is a variable
                {
                    // edge case: if normalized variable is not valid, throw exception
                    if (!(IsVariable(normalize(v))))
                    {
                        throw new FormulaFormatException("ILLEGAL ARGUMENT: A normalized variable does not have a valid variable format");
                    }

                    if (!(isValid(normalize(v))))
                    {
                        throw new FormulaFormatException("ILLEGAL ARGUMENT: A normalized variable does not meet the isValid criteria for variables");
                    }
                    else // if it is a valid variable, replace the old variable with the normalized one
                    {
                        tokens[i] = normalize(v);

                        // add a key, value pair from the old value to the new one
                        normalizedVariables.Add(tokens[i]);
                    }
                }
            }
        }

        /// <summary>
        /// Evaluates this Formula, using the lookup delegate to determine the values of
        /// variables.  When a variable symbol v needs to be determined, it should be looked up
        /// via lookup(normalize(v)). (Here, normalize is the normalizer that was passed to 
        /// the constructor.)
        /// 
        /// For example, if L("x") is 2, L("X") is 4, and N is a method that converts all the letters 
        /// in a string to upper case:
        /// 
        /// new Formula("x+7", N, s => true).Evaluate(L) is 11
        /// new Formula("x+7").Evaluate(L) is 9
        /// 
        /// Given a variable symbol as its parameter, lookup returns the variable's value 
        /// (if it has one) or throws an ArgumentException (otherwise).
        /// 
        /// If no undefined variables or divisions by zero are encountered when evaluating 
        /// this Formula, the value is returned.  Otherwise, a FormulaError is returned.  
        /// The Reason property of the FormulaError should have a meaningful explanation.
        ///
        /// This method should never throw an exception.
        /// </summary>
        public object Evaluate(Func<string, double> lookup)
        {
            string[] substrings = tokens.Cast<string>().ToArray<string>();

            Stack<double> valueStack = new Stack<double>();
            Stack<String> operatorStk = new Stack<String>();

            for (int j = 0; j < substrings.Length; j++)
            {
                String currToken = substrings[j]; // easier to read current token each time we loop through
                double number; //stores token parsed as double if identifying as a number of any kind

                // check if t is an operator
                if (currToken.Equals("*") || currToken.Equals("/") || currToken.Equals("("))
                {
                    operatorStk.Push(currToken);
                }
                else if (currToken.Equals("+") || currToken.Equals("-") || currToken.Equals(")"))
                {
                    String topOperator = "";
                    if (operatorStk.Count > 0)
                    {
                        topOperator = operatorStk.Peek();
                    }

                    if (topOperator.Equals("+") || topOperator.Equals("-"))
                    {
                        double firstValPopped = valueStack.Pop();
                        double secondValPopped = valueStack.Pop();
                        String operation = operatorStk.Pop();
                        double sumOrDifference;

                        // checks if operation string is + or -
                        if (operation.Equals("+"))
                        {
                            sumOrDifference = secondValPopped + firstValPopped;
                        }
                        else
                        {
                            sumOrDifference = secondValPopped - firstValPopped;
                        }
                        valueStack.Push(sumOrDifference);
                    }

                    if (currToken.Equals(")"))
                    {
                        String newTopOperator = "";
                        if (operatorStk.Count > 0)
                        {
                            newTopOperator = operatorStk.Peek();
                        }

                        operatorStk.Pop(); // expect open paren - "("

                        if (operatorStk.Count > 0)
                            newTopOperator = operatorStk.Peek();

                        if (newTopOperator.Equals("*") || newTopOperator.Equals("/"))
                        {
                            double firstValPopped = valueStack.Pop();
                            double secondValPopped = valueStack.Pop();
                            String operation = operatorStk.Pop();
                            double productOrQuotient;

                            // checks if it is * or /
                            if (operation.Equals("*"))
                            {
                                productOrQuotient = secondValPopped * firstValPopped;
                            }
                            else
                            {
                                if (firstValPopped == 0)
                                {
                                    return new FormulaError("Error: Divide by Zero");
                                }
                                productOrQuotient = secondValPopped / firstValPopped;
                            }
                            valueStack.Push(productOrQuotient);
                        }
                    }
                    else
                    { // if t is not a ")", push it onto the stack
                        operatorStk.Push(currToken);
                    }
                }
                // case: checks if it is a number or variable
                else if (Double.TryParse(currToken, out number))
                {
                    double num = number;

                    String topOperator = "";

                    if (operatorStk.Count > 0)
                    {
                        topOperator = operatorStk.Peek();
                    }

                    if (topOperator.Equals("*") || topOperator.Equals("/"))
                    {
                        double firstValPopped = valueStack.Pop();
                        String operation = operatorStk.Pop();
                        double productOrQuotient;

                        if (operation.Equals("*"))
                        {
                            productOrQuotient = firstValPopped * num;
                        }
                        else
                        {
                            // edge case: cannot divide by 0, does not exist, so throw error
                            if (num == 0)
                            {
                                return new FormulaError("Error: Divide by Zero");
                            }
                            productOrQuotient = firstValPopped / num;
                        }
                        valueStack.Push(productOrQuotient);
                    }
                    else
                    {
                        valueStack.Push(num);
                    }
                }
                else // case: variable check!
                {
                    // loops through individual token to check if it is a valid variable!
                    for (int i = 0; i < currToken.Length; i++)
                    {
                        // edge case: if the char in token is not a letter or digit, throw exception
                        char currCharInToken = currToken[i];
                        //if (!(char.IsLetter(current)) && !(char.IsDigit(current)))
                        //    throw new ArgumentException();

                        // if first char is not a letter, throw exception
                        //if ((i == 0) && !(char.IsLetter(current)))                            
                        //    throw new ArgumentException();

                        // edge case: when we find a digit, if none of the chars after that is not a digit, throw exception
                        if (char.IsDigit(currCharInToken))
                        {
                            for (int k = i; k < currToken.Length; k++)
                            {
                                char next = currToken[k];
                            }
                        }

                    } // end of var check

                    double variableValue;
                    try
                    {
                        variableValue = lookup(currToken); // func (delegate) used to check variableEvaluator like A1
                    }
                    catch (ArgumentException e)
                    {
                        return new FormulaError("Error: Your Variable is Undefined");
                    }

                    String topOperator = "";
                    if (operatorStk.Count > 0)
                    {
                        topOperator = operatorStk.Peek();
                    }

                    if (topOperator.Equals("*") || topOperator.Equals("/"))
                    {
                        double firstValPopped = valueStack.Pop();
                        String operation = operatorStk.Pop();

                        double productOrQuotient;
                        if (operation.Equals("*"))
                        {
                            productOrQuotient = firstValPopped * variableValue;
                        }
                        else
                        {
                            if (variableValue == 0)
                            {
                                return new FormulaError("Error: Divide by Zero");
                            }
                            productOrQuotient = firstValPopped / variableValue;
                        }
                        valueStack.Push(productOrQuotient);
                    }
                    else
                    {
                        valueStack.Push(variableValue);
                    }

                } // end of if-else

            } // end of for loop

            if (operatorStk.Count == 0) // if operators stack is empty
            {
                // There should only be one value left on the values stack
                //if (values.Count != 1)
                //    throw new ArgumentException();
                return valueStack.Pop();
            }
            else
            {
                // There should be exactly one operator left on the stack, and it should
                // be either + or -. There should be exactly two values in the value stack.
                //if((operators.Count != 1) || (values.Count != 2))
                //    throw new ArgumentException();

                double firstValPopped = valueStack.Pop();
                double secondValPopped = valueStack.Pop();
                String operation = operatorStk.Pop();

                double sumOrDifference;
                if (operation.Equals("+"))
                {
                    sumOrDifference = secondValPopped + firstValPopped;
                }
                else
                {
                    sumOrDifference = secondValPopped - firstValPopped;
                }
                return sumOrDifference;
            }
        }

        /// <summary>
        /// Enumerates the normalized versions of all of the variables that occur in this 
        /// formula.  No normalization may appear more than once in the enumeration, even 
        /// if it appears more than once in this Formula.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        /// 
        /// new Formula("x+y*z", N, s => true).GetVariables() should enumerate "X", "Y", and "Z"
        /// new Formula("x+X*z", N, s => true).GetVariables() should enumerate "X" and "Z".
        /// new Formula("x+X*z").GetVariables() should enumerate "x", "X", and "z".
        /// </summary>
        public IEnumerable<String> GetVariables()
        {
            List<string> normalizedVars = new List<string>(normalizedVariables);
            return normalizedVars;
        }

        /// <summary>
        /// Returns a string containing no spaces which, if passed to the Formula
        /// constructor, will produce a Formula f such that this.Equals(f).  All of the
        /// variables in the string should be normalized.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        /// 
        /// new Formula("x + y", N, s => true).ToString() should return "X+Y"
        /// new Formula("x + Y").ToString() should return "x+Y"
        /// </summary>
        public override string ToString()
        {
            string formula = "";
            for (int i = 0; i < tokens.Count; i++)
            {
                formula += tokens[i];
            }
            return "=" + formula;
        }

        /// <summary>
        ///  <change> make object nullable </change>
        ///
        /// If obj is null or obj is not a Formula, returns false.  Otherwise, reports
        /// whether or not this Formula and obj are equal.
        /// 
        /// Two Formulae are considered equal if they consist of the same tokens in the
        /// same order.  To determine token equality, all tokens are compared as strings 
        /// except for numeric tokens and variable tokens.
        /// Numeric tokens are considered equal if they are equal after being "normalized" 
        /// by C#'s standard conversion from string to double, then back to string. This 
        /// eliminates any inconsistencies due to limited floating point precision.
        /// Variable tokens are considered equal if their normalized forms are equal, as 
        /// defined by the provided normalizer.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        ///  
        /// new Formula("x1+y2", N, s => true).Equals(new Formula("X1  +  Y2")) is true
        /// new Formula("x1+y2").Equals(new Formula("X1+Y2")) is false
        /// new Formula("x1+y2").Equals(new Formula("y2+x1")) is false
        /// new Formul a("2.0 + x7").Equals(new Formula("2.000 + x7")) is true
        /// </summary>
        public override bool Equals(object? obj)
        {
            // if 'obj' is null or is not a valid Formula type
            if (ReferenceEquals(obj, null) || (obj.GetType() != this.GetType()))
            {
                return false;
            }

            // typecast the obj to a formula if it is a formula
            Formula formula = (Formula)obj;

            // loop through to check that if the string don't match, return false, otherwise return true
            for (int i = 0; i < this.tokens.Count; i++)
            {
                string currToken = this.tokens[i];
                string currFormula = formula.tokens[i];

                // stores double type of the converted this token and formula's number val
                double thisNumber;
                double formulaNumber;

                // as long as both values are valid numbers, return false if they are not the same
                if ((Double.TryParse(currToken, out thisNumber)) && (Double.TryParse(currFormula, out formulaNumber)))
                {
                    if (thisNumber != formulaNumber)
                    {
                        return false;
                    }
                }
                else // vars should be normalized at this point
                {
                    if (!(currToken.Equals(currFormula)))
                    {
                        return false;
                    }
                }
            }
            return true;
        } // end of Equals method!

        /// <summary>
        ///   <change> We are now using Non-Nullable objects.  Thus neither f1 nor f2 can be null!</change>
        /// Reports whether f1 == f2, using the notion of equality from the Equals method.
        /// 
        /// </summary>
        public static bool operator ==(Formula f1, Formula f2)
        {
            // edge case: null pointer check for either formula parameter
            if ((ReferenceEquals(f1, null)) && (ReferenceEquals(f2, null)))
            {
                return true;
            }
            else if ((ReferenceEquals(f1, null)) && !(ReferenceEquals(f2, null)))
            {
                return false;
            }
            else if (!(ReferenceEquals(f1, null)) && (ReferenceEquals(f2, null)))
            {
                return false;
            }

            // if neither are null then we check according to .Equals method
            return (f1.Equals(f2));
        } // end of == method!

        /// <summary>
        ///   <change> We are now using Non-Nullable objects.  Thus neither f1 nor f2 can be null!</change>
        ///   <change> Note: != should almost always be not ==, if you get my meaning </change>
        ///   Reports whether f1 != f2, using the notion of equality from the Equals method.
        /// </summary>
        public static bool operator !=(Formula f1, Formula f2)
        {
            if (!(f1 == f2))
            {
                return true;
            }
            return false;
        } // end of != method!

        /// <summary>
        /// Returns a hash code for this Formula.  If f1.Equals(f2), then it must be the
        /// case that f1.GetHashCode() == f2.GetHashCode().  Ideally, the probability that two 
        /// randomly-generated unequal Formulae have the same hash code should be extremely small.
        /// </summary>
        public override int GetHashCode()
        {
            int hashCode = this.ToString().GetHashCode();
            return hashCode;
        } // end of GetHashCode method!

        /// <summary>
        /// Given an expression, enumerates the tokens that compose it.  Tokens are left paren;
        /// right paren; one of the four operator symbols; a string consisting of a letter or underscore
        /// followed by zero or more letters, digits, or underscores; a double literal; and anything that doesn't
        /// match one of those patterns.  There are no empty tokens, and no token contains white space.
        /// </summary>
        private static IEnumerable<string> GetTokens(String formula)
        {
            // Patterns for individual tokens
            String lpPattern = @"\(";
            String rpPattern = @"\)";
            String opPattern = @"[\+\-*/]";
            String varPattern = @"[a-zA-Z_](?: [a-zA-Z_]|\d)*";
            String doublePattern = @"(?: \d+\.\d* | \d*\.\d+ | \d+ ) (?: [eE][\+-]?\d+)?";
            String spacePattern = @"\s+";

            // Overall pattern
            String pattern = String.Format("({0}) | ({1}) | ({2}) | ({3}) | ({4}) | ({5})", lpPattern, rpPattern, opPattern, varPattern, doublePattern, spacePattern);

            // Enumerate matching tokens that don't consist solely of white space.
            foreach (String s in Regex.Split(formula, pattern, RegexOptions.IgnorePatternWhitespace))
            {
                if (!Regex.IsMatch(s, @"^\s*$", RegexOptions.Singleline))
                {
                    yield return s;
                }
            }
        } // end of GetTokens method!

        /// <summary>
        /// 
        /// </summary>
        /// <param name="t"></param>
        /// <returns></returns>
        private static bool IsVariable(String t)
        {
            if (Regex.IsMatch(t, @"[a-zA-Z_](?: [a-zA-Z_]|\d)*", RegexOptions.Singleline))
            {
                return true;
            }
            else
            {
                return false;
            }
        } // end of IsVariable method!

    } // END OF FORMULA CLASS!!

    /// <summary>
    /// Used to report syntactic errors in the argument to the Formula constructor.
    /// </summary>
    public class FormulaFormatException : Exception
    {
        /// <summary>
        /// Constructs a FormulaFormatException containing the explanatory message.
        /// </summary>
        public FormulaFormatException(String message)
            : base(message)
        {
        }
    }

    /// <summary>
    /// Used as a possible return value of the Formula.Evaluate method.
    /// </summary>
    public struct FormulaError
    {
        /// <summary>
        /// Constructs a FormulaError containing the explanatory reason.
        /// </summary>
        /// <param name="reason"></param>
        public FormulaError(String reason) : this()
        {
            Reason = reason;
        }

        /// <summary>
        ///  The reason why this FormulaError was created.
        /// </summary>
        public string Reason { get; private set; }
    }
}


// <change>
//   If you are using Extension methods to deal with common stack operations (e.g., checking for
//   an empty stack before peeking) you will find that the Non-Nullable checking is "biting" you.
//
//   To fix this, you have to use a little special syntax like the following:
//
//       public static bool OnTop<T>(this Stack<T> stack, T element1, T element2) where T : notnull
//
//   Notice that the "where T : notnull" tells the compiler that the Stack can contain any object
//   as long as it doesn't allow nulls!
// </change>
