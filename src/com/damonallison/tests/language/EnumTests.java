package com.damonallison.tests.language;

import com.damonallison.utils.DaysOfTheWeek;
import com.google.common.base.Preconditions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by dallison on 8/8/15.
 */
public class EnumTests {
    /**
     * Enums in java are more like classes - they can expose methods.
     */
    @Test
    public void testEnums() {

        // Enums can have methods.
        assertTrue(DaysOfTheWeek.SATURDAY.isWeekend());
        // Enums extend from java.lang.Enum and therefore
        // inherit all the properties from that structure.
        for (DaysOfTheWeek d : DaysOfTheWeek.values()) {

            // Creating an enum value from a string.
            DaysOfTheWeek d2 = DaysOfTheWeek.valueOf(d.name());
            assertTrue(d == d2);
            assertEquals(d, d2);
        }
    }
}
