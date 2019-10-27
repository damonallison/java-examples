package com.damonallison.libraries.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Java 7 Introduced the NIO.2 package. NIO.2 introduced {@link Path} and
 * {@link File}. (Basically everything in the java.nio.file package). These
 * tests show the basic usage of {@link Path}.
 */
class IOPathTests {

	/**
	 * {@code Path} is the primary entry point into the NIO.2 package.
	 *
	 * {@code Path} objects are operated on syntactially, they don't access the
	 * file system.
	 */
	@Test
	void pathCreation() {

		Path full = Paths.get("/test/path");
		Path parts = Paths.get("/", "test", "path");
		Path uri = Paths.get(URI.create("file:///test/path"));

		assertEquals(full, parts);
		assertEquals(parts, uri);
	}

	/**
	 * Tests converting a path to/from a string and URI.
	 *
	 * Paths have both string and URI representations.
	 */
	@Test
	void pathConversion() {
		Path p = Paths.get("/this/is/a/test.txt"); // from string
		assertEquals(p.toString(), "/this/is/a/test.txt"); // to string

		URI u = p.toUri(); // to uri
		assertEquals(u.getScheme(), "file");

		Path p2 = Paths.get(u); // from uri
		assertEquals(p, p2);
	}

	@Test
	void pathParsing() {
		String pathStr = "/this/is/a/path.tmp";
		Path p = Paths.get(pathStr); // String -> Path

		assertEquals(pathStr, p.toString()); // Path -> String
		assertTrue(p.isAbsolute());
		assertEquals("path.tmp", p.getFileName().toString(), "getFileName() returns the last path component.");

		// Java uses the term "name" to refer to each path component.
		assertEquals(4, p.getNameCount(), "each path component is a 'name'");
		assertEquals("this", p.getName(0).toString(), "getName() will return the component at the given position. Note that 0 is *not* the root '/'");

		// The root path will be different based on OS. We can't always assume
		// "/" - it may be "C:/"
		assertEquals("/", p.getRoot().toString());

		// Parent
		assertEquals("/this/is/a", p.getParent().toString());
	}

	@Test
	void pathComparison() throws IOException {
		Path parent = Paths.get("/this/is/a");
		Path child = parent.resolve("test.txt"); // Combines paths.
		Path free = Paths.get("test.txt");

		assertEquals("/this/is/a/test.txt", child.toString());

		assertTrue(child.startsWith(parent), "determining parent / child relationship using startsWith");

		assertTrue(child.endsWith(free), "determining if two paths have the same leafs");

		// Determine if two paths resolve to the same file (resolves symlinks).
		// If the paths are identical, no file system operation is performed.
		// if they are not identical, the file system is queried, symlinks
		// resolved, and a flag is returned if both paths point to the same
		// inode.
		assertTrue(Files.isSameFile(parent, Paths.get("/this/is/a/")));

		// .toRealPath() returns the "real" path of a file. It peforms the
		// following
		//
		// 1. Resolves symbolic links.
		// 2. If Path is relative, it returns the absolute path.
		// 3. Normalizes paths.

		// Note : by default, /tmp is symlinked on OS X. Therefore, the real
		// path will be different than the original path returned from
		// createTempFile.
		Path tmp = Files.createTempFile("test", null);
		assertFalse(tmp.toRealPath().equals(tmp));
	}

	/**
	 * Path normalization removes any redundant elements.
	 *
	 * Important : {@code normalize} does *not* check the file system when it
	 * cleans up a path. Normalizing a path where one or more name components
	 * refer to a symbolic link could make the path invalid.
	 */
	@Test
	void pathNormalization() {

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

	/**
	 * Resolve and Relativize create paths between two objects.
	 */
	@Test
	void pathJoining() {
		Path p = Paths.get("/this/is/a");

		// Resolving a partial path will append it.
		assertEquals("/this/is/a/test.txt", p.resolve(Paths.get("test.txt"))
				.toString());

		// Resolving an absolute path will return the absolute path.
		// It's already resolved.
		assertEquals("/a/test.txt", p.resolve(Paths.get("/a/test.txt"))
				.toString());

		// Relativize : creates a relative path from an original path. The
		// nested path must be absolute.
		Path nested = p.relativize(Paths.get("/this/is/a/nested/path.txt"));
		assertEquals("nested/path.txt", nested.toString());

		// What happens if we try to relativize two paths that do not share a
		// parent? Ultimately "/" is the common parent, so a relative path will
		// be created from "/"
		nested = p.relativize(Paths.get("/no/common/parent"));
		assertEquals("../../../no/common/parent", nested.toString());

		// Relativize only works when either both paths are absolute or both
		// relative.
	}
}
