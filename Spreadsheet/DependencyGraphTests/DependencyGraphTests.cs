using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpreadsheetUtilities;


namespace DevelopmentTests
{ 

    /// <summary>
    ///This is a test class for DependencyGraphTest and is intended
    ///to contain all DependencyGraphTest Unit Tests
    ///</summary>
    [TestClass()]
    public class DependencyGraphTest
    {

        /// <summary>
        ///Empty graph should have size 0
        ///</summary>
        [TestMethod()]
        public void EmptySizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Adding dependency to empty graph and checking size
        ///</summary>
        [TestMethod()]
        public void AddedSizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
        }

        /// <summary>
        ///Adding and removing dependency from empty graph and checking size
        ///</summary>
        [TestMethod()]
        public void RemovedSizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t.Size);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Replacing dependents of graph and checking size
        ///</summary>
        [TestMethod()]
        public void ReplacedDependentSizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            t.AddDependency("a", "c");
            Assert.AreEqual(2, t.Size);
            List<string> list = new List<string>();
            list.Add("e");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(1, t.Size);
            list.Add("d");
            list.Add("f");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(3, t.Size);
        }

        /// <summary>
        ///Replacing dependees of graph and checking size
        ///</summary>
        [TestMethod()]
        public void ReplacedDependeeSizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("b", "a");
            Assert.AreEqual(1, t.Size);
            t.AddDependency("c", "a");
            Assert.AreEqual(2, t.Size);
            List<string> list = new List<string>();
            list.Add("e");
            t.ReplaceDependees("a", list);
            Assert.AreEqual(1, t["a"]);
            list.Add("d");
            list.Add("f");
            t.ReplaceDependees("a", list);
            Assert.AreEqual(3, t["a"]);
        }

        /// <summary>
        ///Key that hasn't been added to graph should return 0 when called in 'this'
        ///</summary>
        [TestMethod()]
        public void ThisEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t["a"]);
        }

        /// <summary>
        ///Key that has a dependent (but not dependee) should return 0
        ///</summary>
        [TestMethod()]
        public void ThisAddedDependentTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(0, t["a"]);
        }

        /// <summary>
        ///Key that has a dependee added should return 1
        ///</summary>
        [TestMethod()]
        public void ThisAddedDependeeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t["b"]);
        }

        /// <summary>
        ///Key that has a dependee added then removed should return 0
        ///</summary>
        [TestMethod()]
        public void ThisRemovedDependeeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t["b"]);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t["b"]);
        }

        /// <summary>
        ///Key that has a dependent added then removed should always return 0 from 'this'
        ///</summary>
        [TestMethod()]
        public void ThisRemovedDependentTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(0, t["a"]);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t["a"]);
        }

        /// <summary>
        ///Key that has a dependee added then removed twice should still return 0, not -1
        ///</summary>
        [TestMethod()]
        public void ThisRemovedDependeeTwiceTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t["b"]);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t["b"]);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t["b"]);
        }

        /// <summary>
        ///Key that has a dependee added then a different dependee was removed should return 1
        ///</summary>
        [TestMethod()]
        public void ThisRemovedIrrelevantDependeeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            Assert.AreEqual(1, t["b"]);
            t.RemoveDependency("a", "c");
            Assert.AreEqual(1, t["b"]);
        }

        /// <summary>
        ///Replacing dependees then checking size
        ///</summary>
        [TestMethod()]
        public void ThisReplacedDependeeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t["b"]);
            List<string> list = new List<string>();
            list.Add("c");
            list.Add("e");
            t.ReplaceDependees("b", list);
            Assert.AreEqual(2, t["b"]);
        }

        /// <summary>
        ///Replacing dependents then checking size
        ///</summary>
        [TestMethod()]
        public void ThisReplacedDependentTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t["b"]);
            List<string> list = new List<string>();
            list.Add("c");
            list.Add("e");
            t.ReplaceDependents("b", list);
            Assert.AreEqual(1, t["b"]);
        }

        /// <summary>
        ///Calling this with an invalid key should return 0
        ///</summary>
        [TestMethod()]
        public void ThisInvalidKeyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t["x"]);
        }

        /// <summary>
        ///Calling HasDependents with an empty graph should return false
        ///</summary>
        [TestMethod()]
        public void EmptyHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(false, t.HasDependents("a"));
        }

        /// <summary>
        ///Calling HasDependents after a value has been added should return true for the dependee and 
        ///false for the dependent
        ///</summary>
        [TestMethod()]
        public void AddHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependents("a"));
            Assert.AreEqual(false, t.HasDependents("b"));
        }

        /// <summary>
        ///Calling HasDependents after a value has been added then removed should return false
        ///</summary>
        [TestMethod()]
        public void RemovedHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependents("a"));
            Assert.AreEqual(false, t.HasDependents("b"));
            t.RemoveDependency("a", "b");
            Assert.AreEqual(false, t.HasDependents("a"));
            Assert.AreEqual(false, t.HasDependents("b"));
        }

        /// <summary>
        ///Calling HasDependents after two values have been added then one removed should return true
        ///</summary>
        [TestMethod()]
        public void RemovedOneHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            Assert.AreEqual(true, t.HasDependents("a"));
            Assert.AreEqual(false, t.HasDependents("b"));
            t.RemoveDependency("a", "b");
            Assert.AreEqual(true, t.HasDependents("a"));
            Assert.AreEqual(false, t.HasDependents("b"));
        }

        /// <summary>
        ///Calling HasDependents with an invalid key should return false
        ///</summary>
        [TestMethod()]
        public void InvalidKeyHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(false, t.HasDependents("c"));
            Assert.AreEqual(false, t.HasDependents("b"));
        }

        /// <summary>
        ///Calling HasDependents after dependents have been replaced should return true for a list with values, 
        ///false for an empty list
        ///</summary>
        [TestMethod()]
        public void ReplacedDependentsHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependents("a"));
            List<string> list = new List<string>();
            list.Add("c");
            list.Add("e");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(true, t.HasDependents("a"));
            List<string> emptyList = new List<string>();
            t.ReplaceDependents("a", emptyList);
            Assert.AreEqual(false, t.HasDependents("a"));
            t.AddDependency("a", "t");
            t.AddDependency("e", "c");
            t.ReplaceDependents("e", emptyList);
            Assert.AreEqual(true, t.HasDependents("a"));
        }

        /// <summary>
        ///Calling HasDependents after dependees have been replaced should return true if not all dependents of key
        ///have been replaced, false if they have been
        ///</summary>
        [TestMethod()]
        public void ReplacedDependeesHasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependents("a"));
            List<string> list = new List<string>();
            list.Add("c");
            list.Add("e");
            t.ReplaceDependees("b", list);
            Assert.AreEqual(false, t.HasDependents("a"));
            List<string> emptyList = new List<string>();
            t.ReplaceDependees("b", emptyList);
            Assert.AreEqual(false, t.HasDependents("a"));
            t.AddDependency("a", "g");
            t.ReplaceDependees("b", emptyList);
            Assert.AreEqual(true, t.HasDependents("a"));
        }

        /// <summary>
        ///Calling HasDependees with an empty graph should return false
        ///</summary>
        [TestMethod()]
        public void EmptyHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(false, t.HasDependees("a"));
        }

        /// <summary>
        ///Calling HasDependees after a value has been added should return true for the dependent and 
        ///false for the dependee
        ///</summary>
        [TestMethod()]
        public void AddHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependees("b"));
            Assert.AreEqual(false, t.HasDependees("a"));
        }

        /// <summary>
        ///Calling HasDependees after a value has been added then removed should return false
        ///</summary>
        [TestMethod()]
        public void RemovedHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependees("b"));
            Assert.AreEqual(false, t.HasDependees("a"));
            t.RemoveDependency("a", "b");
            Assert.AreEqual(false, t.HasDependees("b"));
            Assert.AreEqual(false, t.HasDependees("a"));
        }

        /// <summary>
        ///Calling HasDependees after two values have been added then one removed should return true
        ///</summary>
        [TestMethod()]
        public void RemovedOneHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("c", "b");
            Assert.AreEqual(true, t.HasDependees("b"));
            Assert.AreEqual(false, t.HasDependees("a"));
            t.RemoveDependency("a", "b");
            Assert.AreEqual(true, t.HasDependees("b"));
            Assert.AreEqual(false, t.HasDependees("c"));
        }

        /// <summary>
        ///Calling HasDependees with an invalid key should return false
        ///</summary>
        [TestMethod()]
        public void InvalidKeyHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(false, t.HasDependees("c"));
            Assert.AreEqual(false, t.HasDependees("a"));
        }

        /// <summary>
        ///Calling HasDependees after dependents have been replaced should return true if not all dependees of key
        ///have been replaced, false if they have been
        ///</summary>
        [TestMethod()]
        public void ReplacedDependentsHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependees("b"));
            List<string> list = new List<string>();
            list.Add("c");
            list.Add("e");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(false, t.HasDependees("b"));
            t.AddDependency("x", "b");
            t.AddDependency("a", "b");
            List<string> emptyList = new List<string>();
            t.ReplaceDependents("a", emptyList);
            Assert.AreEqual(true, t.HasDependees("b"));
            t.AddDependency("a", "t");
            t.AddDependency("e", "c");
            t.ReplaceDependents("e", emptyList);
            Assert.AreEqual(true, t.HasDependees("b"));
        }

        /// <summary>
        ///Calling HasDependees after dependees have been replaced should return true if replaced with a list with values
        ///and false if replaced with an empty list
        ///</summary>
        [TestMethod()]
        public void ReplacedDependeesHasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            Assert.AreEqual(true, t.HasDependees("b"));
            List<string> list = new List<string>();
            list.Add("c");
            list.Add("e");
            t.ReplaceDependees("b", list);
            Assert.AreEqual(true, t.HasDependees("b"));
            List<string> emptyList = new List<string>();
            t.ReplaceDependees("b", emptyList);
            Assert.AreEqual(false, t.HasDependees("b"));
            t.AddDependency("a", "b");
            t.ReplaceDependees("x", emptyList);
            Assert.AreEqual(true, t.HasDependees("b"));
        }

        /// <summary>
        ///Empty graph should return an empty list of dependents
        ///</summary>
        [TestMethod()]
        public void GetDependentsEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            IEnumerator<string> e1 = t.GetDependents("a").GetEnumerator();
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Added dependency should return correct values
        ///</summary>
        [TestMethod()]
        public void GetDependentsAddedTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            IEnumerator<string> e1 = t.GetDependents("a").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("b", e1.Current);
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Added then removed dependency should return empty list
        ///</summary>
        [TestMethod()]
        public void GetDependentsRemovedTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            IEnumerator<string> e1 = t.GetDependents("a").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("b", e1.Current);
            Assert.IsFalse(e1.MoveNext());
            t.RemoveDependency("a", "b");
            IEnumerator<string> e2 = t.GetDependents("a").GetEnumerator();
            Assert.IsFalse(e2.MoveNext());
        }

        /// <summary>
        ///Added then removed an irrelevant dependency should not affect list of dependents
        ///</summary>
        [TestMethod()]
        public void GetDependentsRemoveIrrelevantTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "e");
            IEnumerator<string> e1 = t.GetDependents("a").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("b", e1.Current);
            t.RemoveDependency("a", "e");
            IEnumerator<string> e2 = t.GetDependents("a").GetEnumerator();
            Assert.IsTrue(e2.MoveNext());
            Assert.AreEqual("b", e2.Current);
            Assert.IsFalse(e2.MoveNext());
        }

        /// <summary>
        ///Replacing dependents then calling getDependents should return same values as the list passed in
        ///</summary>
        [TestMethod()]
        public void GetDependentsReplacedDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("c");
            list.Add("d");
            t.ReplaceDependents("a", list);
            IEnumerator<string> e1 = t.GetDependents("a").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("b", e1.Current);
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("c", e1.Current);
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("d", e1.Current);
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Replacing dependees then calling getDependents should return proper values - i.e. some dependents were removed
        ///and some were not
        ///</summary>
        [TestMethod()]
        public void GetDependentsReplacedDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "x");
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("c");
            list.Add("d");
            t.ReplaceDependees("b", list);
            IEnumerator<string> e1 = t.GetDependents("a").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("x", e1.Current);
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Empty graph should return an empty list of dependees
        ///</summary>
        [TestMethod()]
        public void GetDependeesEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            IEnumerator<string> e1 = t.GetDependees("a").GetEnumerator();
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Added dependency should return correct values when calling getDependees
        ///</summary>
        [TestMethod()]
        public void GetDependeesAddedTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            IEnumerator<string> e1 = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("a", e1.Current);
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Added then removed dependency should return empty list when calling getDependees
        ///</summary>
        [TestMethod()]
        public void GetDependeesRemovedTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            IEnumerator<string> e1 = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("a", e1.Current);
            Assert.IsFalse(e1.MoveNext());
            t.RemoveDependency("a", "b");
            IEnumerator<string> e2 = t.GetDependees("b").GetEnumerator();
            Assert.IsFalse(e2.MoveNext());
        }

        /// <summary>
        ///Added then removed an irrelevant dependency should not affect list of dependees
        ///</summary>
        [TestMethod()]
        public void GetDependeesRemoveIrrelevantTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("e", "c");
            IEnumerator<string> e1 = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("a", e1.Current);
            t.RemoveDependency("e", "c");
            IEnumerator<string> e2 = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e2.MoveNext());
            Assert.AreEqual("a", e2.Current);
            Assert.IsFalse(e2.MoveNext());
        }

        /// <summary>
        ///Replacing dependents then calling getDependees should return proper values - i.e. some dependees were removed
        ///and some were not
        ///</summary>
        [TestMethod()]
        public void GetDependeesReplacedDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("c");
            list.Add("d");
            t.ReplaceDependents("a", list);
            t.AddDependency("e", "b");
            IEnumerator<string> e1 = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("a", e1.Current);
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("e", e1.Current);
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Replacing dependees then calling getDependees should return list that has same values as the list passed in
        ///</summary>
        [TestMethod()]
        public void GetDependeesReplacedDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("x", "b");
            List<string> list = new List<string>();
            list.Add("a");
            list.Add("c");
            list.Add("d");
            t.ReplaceDependees("b", list);
            IEnumerator<string> e1 = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("a", e1.Current);
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("c", e1.Current);
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("d", e1.Current);
            Assert.IsFalse(e1.MoveNext());
        }

        /// <summary>
        ///Testing adding a new key to an empty graph
        ///</summary>
        [TestMethod()]
        public void AddEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
        }

        /// <summary>
        ///Testing adding a value to a key twice
        ///</summary>
        [TestMethod()]
        public void AddTwiceTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            t.AddDependency("a", "c");
            Assert.AreEqual(2, t.Size);
        }

        /// <summary>
        ///Testing adding the same dependency twice to ensure duplicates aren't added
        ///</summary>
        [TestMethod()]
        public void AddDuplicateTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
        }

        /// <summary>
        ///Testing removing from an empty graph does not change size below 0 and doesn't throw an error
        ///</summary>
        [TestMethod()]
        public void RemoveEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Testing removing after adding to a graph returns proper size
        ///</summary>
        [TestMethod()]
        public void RemoveSimpleTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Testing removing twice doesn't change size erroneously and doesn't throw an error
        ///</summary>
        [TestMethod()]
        public void RemoveTwiceTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t.Size);
            t.RemoveDependency("a", "b");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Testing replacing dependents with an empty graph doesn't throw an error, just creates new dependencies
        ///</summary>
        [TestMethod()]
        public void ReplaceDependentsEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("c");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(2, t.Size);
        }

        /// <summary>
        ///Testing replacing dependents with a key that already has dependents
        ///</summary>
        [TestMethod()]
        public void ReplaceDependentSimpleTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("c");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(2, t.Size);
        }

        /// <summary>
        ///Testing replacing dependents with an empty list
        ///</summary>
        [TestMethod()]
        public void ReplaceDependentEmptyListTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            List<string> list = new List<string>();
            t.ReplaceDependents("a", list);
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Testing replacing dependents with a list of all duplicates to ensure it only adds the dependency once
        ///</summary>
        [TestMethod()]
        public void ReplaceDependentDuplicatesTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("b");
            list.Add("b");
            t.ReplaceDependents("a", list);
            Assert.AreEqual(1, t.Size);
        }

        /// <summary>
        ///Testing replacing dependees with an empty graph doesn't throw an error, just creates new dependencies
        ///</summary>
        [TestMethod()]
        public void ReplaceDependeesEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("c");
            t.ReplaceDependees("a", list);
            Assert.AreEqual(2, t.Size);
        }

        /// <summary>
        ///Testing replacing dependees with a key that already has dependees
        ///</summary>
        [TestMethod()]
        public void ReplaceDependeesSimpleTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            List<string> list = new List<string>();
            list.Add("a");
            list.Add("c");
            t.ReplaceDependees("b", list);
            Assert.AreEqual(2, t.Size);
        }

        /// <summary>
        ///Testing replacing dependees with an empty list
        ///</summary>
        [TestMethod()]
        public void ReplaceDependeesEmptyListTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("a", "b");
            Assert.AreEqual(1, t.Size);
            List<string> list = new List<string>();
            t.ReplaceDependees("b", list);
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Testing replacing dependees with a list of all duplicates to ensure it only adds the dependency once
        ///</summary>
        [TestMethod()]
        public void ReplaceDependeesDuplicatesTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
            t.AddDependency("b", "a");
            Assert.AreEqual(1, t.Size);
            List<string> list = new List<string>();
            list.Add("b");
            list.Add("b");
            list.Add("b");
            t.ReplaceDependees("a", list);
            Assert.AreEqual(1, t.Size);
        }

        //Tests below provided by the professor 

        /// <summary>
        ///Empty graph should contain nothing
        ///</summary>
        [TestMethod()]
        public void SimpleEmptyTest()
        {
            DependencyGraph t = new DependencyGraph();
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Empty graph should contain nothing
        ///</summary>
        [TestMethod()]
        public void SimpleEmptyRemoveTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            Assert.AreEqual(1, t.Size);
            t.RemoveDependency("x", "y");
            Assert.AreEqual(0, t.Size);
        }

        /// <summary>
        ///Empty graph should contain nothing
        ///</summary>
        [TestMethod()]
        public void EmptyEnumeratorTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            IEnumerator<string> e1 = t.GetDependees("y").GetEnumerator();
            Assert.IsTrue(e1.MoveNext());
            Assert.AreEqual("x", e1.Current);
            IEnumerator<string> e2 = t.GetDependents("x").GetEnumerator();
            Assert.IsTrue(e2.MoveNext());
            Assert.AreEqual("y", e2.Current);
            t.RemoveDependency("x", "y");
            Assert.IsFalse(t.GetDependees("y").GetEnumerator().MoveNext());
            Assert.IsFalse(t.GetDependents("x").GetEnumerator().MoveNext());
        }

        /// <summary>
        ///Replace on an empty DG shouldn't fail
        ///</summary>
        [TestMethod()]
        public void SimpleReplaceTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "y");
            Assert.AreEqual(t.Size, 1);
            t.RemoveDependency("x", "y");
            t.ReplaceDependents("x", new HashSet<string>());
            t.ReplaceDependees("y", new HashSet<string>());
        }

        ///<summary>
        ///It should be possible to have more than one DG at a time.
        ///</summary>
        [TestMethod()]
        public void StaticTest()
        {
            DependencyGraph t1 = new DependencyGraph();
            DependencyGraph t2 = new DependencyGraph();
            t1.AddDependency("x", "y");
            Assert.AreEqual(1, t1.Size);
            Assert.AreEqual(0, t2.Size);
        }

        /// <summary>
        ///Non-empty graph contains something
        ///</summary>
        [TestMethod()]
        public void SizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("c", "b");
            t.AddDependency("b", "d");
            Assert.AreEqual(4, t.Size);
        }

        /// <summary>
        ///Non-empty graph contains something
        ///</summary>
        [TestMethod()]
        public void EnumeratorTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("c", "b");
            t.AddDependency("b", "d");

            IEnumerator<string> e = t.GetDependees("a").GetEnumerator();
            Assert.IsFalse(e.MoveNext());

            e = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e.MoveNext());
            String s1 = e.Current;
            Assert.IsTrue(e.MoveNext());
            String s2 = e.Current;
            Assert.IsFalse(e.MoveNext());
            Assert.IsTrue(((s1 == "a") && (s2 == "c")) || ((s1 == "c") && (s2 == "a")));

            e = t.GetDependees("c").GetEnumerator();
            Assert.IsTrue(e.MoveNext());
            Assert.AreEqual("a", e.Current);
            Assert.IsFalse(e.MoveNext());

            e = t.GetDependees("d").GetEnumerator();
            Assert.IsTrue(e.MoveNext());
            Assert.AreEqual("b", e.Current);
            Assert.IsFalse(e.MoveNext());
        }

        /// <summary>
        ///Non-empty graph contains something
        ///</summary>
        [TestMethod()]
        public void ReplaceThenEnumerate()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("x", "b");
            t.AddDependency("a", "z");
            t.ReplaceDependents("b", new HashSet<string>());
            t.AddDependency("y", "b");
            t.ReplaceDependents("a", new HashSet<string>() { "c" });
            t.AddDependency("w", "d");
            t.ReplaceDependees("b", new HashSet<string>() { "a", "c" });
            t.ReplaceDependees("d", new HashSet<string>() { "b" });

            IEnumerator<string> e = t.GetDependees("a").GetEnumerator();
            Assert.IsFalse(e.MoveNext());

            e = t.GetDependees("b").GetEnumerator();
            Assert.IsTrue(e.MoveNext());
            String s1 = e.Current;
            Assert.IsTrue(e.MoveNext());
            String s2 = e.Current;
            Assert.IsFalse(e.MoveNext());
            Assert.IsTrue(((s1 == "a") && (s2 == "c")) || ((s1 == "c") && (s2 == "a")));

            e = t.GetDependees("c").GetEnumerator();
            Assert.IsTrue(e.MoveNext());
            Assert.AreEqual("a", e.Current);
            Assert.IsFalse(e.MoveNext());

            e = t.GetDependees("d").GetEnumerator();
            Assert.IsTrue(e.MoveNext());
            Assert.AreEqual("b", e.Current);
            Assert.IsFalse(e.MoveNext());
        }

        /// <summary>
        ///Using lots of data
        ///</summary>
        [TestMethod()]
        public void StressTest()
        {
            // Dependency graph
            DependencyGraph t = new DependencyGraph();

            // A bunch of strings to use
            const int SIZE = 200;
            string[] letters = new string[SIZE];
            for (int i = 0; i < SIZE; i++)
            {
                letters[i] = ("" + (char)('a' + i));
            }

            // The correct answers
            HashSet<string>[] dents = new HashSet<string>[SIZE];
            HashSet<string>[] dees = new HashSet<string>[SIZE];
            for (int i = 0; i < SIZE; i++)
            {
                dents[i] = new HashSet<string>();
                dees[i] = new HashSet<string>();
            }

            // Add a bunch of dependencies
            for (int i = 0; i < SIZE; i++)
            {
                for (int j = i + 1; j < SIZE; j++)
                {
                    t.AddDependency(letters[i], letters[j]);
                    dents[i].Add(letters[j]);
                    dees[j].Add(letters[i]);
                }
            }

            // Remove a bunch of dependencies
            for (int i = 0; i < SIZE; i++)
            {
                for (int j = i + 4; j < SIZE; j += 4)
                {
                    t.RemoveDependency(letters[i], letters[j]);
                    dents[i].Remove(letters[j]);
                    dees[j].Remove(letters[i]);
                }
            }

            // Add some back
            for (int i = 0; i < SIZE; i++)
            {
                for (int j = i + 1; j < SIZE; j += 2)
                {
                    t.AddDependency(letters[i], letters[j]);
                    dents[i].Add(letters[j]);
                    dees[j].Add(letters[i]);
                }
            }

            // Remove some more
            for (int i = 0; i < SIZE; i += 2)
            {
                for (int j = i + 3; j < SIZE; j += 3)
                {
                    t.RemoveDependency(letters[i], letters[j]);
                    dents[i].Remove(letters[j]);
                    dees[j].Remove(letters[i]);
                }
            }

            // Make sure everything is right
            for (int i = 0; i < SIZE; i++)
            {
                Assert.IsTrue(dents[i].SetEquals(new HashSet<string>(t.GetDependents(letters[i]))));
                Assert.IsTrue(dees[i].SetEquals(new HashSet<string>(t.GetDependees(letters[i]))));
            }
        }

        /// <summary>
        ///Returns the correct number of dependees
        ///</summary>
        [TestMethod()]
        public void DependeesSizeTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("c", "b");
            t.AddDependency("b", "d");
            Assert.AreEqual(2, t["b"]);
            Assert.AreEqual(0, t["a"]);
        }

        /// <summary>
        ///Nodes that have dependents returns true, nodes that don't exist or have no dependents returns false
        ///</summary>
        [TestMethod()]
        public void HasDependentsTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("c", "b");
            t.AddDependency("b", "d");
            t.AddDependency("d", "e");
            t.RemoveDependency("d", "e");
            Assert.AreEqual(true, t.HasDependents("a"));
            Assert.AreEqual(false, t.HasDependents("x"));
            Assert.AreEqual(false, t.HasDependents("d"));
        }

        /// <summary>
        ///Nodes that have dependees returns true, nodes that don't exist or have no dependees returns false
        ///</summary>
        [TestMethod()]
        public void HasDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("c", "b");
            t.AddDependency("b", "d");
            t.AddDependency("d", "e");
            t.RemoveDependency("d", "e");
            Assert.AreEqual(true, t.HasDependees("b"));
            Assert.AreEqual(false, t.HasDependees("x"));
            Assert.AreEqual(false, t.HasDependees("a"));
            Assert.AreEqual(false, t.HasDependees("e"));
        }

        /// <summary>
        ///Invoking the ReplaceDependees method on a node that did not previously have dependees
        ///</summary>
        [TestMethod()]
        public void ReplaceDependeesTest()
        {
            DependencyGraph t = new DependencyGraph();
            t.AddDependency("a", "b");
            t.AddDependency("a", "c");
            t.AddDependency("c", "b");
            t.AddDependency("b", "d");
            Assert.AreEqual(false, t.HasDependees("e"));
            t.ReplaceDependees("e", new HashSet<string>() { "c" });
            Assert.AreEqual(true, t.HasDependees("e"));

        }

    }
}