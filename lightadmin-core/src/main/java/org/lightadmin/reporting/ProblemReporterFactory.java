package org.lightadmin.reporting;

public abstract class ProblemReporterFactory {

    public static ProblemReporter failFastReporter() {
        return new FailFastProblemReporter();
    }

    public static ProblemReporter loggingReporter() {
        return new ProblemReporterLogImpl();
    }
}