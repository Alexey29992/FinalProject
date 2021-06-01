package com.repairagency.exceptions;

public class DBException extends Exception {

    private final String publicMessage;

    public DBException(String message, String publicMessage) {
        super(message);
        this.publicMessage = publicMessage;
    }

    public DBException(String message, String publicMessage, Throwable ex) {
        super(message, ex);
        this.publicMessage = publicMessage;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

}
