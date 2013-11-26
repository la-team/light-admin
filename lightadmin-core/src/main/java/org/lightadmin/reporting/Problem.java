package org.lightadmin.reporting;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Problem {

    private final String message;

    private final Throwable rootCause;

    public Problem(final String message) {
        this(message, null);
    }

    public Problem(final String message, final Throwable rootCause) {
        this.message = message;
        this.rootCause = rootCause;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getRootCause() {
        return rootCause;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("message", message).append("rootCause", rootCause).toString();
    }
}