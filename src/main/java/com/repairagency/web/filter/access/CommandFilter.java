package com.repairagency.web.filter.access;

import com.repairagency.bean.User;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.Names;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class CommandFilter implements Filter {

    private static final Logger logger = LogManager.getLogger();
    private static final Map<String, List<String>> commandWhiteLists = new HashMap<>();

    static {
        List<String> managerCommands = Arrays.asList(
                Names.GET_MANAGER_REQUESTS,
                Names.TOP_UP_BALANCE,
                Names.GET_USER,
                Names.GET_REQUEST,
                Names.SET_MASTER,
                Names.SET_PRICE,
                Names.SET_STATUS_MANAGER
        );
        List<String> adminCommands = Arrays.asList(
                Names.GET_ADMIN_USERS,
                Names.CREATE_USER,
                Names.REMOVE_USER,
                Names.REMOVE_REQUEST
        );
        List<String> masterCommands = Arrays.asList(
                Names.GET_MASTER_REQUESTS,
                Names.GET_REQUEST,
                Names.SET_STATUS_MASTER
        );
        List<String> clientCommands = Arrays.asList(
                Names.GET_MASTER_REQUESTS,
                Names.GET_REQUEST,
                Names.SET_STATUS_MASTER,
                Names.CREATE_REQUEST,
                Names.FEEDBACK,
                Names.CHANGE_CLIENT_SETTINGS,
                Names.GET_CLIENT_REQUESTS,
                Names.GET_CLIENT_PAYMENT_RECORDS,
                Names.MAKE_PAYMENT
        );
        List<String> userCommands = Arrays.asList(
                Names.CHANGE_PASSWORD,
                Names.LOGOUT
        );
        List<String> guestCommands = Arrays.asList(
                Names.LOGIN
        );
        commandWhiteLists.put(User.Role.ADMIN.name(), adminCommands);
        commandWhiteLists.put(User.Role.MANAGER.name(), managerCommands);
        commandWhiteLists.put(User.Role.MASTER.name(), masterCommands);
        commandWhiteLists.put(User.Role.CLIENT.name(), clientCommands);
        commandWhiteLists.put("USER", userCommands);
        commandWhiteLists.put("GUEST", guestCommands);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("Checking command access");
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        HttpSession session = httpRequest.getSession();
        User user = (User) session.getAttribute("user");
        logger.trace("User role : {}", user);
        String command = httpRequest.getParameter("command");
        logger.trace("Command : {}", command);
        if (command == null) {
            chain.doFilter(req, resp);
            return;
        }
        if (!isCommandAppropriate(user, command)) {
            logger.trace("Command access check failed. " +
                    "Role is inappropriate to execute this command");
            session.setAttribute("error", ErrorMessages.INVALID_COMMAND);
            httpResponse.sendRedirect(httpRequest.getContextPath() + PagePath.ERROR);
            return;
        }
        chain.doFilter(req, resp);
    }

    private boolean isCommandAppropriate(User user, String command) {
        List<String> whiteList = new ArrayList<>();
        if (user == null) {
            whiteList.addAll(commandWhiteLists.get("GUEST"));
        } else {
            whiteList.addAll(commandWhiteLists.get(user.getRole().name()));
            whiteList.addAll(commandWhiteLists.get("USER"));
        }
        if (user != null && user.getRole().equals(User.Role.ADMIN)) {
            whiteList.addAll(commandWhiteLists.get(User.Role.MANAGER.name()));
        }
        for (String s : whiteList) {
            if (s.equals(command)) {
                return true;
            }
        }
        return false;
    }

}
