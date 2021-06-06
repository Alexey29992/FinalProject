package com.repairagency.web.commands.impl;

import com.repairagency.PagePath;
import com.repairagency.entities.EntityManager;
import com.repairagency.entities.User;
import com.repairagency.exceptions.DBException;
import com.repairagency.web.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateRequest implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : create-request");
        String description = req.getParameter("description");
        User user = (User) req.getSession().getAttribute("user");
        try {
            EntityManager.newRequest(description, user.getId());
        } catch (DBException ex) {
            logger.error("Cannot create request", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        req.getSession().setAttribute("action", "request-success");
        return PagePath.HOME;
    }

}
