package com.repairagency.web.command.impl;

import com.repairagency.web.command.PagePath;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InvalidCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : invalid");
        req.getSession().setAttribute("error", ErrorMessages.INVALID_COMMAND);
        logger.trace("Redirecting to error page...");
        return PagePath.ERROR;
    }

}
