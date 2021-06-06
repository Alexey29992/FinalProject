package com.repairagency.web.commands.impl;

import com.repairagency.PagePath;
import com.repairagency.entities.EntityManager;
import com.repairagency.entities.users.Client;
import com.repairagency.exceptions.DBException;
import com.repairagency.web.commands.Command;
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
            client.setPhNumber(phNumber);
            EntityManager.updateUser(client);
            session.setAttribute("action", "settings-client-success");
            return PagePath.HOME;
        } catch (DBException ex) {
            logger.error("Cannot change user settings", ex);
            session.setAttribute("error", ex.getPublicMessage());
            return PagePath.CHANGE_USER_SETTINGS;
        }
    }

}
