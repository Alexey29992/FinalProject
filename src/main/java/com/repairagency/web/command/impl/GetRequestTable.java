package com.repairagency.web.command.impl;

import com.repairagency.database.wrapper.QueryData;
import com.repairagency.database.wrapper.RequestData;
import com.repairagency.entity.EntityManager;
import com.repairagency.exception.DBException;
import com.repairagency.web.command.PagePath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class GetRequestTable extends GetTable {

    private static final Logger logger = LogManager.getLogger();

    public String getRequestTable(HttpServletRequest req, String address) {
        logger.trace("Parsing HTTP parameters for Request query");
        QueryData queryData = new QueryData();
        parseTableParams(queryData, req);
        parseFilters(req, queryData);
        List<RequestData> requestData;
        try {
            requestData = EntityManager.getRequestList(queryData);
        } catch (DBException ex) {
            logger.error("Cannot get Request List", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        processTableParams(req, requestData);
        req.setAttribute("requestData", requestData);
        return address;
    }

    protected abstract void parseFilters(HttpServletRequest request, QueryData outputData);

}
