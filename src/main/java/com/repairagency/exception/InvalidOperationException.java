package com.repairagency.exception;

/**
 * Exception class that is used for throwing when user input
 * is not valid or queried operation can't be processed.
 * Instances of this exceptions contains public message that intended
 * to be present as information about error for users
 */

public class InvalidOperationException extends AbstractException {

    public InvalidOperationException(String message) {
        this(message, message);
    }

    public InvalidOperationException(String message, String publicMessage) {
        super(message, publicMessage);
    }

}
