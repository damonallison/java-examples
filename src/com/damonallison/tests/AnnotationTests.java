package com.damonallison.tests;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.damonallison.annotations.ClassHeader;
import com.google.common.collect.Sets;

/**
 * Type annotations were created to support stronger type checking. This allows
 * you to use type checking tools (frameworks). Type annotations allow you to
 * build on top of the java type system.
 * <p>
 * For example, if you want to ensure a variable is never assigned to
 * {@code null}, you could annotate that variable with the {@code NonNull}
 * attribute. When compiling the code with the "nonnull check framework" (or
 * whatever framework is providing NonNull checks), the compiler will print a
 * warning if it detects a potential null assignment.
 */
@ClassHeader(author = "Damon Allison", date = "2015/05/11", reviewers = { "Chris Koehnen" })
@ClassHeader(author = "Chris Koehnen", date = "2015/05/12", reviewers = { "Damon Allison" })
@ClassHeader(author = "NoName", reviewers = { "ReviewerName" })
@SuppressWarnings({})
public class AnnotationTests {

	/**
	 * Shows how to use reflection to retrieve annotation values.
	 * 
	 * Remember that only annotations that are visible at runtime
	 * {@code @Retention(RetentionPolicy.RUNTIME)} will be shown.
	 * 
	 * NOTE : Repeated annotations are only available in java 1.8.
	 */
	@Test
	public void readingAnnotationsViaReflection() {

		/**
		 * Retrieve a single annotation.
		 */
		Documented d = ClassHeader.class.getAnnotation(Documented.class);
		Assert.assertNotNull(d);

		// Annotation doesn't exist.
		Inherited i = ClassHeader.class.getAnnotation(Inherited.class);
		Assert.assertNull(i);

		/**
		 * Retrieve a repeated annotation.
		 */
		ClassHeader[] annotations = AnnotationTests.class
				.getAnnotationsByType(ClassHeader.class);

		Set<String> expected = Sets.newHashSet("Damon Allison",
				"Chris Koehnen", "NoName");

		Set<String> actuals = Sets.newHashSet();
		for (ClassHeader ch : annotations) {
			actuals.add(ch.author());

			if (ch.author().equals("NoName")) {
				Assert.assertEquals(ch.date().toLowerCase(), "unknown");
			}
		}

		/**
		 * Verify default values are used.
		 */
		Assert.assertTrue(expected.equals(actuals));
	}
}
