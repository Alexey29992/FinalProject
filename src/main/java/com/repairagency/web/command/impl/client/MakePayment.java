package com.repairagency.web.command.impl.client;

import com.repairagency.web.command.PagePath;
import com.repairagency.bean.EntityManager;
import com.repairagency.bean.user.Client;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  Command of processing payment for the request via Client access page
 */
public class MakePayment implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : make-payment");
        String reqIdAttr =  req.getParameter("request-id");
        logger.trace("Request#{}", reqIdAttr);
        HttpSession session = req.getSession();
        Client client = (Client) session.getAttribute("user");
        logger.info("Client#{} makes payment for Request#{}", client.getId(), reqIdAttr);
        try {
            int requestId = Integer.parseInt(reqIdAttr);
            int newBalance = EntityManager.makePayment(client.getId(), requestId);
            client.setBalance(newBalance);
            req.getSession().setAttribute("action", "payment-success");
            return PagePath.HOME;
        } catch (DBException | InvalidOperationException ex) {
            logger.error("Cannot make payment", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        } catch (NumberFormatException ex) {
            logger.error("Invalid request-id", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
            return PagePath.ERROR;
        }
    }

}
