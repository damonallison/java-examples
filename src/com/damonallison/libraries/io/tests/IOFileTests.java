package com.damonallison.libraries.io.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.spi.FileSystemProvider;

import org.junit.Test;

import com.sun.security.auth.module.UnixSystem;

/**
 * {@code Files}, along with {@code Path} are main entry point into NIO.2. NIO.2
 * is all about better file management.
 *
 * Unlike {@code Path}, which typically does not touch the file system,
 * {@code File} performs operations against the file system.
 *
 * {@link Files} is a main entry point into the NIO.2 package. Files contains
 * static methods.
 */
public class IOFileTests {

	/**
	 * The static class {@link Files} contains a comprehensive list of
	 * operations that can be performed against files. It delegates to other
	 * classes to perform the actual operations.
	 *
	 * For example, most file system operations are delegated to an internal
	 * {@link FileSystemProvider} class.
	 *
	 * This test shows the use of {@link Files} to read metadata (attributes).
	 */
	@Test
	public void fileAttributes() throws IOException {

		Path tempDir = Files.createTempDirectory("temp"); // creates temp.tmp

		// Reading file attributes.
		//
		// These functions all read a single file attribute with each call.
		//
		// Each attribute read hits the disk - so read attributes in bulk if
		// possible.

		assertTrue(Files.exists(tempDir));
		assertTrue(Files.isDirectory(tempDir));
		assertTrue(Files.isReadable(tempDir) && //
				Files.isWritable(tempDir) && //
				Files.isExecutable(tempDir));

		//
		// Basic Attributes
		//
		// If you are going to read attributes in bulk, or need attributes
		// that are not provided at a high level via the {@code Files} class,
		// use {@code ReadAttributes}.
		//
		// File attributes are grouped into "Views". A view maps to
		// a particular file system implementation. In the Following example,
		// {@code BasicFileAttributes} is a view.

		long lastMod = Files.getLastModifiedTime(tempDir).toMillis();
		BasicFileAttributes basicAttrs = Files.readAttributes(tempDir,
				BasicFileAttributes.class);
		assertEquals(lastMod, basicAttrs.lastModifiedTime().toMillis());
		assertEquals(lastMod, basicAttrs.creationTime().toMillis());
		assertEquals(lastMod, basicAttrs.lastAccessTime().toMillis());

		//
		// Posix attributes
		//
		// Posix attributes extends basic attributes by providing owner, group,
		// and permissions.
		PosixFileAttributes posixAttrs = Files.readAttributes(tempDir,
				PosixFileAttributes.class);

		assertTrue(
				"The owner should be able to read a temp file!", //
				posixAttrs.permissions().contains(
						PosixFilePermission.OWNER_READ));

		// Get the current username / groupGid from the system.
		UnixSystem us = new UnixSystem();

		// Get the currently logged in user from the file. This user should be
		// the owner.
		assertEquals("The current user should have created the temp file", //
				posixAttrs.owner().getName(), us.getUsername());

		// Get the GID of the file.
		Integer gid = (Integer) Files.readAttributes(tempDir, "unix:gid").get(
				"gid");
		assertEquals("The current group should be the group on the temp file", //
				us.getGid(), gid.longValue());

		// Example reading / writing custom attributes.
		UserDefinedFileAttributeView view = Files.getFileAttributeView(tempDir,
				UserDefinedFileAttributeView.class);

		// Note : this is *not* supported on all operating systems. If not
		// supported,
		// we get null here.
		if (view != null) {
			// Write
			view.write("mime-type", Charset.defaultCharset().encode("dir"));

			// Read it back.
			ByteBuffer buf = ByteBuffer.allocate(view.size("mime-type"));
			view.read("mime-type", buf);
			buf.flip();
			assertEquals("dir", Charset.defaultCharset().decode(buf).toString());
		}
	}

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

		// .toRealPath() returns an absolute, case correct, and symlinked
		// resolved path.
		//
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
		//
		// /real
		// /a -> /real
		// /b -> /a
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

			assertTrue(total >= used);
			assertTrue(total >= avail);
		}
	}
}
