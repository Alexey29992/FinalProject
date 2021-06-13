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

public class SetPrice implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : set-price");
        String priceAttr = req.getParameter("price");
        logger.trace("Price : {}", priceAttr);
        String requestIdAttr = req.getParameter("request-id");
        logger.trace("Request id : {}", requestIdAttr);
        try {
            int requestId = Integer.parseInt(requestIdAttr);
            Request request = EntityManager.getRequest(requestId);
            int price = Integer.parseInt(priceAttr);
            request.setPrice(price);
            EntityManager.updateRequest(request);
        } catch (DBException ex) {
            logger.error("Cannot set price", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid price", ex);
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid request", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
        }
        return req.getContextPath() + PagePath.MANAGER_REQUEST_INFO;
    }

}
