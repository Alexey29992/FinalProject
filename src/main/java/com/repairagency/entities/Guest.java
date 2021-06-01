package com.repairagency.entities;

import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.ExceptionMessages;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Guest extends Visitor {

    private static final Logger logger = LogManager.getLogger();

    public Guest() {
        super(Role.GUEST);
    }

    public static User signUp(String login, String password)
            throws InvalidOperationException, DBException {
        logger.debug("Registering new User with login '{}'", login);
        return EntityUtils.newUser(login, password, Role.CLIENT);
    }

    public static User signIn(String login, String password)
            throws DBException, InvalidOperationException {
        logger.debug("Signing in with login '{}'", login);
        User user = EntityUtils.userGetByLogin(login);
        if (!user.getPassword().equals(password)) {
            logger.debug("Can't sign in: password not correct");
            throw new InvalidOperationException(ExceptionMessages.PASSWORD_INCORRECT);
        }
        return user;
    }

}
