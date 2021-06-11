package com.repairagency.web.command.impl;

import com.repairagency.web.command.PagePath;
import com.repairagency.entity.EntityManager;
import com.repairagency.entity.User;
import com.repairagency.exception.DBException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
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
            description = Validator.escapeHTMLSpecial(description);
            EntityManager.newRequest(description, user.getId());
            req.getSession().setAttribute("action", "request-success");
            return req.getContextPath() + PagePath.HOME;
        } catch (DBException ex) {
            logger.error("Cannot create request", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return req.getContextPath() + PagePath.ERROR;
        }

    }

}