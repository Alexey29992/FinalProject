package com.repairagency.exception;

/**
 * Exception class that is used for throwing from database layer.
 * Instances of this exceptions contains public message that intended
 * to be present as information about error for users
 */
public class DBException extends AbstractException {

    /**
     * Creates new exception with given default and public messages. Init cause is ignored
     * @param message default message of Throwable
     * @param publicMessage message that intended to be shown to user
     */
    public DBException(String message, String publicMessage) {
        super(message, publicMessage);
    }

    /**
     * Creates new exception with given default and public messages and init cause.
     * @param message default message of Throwable
     * @param publicMessage message that intended to be shown to user
     * @param cause exception that was the cause of this one
     */
    public DBException(String message, String publicMessage, Throwable cause) {
        super(message, publicMessage, cause);
    }

}
