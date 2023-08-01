package com.innowise.enricherapi.exception;

public class ParserException extends RuntimeException {

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }
}