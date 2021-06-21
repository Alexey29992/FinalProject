package com.repairagency.web.command;

/**
 * Command identifier constants
 */
public class CommandNames {

    private CommandNames(){
    }

    public static final String INVALID_COMMAND = "invalid-command";

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String CHANGE_PASSWORD = "change-password";

    public static final String CREATE_REQUEST = "create-request";
    public static final String FEEDBACK = "feedback";
    public static final String CHANGE_CLIENT_SETTINGS = "change-client-settings";
    public static final String GET_CLIENT_REQUESTS = "get-client-requests";
    public static final String GET_CLIENT_PAYMENT_RECORDS = "get-payment-records";
    public static final String MAKE_PAYMENT = "make-payment";

    public static final String GET_MANAGER_REQUESTS = "get-manager-requests";
    public static final String TOP_UP_BALANCE = "top-up-balance";
    public static final String GET_USER = "get-user";
    public static final String GET_REQUEST = "get-request";
    public static final String SET_MASTER = "set-master";
    public static final String SET_PRICE = "set-price";
    public static final String SET_STATUS_MANAGER = "set-status-manager";

    public static final String GET_MASTER_REQUESTS = "get-master-requests";
    public static final String SET_STATUS_MASTER = "set-status-master";

    public static final String GET_ADMIN_USERS = "get-admin-users";
    public static final String CREATE_USER = "create-user";
    public static final String REMOVE_USER = "remove-user";
    public static final String REMOVE_REQUEST = "remove-request";

}
