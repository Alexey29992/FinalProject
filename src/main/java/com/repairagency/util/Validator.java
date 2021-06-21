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

    /**
     * This pattern matches any sequence of Unicode Letters (p{L}), digits and an underscore symbols ('_')
     */
    private static final Pattern allowedCharsPattern = Pattern.compile("^[\\p{L}0-9_]+$");

    private Validator() {
    }

    /**
     * Validates whether {@link Client} can process payment for the {@link Request} or not.
     * @param client Client that pays for the Request
     * @param request Request for which Client pays
     * @throws InvalidOperationException if Client can't pay for this request
     */
    public static void validatePayment(Client client, Request request)
            throws InvalidOperationException {
        logger.debug("Validating payment for Request#{}", request.getId());
        if (client.getBalance() < request.getPrice()) {
            logger.error("Negative balance is not allowed");
            throw new InvalidOperationException(ErrorMessages.USER_UNABLE_PAY_MONEY);
        }
        if (request.getStatus() != Request.Status.WAIT_FOR_PAYMENT) {
            logger.error("Unable to pay for this request");
            throw new InvalidOperationException(ErrorMessages.USER_UNABLE_PAY_STATUS);
        }
    }

    /**
     * Validates the given login string. Checks the login for allowed length and characters.
     * @param login login string to be validated
     * @throws InvalidOperationException if given login is not valid
     */
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

    /**
     * Validates the given password string. Checks the password for allowed length and characters.
     * This method should be invoked before password is hashed.
     * @param password password string to be validated
     * @throws InvalidOperationException if given password is not valid
     */
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

    /**
     * Validates given input string for restricted characters.
     * @param input string to be validated
     * @throws InvalidOperationException if given string contains illegal characters
     */
    public static void validateCharacters(String input)
            throws InvalidOperationException {
        logger.debug("Validating chars");
        Matcher matcher = allowedCharsPattern.matcher(input);
        if (!matcher.find()) {
            throw new InvalidOperationException(ErrorMessages.FORBIDDEN_INPUT_CHARS);
        }
    }

    /**
     * Replaces next HTML characters by their escape sequences:
     * <pre>{@code '&' --> '&amp'
     * '<' --> '&lt'
     * '>' --> '&gt'}</pre>
     * It is required to display them on the HTML page properly.
     * @param input string to be escaped
     * @return escaped input string
     */
    public static String escapeHTMLSpecial(String input) {
        return input.replace("&", "&amp")
                .replace("<", "&lt")
                .replace(">", "&gt");
    }
}
