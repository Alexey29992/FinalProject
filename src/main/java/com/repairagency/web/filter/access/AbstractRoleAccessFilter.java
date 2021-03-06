package com.repairagency.web.filter.access;

import com.repairagency.bean.User;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Base class for all Filters that intended to check registered user access to current page
 */
public abstract class AbstractRoleAccessFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("Checking role access");
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute("user");
        if (!isRoleAppropriate(user.getRole())) {
            logger.trace("Role access check failed. Role is inappropriate to view this page");
            session.setAttribute("error", ErrorMessages.INSUFFICIENT_PERMISSIONS);
            httpResponse.sendRedirect(httpRequest.getContextPath() + PagePath.ERROR);
            return;
        }
        chain.doFilter(req, resp);
    }

    /**
     * Checks whether given {@link User.Role} matches accessed page.
     * @param role Role of user that accesses page
     * @return true if given role matches accessed page, false - if not
     */
    protected abstract boolean isRoleAppropriate(User.Role role);
}
