package com.damonallison.annotations;

import java.lang.annotation.Documented;

/**
 * Annotations are a form of interfacde.
 * 
 * The body of the annotation type elements, which look like methods. Each
 * element can optionally have default values.
 * 
 * The @documented annotation makes the annotation appear in javadoc generated
 * comments.
 * 
 * @author dallison
 *
 */
@Documented
public @interface ClassHeader {

	public String author();

	public String date();

	public int revision() default 1;

	/**
	 * Allows multiple values to be
	 * 
	 * @return
	 */
	String[] reviewers();
}
