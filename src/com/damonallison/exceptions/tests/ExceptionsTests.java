package com.damonallison.exceptions.tests;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import com.damonallison.exceptions.DamonException;

/*-
 * Java has three exception types:
 *
 * 1. Checked Exceptions. These are exceptional conditions that a well-written application
 *    should anticipate and recover from. Checked exceptions are declared as part of the method
 *    definition and must be handled by the caller. All exceptions are checked exceptions,
 *    except if the exception inherits from {@link RuntimeException}.
 *
 * 2. Error. These exceptions are conditions that are unanticipated, like hardware failure.
 *    Errors can be caught, but it may also be better to simply let the process crash.
 *
 * 3. Runtime Exceptions. These are programming bugs, logic errors, or improper use of an API.
 *    Runtime exceptions do <b>not</b> need to be declared. The application can catch this exception,
 *    but it probably makes more sense to eliminate the bug that caused the exception to occur.
 *    All RuntimeExceptions derive from {@link RuntimeException}.
 *
 *  Errors and Runtime Exceptions are collectively known as "unchecked exceptions".
 *
 *  READ THIS:
 *
 *  Some programmers consider checked exceptions a serious flaw in the java language and bypass it
 *  using unchecked exceptions in place of checked exceptions. This is <b>not</b> recommended.
 *  Most applications should *not* throw {@link RuntimeException}s.
 *
 *  Why?
 *
 *  Throwing unchecked exceptions allows programmers to write code
 *  without bothering to specify or catch any exceptions. It sidesteps
 *  the intent exceptions, which should be caught and handled by the caller.
 *
 *  The Exceptions a method can throw is part of the method's interface. They
 *  can be thought of as return values.
 *
 *  Sun's guideline on checked exceptions:
 *
 *  Here's the bottom line guideline: If a client can reasonably be
 *  expected to recover from an exception, make it a checked exception.
 *  If a client cannot do anything to recover from the exception,
 *  make it an unchecked exception.
 *
 */
public class ExceptionsTests {

	/**
	 * Checked exceptions ({@link IOException} must be caught or declared in the
	 * {@code throws} clause of the method declaration. Unchecked exceptions
	 * {@link IllegalArgumentException} do *not* have to be declared in the
	 * method declaration (and typically are not). Adding unchecked exceptions
	 * to the throws clause is for documentation purposes, to inform the caller
	 * that the method will throw this exception.
	 *
	 *
	 * @throws IOException
	 *             thrown if files do not exist.
	 * @throws IllegalArgumentException
	 *             not really thrown from this function, but here for
	 *             documentation purposes
	 */
	private void checkedExceptionExample() throws IOException,
			IllegalArgumentException {
		// Example showing multiple resources declared in the try-with-resources
		// statement.
		try (FileWriter outStream = new FileWriter("/not/there");
				FileWriter outStream2 = new FileWriter("/not/there2")) {
			// this block will never be invoked. The previous line will throw a
			// {@code FileNotFoundException}, which derives from {@code
			// IOException}.
			outStream.write("hello, java");
		}
	}

	/**
	 * If a method can throw a checked exception, it must be declared. Here,
	 * because we do not catch the {@link IOException} that
	 * {@link #checkedExceptionExample()} can throw, we declare
	 * {@code throws IOException} in this method declaration.
	 *
	 * @throws IOException
	 */
	@Test(expected = FileNotFoundException.class)
	public void testCheckedExceptionCanThrow() throws IOException {
		this.checkedExceptionExample();
	}

	/**
	 * Example of handling exceptions. Multiple catch, finally.
	 */
	@Test
	public void testCheckedExceptionCannotThrow() {
		try {
			this.checkedExceptionExample();
		} catch (FileNotFoundException e) {
			// FileNotFoundException is a child of IOException. Always catch
			// exception subclasses before superclases. Specifying a catch for a
			// superclass prior to a subclass is a compile error.
		} catch (IOException e) {
			// This example throws an unchecked exception in place of the
			// checked exception. This is something you should *not* do - Sun
			// recommends you do not throw unchecked exceptions.
			throw new IllegalArgumentException("Bad file name!", e);
		} catch (IllegalArgumentException e) {

		} finally {
			// put all cleanup code (e.g., resource closing) in a finally block.
			// finally blocks always execute. For {@link AutoCloseable}
			// resources, use
			// try with resources (try (Resource r = new Resource()) {})
		}
	}

	@Test
	public void testCustomException() {
		// Custom exceptions are used to hold custom state to describe the
		// exception.
		try {
			throw new DamonException("damon");
		} catch (DamonException e) {
			assertEquals("damon", e.getUserId());
		}
	}
}
