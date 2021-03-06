package com.damonallison.libraries.stdlib;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class StringsTests {

    @Test
    void testCharacters() {
        char[] name = {'d', 'a', 'm', 'o', 'n'};
        assertTrue(Character.isLetter('d'));

        name[0] = Character.toUpperCase(name[0]);
        assertEquals("Damon", String.valueOf(name));
    }

    @Test
    void testStringParsing() {

        String name = " damon ; allison ";

        // Trim all names in string
        String[] parts = Lists.newArrayList(name.split(";")).stream().map(s -> s.trim()).toArray(size -> new String[size]);

        assertEquals("damon", parts[0]);
        assertEquals("allison", parts[1]);

        // Simple regex matching
        assertTrue(name.matches(".*damon.*"));
    }

    /**
     * {@link StringBuffer} and {@link StringBuilder} have identical interfaces.
     * <p>
     * {@link StringBuffer} is thread safe (all it's methods are synchronized).
     * {@link StringBuilder} is *not* thread safe (probably performs better).
     */
    @Test
    void testStringBuffer() {

        StringBuffer sb = new StringBuffer();
        sb.append("nomad");
        sb.replace(4, 5, "D");
        sb.reverse();
        assertEquals("Damon", sb.toString());
    }

    @Test
    void testFindInitials() {
        String s = "Damon R. Allison";
        String[] parts = s.split("\\s+");

        // There *has* to be a way to do this stateless, without having this variable.
        StringBuilder initials = new StringBuilder();
        Arrays.stream(parts).map(name -> name.substring(0, 1)).forEach(initial -> initials.append(initial));
        assertEquals("DRA", initials.toString());
    }

    @Test
    void testAnagrams() {
        assertTrue(areAnagrams("damon", "nomad"));    // happy path
        assertTrue(areAnagrams("  Damon ", "dmaon")); // whitespace and casing
        assertFalse(areAnagrams("ddamon", "damon"));  // different strings
    }

    boolean areAnagrams(String s1, String s2) {
        // Strip all whitespace
        s1 = s1.replaceAll("\\s*", "");
        s2 = s2.replaceAll("\\s*", "");

        char[] s1Chars = s1.toLowerCase().toCharArray();
        char[] s2Chars = s2.toLowerCase().toCharArray();

        Arrays.sort(s1Chars);
        Arrays.sort(s2Chars);

        return Arrays.equals(s1Chars, s2Chars);
    }
}
