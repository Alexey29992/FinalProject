package com.repairagency.web.command.impl.master;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.data.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

public class SetStatusMaster implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : set-status-master");
        String statusAttr = req.getParameter("status");
        logger.trace("Status : {}", statusAttr);
        String requestIdAttr = req.getParameter("request-id");
        logger.trace("Request id : {}", requestIdAttr);
        String statusStr = Util.parseStatusMaster(statusAttr);
        if (statusStr == null) {
            logger.error("Invalid status");
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
            return PagePath.MASTER_REQUEST_INFO;
        }
        try {
            int requestId = Integer.parseInt(requestIdAttr);
            Request request = EntityManager.getRequest(requestId);
            Request.Status status = Request.Status.valueOf(statusStr);
            request.setStatus(status);
            if (status.equals(Request.Status.DONE)) {
                request.setCompletionDate(LocalDateTime.now());
            }
            EntityManager.updateRequest(request);
        } catch (DBException ex) {
            logger.error("Cannot set status", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid request id", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid request", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
        }
        return PagePath.MASTER_REQUEST_INFO;
    }

}
