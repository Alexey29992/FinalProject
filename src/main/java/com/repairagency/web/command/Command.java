package com.repairagency.web.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Command interface. Command is built by Command Pattern and represents
 * the complete action that can be performed by user via JSP page.
 * Result of executing any Command is an address of appropriate JSP page
 * and a set of attributes that will be used to form a response for user.
 * List of available Commands is held be {@link CommandContainer}
 */
public interface Command {

    /**
     * Executes command.
     * @param req request with required for execution data
     * @return address of JSP page or Servlet to make response
     */
    String execute(HttpServletRequest req);

}
