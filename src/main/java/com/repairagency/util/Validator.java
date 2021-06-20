package com.repairagency.util;

import com.repairagency.config.Config;
import com.repairagency.bean.data.Request;
import com.repairagency.bean.user.Client;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class that holds all the methods for validation of user input
 */

public class Validator {

    private static final Logger logger = LogManager.getLogger();

    private static final Pattern allowedCharsPattern = Pattern.compile("^[\\p{L}0-9_]+$");

    private Validator() {
    }

    public static void validatePayment(Client client, Request request, int price)
            throws InvalidOperationException {
        logger.debug("Validating payment for Request#{}", request.getId());
        if (client.getBalance() < price) {
            logger.error("Negative balance is not allowed");
            throw new InvalidOperationException(ErrorMessages.USER_UNABLE_PAY_MONEY);
        }
        if (request.getStatus() != Request.Status.WAIT_FOR_PAYMENT) {
            logger.error("Unable to pay for this request");
            throw new InvalidOperationException(ErrorMessages.USER_UNABLE_PAY_STATUS);
        }
    }

    public static void validateLogin(String login)
            throws InvalidOperationException {
        logger.debug("Validating Login {}", login);
        if (login == null || login.length() < Config.LOGIN_LENGTH_MIN
                || login.length() > Config.LOGIN_LENGTH_MAX) {
            logger.error("Login length is invalid");
            throw new InvalidOperationException(ErrorMessages.LOGIN_INVALID_LENGTH);
        }
        validateCharacters(login);
    }

    public static void validatePassword(String password)
            throws InvalidOperationException {
        logger.debug("Validating password");
        if (password == null || password.length() < Config.PASSWORD_LENGTH_MIN
                || password.length() > Config.PASSWORD_LENGTH_MAX) {
            logger.error("Password length is invalid");
            throw new InvalidOperationException(ErrorMessages.PASSWORD_INVALID_LENGTH);
        }
        validateCharacters(password);
    }

    public static void validateCharacters(String input)
            throws InvalidOperationException {
        logger.debug("Validating chars");
        Matcher matcher = allowedCharsPattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidOperationException(ErrorMessages.FORBIDDEN_INPUT_CHARS);
        }
    }

    public static String escapeHTMLSpecial(String input) {
        return input.replace("&", "&amp")
                .replace("<", "&lt")
                .replace(">", "&gt");
    }
}
