package com.repairagency.web.tag;

import com.repairagency.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ActionProcessor extends TagSupport {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public int doStartTag() throws JspException {
        logger.debug("Tag ActionProcessor invoked");
        HttpSession session = pageContext.getSession();
        String action = (String) session.getAttribute("action");
        logger.trace("action : {}", action);
        if (action == null) {
            action = "";
        }
        String result;
        switch (action) {
            case "sign-up":
                result = ActionProcessorMessages.SIGN_UP;
                break;
            case "sign-in":
                result = String.format(ActionProcessorMessages.SIGN_IN,
                        ((User) session.getAttribute("user")).getRole().toLowerCaseString());
                break;
            case "request-success":
                result = ActionProcessorMessages.REQUEST_SUCCESS;
                break;
            case "payment-success":
                result = ActionProcessorMessages.PAYMENT_SUCCESS;
                break;
            case "top-up-success":
                result = ActionProcessorMessages.TOP_UP_SUCCESS;
                break;
            case "password-success":
                result = ActionProcessorMessages.PASSWORD_SUCCESS;
                break;
            case "settings-client-success":
                result = ActionProcessorMessages.SETTINGS_CLIENT_SUCCESS;
                break;
            case "feedback-success":
                result = ActionProcessorMessages.FEEDBACK_SUCCESS;
                break;
            default:
                result = ActionProcessorMessages.DEFAULT;
        }
        session.removeAttribute("action");
        try {
            pageContext.getOut().println(result);
        } catch (IOException ex) {
            logger.error("Cannot write on page", ex);
            throw new JspException(ex);
        }
        return SKIP_BODY;
    }

}
