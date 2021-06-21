package com.repairagency.exception;

/**
 * Base {@link Exception} wrapper class that holds public message string.
 */
public abstract class AbstractException extends Exception {

    private final String publicMessage;

    /**
     * Creates new exception with given default and public messages. Init cause is ignored
     * @param message default message of Throwable
     * @param publicMessage message that intended to be shown to user
     */
    protected AbstractException(String message, String publicMessage) {
        super(message);
        this.publicMessage = publicMessage;
    }

    /**
     * Creates new exception with given default and public messages and init cause.
     * @param message default message of Throwable
     * @param publicMessage message that intended to be shown to user
     * @param cause exception that was the cause of this one
     */
    protected AbstractException(String message, String publicMessage, Throwable cause) {
        super(message, cause);
        this.publicMessage = publicMessage;
    }

    public String getPublicMessage() {
        return publicMessage;
    }

}
