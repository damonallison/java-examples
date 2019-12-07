package com.damonallison.libraries.io;

import com.google.common.base.Preconditions;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class IOUtilities {

    /**
     * Performs a low level, byte by byte copy of {@code in} to {@code out}.
     * <p>
     * FileInputStreamByte streams are the lowest level of I/O. If you are
     * dealing with a known data format (i.e., character data), use something
     * higher level, like a character stream.
     * <p>
     * Byte streams are the basis for all other streams.
     *
     * @param in  the source file to copy from
     * @param out the destination file to copy to
     */
    public static void byteCopy(Path in, Path out) throws IOException {

        Preconditions.checkNotNull(in);
        Preconditions.checkNotNull(out);

        try (FileInputStream istream = new FileInputStream(in.toFile());
             FileOutputStream ostream = new FileOutputStream(out.toFile())) {

            int c; // holds 8 bit chars. Byte based reads are 8 bits.
            while ((c = istream.read()) != -1) {
                ostream.write(c);
            }

        }
    }

    /**
     * Performs a character by character copy using character streams.
     * <p>
     * Character streams translate the underlying Unicode characters to/from the
     * local character set.
     *
     * @param in  the source text file to copy from
     * @param out the destination text file to copy to
     */
    public static void charCopy(Path in, Path out) throws IOException {

        try (FileReader reader = new FileReader(in.toFile());
             FileWriter writer = new FileWriter(out.toFile())) {

            int c; // holds Unicode chars. Character based reads are 16 bits.
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
        }
    }

    /**
     * Performs a line by line copy using buffered streams.
     * <p>
     * Buffered streams read / write into buffers to minimize the amount of
     * actual disk I/O that must be performed.
     *
     * @param in  the source text file to copy from
     * @param out the destination text file to copy to
     * @throws IOException
     */
    public static void lineCopy(Path in, Path out) throws IOException {

        // Buffered streams wrap underlying non-buffered streams.
        //
        // There are 4 buffered stream classes used to wrap non-buffered
        // streams:
        //
        // BufferedInputStream / BufferedOutputStream - buffered byte streams.
        // BufferedReader / BufferedWriter - buffered character streams.

        try (BufferedReader reader = new BufferedReader(new FileReader(in.toFile()));
             PrintWriter writer = new PrintWriter(new FileWriter(out.toFile()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
        }
    }

    /**
     * Data streams read and write primitive types.
     * <p>
     * This will *not* write object types.
     */
    public static void dataCopy(List<String> headers, List<Integer> values,
                                Path out) throws IOException {

        Preconditions.checkNotNull(headers);
        Preconditions.checkNotNull(values);
        Preconditions.checkNotNull(out);
        Preconditions.checkArgument(headers.size() == values.size());

        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(out.toFile()))) {

            for (int i = 0; i < headers.size(); i++) {
                outputStream.writeUTF(headers.get(i));
            }

            for (int i = 0; i < values.size(); i++) {
                outputStream.writeInt(Integer.valueOf(values.get(i)));
            }

        }
    }

    public static void objectCopy(List<String> headers,
                                  List<Serializable> values, Path out) throws IOException {

        Preconditions.checkNotNull(headers);
        Preconditions.checkNotNull(values);
        Preconditions.checkNotNull(out);

        Preconditions.checkArgument(headers.size() == values.size());

        try (ObjectOutputStream outputStream = new ObjectOutputStream(
                new FileOutputStream(out.toFile()))) {

            for (int i = 0; i < headers.size(); i++) {
                outputStream.writeObject(headers.get(i));
            }

            for (int i = 0; i < values.size(); i++) {
                outputStream.writeObject(values.get(i));
            }
        }
    }

    /**
     * Tokenize breaks input into tokens using {@code regex} as the delimiter.
     */
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
