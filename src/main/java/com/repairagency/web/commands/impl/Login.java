package com.repairagency.web.commands.impl;

import com.repairagency.PagePath;
import com.repairagency.entities.EntityManager;
import com.repairagency.entities.User;
import com.repairagency.exceptions.ErrorMessages;
import com.repairagency.utils.Validator;
import com.repairagency.web.commands.Command;
import com.repairagency.exceptions.DBException;
import com.repairagency.exceptions.InvalidOperationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : login");
        HttpSession session = req.getSession();
        String type = req.getParameter("type");
        logger.trace("Type : {}", type);
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user = null;
        try {
            Validator.validateLogin(login);
            login = Validator.escapeHTMLSpecial(login);
            Validator.validatePassword(password);
            if ("sign-up".equals(type)) {
                logger.trace("Attempt to sign up");
                user = EntityManager.signUp(login, password);
            }
            if ("sign-in".equals(type)) {
                logger.trace("Attempt to sign in");
                user = EntityManager.signIn(login, password);
            }
            if (user == null) {
                logger.trace("Unable to log in");
                session.setAttribute("error", ErrorMessages.UNEXPECTED);
                return PagePath.ERROR;
            }
            logger.trace("Logged as (role) : {}", user.getRole());
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
