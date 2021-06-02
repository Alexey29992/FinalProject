package com.repairagency.exceptions;

public class AbstractException extends Exception {

    private final String publicMessage;

    public AbstractException(String message, String publicMessage) {
        super(message);
        this.publicMessage = publicMessage;
    }

    public AbstractException(String message, String publicMessage, Throwable ex) {
        super(message, ex);
        this.publicMessage = publicMessage;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

}
