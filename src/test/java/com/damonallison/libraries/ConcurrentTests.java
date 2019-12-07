package com.damonallison.libraries;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Java has two basic synchronization idioms:
 *
 * * Synchronized methods
 * * Synchronized statements
 *
 * Every object has an intrinsic lock. When a synchronized method is called, it acquires the intrinsic lock
 * for that method's object.
 *
 * When a static synchronized method is invoked, the thread acquires the intrinsic lock for the `Class` object
 * associated with the class.
 *
 * Locks are reentrant. Reentrant locks allow the thread to enter multiple synchronized blocks which require
 * the same lock.
 */
public class ConcurrentTests {

    private static final Logger LOGGER = Logger.getLogger(ConcurrentTests.class.getCanonicalName());

    private final class Counter {
        /**
         * {@code volatile} provides atomic reads / writes.
         *
         * Reads and writes are atomic by default for reference types and most primitive types (except for long / double).
         * {@code volatile)} makes reads / writes atomic for *all* types, including long / double.
         *
         * {@code volatile} is not necessary here since all access to {@code counter} is synchronized, but
         * it won't hurt anything to have it here for explanation purposes.
         */
        private volatile long counter = 0;

        public synchronized void increment(String caller) {
            LOGGER.info(String.format("increment: %s", caller));
            counter++;
        }
        public synchronized void decrement(String caller) {
            LOGGER.info(String.format("decrement: %s", caller));
            counter--;
        }
        public synchronized long value() {
            return counter;
        }
    }

    private final Counter c = new Counter();

    private final class CounterRunnable implements Runnable {
        private final String name;
        public CounterRunnable(String name) {
            Preconditions.checkNotNull(name);
            try {
                Thread.sleep(100); // Throw some delay in here to test concurrency
            } catch (InterruptedException ex) {
                fail("should have completed");
            }
            this.name = name;
        }
        @Override
        public void run() {
            c.increment(this.name);
        }
    }
    @Test
    public void testSynchronizedCounter() {
        Thread t = new Thread(new CounterRunnable("one"));
        t.run();
        try {
            t.join(1000);
        } catch (InterruptedException ex) {
            fail("should have completed");
        }
        assertFalse(t.isAlive());

    }
}
