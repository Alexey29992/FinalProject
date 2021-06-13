package com.repairagency.web.command.impl.master;

import com.repairagency.bean.User;
import com.repairagency.database.QueryGetData;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import com.repairagency.web.command.impl.common.GetRequestTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetRequestsMaster extends GetRequestTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-master-requests");
        return getRequestTable(req, PagePath.MASTER_REQUESTS);
    }

    @Override
    protected void parseFilters(HttpServletRequest req, QueryGetData data) {
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("filter-status : {}", statusFilterAttr);
        User master = (User) req.getSession().getAttribute("user");
        String statusName = Util.parseStatus(statusFilterAttr);
        if (statusName != null) {
            data.setFilterFactor("status_name", statusName);
        }
        data.setFilterFactor("master_id", String.valueOf(master.getId()));
    }



}
