package com.damonallison.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotations are metadata that can be added to the type system.
 * 
 * Annotation use cases:
 * 
 * <ul>
 * <li>Compiler Information : The compiler can use annotations to detect errors
 * or suppress warnings.
 * <li>Tools : Tools (compilers, documentation systems) use annotations to
 * generate code, XML files, etc.
 * <li>Runtime Processing : Annotations can be examined at runtime.
 * <li>Replacing comments : Annotations can be used to replace repetitive
 * comments with structures that are parsable and
 * </ul>
 * 
 * Annotation Elements:
 * 
 * Annotations can contain elements, which look like methods. Each element can
 * optionally have default values. If an annotation only has one element, named
 * "value", the name can be ommitted when using the annotation.
 * 
 * 
 * 
 * Annotations used here:
 * 
 * {@code @documented} indicates that whenever this annotation is used, the
 * elements it's used on should be documented using Javadoc. By default,
 * annotations are not included in Javadoc).
 *
 * {@code @repeatable} requires a "container annotation" that is automatically
 * generated by the compiler. In order for the compiler to do this, the
 * container annotation must be defined in your code. We'll define the container
 * annotation in {@link ClassHeaders}.
 * 
 * {@code @retention} allows the annotation to be accessed via reflection. There
 * are three retention policies:
 * 
 * <ul>
 * <li>@Retention(RetentionPolicy.SOURCE) : Annotation is retained only in the
 * source and is ignored by the compiler.
 * <li>@Retention(RetentionPolicy.CLASS) : Annotation is retained by the
 * compiler, but ignored at runtime.
 * <li>@Retention(RetentionPolicy.RUNTIME) : Annotation is retained by the JVM
 * and can be used at runtime.
 * </ul>
 * 
 * Repeating annotations:
 * 
 * Repeating annotations require a "container annotation". The container
 * annotation (in this case {@code ClassHeaders}) must have a single "value"
 * element with an array type
 *
 * @author Damon Allison
 *
 */
@Documented
@Repeatable(ClassHeaders.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassHeader {

	String author();

	String date() default "unknown";

	int revision() default 1;

	/**
	 * A list of people who have reviewed this class.
	 * 
	 * Example of using the array type:
	 * 
	 * {@code @ClassHeader(reviewers = "Damon", "Chris" }
	 */
	String[] reviewers();
}
