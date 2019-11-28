package com.damonallison.exceptions;

import java.io.FileNotFoundException;

/**
 * This is an example of a custom exception. You write your own exception when:
 *
 * <ul>
 * <li>You want to differentiate your exception from other system exceptions.
 * <li>To simplify your API by exposing a common set of exceptions to callers
 * which wrap underlying java exceptions into higher level exceptions.
 * <p>
 * Types of exceptions:
 * <ul>
 * <li>Checked Exceptions : exceptions a well-written application should
 * anticipate and recover from. For example, a {@link FileNotFoundException}
 * should be thrown by a function and caught by the caller.
 * <p>
 * Checked exceptions are subject to the "catch or specify" requirement. The
 * exception must be caught via a try/catch or declared as throws in the calling
 * method's declaration.
 * <p>
 * All exceptions are checked exceptions except for those which inherit from
 * {@link RuntimeException}.
 *
 * <li>Errors : Errors are exceptional and external to the application.
 * Typically, these are fatal and not caught.
 *
 * <li>
 * {@link RuntimeException} : exceptions that are internal to the application
 * that the application usually cannot anticipate or recover from. These are
 * logic errors or API misuse errors. The application can catch the exception,
 * but it probably makes more sense to eliminate the bug that caused the
 * exception to occur. For example, a {@link NullPointerException} can be
 * caught, but it probably makes more sense to eliminate the cause of the NPE.
 * <p>
 * {@link RuntimeException} is considered an avoidable programmer problem, not
 * the problem of the method where the exception was thrown. RuntimeExceptions
 * would reduce the program's clarity since they can be numerous.
 * <p>
 * <p>
 * Some programmers feel checked exceptions (and the "catch or specify"
 * requirement) is a serious flaw.
 * <p>
 * Checked exceptions were added to the language to force the callers to handle
 * the exception. In essence, the exception is part of the method's declaration.
 * <p>
 * <p>
 * Directly from Sun/Oracle:
 * <p>
 * "Here's the bottom line guideline: If a client can reasonably be expected to
 * recover from an exception, make it a checked exception. If a client cannot do
 * anything to recover from the exception, make it an unchecked exception."
 */
public class DamonException extends RuntimeException {

    private static final long serialVersionUID = -2700356863258450299L;

    private final String userId;

    public DamonException(String userId) {
        this(userId, null);
    }

    public DamonException(String userId, Throwable cause) {
        super(String.format("userId[%s] is invalid.", userId), cause);
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }
}
