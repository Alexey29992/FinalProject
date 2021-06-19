package com.repairagency.web.filter.access;

import com.repairagency.bean.User;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("Checking authorization");
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.trace("Role access check failed. User is null");
            httpResponse.sendRedirect(httpRequest.getContextPath() + PagePath.LOGIN);
            return;
        }
        chain.doFilter(req, resp);
    }

}
