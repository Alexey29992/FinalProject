package com.repairagency.web.command.impl.manager;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.bean.data.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Command of setting price of Request via Manager and Admin access page
 */
public class SetPrice implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : set-price");
        String priceAttr = req.getParameter("price");
        logger.trace("Price : {}", priceAttr);
        String reqIdAttr = req.getParameter("request-id");
        logger.trace("Request id : {}", reqIdAttr);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        logger.info("Manager#{} sets price of Request#{} to {}",
                user.getId(), reqIdAttr, priceAttr);
        try {
            int requestId = Integer.parseInt(reqIdAttr);
            Request request = EntityManager.getRequest(requestId);
            int price = Integer.parseInt(priceAttr);
            if (price <= 0) {
                session.setAttribute("error", ErrorMessages.LOWER_THAN_NULL);
                return PagePath.MANAGER_REQUEST_INFO;
            }
            request.setPrice(price);
            EntityManager.updateRequest(request);
        } catch (DBException ex) {
            logger.error("Cannot set price", ex);
            session.setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid price", ex);
            session.setAttribute("error", ErrorMessages.INVALID_INPUT);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid request", ex);
            session.setAttribute("error", ErrorMessages.UNEXPECTED);
        }
        return PagePath.MANAGER_REQUEST_INFO;
    }

}
