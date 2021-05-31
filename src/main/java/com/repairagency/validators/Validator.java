package com.repairagency.validators;

import com.repairagency.config.Config;
import com.repairagency.entities.items.Request;
import com.repairagency.exceptions.ExceptionMessages;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {

    private static final Logger logger = LogManager.getLogger();

    private Validator() {
    }

    public static void validatePayment(int balance, int price, Request request) throws InvalidOperationException {
        if (balance < price) {
            logger.error("Negative balance is not allowed");
            throw new InvalidOperationException(ExceptionMessages.PAYMENT_NOT_ENOUGH_MONEY);
        }
        if (request.getStatus() != Request.Status.WAIT_FOR_PAYMENT) {
            logger.error("Unable to pay for this request");
            throw new InvalidOperationException(ExceptionMessages.PAYMENT_INVALID);
        }
    }

    // some character restrictions should be added
    public static void validateLogin(String login) throws InvalidOperationException {
        if (login == null) {
            logger.error("Login is null");
            throw new InvalidOperationException(ExceptionMessages.LOGIN_INVALID);
        }
        if (login.length() < Config.LOGIN_LENGTH_MIN
                || login.length() > Config.LOGIN_LENGTH_MAX) {
            logger.error("Login length is invalid");
            throw new InvalidOperationException(ExceptionMessages.LOGIN_INVALID_LENGTH);
        }
    }

    // some character restrictions should be added
    public static void validatePassword(String password) throws InvalidOperationException {
        if (password == null) {
            logger.error("Password is null");
            throw new InvalidOperationException(ExceptionMessages.PASSWORD_INVALID);
        }
        if (password.length() < Config.PASSWORD_LENGTH_MIN
                || password.length() > Config.PASSWORD_LENGTH_MAX) {
            logger.error("Password length is invalid");
            throw new InvalidOperationException(ExceptionMessages.PASSWORD_INVALID_LENGTH);
        }
    }

}
