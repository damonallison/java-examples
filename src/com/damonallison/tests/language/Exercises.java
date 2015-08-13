package com.damonallison.tests.language;

import org.junit.Test;

import com.damonallison.classes.DataStructure;
import com.damonallison.classes.DataStructure.DataStructureIterator;


public class Exercises {

	public class Class1 {
		protected InnerClass1 ic;

		public Class1() {
			ic = new InnerClass1();
		}

		public void displayStrings() {
			System.out.println(ic.getString() + ".");
			System.out.println(ic.getAnotherString() + ".");
		}

		protected class InnerClass1 {
			public String getString() {
				return "InnerClass1: getString invoked";
			}

			public String getAnotherString() {
				return "InnerClass1: getAnotherString invoked";
			}
		}
	}

	@Test
	public void exercise() {
		Class1 c1 = new Class1();
		c1.displayStrings();
	}
	@Test
	public void exercise2() {
		// Fill the array with integer values and print out only
		// values of even indices
		DataStructure ds = new DataStructure();
		ds.printEven();

		// Define a function named print(DataStructureIterator) that takes an
		// EventIterator and does the same thing that printEven does.
		DataStructure.DataStructureIterator iterator = ds.new EvenIterator();
		ds.print(iterator);

		// Invoke print() to print odd values. Use an anonymous class as the method's
		// argument
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

	@Test
	public void exercise3() {
		DataStructure ds = new DataStructure();
		ds.print(position -> position % 2 == 0);
		ds.print(position-> position % 2 == 1);
	}

	// Shows using method references to replace the lambda expressions
	// above.
	@Test
	public void exercise4() {
		DataStructure ds = new DataStructure();
		ds.print(DataStructure::isEvenIndex);
		ds.print(DataStructure::isOddIndex);
	}
}
