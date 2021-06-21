package com.repairagency.web.command.impl.master;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
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
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 *  Command of setting Request Status via Master access page
 */
public class SetStatusMaster implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : set-status-master");
        String statusAttr = req.getParameter("status");
        logger.trace("Status : {}", statusAttr);
        String reqIdAttr = req.getParameter("request-id");
        logger.trace("Request id : {}", reqIdAttr);
        String statusStr = Util.parseStatusMaster(statusAttr);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        logger.info("Master#{} sets Status of Request#{} to {}",
                user.getId(), reqIdAttr, statusAttr);
        if (statusStr == null) {
            logger.error("Invalid status");
            session.setAttribute("error", ErrorMessages.INVALID_INPUT);
            return PagePath.MASTER_REQUEST_INFO;
        }
        try {
            int requestId = Integer.parseInt(reqIdAttr);
            Request request = EntityManager.getRequest(requestId);
            Request.Status status = Request.Status.valueOf(statusStr);
            request.setStatus(status);
            if (status.equals(Request.Status.DONE)) {
                request.setCompletionDate(LocalDateTime.now());
            }
            EntityManager.updateRequest(request);
        } catch (DBException ex) {
            logger.error("Cannot set status", ex);
            session.setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid request id", ex);
            session.setAttribute("error", ErrorMessages.UNEXPECTED);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid request", ex);
            session.setAttribute("error", ErrorMessages.UNEXPECTED);
        }
        return PagePath.MASTER_REQUEST_INFO;
    }

}
