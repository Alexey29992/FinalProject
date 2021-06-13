package com.repairagency.web.command.impl.client;

import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.database.QueryGetData;
import com.repairagency.bean.User;
import com.repairagency.web.command.impl.common.GetRequestTable;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetRequestsClient extends GetRequestTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-client-requests");
        return getRequestTable(req, PagePath.CLIENT_REQUESTS);
    }

    protected void parseFilters(HttpServletRequest req, QueryGetData data) {
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("filter-status : {}", statusFilterAttr);
        User user = (User) req.getSession().getAttribute("user");
        logger.trace("client : {}", user);
        Util.parseStatus(statusFilterAttr);
        String statusFilter = Util.parseStatus(statusFilterAttr);
        if (statusFilter != null) {
            data.setFilterFactor("status_name", statusFilter);
        }
        data.setFilterFactor("client_id", String.valueOf(user.getId()));
    }

}
