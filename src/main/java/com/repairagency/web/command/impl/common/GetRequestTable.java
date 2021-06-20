package com.repairagency.web.command.impl.common;

import com.repairagency.bean.data.Request;
import com.repairagency.database.QueryGetData;
import com.repairagency.bean.EntityManager;
import com.repairagency.exception.DBException;
import com.repairagency.exception.InvalidOperationException;
import com.repairagency.web.command.PagePath;
import com.repairagency.web.command.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Abstract class that encapsulates common functionality to receive list of requests.
 * Commands that intended to get request list should be inherited from this class
 */

public abstract class GetRequestTable extends GetTable {

    private static final Logger logger = LogManager.getLogger();

    public String getRequestTable(HttpServletRequest req, String address)
            throws InvalidOperationException {
        logger.trace("Parsing HTTP parameters for Request query");
        String sortFactorAttr = req.getParameter("sort-factor");
        logger.trace("sort-factor : {}", sortFactorAttr);
        QueryGetData queryData = new QueryGetData();
        parseTableParams(queryData, req);
        String sortFactor = Util.parseSortRequest(sortFactorAttr);
        queryData.setSortFactor(sortFactor);
        setFilters(req, queryData);
        List<Request> requests;
        try {
            requests = EntityManager.getRequestList(queryData);
        } catch (DBException ex) {
            logger.error("Cannot get Request List", ex);
            req.getSession().setAttribute("error", ex.getPublicMessage());
            return PagePath.ERROR;
        }
        processTableParams(req, requests);
        req.setAttribute("requests", requests);
        return address;
    }

    protected abstract void setFilters(HttpServletRequest request, QueryGetData outputData)
            throws InvalidOperationException;

}
