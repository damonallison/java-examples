package com.damonallison.libraries.io.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class NIO2FileTests {

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
		// FileSystem fs = FileSystems.getDefault();
		// for (FileStore store : fs.getFileStores()) {
		// long total = store.getTotalSpace();
		// long used = store.getTotalSpace() - store.getUnallocatedSpace();
		// long avail = store.getUsableSpace();
		// System.out.println("looking at " + store + ": total=" + total
		// + " used=" + used + " avail=" + avail);
		// }
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
	@Test
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
		Files.write(textPath, "Hello, world".getBytes());
		Files.copy(textPath, copyPath, StandardCopyOption.REPLACE_EXISTING);

		String textString = new String(Files.readAllBytes(textPath));
		String copyString = new String(Files.readAllBytes(copyPath));
		assertEquals(textString, copyString);

		// Move

		final Path movePath = tempDir.resolve("moved.txt");
		Files.move(copyPath, movePath, StandardCopyOption.REPLACE_EXISTING);
		assertTrue(Files.notExists(copyPath));
		assertTrue(Files.exists(movePath));
	}

	@Test
	public void testFileReading() throws IOException {

		// Simple read/write operations on Files.

		// The easiest method for reading / writing bytes
		// is with the static "readAll*" methods on Files.
		Path tempPath = Files.createTempFile("file", "tmp");
		Files.write(tempPath, "hello, world!".getBytes());
		String input = new String(Files.readAllBytes(tempPath));
		assertEquals("hello, world!", input);

		// Buffered / Unbuffered Streams

		// Buffered readers are the next least complex reading/writing option.
		// Buffered readers save on I/O operations.
		// These are defined in the java.io package, therefore they are NIO and
		// IO
		// compatible.
		Path copyPath = Files.createTempFile("copy", "tmp");
		try (BufferedReader reader = Files.newBufferedReader(tempPath);
				BufferedWriter writer = Files.newBufferedWriter(copyPath)) {
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
			}
		}

		String textString = new String(Files.readAllBytes(tempPath));
		String copyString = new String(Files.readAllBytes(copyPath));
		assertEquals(textString, copyString);

		// Channels

		// Streams reads and writes a character at a time. Channel I/O reads
		// buffers at a time.

		String encoding = System.getProperty("file.encoding");
		try (SeekableByteChannel sbc = Files.newByteChannel(tempPath)) {
			ByteBuffer buf = ByteBuffer.allocate(2);
			// not ideal. we should specify an ideal bounds.
			ByteBuffer out = ByteBuffer.allocate(200);
			int read = 0;
			while ((read = sbc.read(buf)) > 0) {
				out.put(buf.array(), 0, read);
				buf.rewind();
			}
			copyString = new String(out.array(), 0, out.position(),
					Charset.forName(encoding));
			assertEquals(textString, copyString);
		}
	}

	/**
	 * Example of specifying a glob to find all files in a single directory
	 * matching a pattern.
	 */
	@Test
	public void testDirectoryReading() throws IOException {

		// TODO : recursive tree walking requires a FileVisitor.
		// Implement a FileVisitor which returns a list of paths (sorted)
		// that match a glob pattern. (Find a library that does this already -
		// there has to be one out there if the default FileVisitor doesn't
		// support it.

		Path tempDir = Files.createTempDirectory("tmp");
		Path javaFile = tempDir.resolve("java.txt");
		Path testFile = tempDir.resolve("test.txt");
		Path binFile = tempDir.resolve("java.bin");
		Path noFile = tempDir.resolve("no.file");
		Files.createFile(javaFile);
		Files.createFile(testFile);
		Files.createFile(binFile);
		Files.createFile(noFile);

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir,
				"*.{txt,bin}")) {
			List<Path> paths = Lists.newArrayList(stream);
			assertTrue(paths.containsAll(Lists.newArrayList(javaFile, testFile,
					binFile)));
			assertFalse(paths.contains(noFile));
		}

		// Custom Filter

		DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
			@Override
			public boolean accept(Path entry) throws IOException {
				return entry.getFileName().toString().toLowerCase()
						.contains("java");
			}

		};
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir,
				filter)) {
			List<Path> paths = Lists.newArrayList(stream);
			assertTrue(paths.containsAll(Lists.newArrayList(javaFile, binFile)));
		}
	}
}
