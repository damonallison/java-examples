package com.damonallison.libraries.io.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

/**
 * Java 7 Introduced the NIO.2 package. These tests show the basic usage of
 * NIO.2.
 *
 * TODO : Symlinks (w/ circular references)
 *
 */
public class NIO2Tests {

	@Test
	public void testPathCreation() {

		Path full = Paths.get("/test/path");
		Path parts = Paths.get("/", "test", "path");
		Path uri = Paths.get(URI.create("file:///test/path"));

		assertEquals(full, parts);
		assertEquals(parts, uri);

	}

	@Test
	public void testPathParsing() {
		String pathStr = "/this/is/a/path.tmp";
		Path p = Paths.get(pathStr);

		assertEquals(pathStr, p.toString());
		assertTrue(p.isAbsolute());
		assertEquals("getFileName() returns the last path component.",
				"path.tmp", p.getFileName().toString());

		// Java uses the term "name" to refer to each path component. Confusing!
		assertEquals("each path component is a 'name'", 4, p.getNameCount());
		assertEquals(
				"getName() will return the component at the given position.",
				"this", p.getName(0).toString());

		// The root path will be different based on OS. We can't always assume
		// "/" - it may be "C:/"
		assertEquals("/", p.getRoot().toString());

		// Parent
		assertEquals("/this/is/a", p.getParent().toString());

	}

	@Test
	public void testPathNormalization() {

		// Final path should be /test/file
		Path p = Paths.get("/test/./child/../file");

		assertEquals(5, p.getNameCount());
		assertEquals(".", p.getName(1).toString());

		assertEquals("/test/file", p.normalize().toString());

		// Normalize does *not* look at the file system when normalizing paths.
		// It is strictly a syntactic operation.
		p = Paths.get("~/test").normalize();
		assertEquals(p.toString(), "~/test");
	}

}
