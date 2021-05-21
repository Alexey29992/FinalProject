package com.my.entities.users;

import com.my.constants.Configuration;
import com.my.exceptions.ExceptionMessages;
import com.my.exceptions.InvalidOperationException;
import com.my.utils.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class User extends Visitor {

    private static final Logger logger = LogManager.getLogger(User.class.getName());

    private int id;
    private final String login;
    private String password;

    protected User(String login, String password, Role role)
            throws InvalidOperationException {
        super(role);
        validateLogin(login);
        this.login = login;
        validatePassword(password);
        this.password = password;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
            throws InvalidOperationException {
        validatePassword(password);
        this.password = password;
    }

    public void signOut() {
        // should be realized (deleting Cookies)
    }

    public void submitChanges() {
        UserManager.updateUser(this);
    }

    // some character restrictions should be added
    private static void validateLogin(String login)
            throws InvalidOperationException {
        if (login == null) {
            logger.fatal("Login is null");
            throw new IllegalArgumentException();
        }
        if (login.length() < Configuration.LOGIN_LENGTH_MIN
                || login.length() > Configuration.LOGIN_LENGTH_MAX) {
            logger.fatal("Login length is invalid");
            throw new InvalidOperationException(ExceptionMessages.INVALID_LOGIN_LENGTH);
        }
    }

    // some character restrictions should be added
    private static void validatePassword(String password)
            throws InvalidOperationException {
        if (password == null) {
            logger.fatal("Password is null");
            throw new IllegalArgumentException();
        }
        if (password.length() < Configuration.PASSWORD_LENGTH_MIN
                || password.length() > Configuration.PASSWORD_LENGTH_MAX) {
            logger.fatal("Password length is invalid");
            throw new InvalidOperationException(ExceptionMessages.INVALID_PASSWORD_LENGTH);
        }
    }

}
