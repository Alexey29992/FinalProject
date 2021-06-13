package com.repairagency.web.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class RequestForwarder extends TagSupport {

    private static final Logger logger = LogManager.getLogger();

    private String command;

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public int doStartTag() throws JspException {
        logger.debug("Forwarding tag start");
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        logger.trace("REQUEST URL : {}?{}", request.getRequestURL(), request.getQueryString());
        ServletRequest req = pageContext.getRequest();
        ServletResponse resp = pageContext.getResponse();
        String parameter = req.getParameter("command");
        logger.trace("Command parameter : {}", parameter);
        logger.trace("Command attribute : {}", command);
        StringBuilder address = new StringBuilder("/controller");
        boolean isAttributeExists = command != null && command.length() != 0;
        boolean isParameterExists = parameter != null && parameter.length() != 0;
        if (!req.getDispatcherType().equals(DispatcherType.REQUEST)
                || (!isAttributeExists && !isParameterExists)) {
            return SKIP_BODY;
        }
        if (!isParameterExists) {
            address.append("?command=").append(command);
        }
        try {
            logger.trace("Redirecting to : {}", address);
            req.getRequestDispatcher(address.toString()).forward(req, resp);
            return SKIP_PAGE;
        } catch (ServletException | IOException ex) {
            logger.error("Cannot process forward", ex);
            throw new JspException(ex);
        }
    }

}
