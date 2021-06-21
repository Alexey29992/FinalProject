package com.repairagency.web.command.impl.master;

import com.repairagency.bean.User;
import com.repairagency.database.QueryGetData;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.Command;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import com.repairagency.web.command.impl.common.GetRequestTable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Command of receiving list of requests via Master access page
 */
public class GetRequestsMaster extends GetRequestTable implements Command {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req) {
        logger.debug("Executing command : get-master-requests");
        try {
            return getRequestTable(req, PagePath.MASTER_REQUESTS);
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
        User master = (User) req.getSession().getAttribute("user");
        String statusName = Util.parseStatus(statusFilterAttr);
        if (statusName != null) {
            data.setFilterFactor("status_name", statusName);
        }
        data.setFilterFactor("master_id", String.valueOf(master.getId()));
    }


}
