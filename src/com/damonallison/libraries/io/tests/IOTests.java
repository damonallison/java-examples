package com.damonallison.libraries.io.tests;

import static org.junit.Assert.assertEquals;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import com.damonallison.classes.Pair;
import com.damonallison.libraries.io.IOUtilities;
import com.google.common.collect.Lists;

/**
 *
 *
 */
public class IOTests {

	@Test
	public void testFileCopy() throws IOException {

		final String contents = "hello, world";

		// TODO : look into Java 7's NIO package for a newer alternative.
		File in = File.createTempFile("infile", null);
		File outBytes = File.createTempFile("outbytes", null);
		File outText = File.createTempFile("outchars", null);
		File outLines = File.createTempFile("outline", null);

		try (FileWriter writer = new FileWriter(in)) {
			writer.write(contents);
		}

		// byte-by-byte copy
		IOUtilities.byteCopy(in, outBytes);
		String output = new String(Files.readAllBytes(Paths.get(outBytes
				.getCanonicalPath())), Charset.forName("UTF-8"));
		assertEquals(contents, output);

		// char-by-char copy
		IOUtilities.charCopy(in, outText);
		output = new String(Files.readAllBytes(Paths.get(outText
				.getCanonicalPath())), Charset.forName("UTF-8"));
		assertEquals(contents, output);

		// line copy
		IOUtilities.lineCopy(in, outLines);
		output = new String(Files.readAllBytes(Paths.get(outText
				.getCanonicalPath())), Charset.forName("UTF-8"));
		assertEquals(contents, output);

	}

	@Test
	public void testDataCopy() throws IOException {
		List<String> headers = new ArrayList<>();
		List<Integer> values = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			headers.add(String.format("header %d", i));
			values.add(Integer.valueOf(i));
		}
		File outData = File.createTempFile("outdata", null);
		IOUtilities.dataCopy(headers, values, outData);

		List<String> inHeaders = new ArrayList<>(headers.size());
		List<Integer> inValues = new ArrayList<>(values.size());

		try (DataInputStream inputStream = new DataInputStream(
				new FileInputStream(outData))) {

			for (int i = 0; i < 10; i++) {
				inHeaders.add(inputStream.readUTF());
			}

			for (int i = 0; i < 10; i++) {
				inValues.add(Integer.valueOf(inputStream.readInt()));
			}
		}

		assertEquals(headers, inHeaders);
		assertEquals(values, inValues);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testObjectCopy() throws IOException, ClassNotFoundException {
		List<String> headers = new ArrayList<>();
		List<Serializable> values = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			headers.add(String.format("header %d", i));
			values.add(new Pair<String, String>(String.format("first %d", i),
					String.format("last %d", i)));
		}
		File outData = File.createTempFile("outdata", null);
		IOUtilities.objectCopy(headers, values, outData);

		List<String> inHeaders = new ArrayList<>(headers.size());
		List<Pair<String, String>> inValues = new ArrayList<>(values.size());

		try (ObjectInputStream inputStream = new ObjectInputStream(
				new FileInputStream(outData))) {

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
	public void testScanner() {
		String input = "  Damon     Ryan			Allison  ";
		List<String> expected = Lists.newArrayList("Damon", "Ryan", "Allison");
		List<String> actual = IOUtilities.tokenize(input,
				Pattern.compile("\\s+"));
		assertEquals(expected, actual);
	}

	@Test
	public void testFormatter() throws IOException {

		// PrintWriter implements formatting.
		try (StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw)) {
			pw.format("Hello %s", "damon");
			pw.flush();
			assertEquals("Hello damon", sw.toString());
		}

	}
}
