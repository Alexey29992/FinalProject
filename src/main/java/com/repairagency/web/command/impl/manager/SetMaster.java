package com.repairagency.web.command.impl.manager;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.data.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SetMaster implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : set-master");
        String masterIdAttr = req.getParameter("master-id");
        logger.trace("Master id : {}", masterIdAttr);
        String requestIdAttr = req.getParameter("request-id");
        logger.trace("Request id : {}", requestIdAttr);
        try {
            int masterId = 0;
            if (masterIdAttr != null && !masterIdAttr.isEmpty()) {
                masterId = Integer.parseInt(masterIdAttr);
            }
            int requestId = Integer.parseInt(requestIdAttr);
            Request request = EntityManager.getRequest(requestId);
            request.setMasterId(masterId);
            EntityManager.updateRequest(request);
        } catch (DBException ex) {
            logger.error("Cannot set master", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid attributes", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid request id", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
        }
        return req.getContextPath() + PagePath.MANAGER_REQUEST_INFO;
    }

}
