package com.repairagency.web.controller;

import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * ErrorHandler is a Servlet that processes errors. It is intended to process exceptions
 * that were not caught in the {@link Command}s. This Servlet is configured to be invoked
 * by Servlet Container in deployment descriptor (web.xml).
 */

@WebServlet("/ErrorHandler")
public class ErrorHandler extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        onError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        onError(req, resp);
    }

    private void onError(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Throwable throwable = (Throwable) req.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        logger.error("Error status code {}", statusCode);
        if (throwable != null) {
            logger.error("Uncaught exception", throwable);
        }
        HttpSession session = req.getSession();
        String publicMessage;
        if (statusCode == 404) {
            publicMessage = ErrorMessages.URL_NOT_FOUND;
        } else {
            publicMessage = ErrorMessages.UNEXPECTED;
        }
        session.setAttribute("error", publicMessage);
        resp.sendRedirect(req.getContextPath() + PagePath.ERROR);
    }

}
