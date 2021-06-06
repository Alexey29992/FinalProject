package com.repairagency.web.commands.impl;

import com.repairagency.PagePath;
import com.repairagency.entities.User;
import com.repairagency.entities.users.Client;
import com.repairagency.exceptions.ErrorMessages;
import com.repairagency.web.commands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckPhone implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : check-phone");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        logger.trace("User:{}", user);
        if (!(user instanceof Client)) {
            logger.fatal("Cannot cast User to Client. Role access violation");
            throw new IllegalStateException(ErrorMessages.INVALID_ROLE);
        }
        Client client = (Client) user;
        String phNumber = client.getPhNumber();
        req.setAttribute("isNumberSet", phNumber != null && phNumber.length() != 0);
        return PagePath.REQUEST_CREATE;
    }

}
