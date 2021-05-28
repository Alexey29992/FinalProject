package com.my.exceptions;

import com.my.config.Config;

public class ExceptionMessages {

    private ExceptionMessages() {
    }

    public static final String DESCRIPTION_INVALID = "Entered description is not correct";

    public static final String PASSWORD_INCORRECT = "Entered password is not correct";
    public static final String PASSWORD_INVALID = "Invalid password. Follow restrictions, please";
    public static final String PASSWORD_INVALID_LENGTH = "Invalid password. Password should contain from "
            + Config.PASSWORD_LENGTH_MIN + " to " + Config.PASSWORD_LENGTH_MAX + " characters";

    public static final String LOGIN_INVALID = "Invalid login. Follow restrictions, please";
    public static final String LOGIN_INVALID_LENGTH = "Invalid login. Login should contain from "
            + Config.LOGIN_LENGTH_MIN + " to " + Config.LOGIN_LENGTH_MAX + " characters";

    public static final String DB_UNEXPECTED = "Unexpected database error occurs";
    public static final String DB_NO_CONNECTION = "Database connection cannot be received";
    public static final String DB_INVALID_QUERY =
            "Queried entity does not exist in the database, or some other DB-errors occurred";
    public static final String DB_DAO_CLOSED = "Attempt to use closed Connection through closed DAO";
    public static final String DB_CONNECTION_NOT_CLOSED = "Connection should be closed first";
    public static final String DB_FATAL = "Database corruption detected";

    public static final String PAYMENT_INVALID = "You cannot pay for this request";
    public static final String PAYMENT_NOT_ENOUGH_MONEY = "Not enough money to proceed payment";

    public static final String USER_CREATE_INVALID_ROLE = "User with given role cannot be created";
    public static final String USER_ADD_INVALID_ROLE = "User with this role cannot be added to DB";


}
