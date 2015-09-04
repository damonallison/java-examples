package com.damonallison.annotations.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.damonallison.annotations.ClassHeader;
import com.google.common.collect.Sets;

/**
 * Annotations allow you to add metadata to the type system. This allows for
 * cool tricks, like meta-programming. Annotations are used all over the place
 * within java.
 */
@ClassHeader(author = "Damon Allison", date = "2015/05/11", reviewers = { "Chris Koehnen" })
@ClassHeader(author = "Chris Koehnen", date = "2015/05/12", reviewers = { "Damon Allison" })
@ClassHeader(author = "NoName", reviewers = { "ReviewerName", "Damon Allison" })
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
		assertNotNull(d);

		// Annotation doesn't exist.
		Inherited i = ClassHeader.class.getAnnotation(Inherited.class);
		assertNull(i);

		/**
		 * Retrieve a repeated annotation.
		 */
		ClassHeader[] annotations = AnnotationTests.class
				.getAnnotationsByType(ClassHeader.class);

		Stream<ClassHeader> classHeaders = Arrays.stream(annotations);
		Object[] filtered = classHeaders.filter(ann -> ann.author().toLowerCase().equals("noname")).toArray();
		assertEquals(1, filtered.length);
		/**
		 * Verify default values are used - the date default is "unknown"
		 */
		assertEquals("unknown", ((ClassHeader)filtered[0]).date().toLowerCase());

		Set<String> actuals = Arrays.stream(annotations).flatMap(h -> Arrays.stream(h.reviewers())).collect(Collectors.toSet());
		Set<String> expected = Sets.newHashSet("Damon Allison","Chris Koehnen", "ReviewerName");
		assertEquals(actuals, expected);
	}

	/*
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
	@Test
	public void typeAnnotations() {
		// TODO : determine what type annotations are available in java (Nullable?) and use them
	}
}
