package com.repairagency.web.command.impl.client;

import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.PagePath;
import com.repairagency.bean.EntityManager;
import com.repairagency.bean.data.Request;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Feedback implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : feedback");
        String idAttr =  req.getParameter("request-id");
        logger.trace(idAttr);
        try {
            int id = Integer.parseInt(idAttr);
            String feedback = req.getParameter("feedback");
            feedback = Validator.escapeHTMLSpecial(feedback);
            Request request = EntityManager.getRequest(id);
            if (request.getStatus() != Request.Status.DONE && request.getUserReview() == null) {
                req.getSession().setAttribute("error", ErrorMessages.USER_UNABLE_FEEDBACK);
                return req.getContextPath() + PagePath.ERROR;
            }
            request.setUserReview(feedback);
            EntityManager.updateRequest(request);
            req.getSession().setAttribute("action", "feedback-success");
            return req.getContextPath() + PagePath.HOME;
        } catch (DBException ex) {
            logger.error("Cannot set feedback", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return req.getContextPath() + PagePath.ERROR;
        } catch (NumberFormatException | InvalidOperationException ex) {
            logger.error("Invalid request-id", ex);
            req.getSession().setAttribute("error", ErrorMessages.UNEXPECTED);
            return req.getContextPath() + PagePath.ERROR;
        }
    }

}
