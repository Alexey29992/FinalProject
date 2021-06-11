package com.repairagency.web.command.impl;

import com.repairagency.web.command.PagePath;
import com.repairagency.entity.EntityManager;
import com.repairagency.entity.user.Client;
import com.repairagency.exception.DBException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeClientSettings implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : change-client-settings");
        String phNumber = req.getParameter("ph-number");
        HttpSession session = req.getSession();
        Client client = (Client) session.getAttribute("user");
        try {
            Validator.escapeHTMLSpecial(phNumber);
            client.setPhNumber(phNumber);
            EntityManager.updateUser(client);
            session.setAttribute("action", "settings-client-success");
            return req.getContextPath() + PagePath.HOME;
        } catch (DBException ex) {
            logger.error("Cannot change user settings", ex);
            session.setAttribute("error", ex.getPublicMessage());
            return req.getContextPath() + PagePath.ERROR;
        }
    }

}
