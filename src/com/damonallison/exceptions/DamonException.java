package com.damonallison.exceptions;

/**
 * This is an example of a custom exception. You write your own exception when:
 *
 * <ul>
 * <li>You want to differentiate your exception from other system exceptions.
 * <li>Simplifies your API by exposing a common set of exceptions to callers
 * which wrap underlying java exceptions into higher level exceptions.
 *
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
