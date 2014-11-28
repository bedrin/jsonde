package com.jsonde.instrumentation;

public class SkipClassException extends RuntimeException {

    public SkipClassException() {
    }

    public SkipClassException(String message) {
        super(message);
    }

    public SkipClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public SkipClassException(Throwable cause) {
        super(cause);
    }
}
