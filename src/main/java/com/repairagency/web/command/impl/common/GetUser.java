package com.repairagency.web.command.impl.common;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.config.Config;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 *  Command of receiving single user info for Admin and Manager roles
 */
public class GetUser implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : get-user");
        String userLoginAttr = req.getParameter("user-login");
        String userIdAttr = req.getParameter("user-id");
        logger.trace("Given user id : {}, login : {}", userIdAttr, userLoginAttr);
        try {
            User user;
            if (userLoginAttr != null && userLoginAttr.length() != 0) {
                Validator.validateLogin(userLoginAttr);
                user = EntityManager.getUser(userLoginAttr);
            } else {
                int userId = Integer.parseInt(userIdAttr);
                user = EntityManager.getUser(userId);
            }
            if (Config.ADMIN_LOGIN.equals(user.getLogin())) {
                logger.trace("Attempt to retrieve Admin");
                req.getSession().removeAttribute("userId");
                req.getSession().setAttribute("error", ErrorMessages.NO_SUCH_USER);
            } else {
                req.setAttribute("currentUser", user);
                req.getSession().setAttribute("userId", user.getId());
            }
        } catch (DBException | InvalidOperationException ex) {
            logger.error("Cannot get user", ex);
            req.getSession().removeAttribute("userId");
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid attributes", ex);
            req.getSession().removeAttribute("userId");
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
        }
        User user = (User) req.getSession().getAttribute("user");
        return Util.getRoleDependentAddress(user.getRole(), "", PagePath.USER_INFO);
    }

}
