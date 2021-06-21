package com.repairagency.web.command.impl.manager;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Command of replenishment Client's balance via Manager and Admin access page
 */
public class TopUpBalance implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : top-up-balance");
        String amountAttr =  req.getParameter("amount");
        logger.trace("Amount : {}", amountAttr);
        String clientIdAttr = req.getParameter("client-id");
        logger.trace("Client#{}", clientIdAttr);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        logger.info("Manager#{} replenishes Client#{} balance on {}",
                user.getId(), clientIdAttr, amountAttr);
        try {
            int clientId = Integer.parseInt(clientIdAttr);
            int amount = Integer.parseInt(amountAttr);
            if (amount <= 0) {
                session.setAttribute("error", ErrorMessages.LOWER_THAN_NULL);
                return PagePath.MANAGER_USER_INFO;
            }
            int newBalance = EntityManager.topUpClientBalance(clientId, amount);
            Map<Integer, Integer> updates = (Map<Integer, Integer>)
                    req.getServletContext().getAttribute("user-balance-updates");
            updates.put(clientId, newBalance);
        } catch (DBException ex) {
            logger.error("Cannot top up balance", ex);
            session.setAttribute("error", ex.getPublicMessage());
        } catch (NumberFormatException ex) {
            logger.error("Invalid money amount", ex);
            session.setAttribute("error", ErrorMessages.INVALID_INPUT);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid client id", ex);
            session.setAttribute("error", ErrorMessages.UNEXPECTED);
        }
        return PagePath.MANAGER_USER_INFO;
    }

}
