package com.repairagency.web.tag;

import com.repairagency.bean.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ActionProcessor extends TagSupport {

    private static final Logger logger = LogManager.getLogger();

    private String var;
    private int scope = 1;

    public void setVar(String var) {
        this.var = var;
    }

    public void setScope(String scope) {
        if ("request".equalsIgnoreCase(scope)) {
            this.scope = 2;
        } else if ("session".equalsIgnoreCase(scope)) {
            this.scope = 3;
        } else if ("application".equalsIgnoreCase(scope)) {
            this.scope = 4;
        }
    }

    @Override
    public int doStartTag() throws JspException {
        logger.debug("Tag ActionProcessor invoked");
        HttpSession session = pageContext.getSession();
        String action = (String) session.getAttribute("action");
        User user = (User) session.getAttribute("user");
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
                result = ActionProcessorMessages.SIGN_IN;
                break;
            case "request-success":
                result = ActionProcessorMessages.REQUEST_SUCCESS;
                break;
            case "payment-success":
                result = ActionProcessorMessages.PAYMENT_SUCCESS;
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
            case "create-user-success":
                result = ActionProcessorMessages.CREATE_USER_SUCCESS;
                break;
            case "remove-user-success":
                result = ActionProcessorMessages.REMOVE_USER_SUCCESS;
                break;
            case "remove-request-success":
                result = ActionProcessorMessages.REMOVE_REQUEST_SUCCESS;
                break;
            case "client-no-phone":
                result = ActionProcessorMessages.CLIENT_PHONE_REQUIRED;
                break;
            default:
                result = ActionProcessorMessages.defaultHome(user);
        }
        session.removeAttribute("action");
        try {
            if (var != null) {
                pageContext.setAttribute(var, result, scope);
            } else {
                pageContext.getOut().println(result);
            }
        } catch (IOException ex) {
            logger.error("Cannot write on page", ex);
            throw new JspException(ex);
        }
        return SKIP_BODY;
    }

}
