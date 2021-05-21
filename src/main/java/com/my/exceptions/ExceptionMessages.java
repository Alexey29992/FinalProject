package com.my.exceptions;

import com.my.constants.Configuration;

public class ExceptionMessages {

    private ExceptionMessages() {
    }

    public static final String NOT_ENOUGH_MONEY = "Not enough money to proceed payment";
    public static final String INVALID_DESCRIPTION = "Entered description is not correct";
    public static final String INCORRECT_PASSWORD = "Entered password is not correct";
    public static final String INVALID_LOGIN_LENGTH = "Invalid login. Login should contain from "
            + Configuration.LOGIN_LENGTH_MIN + " to " + Configuration.LOGIN_LENGTH_MAX + " characters";
    public static final String INVALID_PASSWORD_LENGTH = "Invalid password. Password should contain from "
            + Configuration.PASSWORD_LENGTH_MIN + " to " + Configuration.PASSWORD_LENGTH_MAX + " characters";
    public static final String ADMIN_CANNOT_ADD_USER_ROLE = "User with requested role cannot be added";

}
