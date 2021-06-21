package com.repairagency.web.command.impl.common;

import com.repairagency.web.command.CommandContainer;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Command;
import com.repairagency.exception.ErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 *  Command that is called by default to signalize error
 *  when {@link CommandContainer} can't resolve command identifier
 */
public class InvalidCommand implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : invalid");
        req.getSession().setAttribute("error", ErrorMessages.INVALID_COMMAND);
        logger.trace("Redirecting to error page...");
        return PagePath.ERROR;
    }

}
