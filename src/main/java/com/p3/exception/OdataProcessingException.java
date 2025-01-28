package com.p3.exception;

public class OdataProcessingException extends RuntimeException {
    public OdataProcessingException(String message) {
        super(message);
    }

    public OdataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
