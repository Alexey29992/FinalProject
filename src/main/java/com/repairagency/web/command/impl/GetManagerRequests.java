package com.repairagency.web.command.impl;

import com.repairagency.PagePath;
import com.repairagency.database.wrapper.ManagerRequestData;
import com.repairagency.entity.EntityManager;

import com.repairagency.exception.DBException;
import com.repairagency.database.wrapper.QueryData;
import com.repairagency.web.command.impl.parser.Parsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class GetManagerRequests extends GetTableContent {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        logger.debug("Executing command : get-manager-requests");
        QueryData queryData = new QueryData();
        parseStandardParams(queryData, req);
        parseFilters(queryData, req);
        List<ManagerRequestData> requestData;
        try {
            requestData = EntityManager.getManagerRequestList(queryData);
        } catch (DBException ex) {
            logger.error("Cannot get Request List", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        req.setAttribute("page", page);
        boolean hasNextPage = false;
        boolean hasPrevPage = false;
        if (requestData.size() == size + 1) {
            requestData.remove(size - 1);
            hasNextPage = true;
        }
        if (page > 0) {
            hasPrevPage = true;
        }
        req.setAttribute("requestData", requestData);
        req.setAttribute("hasNextPage", hasNextPage);
        req.setAttribute("hasPrevPage", hasPrevPage);
        req.setAttribute("forwarded", 1);
        return PagePath.MANAGER_REQUESTS;
    }

    void parseFilters(QueryData data, HttpServletRequest req) {
        logger.trace("Parsing HTTP parameters for Request query");
        String statusFilterAttr = req.getParameter("filter-status");
        logger.trace("filter-status : {}", statusFilterAttr);
        String masterFilterAttr = req.getParameter("filter-master");
        logger.trace("filter-master : {}", masterFilterAttr);
        String clientFilterAttr = req.getParameter("filter-client");
        logger.trace("filter-client : {}", clientFilterAttr);

        String statusName = Parsers.parseStatusFilter(statusFilterAttr);

        if (statusName != null) {
            data.setFilterFactor("status_name", statusName);
        }
        if (masterFilterAttr != null && !masterFilterAttr.isEmpty()) {
            data.setFilterFactor("master.login", masterFilterAttr);
            logger.trace("hello, {}", masterFilterAttr.getBytes(StandardCharsets.UTF_8));
        }
        if (clientFilterAttr != null && !clientFilterAttr.isEmpty()) {
            data.setFilterFactor("client.login", clientFilterAttr);
        }
    }



}
