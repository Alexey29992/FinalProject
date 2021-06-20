package com.repairagency.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command interface. Command is built by Command Pattern and represents
 * the complete action that can be performed by user via JSP page.
 * Result of executing any Command is an address of appropriate JSP page
 * and a set of attributes that will be used to form a response for user.
 * List of available Commands is held be {@link CommandContainer}
 */

public interface Command {

    String execute(HttpServletRequest req, HttpServletResponse resp);

}
