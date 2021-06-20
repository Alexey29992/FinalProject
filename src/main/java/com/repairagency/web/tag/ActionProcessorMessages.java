package com.repairagency.web.tag;

import com.repairagency.bean.User;

/**
 * Messages constants
 */

public class ActionProcessorMessages {

    private ActionProcessorMessages() {
    }

    public static final String REQUEST_SUCCESS = "action.request_success";
    public static final String PASSWORD_SUCCESS = "action.password_success";
    public static final String SETTINGS_CLIENT_SUCCESS = "action.settings_client_success";
    public static final String FEEDBACK_SUCCESS = "action.feedback_success";
    public static final String PAYMENT_SUCCESS = "action.payment_success";
    public static final String CREATE_USER_SUCCESS = "action.create_user_success";
    public static final String REMOVE_USER_SUCCESS = "action.remove_user_success";
    public static final String REMOVE_REQUEST_SUCCESS = "action.remove_request_success";
    public static final String CLIENT_PHONE_REQUIRED = "action.client_phone_required";
    public static final String SIGN_UP = "action.sign_up";
    public static final String SIGN_IN = "action.sign_in";

    public static String defaultHome(User user) {
        if (user == null) {
            return "action.default_home.guest";
        }
        if (user.getRole().equals(User.Role.CLIENT)) {
            return "action.default_home.client";
        }
        return null;
    }
}
