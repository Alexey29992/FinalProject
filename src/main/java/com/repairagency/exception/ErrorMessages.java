package com.repairagency.exception;

/**
 * Set of constants that holds keys for appropriate messages of errors in resource bundles
 */

public class ErrorMessages {

    private ErrorMessages() {
    }

    public static final String INVALID_INPUT = "error.invalid_input";
    public static final String INVALID_COMMAND = "error.invalid_command";
    public static final String UNEXPECTED = "error.unexpected";
    public static final String INSUFFICIENT_PERMISSIONS = "error.insufficient_permissions";
    public static final String URL_NOT_FOUND = "error.url_not_found";
    public static final String DB_INTERNAL = "error.db_internal";
    public static final String DB_NOT_FOUND = "error.db_not_found";

    public static final String USER_CREATE_INVALID_ROLE = "error.user_create_invalid_role";
    public static final String USER_CREATE_LOGIN_REGISTERED = "error.user_create_login_registered";
    public static final String PASSWORD_INCORRECT = "error.password_incorrect";
    public static final String LOGIN_NOT_FOUND = "error.login_not_found";
    public static final String PASSWORD_INVALID_LENGTH = "error.password_invalid_length";
    public static final String LOGIN_INVALID_LENGTH = "error.login_invalid_length";
    public static final String FORBIDDEN_INPUT_CHARS = "error.forbidden_input_chars";
    public static final String USER_UNABLE_FEEDBACK = "error.user_unable_feedback";
    public static final String USER_UNABLE_PAY_STATUS = "error.user_unable_pay_status";
    public static final String USER_UNABLE_PAY_MONEY = "error.user_unable_pay_money";
    public static final String LOWER_THAN_NULL = "error.lower_than_null";

    public static final String NO_SUCH_USER = "error.no_such_user";
    public static final String NO_SUCH_REQUEST = "error.no_such_request";

}
