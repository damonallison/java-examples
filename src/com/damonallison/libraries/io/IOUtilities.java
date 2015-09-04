package com.damonallison.libraries.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class IOUtilities {

	/**
	 * Performs a low level, byte by byte copy of {@code in} to {@code out}.
	 *
	 * Byte streams are the lowest level of I/O. If you are dealing with a known
	 * data format (i.e., character data), use something higher level, like a
	 * character stream.
	 *
	 *
	 * @param in
	 *            the source file to copy from
	 * @param out
	 *            the destination file to copy to
	 */
	public static void byteCopy(File in, File out) throws IOException {
		try (FileInputStream istream = new FileInputStream(in);
				FileOutputStream ostream = new FileOutputStream(out)) {
			int c; // holds 8 bit chars. Byte based reads are 8 bits.
			while ((c = istream.read()) != -1) {
				ostream.write(c);
			}
		}
	}

	/**
	 * Performs a character by character copy using character streams.
	 *
	 * @param in
	 *            the source text file to copy from
	 * @param out
	 *            the destination text file to copy to
	 */
	public static void charCopy(File in, File out) throws IOException {

		try (FileReader reader = new FileReader(in);
				FileWriter writer = new FileWriter(out)) {

			int c; // holds 16 bit chars. Character based reads are 16 bits.
			while ((c = reader.read()) != -1) {
				writer.write(c);
			}
		}
	}

	/**
	 * Performs a line by line copy using buffered streams.
	 *
	 * Buffered streams read / write into buffers to minimize the amount of
	 * actual disk I/O that must be performed.
	 *
	 * @param in
	 *            the source text file to copy from
	 * @param out
	 *            the destination text file to copy to
	 * @throws IOException
	 */
	public static void lineCopy(File in, File out) throws IOException {

		// Buffered streams wrap underlying non-buffered streams.
		//
		// There are 4 buffered stream classes used to wrap non-buffered
		// streams:
		//
		// BufferedInputStream / BufferedOutputStream - buffered byte streams.
		// BufferedReader / BufferedWriter - buffered character streams.

		try (BufferedReader reader = new BufferedReader(new FileReader(in));
				PrintWriter writer = new PrintWriter(new FileWriter(out))) {
			String line;
			while ((line = reader.readLine()) != null) {
				writer.println(line);
			}
		}
	}

	public static List<String> tokenize(String input, Pattern regex) {
		List<String> lst = new ArrayList<>();
		try (Scanner s = new Scanner(input)) {
			s.useDelimiter(regex);
			while (s.hasNext()) {
				lst.add(s.next());
			}
		}
		return lst;
	}

}
