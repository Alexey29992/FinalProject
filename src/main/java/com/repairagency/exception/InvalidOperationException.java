package com.repairagency.exception;

/**
 * Exception class that is used for throwing when user input
 * is not valid or queried operation can't be processed. This
 * exception intended to be thrown from if and switch clauses
 * and that's why it doesn't have constructor with cause.
 * Instances of this exceptions contains public message that intended
 * to be present as information about error for users.
 */
public class InvalidOperationException extends AbstractException {

    /**
     * Creates new exception with given default and public messages.
     * @param message default message of Throwable and public message at the same time
     */
    public InvalidOperationException(String message) {
        this(message, message);
    }

    /**
     * Creates new exception with given default and public messages.
     * @param message default message of Throwable
     * @param publicMessage message that intended to be shown to user
     */
    public InvalidOperationException(String message, String publicMessage) {
        super(message, publicMessage);
    }

}
