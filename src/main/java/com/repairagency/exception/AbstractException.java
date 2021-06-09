package com.repairagency.exception;

public abstract class AbstractException extends Exception {

    private final String publicMessage;

    protected AbstractException(String message, String publicMessage) {
        super(message);
        this.publicMessage = publicMessage;
    }

    protected AbstractException(String message, String publicMessage, Throwable ex) {
        super(message, ex);
        this.publicMessage = publicMessage;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

}
