package com.damonallison.classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloneableTestTests {

    @Test
    public void testCloneable() {
        CloneableTest t = new CloneableTest("damon");
        CloneableTest t2 = (CloneableTest)t.clone();

        assertEquals(t, t2);
    }
}
