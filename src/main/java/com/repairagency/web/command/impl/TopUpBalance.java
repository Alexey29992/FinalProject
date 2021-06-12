package com.repairagency.web.command.impl;

import com.repairagency.bean.EntityManager;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class TopUpBalance implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : top-up-balance");
        String amountAttr =  req.getParameter("amount");
        logger.trace("Amount : {}", amountAttr);
        String idAttr = req.getParameter("client-id");
        logger.trace("Client#{}", idAttr);
        ServletContext appContext = req.getServletContext();
        Map<Integer, Integer> updates = (Map<Integer, Integer>) appContext.getAttribute("user-balance-updates");
        int clientId;
        try {
             clientId = Integer.parseInt(idAttr);
        } catch (NumberFormatException ex) {
            logger.error("Invalid attributes", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
            return req.getContextPath() + PagePath.ERROR;
        }
        try {
            int amount = Integer.parseInt(amountAttr);
            int newBalance = EntityManager.topUpClientBalance(clientId, amount);
            updates.put(clientId, newBalance);
            req.getSession().setAttribute("action", "top-up-success");
            return req.getContextPath() + PagePath.HOME;
        } catch (DBException ex) {
            logger.error("Cannot top up balance", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return req.getContextPath() + PagePath.ERROR;
        } catch (NumberFormatException ex) {
            logger.error("Invalid money amount", ex);
            req.getSession().setAttribute("error", ErrorMessages.MANAGER_INVALID_TOP_UP);
            return req.getContextPath() + PagePath.ERROR;
        } catch (InvalidOperationException ex) {
            logger.error("Invalid client id", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
            return req.getContextPath() + PagePath.ERROR;
        }
    }

}
