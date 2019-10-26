package com.damonallison.classes;

import java.util.function.Function;

import com.damonallison.language.tests.NestedClassExercises;

/**
 * DataStructure is a sample class that was created by Oracle and used as the
 * basis for lesson exercises.
 *
 * @see NestedClassExercises
 */
public class DataStructure {

	// Create an array
	public final static int SIZE = 15;
	private int[] arrayOfInts = new int[SIZE];

	public DataStructure() {
		// fill the array with ascending integer values
		for (int i = 0; i < SIZE; i++) {
			arrayOfInts[i] = i;
		}
	}

	public int[] getArrayOfInts() {
		return arrayOfInts;
	}

	public String printEven() {

		// Print out values of even indices of the array
		DataStructureIterator iterator = new EvenIterator();
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			sb.append(iterator.next() + " ");
		}
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	public static boolean isEvenIndex(int x) {
		return x % 2 == 0;
	}

	public static boolean isOddIndex(int x) {
		return !isEvenIndex(x);
	}

	public interface DataStructureIterator extends java.util.Iterator<Integer> {
	}

	// Inner class implements the DataStructureIterator interface,
	// which extends the Iterator<Integer> interface

	public class EvenIterator implements DataStructureIterator {

		// Start stepping through the array from the beginning
		private int nextIndex = 0;

		@Override
		public boolean hasNext() {

			// Check if the current element is the last in the array
			return (nextIndex <= SIZE - 1);
		}

		@Override
		public Integer next() {

			// Record a value of an even index of the array
			Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);

			// Get the next even element
			nextIndex += 2;
			return retValue;
		}
	}

	// Define a method named print(DataStructureIterator iterator). Invoke this
	// method
	// with an instance of the class EvenIterator so that it performs the same
	// function
	// as {@code printEven}
	public String print(DataStructureIterator iterator) {
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			sb.append(iterator.next() + " ");
		}
		sb.append(System.lineSeparator());
		return sb.toString();
	}

	// Define a method named print(java.util.Function<Integer, Boolean>
	// iterator) that performs
	// the same function as print(DataStructureIterator iterator).
	// Invoke this method with a lambda expression to print elements that have
	// an even index value.
	// Invoke this method again with a lambda expression to print elements that
	// have an odd index value.

	public String print(Function<Integer, Boolean> iterator) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			if (iterator.apply(i)) {
				sb.append(Integer.valueOf(i).toString() + " ");
			}
		}
		sb.append(System.lineSeparator());
		return sb.toString();
	}
}