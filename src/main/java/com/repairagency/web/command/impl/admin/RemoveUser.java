package com.repairagency.web.command.impl.admin;

import com.repairagency.bean.EntityManager;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 *  Command of removing User via Admin access page
 */
public class RemoveUser implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : remove-user");
        String userIdAttr = req.getParameter("user-id");
        logger.trace("user-id : {}", userIdAttr);
        try {
            int id = Integer.parseInt(userIdAttr);
            EntityManager.removeUser(id);
            req.getSession().setAttribute("action", "remove-user-success");
            return PagePath.ADMIN_USERS;
        } catch (DBException ex) {
            logger.error("Cannot remove user", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Cannot remove user", ex);
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
        }
        return PagePath.ERROR;
    }

}
