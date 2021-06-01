package com.repairagency.exceptions;

public class DBException extends AbstractException {

    public DBException(String message, String publicMessage) {
        super(message, publicMessage);
    }

    public DBException(String message, String publicMessage, Throwable ex) {
        super(message, publicMessage, ex);
    }

}
