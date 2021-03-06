package com.repairagency.web.command.impl.common;

import com.repairagency.util.PasswordHash;
import com.repairagency.web.command.PagePath;
import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import com.repairagency.exception.DBException;
import com.repairagency.exception.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Command of sign in or sign up for guests
 */
public class Login implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : login");
        HttpSession session = req.getSession();
        String type = req.getParameter("type");
        logger.trace("Type : {}", type);
        String login = req.getParameter("login");
        logger.trace("login : {}", login);
        String password = req.getParameter("password");
        logger.trace("password : {}", password);
        logger.info("Registering/Entering account with login '{}'", login);
        User user = null;
        try {
            Validator.validateLogin(login);
            Validator.validatePassword(password);
            password = PasswordHash.getHash(password);
            if ("sign-up".equals(type)) {
                logger.trace("Attempt to sign up");
                user = EntityManager.newUser(login, password, User.Role.CLIENT);
            }
            if ("sign-in".equals(type)) {
                logger.trace("Attempt to sign in");
                user = EntityManager.getUser(login);
                if (!user.getPassword().equals(password)) {
                    logger.trace("Can not enter: password not correct");
                    req.getSession().setAttribute("error", ErrorMessages.PASSWORD_INCORRECT);
                    return PagePath.LOGIN;
                }
            }
            session.setAttribute("user", user);
            session.setAttribute("action", type);
            return PagePath.HOME;
        } catch (DBException | InvalidOperationException ex) {
            logger.error("Cannot log in", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.LOGIN;
        }
    }

}
