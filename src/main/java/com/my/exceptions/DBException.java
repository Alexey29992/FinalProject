package com.my.exceptions;

public class DBException extends Exception {

    public DBException(String message, Throwable ex) {
        super(message, ex);
    }

}
