package com.damonallison.language.tests;

import com.google.common.base.Preconditions;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by dallison on 8/8/15.
 */
public class SwitchTests {
    @Test
    public void testSwitch() {
        /**
         * Java 1.7 allows you to switch on {@code String} values.
         *
         * {@code break} is necessary because java will fall through.
         */

        String s = "damon";
        // You need to always, manually check for null prior to using
        // a string in the switch statement to prevent a NullPointerException.
        Preconditions.checkNotNull(s);

        switch (s.toLowerCase().trim()) {
            case "damon":
            case "allison":
                break;
            case "other":
                fail();
            default:
                fail();
        }

        // Switch works with primitive types, enum types, String, and a few
        // classes that wrap primitive types (Character, Byte, Short, Integer)
        byte x = 0x3;
        switch (x) {
            case 3:
                break;
            default:
                fail();
        }
    }

    /**
     * If you use an object, it can't be null!
     */
    @Test(expected = NullPointerException.class)
    public void testNullSwitch() {
        String s = null;
        switch (s) {
            default:
                fail();
        }
    }

}
