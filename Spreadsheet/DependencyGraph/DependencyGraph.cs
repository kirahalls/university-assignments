
// Skeleton implementation written by Joe Zachary for CS 3500, September 2013.
// Version 1.1 (Fixed error in comment for RemoveDependency.)
// Version 1.2 - Daniel Kopta 
//               (Clarified meaning of dependent and dependee.)
//               (Clarified names in solution/project structure.)

using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;

/// <summary>
/// Author:    Kira Halls
/// Partner:   None
/// Date:      January 24, 2023
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
/// This file uses two dictionaries of string keys and their associated List<string> values to represent a dependency graph. 
/// Each key in the dictionaries represents a node in the graph. There are to dictionaries--one to represent a node's dependents 
/// (the nodes that rely on the key node) and another to represent a node's dependees (the nodes that the key depends on). This 
/// class constructs the graph, adds and removes dependencies, replaces dependencies with a given list of new dependencies, 
/// returns the number of ordered pairs contained in the graph, and finally, gets values such as a node's list of dependents and dependees.
///
/// 
/// </summary>
namespace SpreadsheetUtilities
{

    /// <summary>
    /// (s1,t1) is an ordered pair of strings
    /// t1 depends on s1; s1 must be evaluated before t1
    /// 
    /// A DependencyGraph can be modeled as a set of ordered pairs of strings.  Two ordered pairs
    /// (s1,t1) and (s2,t2) are considered equal if and only if s1 equals s2 and t1 equals t2.
    /// Recall that sets never contain duplicates.  If an attempt is made to add an element to a 
    /// set, and the element is already in the set, the set remains unchanged.
    /// 
    /// Given a DependencyGraph DG:
    /// 
    ///    (1) If s is a string, the set of all strings t such that (s,t) is in DG is called dependents(s).
    ///        (The set of things that depend on s)    
    ///        
    ///    (2) If s is a string, the set of all strings t such that (t,s) is in DG is called dependees(s).
    ///        (The set of things that s depends on) 
    //
    // For example, suppose DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")}
    //     dependents("a") = {"b", "c"}
    //     dependents("b") = {"d"}
    //     dependents("c") = {}
    //     dependents("d") = {"d"}
    //     dependees("a") = {}
    //     dependees("b") = {"a"}
    //     dependees("c") = {"a"}
    //     dependees("d") = {"b", "d"}
    /// </summary>
    public class DependencyGraph
    {
        private Dictionary<String, HashSet<string>> dependents;
        private Dictionary<String, HashSet<string>> dependees;
        int orderedPairs = 0;

        /// <summary>
        /// Creates an empty DependencyGraph.
        /// </summary>
        public DependencyGraph()
        {
            dependents = new Dictionary<String, HashSet<string>>();
            dependees = new Dictionary<String, HashSet<string>>();
        }

        /// <summary>
        /// The number of ordered pairs in the DependencyGraph.
        /// </summary>
        public int Size
        {
            get { return orderedPairs; }
        }

        /// <summary>
        /// The size of dependees(s).
        /// This property is an example of an indexer.  If dg is a DependencyGraph, you would
        /// invoke it like this:
        /// dg["a"]
        /// It should return the size of dependees("a")
        /// </summary>
        public int this[string s]
        {
            get { if (!(dependees.ContainsKey(s))) { return 0; } else { return dependees[s].Count; } }
        }

        /// <summary>
        /// Reports whether dependents(s) is non-empty.
        /// </summary>
        public bool HasDependents(string s)
        {
            if (!(dependents.ContainsKey(s)))
            {
                return false;
            }
            HashSet<string> dependentsList = dependents[s];
            if (dependentsList.Count != 0)
            {
                return true;
            }
            return false;
        }

        /// <summary>
        /// Reports whether dependees(s) is non-empty.
        /// </summary>
        public bool HasDependees(string s)
        {
            if (!(dependees.ContainsKey(s)))
            {
                return false;
            }
            HashSet<string> dependeesList = dependees[s];
            if (dependeesList.Count != 0)
            {
                return true;
            }
            return false;
        }

        /// <summary>
        /// Enumerates dependents(s).
        /// </summary>
        public IEnumerable<string> GetDependents(string s)
        {
            if (!(dependents.ContainsKey(s)))
            {
                return new HashSet<string>() as IEnumerable<string>;
            }
            return dependents[s] as IEnumerable<string>;
        }

        /// <summary>
        /// Enumerates dependees(s).
        /// </summary>
        public IEnumerable<string> GetDependees(string s)
        {
            if (!(dependees.ContainsKey(s)))
            {
                return new HashSet<string>() as IEnumerable<string>;
            }
            return dependees[s] as IEnumerable<string>;
        }

        /// <summary>
        /// <para>Adds the ordered pair (s,t), if it doesn't exist</para>
        /// 
        /// <para>This should be thought of as:</para>   
        /// 
        ///   t depends on s
        ///
        /// </summary>
        /// <param name="s"> s must be evaluated first. T depends on S</param>
        /// <param name="t"> t cannot be evaluated until s is</param>        /// 
        public void AddDependency(string s, string t)
        {
            //Check if s exists as a key in the dependents dictionary 
            if (!(dependents.ContainsKey(s)))
            {
                HashSet<string> valueList = new HashSet<string>();
                dependents.Add(s, valueList);
            }
            //Only add dependency if it does not already exist 
            if (!(dependents[s].Contains(t)))
            {
                dependents[s].Add(t);
            }

            //Check if t exists as a key in the dependees dictionary
            if (!(dependees.ContainsKey(t)))
            {
                HashSet<string> valueList = new HashSet<string>();
                dependees.Add(t, valueList);
            }
            if (!(dependees[t].Contains(s)))
            {
                dependees[t].Add(s);
                orderedPairs++; //Only want to increase ordered pair count if the dependency didn't already exist
            }

        }

        /// <summary>
        /// Removes the ordered pair (s,t), if it exists
        /// </summary>
        /// <param name="s"></param>
        /// <param name="t"></param>
        public void RemoveDependency(string s, string t)
        {
            //Logic in AddDependency ensures we will not have a duplicate, so we do not need to check for that
            if (dependents.ContainsKey(s))
            {
                if (dependents[s].Contains(t))
                {
                    dependents[s].Remove(t);
                    orderedPairs--; //only reduce size if something was contained and actually removed
                }
            }
            if (dependees.ContainsKey(t))
            {
                dependees[t].Remove(s); //can assume a dependee relationship exists if it made it through logic above
            }

        }

        /// <summary>
        /// Removes all existing ordered pairs of the form (s,r).  Then, for each
        /// t in newDependents, adds the ordered pair (s,t).
        /// </summary>
        public void ReplaceDependents(string s, IEnumerable<string> newDependents)
        {

            if (!(dependents.ContainsKey(s)))
            {
                HashSet<string> valueList = new HashSet<string>();
                dependents.Add(s, valueList);
            }
            else
            {
                IEnumerable<string> oldDependents = GetDependents(s);
                foreach (string dependent in oldDependents.ToList())
                {
                    this.RemoveDependency(s, dependent);
                }
            }

            foreach (string dependent in newDependents)
            {
                this.AddDependency(s, dependent);
            }
        }

        /// <summary>
        /// Removes all existing ordered pairs of the form (r,s).  Then, for each 
        /// t in newDependees, adds the ordered pair (t,s).
        /// </summary>
        public void ReplaceDependees(string s, IEnumerable<string> newDependees)
        {
            if (!(dependees.ContainsKey(s)))
            {
                HashSet<string> valueList = new HashSet<string>();
                dependees.Add(s, valueList);
            }
            else
            {
                IEnumerable<string> oldDependees = GetDependees(s);
                foreach (string dependee in oldDependees.ToList())
                {
                    this.RemoveDependency(dependee, s);
                }
            }

            foreach (string dependee in newDependees)
            {
                this.AddDependency(dependee, s);
            }
        }
    }
}

