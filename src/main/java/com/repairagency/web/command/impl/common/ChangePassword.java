package com.repairagency.web.command.impl.common;

import com.repairagency.util.PasswordHash;
import com.repairagency.web.command.PagePath;
import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  Command of changing password for all roles
 */
public class ChangePassword implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : change-password");
        String oldPass = req.getParameter("old-password");
        String newPass = req.getParameter("new-password");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        logger.info("User#{} changes his password", user.getId());
        oldPass = PasswordHash.getHash(oldPass);
        if (user.getPassword().equals(oldPass)) {
            try {
                Validator.validatePassword(newPass);
                newPass = PasswordHash.getHash(newPass);
                user.setPassword(newPass);
                EntityManager.updateUser(user);
                session.setAttribute("action", "password-success");
                return PagePath.HOME;
            } catch (InvalidOperationException | DBException ex) {
                logger.error("Cannot change user password", ex);
                session.setAttribute("error", ex.getPublicMessage());
            }
        } else {
            logger.error("Cannot change user password. Password confirmation failed");
            session.setAttribute("error", ErrorMessages.PASSWORD_INCORRECT);
        }
        return PagePath.SETTINGS;
    }

}
