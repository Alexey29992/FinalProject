package com.repairagency.web.tag;

import com.repairagency.bean.User;

public class ActionProcessorMessages {

    private ActionProcessorMessages() {
    }

    public static final String REQUEST_SUCCESS = "Request successfully created. " +
            "Our managers contact you as soon as possible.";
    public static final String PASSWORD_SUCCESS = "Your password was successfully updated";
    public static final String SETTINGS_CLIENT_SUCCESS = "Your phone number was successfully updated";

    public static final String SIGN_UP = "You have been successfully registered as a new client of our site.";
    public static final String SIGN_IN = "You have been successfully entered as a %s.";
    public static final String FEEDBACK_SUCCESS = "Thanks for your feedback!";
    public static final String PAYMENT_SUCCESS = "Payment successfully received.";
    public static final String CREATE_USER_SUCCESS = "User successfully created";
    public static final String REMOVE_USER_SUCCESS = "User successfully removed";

    public static String defaultHome(User user) {
        String message = "Hello, Guest! We are happy to see you on out site. " +
                "You can place repair request here. " +
                "Our manager will contact you shortly. " +
                "To proceed, you have to register first.";
        User.Role role;
        if (user != null) {
            role = user.getRole();
            switch (role) {
                case CLIENT:
                    message = "Hello, dear Client! You can create new request or browse history here";
                    break;
                case MANAGER:
                    message = "Hello, Manager!";
                    break;
                case MASTER:
                    message = "Hello, Master!";
                    break;
                case ADMIN:
                    message = "Hello, Admin!";
                    break;
            }
        }
        return message;
    }
}
