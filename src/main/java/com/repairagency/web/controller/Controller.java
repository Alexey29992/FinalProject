package com.repairagency.web.controller;

import com.repairagency.web.command.Command;
import com.repairagency.web.command.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller is the main Servlet that receives all the HTTP requests that
 * refer to {@link Command} execution. After receiving HTTP request Controller invokes
 * an appropriate Command and forwards to page with address returned by this Command
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final Logger logger = LogManager.getLogger();
    private static final String COMMAND = "command";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Controller receives 'get' request");
        String command = req.getParameter(COMMAND);
        logger.trace("{} : {}", COMMAND, command);
        String address = CommandContainer.getCommand(command).execute(req);
        logger.trace("Forward to : {}", address);
        req.getRequestDispatcher(address).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Controller receives 'post' request");
        String command = req.getParameter(COMMAND);
        logger.trace("{} : {}", COMMAND, command);
        String address = CommandContainer.getCommand(command).execute(req);
        address = req.getContextPath() + address;
        logger.trace("Send redirect to : {}", address);
        resp.sendRedirect(address);
    }

}
