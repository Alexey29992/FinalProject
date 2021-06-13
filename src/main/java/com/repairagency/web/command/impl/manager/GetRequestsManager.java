package com.repairagency.web.command.impl.manager;

import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;

import com.repairagency.database.QueryGetData;
import com.repairagency.web.command.impl.GetRequestTable;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetRequestsManager extends GetRequestTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-manager-requests");
        return getRequestTable(req, PagePath.MANAGER_REQUESTS);
    }

    @Override
    protected void parseFilters(HttpServletRequest req, QueryGetData data) {
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("filter-status : {}", statusFilterAttr);
        String masterFilterAttr = req.getParameter("filter-master");
        logger.trace("filter-master : {}", masterFilterAttr);
        String clientFilterAttr = req.getParameter("filter-client");
        logger.trace("filter-client : {}", clientFilterAttr);
        String statusName = Util.parseStatus(statusFilterAttr);
        if (statusName != null) {
            data.setFilterFactor("status_name", statusName);
        }
        if (masterFilterAttr != null && !masterFilterAttr.isEmpty()) {
            data.setFilterFactor("master.login", masterFilterAttr);
        }
        if (clientFilterAttr != null && !clientFilterAttr.isEmpty()) {
            data.setFilterFactor("client.login", clientFilterAttr);
        }
    }



}
