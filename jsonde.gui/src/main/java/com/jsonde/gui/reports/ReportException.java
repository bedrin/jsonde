package com.jsonde.gui.reports;

public class ReportException extends Exception {

    public ReportException() {
    }

    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportException(Throwable cause) {
        super(cause);
    }
    
}
