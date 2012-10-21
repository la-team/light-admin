package org.lightadmin.core.reporting;

public class Problem {

	private final String message;

	private final Throwable rootCause;

	public Problem( final String message ) {
		this( message, null );
	}

	public Problem( final String message, final Throwable rootCause ) {
		this.message = message;
		this.rootCause = rootCause;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getRootCause() {
		return rootCause;
	}
}