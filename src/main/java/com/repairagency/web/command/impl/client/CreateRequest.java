package com.repairagency.web.command.impl.client;

import com.repairagency.web.command.PagePath;
import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.exception.DBException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 *  Command of creating Request via Client access page
 */
public class CreateRequest implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : create-request");
        String descriptionAttr = req.getParameter("description");
        logger.trace("Description : {}", descriptionAttr);
        User user = (User) req.getSession().getAttribute("user");
        try {
            String description = Validator.escapeHTMLSpecial(descriptionAttr);
            EntityManager.newRequest(description, user.getId());
            req.getSession().setAttribute("action", "request-success");
            return PagePath.HOME;
        } catch (DBException ex) {
            logger.error("Cannot create request", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }

    }

}
