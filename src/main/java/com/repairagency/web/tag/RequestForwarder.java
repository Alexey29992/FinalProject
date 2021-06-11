package com.repairagency.web.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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
        ServletRequest req = pageContext.getRequest();
        ServletResponse resp = pageContext.getResponse();
        StringBuilder address = new StringBuilder("/controller");
        if (req.getDispatcherType().equals(DispatcherType.REQUEST)) {
            String parameter = req.getParameter("command");
            if (parameter == null || parameter.length() == 0) {
                 address.append("?command=").append(command);
            }
            try {
                req.getRequestDispatcher(address.toString()).forward(req, resp);
            } catch (ServletException | IOException ex) {
                logger.error("Cannot process forward", ex);
                throw new JspException(ex);
            }
        }
        return SKIP_BODY;
    }

}
