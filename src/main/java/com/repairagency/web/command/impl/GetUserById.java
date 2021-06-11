package com.repairagency.web.command.impl;

import com.repairagency.entity.EntityManager;
import com.repairagency.entity.User;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetUserById implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-user-by-id");
        logger.trace("Parsing HTTP parameters for Request query");
        String userIdAttr = req.getParameter("user-id");
        logger.trace("User id : {}", userIdAttr);
        try {
            int userId = Integer.parseInt(userIdAttr);
            User user = EntityManager.getUserById(userId);
            req.setAttribute("user", user);
            return PagePath.MANAGER_USER_INFO;
        } catch (DBException ex) {
            logger.error("Cannot get user", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }  catch (NumberFormatException ex) {
            logger.error("Invalid id attribute", ex);
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
            return req.getContextPath() + PagePath.ERROR;
        }
    }

}
