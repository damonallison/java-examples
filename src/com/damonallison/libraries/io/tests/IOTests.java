package com.damonallison.libraries.io.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

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
