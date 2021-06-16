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


public abstract class AbstractAccessFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("Checking role access");
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null || !user.getRole().equals(getRole())) {
            logger.trace("Role access check failed");
            session.setAttribute("error", ErrorMessages.INSUFFICIENT_PERMISSIONS);
            httpResponse.sendRedirect(httpRequest.getContextPath() + PagePath.ERROR);
            return;
        }
        chain.doFilter(req, resp);
    }

    protected abstract User.Role getRole();

}
