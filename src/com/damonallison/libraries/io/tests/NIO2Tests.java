package com.damonallison.libraries.io.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import org.junit.Test;

/**
 * Java 7 Introduced the NIO.2 package. These tests show the basic usage of
 * NIO.2.
 *
 * TODO : Symlinks (w/ circular references)
 *
 */
public class NIO2Tests {

	/**
	 * {@code Path} is the primary entry point into the NIO.2 package.
	 *
	 * {@code Path} objects are operated on syntactially, they don't access the
	 * file system.
	 */
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

		// Java uses the term "name" to refer to each path component.
		assertEquals("each path component is a 'name'", 4, p.getNameCount());
		assertEquals(
				"getName() will return the component at the given position. Note that 0 is *not* the root '/'",
				"this", p.getName(0).toString());

		// The root path will be different based on OS. We can't always assume
		// "/" - it may be "C:/"
		assertEquals("/", p.getRoot().toString());

		// Parent
		assertEquals("/this/is/a", p.getParent().toString());
	}

	@Test
	public void testPathComparison() throws IOException {
		Path parent = Paths.get("/this/is/a");
		Path child = parent.resolve("test.txt");
		Path free = Paths.get("test.txt");

		assertEquals("/this/is/a/test.txt", child.toString());
		assertTrue("determining parent / child relationship using startsWith",
				child.startsWith(parent));
		assertTrue("determining if two paths have the same leafs",
				child.endsWith(free));

		// determine if two paths resolve to the same file (resolves symlinks).
		// If the paths are identical, no file system operation is performed.
		// if they are not identical, the file system is queried, symlinks
		// resolved,
		// and a flag is returned if both paths point to the same inode.
		assertTrue(Files.isSameFile(parent, Paths.get("/this/is/a/")));
	}

	/**
	 * Path normalization removes any redundant elements.
	 *
	 * Important : {@code normalize} does *not* check the file system when it
	 * cleans up a path. Normalizing a path where one or more name components
	 * refer to a symbolic link could make the path invalid.
	 */
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

	/**
	 * Tests converting a path to/from a string and URI.
	 */
	@Test
	public void testPathConversion() {
		Path p = Paths.get("/this/is/a/test.txt"); // from string
		assertEquals(p.toString(), "/this/is/a/test.txt"); // to string

		URI u = p.toUri(); // to uri
		assertEquals(u.getScheme(), "file");

		Path p2 = Paths.get(u); // from uri
		assertEquals(p, p2);
	}

	@Test
	public void testJoiningPaths() {
		Path p = Paths.get("/this/is/a");

		// Resolving a partial path will append it.
		assertEquals("/this/is/a/test.txt", p.resolve(Paths.get("test.txt"))
				.toString());

		// Resolving an absolute path will return the absolute path.
		// It's already resolved.
		assertEquals("/a/test.txt", p.resolve(Paths.get("/a/test.txt"))
				.toString());

		// Relativize : creates a relative path from an original path. The
		// nested path must be
		// absolute.
		Path nested = p.relativize(Paths.get("/this/is/a/nested/path.txt"));
		assertEquals("nested/path.txt", nested.toString());

		// What happens if we try to relativize two paths that do not share a
		// parent? Relativize will still create a relative path!
		nested = p.relativize(Paths.get("/no/common/parent"));
		assertEquals("../../../no/common/parent", nested.toString());

		// Relativize only works when either both paths are absolute or both
		// relative.
	}

	// -------------------------------------------------------------------------
	// Files tests
	// -------------------------------------------------------------------------

	/**
	 * Tests symbolic link creation and other operations.
	 *
	 * {@code .toRealPath()} Does the following:
	 *
	 * <ul>
	 * <li>Resolves symbolic links
	 * <li>If the path is relative, returns the absolute path.
	 * <li>If the path contains any redundant elements, they are removed.
	 * <li>Throws an exception if the file does not exist or cannot be accessed.
	 * </ul>
	 */
	@Test
	public void testSymbolicLinks() throws IOException {

		/*-
		 * Creates
		 * tmpXXX/
		 *   a/
		 *     b/
		 *       test.txt
		 * test.txt (symlink to a/b/test.txt)
		 *
		 */

		final Path tempDir = Files.createTempDirectory("tmp");
		final Path tempDirName = tempDir.getFileName();
		Path ab = Files.createDirectories(tempDir.resolve("a/b"));
		assertTrue(Files.exists(tempDir.resolve("a/b")));

		Path textFile = ab.resolve("test.txt");
		Files.write(textFile, "hello, world".getBytes());
		assertTrue(Files.exists(textFile));

		// Create a path /tmp/test.txt that points to /tmp/a/b/test.txt to test
		// symbolic link resolution.
		Path symlink = tempDir.resolve("test.txt");
		Files.createSymbolicLink(symlink, textFile);
		assertTrue(Files.exists(symlink));
		assertTrue(Files.isSymbolicLink(symlink));

		// The real file ends in a/b/test.txt
		// The symlink ends with tmp/test.txt
		assertTrue(textFile.endsWith("a/b/test.txt"));
		assertTrue(symlink.endsWith(tempDirName.resolve("test.txt")));

		assertTrue(Files.isSameFile(symlink, textFile));

		// Convert the symlink to a real path.
		// Note : we need to convert *both* to their real paths - the /tmp
		// directory is also symlinked in OS X.
		assertEquals(textFile.toRealPath(), symlink.toRealPath());
	}

	@Test(expected = FileSystemException.class)
	public void testCircularLinks() throws IOException {
		final Path tempDir = Files.createTempDirectory("tmp").toRealPath();
		final Path a = tempDir.resolve("a");
		final Path b = tempDir.resolve("b");

		// Create and test valid symlinks
		Path real = Files.createDirectory(tempDir.resolve("real"));
		Files.createSymbolicLink(a, real);
		Files.createSymbolicLink(b, a);

		assertEquals(real, a.toRealPath());
		assertEquals(real, b.toRealPath());

		// Create a circular dependency c -> a and a -> c
		final Path c = tempDir.resolve("c");
		Files.createSymbolicLink(c, a);
		Files.delete(a);
		Files.createSymbolicLink(a, c);

		// Now try to resolve a symbolic link.
		// This will throw a FileSystemException:
		// "Too many levels of symbolic links"
		final Path resolved = c.toRealPath();
		fail("Circular path dependency should have failed "
				+ resolved.toString());
	}

	@Test
	public void testFileAttributes() throws IOException {
		// Create

		Path tempDir = Files.createTempDirectory("tmp");

		// Reading file attributes.
		// File attributes are grouped into "Views". A view maps to
		// a particular file system implementation.

		assertTrue(Files.exists(tempDir));
		assertTrue(Files.isDirectory(tempDir));
		assertTrue(Files.isReadable(tempDir) && //
				Files.isWritable(tempDir) && //
				Files.isExecutable(tempDir));

		// The "Files" class provides static methods for reading attributes.
		// Remember, each attr read hits the disk - so read them in bulk if
		// possible.
		// If you are going to read attributes in bulk, or need attributes
		// that are not provided at a high level via the {@code Files} class,
		// use {@code ReadAttributes}

		long lastMod = Files.getLastModifiedTime(tempDir).toMillis();
		BasicFileAttributes basicAttrs = Files.readAttributes(tempDir,
				BasicFileAttributes.class);
		assertEquals(lastMod, basicAttrs.lastModifiedTime().toMillis());

		// Posix attrs extends basic attributes by providing owner, group, and
		// permissions.
		PosixFileAttributes posixAttrs = Files.readAttributes(tempDir,
				PosixFileAttributes.class);
		assertTrue(posixAttrs.permissions().contains(
				PosixFilePermission.OWNER_READ));

		// Example reading / writing custom attributes.
		UserDefinedFileAttributeView view = Files.getFileAttributeView(tempDir,
				UserDefinedFileAttributeView.class);

		// Note : this is *not* supported on all operating systems. If not
		// supported,
		// we get null here.
		if (view != null) {
			view.write("mime-type", Charset.defaultCharset().encode("dir"));

			ByteBuffer buf = ByteBuffer.allocate(view.size("mime-type"));
			view.read("mime-type", buf);
			buf.flip();
			assertEquals("dir", Charset.defaultCharset().decode(buf).toString());
		}
	}

	/**
	 * Shows retrieving information on all file stores on the system
	 *
	 * @throws IOException
	 */
	@Test
	public void testFileStore() throws IOException {
		FileSystem fs = FileSystems.getDefault();
		for (FileStore store : fs.getFileStores()) {
			long total = store.getTotalSpace();
			long used = store.getTotalSpace() - store.getUnallocatedSpace();
			long avail = store.getUsableSpace();
			System.out.println("looking at " + store + ": total=" + total
					+ " used=" + used + " avail=" + avail);
		}
	}

	/**
	 * Shows common operations when working with files.
	 *
	 * {@link Files} is a main entry point into the NIO.2 package. Files
	 * contains static methods.
	 *
	 * @throws IOException
	 *
	 */
	public void testFileOperations() throws IOException {

		// Create

		Path tempDir = Files.createTempDirectory("tmp");

		// Delete

		// Directories must be empty or delete will fail. You can catch
		// DirectoryNotEmptyException.
		try {
			Files.delete(tempDir);
		} catch (DirectoryNotEmptyException e) {
			fail("Directory should have been empty!");
		} catch (IOException e) {
			// File permissions problems are caught here
			fail("Directory should have existed with read permissions");
		}

		// Copy

		tempDir = Files.createTempDirectory("tmp");
		final Path textPath = tempDir.resolve("test.txt");
		final Path copyPath = tempDir.resolve("test2.txt");
		Files.write(textPath, "Hello, world".getBytes(Charset.forName("UTF-8")));
		Files.copy(textPath, copyPath, StandardCopyOption.REPLACE_EXISTING);

		assertEquals(Files.readAllBytes(textPath), Files.readAllBytes(copyPath));

		// Move

		final Path movePath = tempDir.resolve("moved.txt");
		Files.move(copyPath, movePath, StandardCopyOption.REPLACE_EXISTING);
		assertTrue(Files.notExists(copyPath));
		assertTrue(Files.exists(movePath));
	}
}
