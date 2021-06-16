package com.repairagency.web.command.impl.admin;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.DBException;
import com.repairagency.exception.ErrorMessages;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import com.repairagency.web.command.impl.common.GetTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class GetAdminUsers extends GetTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-admin-users");
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sort-factor : {}", sortFactorAttr);
        String sortFactor = Util.parseSortUser(sortFactorAttr);
        String roleFilterAttr = req.getParameter("filter-role");
        logger.trace("filter-role : {}", roleFilterAttr);
        String roleFilter = Util.parseUserRole(roleFilterAttr);
        QueryGetData queryData = new QueryGetData();
        parseTableParams(queryData, req);
        queryData.setSortFactor(sortFactor);
        if (roleFilter != null) {
            queryData.setFilterFactor("role_name", roleFilter);
        }
        List<User> users;
        try {
            users = EntityManager.getUserList(queryData);
        } catch (DBException ex) {
            logger.error("Cannot get User List", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        processTableParams(req, users);
        req.setAttribute("users", users);
        return PagePath.ADMIN_USERS;
    }

}
