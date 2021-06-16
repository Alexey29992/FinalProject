package com.repairagency.web.command.impl.common;

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
import javax.servlet.http.HttpServletResponse;


public class GetRequest implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-request-edit");
        String requestIdAttr = req.getParameter("request-id");
        logger.trace("Request id : {}", requestIdAttr);
        try {
            int requestId = Integer.parseInt(requestIdAttr);
            Request request = EntityManager.getRequest(requestId);
            req.setAttribute("currentRequest", request);
            req.getSession().setAttribute("requestId", request.getId());
        } catch (DBException | InvalidOperationException ex) {
            logger.error("Cannot get request", ex);
            req.getSession().removeAttribute("requestId");
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid id attribute", ex);
            req.getSession().removeAttribute("requestId");
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
        }
        User user = (User) req.getSession().getAttribute("user");
        return Util.getRoleDependentAddress(user.getRole(), "", PagePath.REQUEST_INFO);
    }

}
