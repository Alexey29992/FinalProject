package com.repairagency.web.command.impl.admin;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.util.Validator;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CreateUser implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : create-user");
        String loginAttr = req.getParameter("login");
        logger.trace("login : {}", loginAttr);
        String passwordAttr = req.getParameter("password");
        logger.trace("password : {}", passwordAttr);
        String roleAttr = req.getParameter("role");
        logger.trace("role : {}", roleAttr);
        String roleStr = Util.parseUserRole(roleAttr);
        if (roleStr == null) {
            logger.error("Cannot create user. Invalid role");
            req.getSession().setAttribute("error", ErrorMessages.INVALID_INPUT);
            return PagePath.ADMIN_CREATE_USER;
        }
        User.Role role = User.Role.valueOf(roleStr);
        try {
            Validator.validateLogin(loginAttr);
            Validator.validatePassword(passwordAttr);
            User user = EntityManager.newUser(loginAttr, passwordAttr, role);
            if (user.getRole().equals(User.Role.MASTER)) {
                Map<Integer, String> map = (Map<Integer, String>)
                        req.getServletContext().getAttribute("masterMap");
                map.put(user.getId(), user.getLogin());
            }
            req.getSession().setAttribute("action", "create-user-success");
        } catch (DBException | InvalidOperationException ex) {
            logger.error("Cannot create user", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
        }
        return PagePath.HOME;
    }

}
