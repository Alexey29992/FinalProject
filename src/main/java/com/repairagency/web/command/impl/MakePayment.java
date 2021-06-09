package com.repairagency.web.command.impl;

import com.repairagency.PagePath;
import com.repairagency.entity.EntityManager;
import com.repairagency.entity.bean.Request;
import com.repairagency.entity.user.Client;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MakePayment implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : make-payment");
        String idAttr =  req.getParameter("request-id");
        HttpSession session = req.getSession();
        Client client = (Client) session.getAttribute("user");
        logger.trace(idAttr);
        try {
            int id = Integer.parseInt(idAttr);

            Request request = EntityManager.getRequest(id);
            if (request.getStatus() != Request.Status.WAIT_FOR_PAYMENT) {
                req.getSession().setAttribute("error", ErrorMessages.USER_UNABLE_PAY_STATUS);
                return PagePath.ERROR;
            }
            EntityManager.makePayment(client.getId(), request);
            EntityManager.updateRequest(request);
        } catch (DBException ex) {
            logger.error("Cannot set feedback", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        } catch (NumberFormatException ex) {
            logger.error("Invalid request-id", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
            return PagePath.ERROR;
        } catch (InvalidOperationException ex) {
            logger.error("Not enough money to make payment", ex);
            req.getSession().setAttribute("error", ErrorMessages.USER_UNABLE_PAY_MONEY);
            return PagePath.ERROR;
        }
        req.getSession().setAttribute("action", "feedback-success");
        return PagePath.HOME;
    }

}
