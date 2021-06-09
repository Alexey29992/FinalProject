package com.repairagency.util;

import com.repairagency.config.Config;
import com.repairagency.entity.bean.Request;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final Logger logger = LogManager.getLogger();

    private static final Pattern allowedCharsPattern = Pattern.compile("^[\\p{L}0-9-_]+$");

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
        validateCharacters(login);
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
        validateCharacters(password);
        logger.debug("Validating password");
        if (password == null || password.length() < Config.PASSWORD_LENGTH_MIN
                || password.length() > Config.PASSWORD_LENGTH_MAX) {
            logger.error("Password length is invalid");
            throw new InvalidOperationException(ErrorMessages.USER_PASSWORD_INVALID_LENGTH);
        }
    }

    public static void validateCharacters(String input)
            throws InvalidOperationException {
        Matcher matcher = allowedCharsPattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidOperationException(ErrorMessages.USER_FORBIDDEN_INPUT_CHARS);
        }
    }

    public static String escapeHTMLSpecial(String input) {
        return input.replace("&", "&amp")
                .replace("<", "&lt")
                .replace(">", "&gt");
    }
}
