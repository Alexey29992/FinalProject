package com.repairagency.exception;

/**
 * Exception class that is used for throwing from database layer.
 * Instances of this exceptions contains public message that intended
 * to be present as information about error for users
 */

public class DBException extends AbstractException {

    public DBException(String message, String publicMessage) {
        super(message, publicMessage);
    }

    public DBException(String message, String publicMessage, Throwable ex) {
        super(message, publicMessage, ex);
    }

}
