package com.repairagency.web.command.impl;

import com.repairagency.web.command.PagePath;
import com.repairagency.entity.EntityManager;
import com.repairagency.entity.User;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangePassword implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : change-password");
        String oldPass = req.getParameter("old-password");
        String newPass = req.getParameter("new-password");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user.getPassword().equals(oldPass)) {
            try {
                Validator.validatePassword(newPass);
                user.setPassword(newPass);
                EntityManager.updateUser(user);
                session.setAttribute("action", "password-success");
                return req.getContextPath() + PagePath.HOME;
            } catch (InvalidOperationException | DBException ex) {
                logger.error("Cannot change user password", ex);
                session.setAttribute("error", ex.getPublicMessage());
                return req.getContextPath() + PagePath.CHANGE_USER_SETTINGS;
            }
        }
        logger.error("Cannot change user password. Password confirmation failed");
        session.setAttribute("error", ErrorMessages.USER_PASSWORD_INCORRECT);
        return req.getContextPath() + PagePath.CHANGE_USER_SETTINGS;
    }

}
