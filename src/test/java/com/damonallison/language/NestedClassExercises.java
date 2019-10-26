package com.damonallison.language;

import org.junit.Test;

import com.damonallison.classes.DataStructure;
import com.damonallison.classes.DataStructure.DataStructureIterator;

/**
 * Nested Classes Exercises from :
 * https://docs.oracle.com/javase/tutorial/java/javaOO
 * /QandE/nested-questions.html
 *
 */
public class NestedClassExercises {

	/**
	 * Class1 is a sample program that Oracle created to test nested classes.
	 * See also {@link com.damonallison.classes.DataStructure}
	 */
	public class Class1 {
		protected InnerClass1 ic;

		public Class1() {
			ic = new InnerClass1();
		}

		public String displayStrings() {
			return ic.getString() + "," + ic.getAnotherString();
		}

		/**
		 * Here is the nested class.
		 */
		protected class InnerClass1 {
			public String getString() {
				return "InnerClass1: getString invoked";
			}

			public String getAnotherString() {
				return "InnerClass1: getAnotherString invoked";
			}
		}
	}

	/**
	 * Exercise 2a :
	 *
	 * Define a method named print(DataStructureIterator) that takes an
	 * EventIterator and does the same thing that printEven does.
	 * 
	 * The trick with this example is that EvenIterator is nested within
	 * DataStructure. You create an EvenIterator instance from an instance of
	 * DataStructure.
	 */
	@Test
	public void exercise2a() {
		DataStructure ds = new DataStructure();
		DataStructure.DataStructureIterator iterator = ds.new EvenIterator();
		ds.print(iterator);
	}

	/**
	 * Exercise 2b : Invoke print() to print odd values. Use an anonymous class
	 * as the method's argument.
	 */
	@Test
	public void exercise2b() {
		DataStructure ds = new DataStructure();
		ds.print(new DataStructureIterator() {
			// Start stepping through the array from the first odd value
			private int nextIndex = 1;

			@Override
			public boolean hasNext() {

				// Check if the current element is the last in the array
				return (nextIndex <= DataStructure.SIZE - 1);
			}

			@Override
			public Integer next() {

				// Record a value of an even index of the array
				Integer retValue = Integer.valueOf(ds.getArrayOfInts()[nextIndex]);

				// Get the next even element
				nextIndex += 2;
				return retValue;
			}
		});

	}

	/**
	 * Exercise 2c
	 *
	 * Define a method named print(java.util.Function<Integer, Boolean>
	 * iterator) that that performs the same function as
	 * print(DataStructureIterator iterator).
	 *
	 * Invoke this method with a lambda expression to print elements that have
	 * an even index value. Invoke this method again with a lambda expression to
	 * print elements that have an odd index value.
	 */
	@Test
	public void exercise2c() {
		DataStructure ds = new DataStructure();
		ds.print(position -> position % 2 == 0);
		ds.print(position -> position % 2 == 1);
	}

	/**
	 * Define two methods so that the following two statements print elements
	 * that have an even index value and elements that have an odd index value:
	 *
	 * <code>
	 * DataStructure ds = new DataStructure()
	 * // ...
	 * ds.print(DataStructure::isEvenIndex);
	 * ds.print(DataStructure::isOddIndex);
	 * </code>
	 *
	 */
	@Test
	public void exercise3d() {
		DataStructure ds = new DataStructure();
		ds.print(DataStructure::isEvenIndex);
		ds.print(DataStructure::isOddIndex);
	}
}
