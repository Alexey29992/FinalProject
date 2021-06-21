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
 *  Command of removing Request via Admin access page
 */
public class RemoveRequest implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : remove-request");
        String reqIdAttr = req.getParameter("request-id");
        logger.trace("request-id : {}", reqIdAttr);
        logger.info("Admin removes Request#{}", reqIdAttr);
        try {
            int id = Integer.parseInt(reqIdAttr);
            EntityManager.removeRequest(id);
            req.getSession().setAttribute("action", "remove-request-success");
            return PagePath.MANAGER_REQUESTS;
        } catch (DBException ex) {
            logger.error("Cannot remove request", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Cannot remove request", ex);
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
        }
        return PagePath.ERROR;
    }

}
