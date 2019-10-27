package com.damonallison.libraries.io;

import com.damonallison.classes.Pair;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests which show I/O operations against files (reading / writing).
 *
 * Java has multiple ways to write files ranging from simple {@link Files#write} to
 * complex (channels). These tests give examples of reading / writing data using
 * different I/O types.
 *
 *
 * @formatter:off
 *
 *
 * Stream hierarchy:
 * 	InputStream / OutputStream
 * 		FileInputStream / FileOutputStream : bytes to/from files.
 * 		DataInputStream / DataOutputStream : primitives to/from other streams.
 * 		ObjectInputStream / ObjectOutputStream : object serialization/deserialization to/from other streams.
 *
 * Readers / writers read/write to underlying streams. They require an underlying stream to perform
 * the I/O operations.
 *
 *  Reader / Writer
 *  	BufferedReader / BufferedWriter : wrap buffered readers/writers around readers/writers which have costly
 *                                        read/write operations (FileWriters / OutputStreamWriters).
 *      	FileReader / FileWriter : read/write files. Read/write operations are expensive.
 *
 *
 */
public class IOTests {

	/**
	 * Shows common operations when working with files.
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

		assertTrue(Arrays.equals( //
				Files.readAllBytes(textPath), //
				Files.readAllBytes(copyPath)));

		// Move

		final Path movePath = tempDir.resolve("moved.txt");
		Files.move(copyPath, movePath, StandardCopyOption.REPLACE_EXISTING);
		assertTrue(Files.notExists(copyPath));
		assertTrue(Files.exists(movePath));
	}

	/**
	 * There are multiple ways to copy a file, ranging from very simple and
	 * atomic {@code Files}.
	 */
	@Test
	void fileCopy() throws IOException {

		final byte[] bytes = "Damon Allison".getBytes();

		Path in = Files.createTempFile("infile", null);
		Path outBytes = Files.createTempFile("outbytes", null);
		Path outText = Files.createTempFile("outchars", null);
		Path outLines = Files.createTempFile("outline", null);

		// Easiest read/write operations.
		Files.write(in, bytes);
		assertArrayEquals(bytes, Files.readAllBytes(in));

		// The dead simple way to copy a file is:
		Path copy = Files.createTempFile("copy", null);
		Files.delete(copy);
		Files.copy(new FileInputStream(in.toFile()), copy);
		assertArrayEquals(bytes, Files.readAllBytes(copy));

		// byte-by-byte copy
		IOUtilities.byteCopy(in, outBytes);
		assertArrayEquals(bytes, Files.readAllBytes(outBytes));

		// char-by-char copy
		IOUtilities.charCopy(in, outText);
		assertArrayEquals(bytes, Files.readAllBytes(outText));

		// line copy
		IOUtilities.lineCopy(in, outLines);
		final byte[] line = ("Damon Allison" + System.lineSeparator())
				.getBytes();
		assertArrayEquals(line, Files.readAllBytes(outLines));
	}

	/**
	 * {@link DataInputStream} provides methods to write primitive values to the
	 * output stream.
	 *
	 * @throws IOException
	 */
	@Test
	void dataCopy() throws IOException {
		List<String> headers = new ArrayList<>();
		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			headers.add(String.format("header %d", i));
			values.add(i);
		}
		Path outData = Files.createTempFile("outdata", null);
		IOUtilities.dataCopy(headers, values, outData);

		List<String> inHeaders = new ArrayList<>(headers.size());
		List<Integer> inValues = new ArrayList<>(values.size());

		try (DataInputStream inputStream = new DataInputStream(
				new FileInputStream(outData.toFile()))) {

			for (int i = 0; i < 10; i++) {
				inHeaders.add(inputStream.readUTF());
			}

			for (int i = 0; i < 10; i++) {
				inValues.add(inputStream.readInt());
			}
		}

		assertEquals(headers, inHeaders);
		assertEquals(values, inValues);

	}

	/**
	 * {@link ObjectOutputStream} provides methods to write entire objects to
	 * the output stream (via serialization).
	 *
	 * In this method, notice that {@link Pair} is serializable and is being
	 * written / read into
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	@Test
	void objectCopy() throws IOException, ClassNotFoundException {
		List<String> headers = new ArrayList<>();
		List<Serializable> values = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			headers.add(String.format("header %d", i));
			values.add(new Pair<String, String>(String.format("first %d", i),
					String.format("last %d", i)));
		}
		Path outData = Files.createTempFile("outdata", null);
		IOUtilities.objectCopy(headers, values, outData);

		List<String> inHeaders = new ArrayList<>(headers.size());
		List<Pair<String, String>> inValues = new ArrayList<>(values.size());

		// Read back what was just output, verify the contents.
		try (ObjectInputStream inputStream = new ObjectInputStream(
				Files.newInputStream(outData))) {

			for (int i = 0; i < 10; i++) {
				inHeaders.add((String) inputStream.readObject());
			}

			for (int i = 0; i < 10; i++) {
				inValues.add((Pair<String, String>) inputStream.readObject());
			}
		}

		assertEquals(headers, inHeaders);
		assertEquals(values, inValues);

	}

	@Test
	void testScanner() {
		String input = "  Damon     Ryan			Allison  ";
		List<String> expected = Lists.newArrayList("Damon", "Ryan", "Allison");
		List<String> actual = IOUtilities.tokenize(input,
				Pattern.compile("\\s+"));
		assertEquals(expected, actual);
	}

	@Test
	void testFormatter() throws IOException {

		// PrintWriter implements formatting.
		try (StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw)) {
			pw.format("Hello %s", "damon");
			pw.flush();
			assertEquals("Hello damon", sw.toString());
		}

	}

	/**
	 * Java has multiple I/O methods - ranging from very simple to very complex.
	 *
	 * At the very easiest, we can read and write entire files in a single
	 * operation.
	 *
	 * At the most complex, we can stream I/O via a channel, manually lock
	 * files, and have memory-mapped I/O regions for optimal performance.
	 */
	@Test
	void fileReadingWriting() throws IOException {

		// Simple read/write operations on Files.

		// The easiest method for reading / writing bytes
		// is with the static "readAll*" methods on Files.
		Path tempPath = Files.createTempFile("file", "tmp");
		Files.write(tempPath, "hello, world!".getBytes());
		String input = new String(Files.readAllBytes(tempPath));
		assertEquals("hello, world!", input);

		//
		// Buffered / Unbuffered Streams
		//
		// Buffered readers are the next least complex reading/writing option.
		// Buffered readers save on I/O operations by holding local buffers and
		// only flush them at periodic intervals or when told.
		//
		// These are defined in the java.io package, therefore they are NIO and
		// IO compatible.
		Path copyPath = Files.createTempFile("copy", "tmp");
		try (BufferedReader reader = Files.newBufferedReader(tempPath);
				BufferedWriter writer = Files.newBufferedWriter(copyPath)) {
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
			}
		}
		assertArrayEquals(Files.readAllBytes(tempPath), Files.readAllBytes(copyPath));

		Files.delete(copyPath);

		//
		// Channels (introduced in 1.4 with NIO).
		//
		// Channel I/O reads buffers at a time.
		// Channels (FileChannel in particular) provide low level access to the
		// reading / writing process. For example, you can access files randomly
		// and create memory mapped regions for faster performance.

		try (SeekableByteChannel inChannel = Files.newByteChannel(tempPath,
				StandardOpenOption.READ);
				SeekableByteChannel outChannel = Files.newByteChannel(copyPath, //
						StandardOpenOption.CREATE_NEW, //
						StandardOpenOption.WRITE)) {

			ByteBuffer buf = ByteBuffer.allocate(2);
			while (inChannel.read(buf) > 0) {
				// Does *not* copy the underlying array!
				ByteBuffer bb = ByteBuffer.wrap(buf.array(), 0, buf.position());
				outChannel.write(bb);
				buf.rewind(); // prepares the buf for the next read.
			}
		}
		assertArrayEquals(Files.readAllBytes(tempPath), Files.readAllBytes(copyPath));
	}

	/*-
	 * Example of specifying a glob to find all files in a single directory
	 * matching a pattern.
	 *
	 * Globs have the following behavior
	 *
	 * "*"  Matches any number of characters. Does not cross directory boundaries.
	 * "**" Matches any number of characters. Crosses directory boundaries.
	 * "?"  Matches exactly 1 character.
	 * "[]" Matches a single character within a given range.
	 *      [A-Z,a-z]
	 *      [?\*] (within brackets, these characters match themselves - do not escape.
	 * "{test,test2}" Matches a collection of subpatterns. Wildcards can be used.
	 *                '{foo*, *[0-9]*}
	 */
	@Test
	void testDirectoryReading() throws IOException {

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
