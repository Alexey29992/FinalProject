package com.repairagency.web.command.impl.admin;

import com.repairagency.bean.EntityManager;
import com.repairagency.bean.User;
import com.repairagency.database.DBFields;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.DBException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  Command of receiving list of registered users via Admin access page
 */
public class GetAdminUsers implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : get-admin-users");
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sort-factor : {}", sortFactorAttr);
        String sortFactor = Util.parseSortUser(sortFactorAttr);
        String roleFilterAttr = req.getParameter("filter-role");
        logger.trace("filter-role : {}", roleFilterAttr);
        String roleFilter = Util.parseUserRole(roleFilterAttr);
        QueryGetData queryData = new QueryGetData();
        int[] pageData = Util.parseTableParams(queryData, req);
        queryData.setSortFactor(sortFactor);
        if (roleFilter != null) {
            queryData.setFilterFactor(DBFields.ROLE_NAME, roleFilter);
        }
        List<User> users;
        try {
            users = EntityManager.getUserList(queryData);
        } catch (DBException ex) {
            logger.error("Cannot get User List", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        Util.setPageParams(req, users, pageData);
        req.setAttribute("users", users);
        return PagePath.ADMIN_USERS;
    }

}
