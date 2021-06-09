package com.repairagency.web.command.impl;

import com.repairagency.PagePath;
import com.repairagency.database.wrapper.QueryData;
import com.repairagency.entity.EntityManager;
import com.repairagency.entity.User;
import com.repairagency.entity.bean.Request;
import com.repairagency.exception.DBException;
import com.repairagency.web.command.impl.parser.Parsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


public class GetClientRequests extends GetTableContent {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-client-requests");
        HttpSession session = req.getSession();
        QueryData queryData = new QueryData();
        parseStandardParams(queryData, req);
        parseFilters(queryData, req);
        List<Request> requestList;
        try {
            requestList = EntityManager.getClientRequestList(queryData);
        } catch (DBException ex) {
            logger.error("Cannot get Request List", ex);
            session.setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        req.setAttribute("page", page);
        boolean hasNextPage = false;
        boolean hasPrevPage = false;
        if (requestList.size() == size + 1) {
            requestList.remove(size - 1);
            hasNextPage = true;
        }
        if (page > 0) {
            hasPrevPage = true;
        }
        req.setAttribute("requestList", requestList);
        req.setAttribute("hasNextPage", hasNextPage);
        req.setAttribute("hasPrevPage", hasPrevPage);
        req.setAttribute("forwarded", 1);
        return PagePath.CLIENT_REQUESTS;
    }

    void parseFilters(QueryData data, HttpServletRequest req) {
        logger.trace("Parsing HTTP parameters for Request query");
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("filter-status : {}", statusFilterAttr);
        User user = (User) req.getSession().getAttribute("user");
        logger.trace("client : {}", user);
        Parsers.parseStatusFilter(statusFilterAttr);
        String statusFilter = Parsers.parseStatusFilter(statusFilterAttr);
        if (statusFilter != null) {
            data.setFilterFactor("status_name", statusFilter);
        }
        data.setFilterFactor("client_id", String.valueOf(user.getId()));
    }

}
