package com.repairagency.exceptions;

public class InvalidOperationException extends AbstractException {

    public InvalidOperationException(String message) {
        this(message, message);
    }

    public InvalidOperationException(String message, String publicMessage) {
        super(message, publicMessage);
    }

}
