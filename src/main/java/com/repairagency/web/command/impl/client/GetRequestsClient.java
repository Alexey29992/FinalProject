package com.repairagency.web.command.impl.client;

import com.repairagency.database.DBFields;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.database.QueryGetData;
import com.repairagency.bean.User;
import com.repairagency.web.command.impl.common.GetRequestTable;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 *  Command of receiving list of requests via Client access page
 */
public class GetRequestsClient extends GetRequestTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : get-client-requests");
        try {
            return getRequestTable(req, PagePath.CLIENT_REQUESTS);
        } catch (InvalidOperationException ex) {
            logger.error("Invalid filter values", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
    }

    @Override
    protected void setFilters(HttpServletRequest req, QueryGetData data) {
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("filter-status : {}", statusFilterAttr);
        User user = (User) req.getSession().getAttribute("user");
        logger.trace("client : {}", user);
        Util.parseStatus(statusFilterAttr);
        String statusFilter = Util.parseStatus(statusFilterAttr);
        if (statusFilter != null) {
            data.setFilterFactor(DBFields.STATUS_NAME, statusFilter);
        }
        data.setFilterFactor(DBFields.REQUEST_CLIENT_ID, String.valueOf(user.getId()));
    }

}
