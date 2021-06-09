package com.repairagency.utils;

import com.repairagency.config.Config;
import com.repairagency.entities.beans.Request;
import com.repairagency.exceptions.ErrorMessages;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {

    private static final Logger logger = LogManager.getLogger();

    private Validator() {
    }

    public static void validatePayment(int balance, int price, Request request)
            throws InvalidOperationException {
        logger.debug("Validating payment for Request#{}", request.getId());
        if (balance < price) {
            logger.error("Negative balance is not allowed");
            throw new InvalidOperationException(ErrorMessages.PAYMENT_NOT_ENOUGH_MONEY);
        }
        if (request.getStatus() != Request.Status.WAIT_FOR_PAYMENT) {
            logger.error("Unable to pay for this request");
            throw new InvalidOperationException(ErrorMessages.PAYMENT_INVALID);
        }
    }

    // some character restrictions should be added
    public static void validateLogin(String login)
            throws InvalidOperationException {
        logger.debug("Validating Login {}", login);
        if (login == null || login.length() < Config.LOGIN_LENGTH_MIN
                || login.length() > Config.LOGIN_LENGTH_MAX) {
            logger.error("Login length is invalid");
            throw new InvalidOperationException(ErrorMessages.USER_LOGIN_INVALID_LENGTH);
        }
    }

    // some character restrictions should be added
    public static void validatePassword(String password)
            throws InvalidOperationException {
        logger.debug("Validating password");
        if (password == null || password.length() < Config.PASSWORD_LENGTH_MIN
                || password.length() > Config.PASSWORD_LENGTH_MAX) {
            logger.error("Password length is invalid");
            throw new InvalidOperationException(ErrorMessages.USER_PASSWORD_INVALID_LENGTH);
        }
    }



}
